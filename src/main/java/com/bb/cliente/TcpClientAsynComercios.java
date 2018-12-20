package com.bb.cliente;



import java.net.InetSocketAddress;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


import com.bb.codecs.PostillionCodecFactory;


public class TcpClientAsynComercios extends IoHandlerAdapter implements ITcpClientAsyn{
    
    private SocketConnector connector;
    private ConnectFuture connFuture;
    @SuppressWarnings("rawtypes")
	private static ConcurrentMap responseMap = new ConcurrentHashMap();
    private static IoSession session;
    private String HOST ;//= System.getProperty("host", "172.20.32.56");
    private int PORT ;//= Integer.parseInt(System.getProperty("port", "3681"));
   // private int TIMEOUT = 30000;
    //private static int READ_TIMEOUT = 25000;
    private boolean conectado = false;    
    
    private static Logger logger = Logger.getLogger(TcpClientAsynComercios.class);

    /**
     * Constructor de la clase utilitaria TcpClientAsyn
     * 
     * @param proveedor     tipo de proveedor de trama(PGDIRECT)
     * @param ip            ip del servidor destino
     * @param puerto        puerto escucha del servidor destino
     * @param timeout       tiempo maximo de espera por respuesta del servidor destino
     * @throws Exception    Exception lanzada en caso de errores al inicializar
     */
    public TcpClientAsynComercios(String ip, int puerto, int timeout) throws Exception {
        HOST = ip;
        PORT = puerto;
       // TIMEOUT = timeout;
      
        conectar();
        
    }

    /**
     * Constructor sin parametros de la clase utilitaria TcpClientAsyn
     * 
     * @throws Exception
     */
    public TcpClientAsynComercios() throws Exception {
        conectar();
    }
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.error("Error inesperado: ", cause);
    }
 
    @SuppressWarnings("unchecked")
	@Override
    public void messageReceived(IoSession session, Object message) throws Exception {
    	
       // Message response = (Message) message;
        //byte[] mensajeRespuesta = message.toString().getBytes();
        
        long correlationId = 0;
                
       // correlationId = Long.parseLong((new String(Arrays.copyOfRange(mensajeRespuesta, 181, 187))).trim());
        correlationId = Long.parseLong(message.toString().substring(181 ,187));
        //System.out.println("respuesta correlationId:"+correlationId);
        responseMap.put(correlationId, message.toString());
       // task.setResponseMap(responseMap);
        logger.debug("<tcpasync><messageReceived>responseMap.put = "+correlationId+"-"+ message.toString());  
        
        
        
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    //	session.resumeRead();
    	logger.debug("Mensaje enviado" + "["+session+"]");
		//System.out.println("["+session+"]");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
       logger.info("<client> el cliente ha sido desconectado");
        conectado = false;
        connector.dispose();
        //Thread reconexionTask = new Thread(new ReconexionTask());
        //reconexionTask.start();
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {    
        responseMap.clear();
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
    }
       
   /* private static byte[] prepararMensaje(byte[] trama) {
        System.out.println("<tcpasync><prepararMensaje>begin");
        if(trama.length <= 0) return trama;
        
        int i = trama.length - 1;
        
        
        byte msgDataTemp[] = Arrays.copyOf(trama, i + 1);
        byte[] msgData = null; 
        if(PROVEEDOR.equals("PGDIREC")){
            int tamanio = msgDataTemp.length + 2;
            msgData = new byte[tamanio];
            msgData[0] = (byte)((tamanio) / 256); //0
            msgData[1] = (byte)((tamanio) % 256); //
            for (i = 0; i < msgDataTemp.length; i++) {
                msgData[i + 2] = msgDataTemp[i];
            }
        }
        System.out.println("<tcpasync><prepararMensaje>end - msgData="+msgData);
        return msgData;
        //return null;
    }*/

    public synchronized boolean conectar() {
    	
    	
       connector = new NioSocketConnector();
       connector.setConnectTimeoutMillis(10000);
       
       connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 40);
        //connector.getSessionConfig().setWriteTimeout(new Integer(Utils.getProperty("client.timeout")));
       connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PostillionCodecFactory()));
       connector.getFilterChain().addLast("encoded_logger",new LoggingFilter());
       
       connector.setHandler(this); 
       connFuture = connector.connect(new InetSocketAddress(HOST, PORT));
       //connFuture.awaitUninterruptibly(TIMEOUT, TimeUnit.MILLISECONDS);  
       connFuture.awaitUninterruptibly();
       conectado = connFuture.isConnected();
       if(conectado){
         session = connFuture.getSession();         
         logger.info("<client> cliente conectado satisfactotiamente");
       }else{
          logger.info("<client> cliente no se ha podido conectar");
       }
       return conectado;
    }
    
    
    public void desconectar() {
        connector.dispose();
    }        

    public synchronized String writeToSocket(String mensaje, int readTimeout) {
        String respuesta = null;
        try{
           // System.out.println("<tcpasync><writeToSocket>begin");
            if(!conectado){
                conectar();
            }
            if(conectado){
               // System.out.println("<tcpasync><writeToSocket>mensaje a escribir"+mensaje);
               // byte[] trama = mensaje.getBytes();//Arrays.copyOfRange(mensaje.getBytes(), 2, mensaje.getBytes().length-2);
                //Message message = new Message();
                //String  data=new String(trama);
                //message.setData(data);
                //message.setLength(data.length());                
                session.write(mensaje);
                //session.getCloseFuture().awaitUninterruptibly(); 
        		//connector.dispose();
                logger.debug("<tcpasync><writeToSocket>mensaje escrito:"+mensaje);
                //long correlationId = Long.parseLong(new String(Arrays.copyOfRange(trama, 181, 187)));
                long correlationId =Long.parseLong(mensaje.substring(181 ,187));
                ExecutorService executor = Executors.newSingleThreadExecutor();
                ResponseTask task = new ResponseTask(correlationId);
                
                //System.out.println("correlationId:"+correlationId);
                task.setResponseMap(responseMap);
                executor.invokeAll(Arrays.asList(task),readTimeout, TimeUnit.MILLISECONDS);
                executor.shutdown();
                if(task.getResponse() != null)
                 //respuesta = prepararMensaje(task.getResponse());
              	respuesta = task.getResponse();
                else
                  respuesta = null;
            }
        }catch(Exception e){
            logger.error("<client> cliente no ha podido enviar el mensaje", e);
            desconectar();
        }
        logger.debug("<tcpasync><writeToSocket>end - respuesta="+respuesta);
        return respuesta;
    }
  
 /*  public static void main(String[] args) throws Exception {
       String mensaje ="0475O00000000000000000000000000000000001305020096             0000000000000000000000000000000000                                                        00998        0000597777      140358C                                                                                                                                                                                                                                                                                                                      00000000000000000";
       TcpClientAsynComercios client = new TcpClientAsynComercios("172.16.30.93",2222,10000);
       String response = client.writeToSocket(mensaje, READ_TIMEOUT);
       if(response != null){
            System.out.println("response - "+ response);
            client.desconectar();
       }

    }*/
    
    public boolean isConectado() {
        return conectado;
    }

    public IoSession getSession() {
        return session;
    }
}
