package com.bb.cliente;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import com.bb.cliente.dto.TramaComercios;
import com.bb.enums.CodigoTransaccion;
import com.bb.enums.TipoCuenta;
import com.bb.enums.TipoOperacion;
import com.bb.enums.TipoProceso;
import com.bb.enums.TipoTransaccion;

/**
 * Cliente Asincrono TCP ISO8583 para modulo de comercios - postillion
 * @author jvillavc
 *
 */
public class ClientePostillion implements IClientePostillion{

	public static final Charset CODIFICACION = Charset.forName("UTF-8");
	private String ipPostillion;
	private int puertoPostillion;
	private static ClientePostillion singletonPost;
	
	public static ClientePostillion getClientePostillion(String ip,int puerto) {
		if (singletonPost==null) {
			singletonPost=new ClientePostillion(ip,puerto);
	}
			 return singletonPost;
	}
	
	private ClientePostillion(String ip,int puerto){
		this.ipPostillion =ip;
		this.puertoPostillion = puerto;
	}
	
	/**
	 * Serializa la trama de comercios para el envio a postillion
	 * @param operacion La operacion a realizar
	 * @param trama La trama de comercios a serializar
	 * @return La trama lista para enviar al modulo de comercios
	 */
	private String serializar(TipoOperacion operacion,TramaComercios trama){
		StringBuilder tramaFinal = new StringBuilder();
		
		switch(operacion){
			case CONSULTA_SALDOS:
				tramaFinal.append(trama.getCodigoTransaccion().getCodigo());
				tramaFinal.append(trama.getTipoTransaccion().getTipo());
				tramaFinal.append(trama.getTarjeta());
				tramaFinal.append(trama.getCuenta());
				tramaFinal.append(trama.getError());
				tramaFinal.append(trama.getErrorReal());
				tramaFinal.append(trama.getMoneda());
				tramaFinal.append(StringUtils.leftPad("",17,"0"));
				tramaFinal.append(StringUtils.leftPad("",17,"0"));
				tramaFinal.append(trama.getReferencia());
				tramaFinal.append(trama.getClave());
				tramaFinal.append(trama.getTerminal());
				tramaFinal.append(trama.getOperador());
				tramaFinal.append(trama.getAbaEmisor());
				tramaFinal.append(trama.getCodigoServicio());
				tramaFinal.append(trama.getOrigenTransaccion());
				tramaFinal.append(trama.getSecuencia());
				tramaFinal.append(trama.getTipoProceso().getTipo());
				tramaFinal.append(trama.getPista2());
				tramaFinal.append(trama.getPista3());
				tramaFinal.append(trama.getReferencia2());
				tramaFinal.append(trama.getReferencia3());
				tramaFinal.append(trama.getReferencia4());
				tramaFinal.append(trama.getReferencia5());
				tramaFinal.append(StringUtils.leftPad("",17,"0"));
				break;
			case TRANSFERENCIA:
				tramaFinal.append(trama.getCodigoTransaccion().getCodigo());
				tramaFinal.append(trama.getTipoTransaccion().getTipo());
				tramaFinal.append(trama.getTarjeta());
				tramaFinal.append(trama.getCuenta());
				tramaFinal.append(trama.getError());
				tramaFinal.append(trama.getErrorReal());
				tramaFinal.append(trama.getMoneda());
				tramaFinal.append(trama.getMontoTrama());
				tramaFinal.append(trama.getComisionTrama());
				tramaFinal.append(trama.getReferencia());
				tramaFinal.append(trama.getClave());
				tramaFinal.append(trama.getTerminal());
				tramaFinal.append(trama.getOperador());
				tramaFinal.append(trama.getAbaEmisor());
				tramaFinal.append(trama.getCodigoServicio());
				tramaFinal.append(trama.getOrigenTransaccion());
				tramaFinal.append(trama.getSecuencia());
				tramaFinal.append(trama.getTipoProceso().getTipo());
				tramaFinal.append(trama.getPista2());
				tramaFinal.append(trama.getPista3());
				tramaFinal.append(trama.getReferencia2());
				tramaFinal.append(trama.getReferencia3());
				tramaFinal.append(trama.getReferencia4());
				tramaFinal.append(trama.getReferencia5());
				tramaFinal.append(trama.getMontoTrama());
				break;
			case BLOQUEO_TARJETA:
				//TODO Por implementar
				break;
		}
		
		return tramaFinal.toString();
	}

	
	/**
	 * Obtiene el secuencial a usar para las transacciones de postillion
	 * @return El secuencial de 6 digitos requerido por el modulo de comercios
	 */
	private static synchronized String obtenerSecuencial(){
		int secuencial = (int) (Math.random() * 999999) + 1;
		return String.format("%06d",secuencial);
	}

	
	private String ejecutarOperacionAsync(TipoOperacion operacion,TramaComercios request)throws Exception{
	String respuesta ="";
		TcpClientAsynComercios  TcpClientAsyn = new TcpClientAsynComercios(ipPostillion, puertoPostillion, 30000);
		 String response =TcpClientAsyn.writeToSocket(serializar(operacion,request), 30000);
	       if(response != null){
	    	   respuesta =new String(response);
	            System.out.println("response - "+ respuesta);
	            TcpClientAsyn.desconectar();
	       }

	       return respuesta;
	}
	
	
	/**
	 * Ejecutar una operacion asincrona contra postillion
	 * @param operacion El tipo de operacion a realizar
	 * @param request La trama de comercios a enviar
	 * @throws Exception Los errores que existan en el proceso
	 */
	/*private void ejecutarOperacionAsync2(TipoOperacion operacion,TramaComercios request)throws Exception{
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(10000);
		 
		ProtocolCodecFactory codecFactory = new PostillionCodecFactory();
		
		connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(codecFactory));
		connector.getFilterChain().addLast("encoded_logger", new LoggingFilter());
		connector.setHandler(new SessionHandlerConexion());
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,10);
		IoSession session;
			try { 
				ConnectFuture future = connector.connect(new InetSocketAddress(ipPostillion,puertoPostillion)); 
				future.awaitUninterruptibly();
				if(future.isConnected()){
				}
				session = future.getSession();
		  
			}catch (RuntimeIoException e) {
		 
				try { Thread.sleep(5000); } catch (InterruptedException e1) {e1.printStackTrace();}
		 
				throw new Exception("Error de conexion con Postillion: "+e.getMessage());
			} 		
		session.write(serializar(operacion,request));
		session.getCloseFuture().awaitUninterruptibly(); 
		connector.dispose();
		
	}*/
	
	/* (non-Javadoc)
	 * @see com.bb.cliente.IClientePostillion#consultarSaldoOffline(com.bb.enums.TipoCuenta, java.lang.String)
	 */
	public RespuestaConsulta consultarSaldoOffline(TipoCuenta tipo, String cuenta){
		
		RespuestaConsulta respuestaConsulta = new RespuestaConsulta();

		try{
		
			TramaComercios request = new TramaComercios();
			CodigoTransaccion codigoTransaccion;
	
			switch (tipo) {
				case AHORROS:
					codigoTransaccion = CodigoTransaccion.CONSULTA_AHORROS;
					break;
				case CORRIENTE:
					codigoTransaccion = CodigoTransaccion.CONSULTA_CORRIENTE;
					break;
				case VIRTUAL:
					codigoTransaccion = CodigoTransaccion.CONSULTA_VIRTUAL;
					break;
				default:
					codigoTransaccion = CodigoTransaccion.CONSULTA_AHORROS;
			}
	
			request.setCodigoTransaccion(codigoTransaccion);
			request.setTipoTransaccion(TipoTransaccion.ORIGINAL);
			request.setTarjeta(StringUtils.leftPad("", 20, "0"));
			request.setCuenta(StringUtils.leftPad(cuenta, 24, "0"));
			request.setError(StringUtils.leftPad("", 4, " "));
			request.setErrorReal(StringUtils.leftPad("", 6, " "));
			request.setMoneda(StringUtils.leftPad("", 3, " "));
			request.setMonto(0);
			request.setComision(0);
			request.setReferencia(StringUtils.leftPad("", 40, " "));
			request.setClave(StringUtils.leftPad("", 16, " "));
			request.setTerminal("00998");
			request.setOperador(StringUtils.leftPad("", 8, " "));
			request.setAbaEmisor("0000597777");
			request.setCodigoServicio(StringUtils.leftPad("", 3, " "));
			request.setOrigenTransaccion(StringUtils.leftPad("", 3, " "));
			//request.setSecuencia("002903");
			request.setSecuencia(obtenerSecuencial());
			request.setTipoProceso(TipoProceso.CUENTA);
			request.setPista2(StringUtils.leftPad("", 40, " "));
			request.setPista3(StringUtils.leftPad("", 110, " "));
			request.setReferencia2(StringUtils.leftPad("", 40, " "));
			request.setReferencia3(StringUtils.leftPad("", 40, " "));
			request.setReferencia4(StringUtils.leftPad("", 40, " "));
			request.setReferencia5(StringUtils.leftPad("", 40, " "));
			request.setMonto2(0);
			request.setFecha(null);
			

			String respuestaFinal =ejecutarOperacionAsync(TipoOperacion.CONSULTA_SALDOS,request);
			
			if(respuestaFinal!=null && !respuestaFinal.trim().equalsIgnoreCase("")){
				TramaComercios respuestaRecibida = new TramaComercios(respuestaFinal);
				
				if(respuestaRecibida.getError().equals("0000")){ 
					
					respuestaConsulta.setExitoOperacion(true);
					respuestaConsulta.setMensajeError("Transaccion Exitosa");
					respuestaConsulta.setRespuesta("0000");
					respuestaConsulta.setSaldoDisponible(respuestaRecibida.getSaldoDisponible());
					respuestaConsulta.setSaldoContable(respuestaRecibida.getSaldoContable());
					
					System.out.println("Saldo Disponible Consulta: "+respuestaConsulta.getSaldoDisponible());
					
				}else{
					
					respuestaConsulta.setRespuesta(respuestaRecibida.getError());
					respuestaConsulta.setSaldoDisponible(0);
					respuestaConsulta.setSaldoContable(0);
					
					throw new Exception("Error "+respuestaRecibida.getError()+" de postillion");
				}
			}else{
				
				respuestaConsulta.setRespuesta("00E1");
				respuestaConsulta.setSaldoDisponible(0);
				respuestaConsulta.setSaldoContable(0);
				
				throw new Exception("No se recibio respuesta de postillion");
			}
		}catch(Exception ex){
			respuestaConsulta.setExitoOperacion(false);
			respuestaConsulta.setMensajeError(ex.getMessage());
		}
		
		return respuestaConsulta;
	}

	/**
	 * Permite realizar el bloqueo de una tarjeta de credito
	 * @param tarjeta La tarjeta a bloquear
	 */
	/*public void bloquearTarjeta(String tarjeta)throws Exception{
		TramaComercios request = new TramaComercios();
		
		request.setCodigoTransaccion(CodigoTransaccion.BLOQUEO_TARJETA);
		request.setTipoTransaccion(TipoTransaccion.ORIGINAL);
		request.setTarjeta(StringUtils.leftPad(tarjeta, 20, "0"));
		request.setCuenta(StringUtils.leftPad("", 24, "0"));
		request.setError(StringUtils.leftPad("", 4, " "));
		request.setErrorReal(StringUtils.leftPad("", 6, " "));
		request.setMoneda(StringUtils.leftPad("", 3, " "));
		request.setMonto(0);
		request.setComision(0);
		request.setReferencia(StringUtils.leftPad("", 40, " "));
		request.setClave(StringUtils.leftPad("", 16, " "));
		request.setTerminal("00998");
		request.setOperador(StringUtils.leftPad("", 8, " "));
		request.setAbaEmisor("0000597777");
		request.setCodigoServicio(StringUtils.leftPad("", 3, " "));
		request.setOrigenTransaccion(StringUtils.leftPad("", 3, " "));
		//request.setSecuencia("002903");
		request.setSecuencia(obtenerSecuencial());
		request.setTipoProceso(TipoProceso.CUENTA);
		request.setPista2(StringUtils.leftPad("", 40, " "));
		request.setPista3(StringUtils.leftPad("", 110, " "));
		request.setReferencia2(StringUtils.leftPad("", 40, " "));
		request.setReferencia3(StringUtils.leftPad("", 40, " "));
		request.setReferencia4(StringUtils.leftPad("", 40, " "));
		request.setReferencia5(StringUtils.leftPad("", 40, " "));
		request.setMonto2(0);
		request.setFecha(null);
		
		this.ejecutarOperacionAsync(TipoOperacion.BLOQUEO_TARJETA,request);
		
	}*/
	
	@Override
	public RespuestaConsulta debito(TipoCuenta tipo, String cuenta, double saldo,double comision) {
		CodigoTransaccion codigoTransaccion;
		
		switch (tipo) {
		case AHORROS:
			codigoTransaccion = CodigoTransaccion.TRX_CTA_AHO_DEB;
			break;
		case CORRIENTE:
			codigoTransaccion = CodigoTransaccion.TRX_CTA_CTE_DEB;
			break;
		default:
			codigoTransaccion = CodigoTransaccion.CONSULTA_AHORROS;
	}
		TramaComercios request = new TramaComercios();
		request.setCodigoTransaccion(codigoTransaccion);
		request.setMonto(saldo);
		request.setComision(comision);
		request.setCuenta(StringUtils.leftPad(cuenta, 24, "0"));
		request.setMonto2(saldo);
		return actualizarSaldoOffline(request);
	}
	@Override
	public RespuestaConsulta credito(TipoCuenta tipo, String cuenta, double saldo,double comision) {
		CodigoTransaccion codigoTransaccion;
		
		switch (tipo) {
		case AHORROS:
			codigoTransaccion = CodigoTransaccion.TRX_CTA_AHO_CRED;
			break;
		case CORRIENTE:
			codigoTransaccion = CodigoTransaccion.TRX_CTA_CTE_CRED;
			break;
		default:
			codigoTransaccion = CodigoTransaccion.CONSULTA_AHORROS;
	}
		TramaComercios request = new TramaComercios();
		request.setCodigoTransaccion(codigoTransaccion);
		request.setMonto(saldo);
		request.setComision(comision);
		request.setCuenta(StringUtils.leftPad(cuenta, 24, "0"));
		request.setMonto2(saldo);
		return actualizarSaldoOffline(request);
	}
	private RespuestaConsulta actualizarSaldoOffline(TramaComercios request) {
		
		RespuestaConsulta respuestaConsulta = new RespuestaConsulta();

		try{
				
//          request.setCodigoTransaccion(codigoTransaccion);
			request.setTipoTransaccion(TipoTransaccion.ORIGINAL);
			request.setTarjeta(StringUtils.leftPad("", 20, "0"));
//			request.setCuenta(StringUtils.leftPad(cuenta, 24, "0"));
			request.setError(StringUtils.leftPad("", 4, " "));
			request.setErrorReal(StringUtils.leftPad("", 6, " "));
			request.setMoneda(StringUtils.leftPad("", 3, " "));
//          request.setMonto(saldo);
//          request.setComision(comision);
			request.setReferencia(StringUtils.leftPad("", 40, " "));
			request.setClave(StringUtils.leftPad("", 16, " "));
			request.setTerminal("00998");
			request.setOperador(StringUtils.leftPad("", 8, " "));
			request.setAbaEmisor("0000597777");
			request.setCodigoServicio(StringUtils.leftPad("", 3, " "));
			request.setOrigenTransaccion(StringUtils.leftPad("", 3, " "));
//          request.setSecuencia("002903");
			request.setSecuencia(obtenerSecuencial());
			request.setTipoProceso(TipoProceso.CUENTA);
			request.setPista2(StringUtils.leftPad("", 40, " "));
			request.setPista3(StringUtils.leftPad("", 110, " "));
			request.setReferencia2(StringUtils.leftPad("", 40, " "));
			request.setReferencia3(StringUtils.leftPad("", 40, " "));
			request.setReferencia4(StringUtils.leftPad("", 40, " "));
			request.setReferencia5(StringUtils.leftPad("", 40, " "));
//          request.setMonto2(saldo);
			request.setFecha(null);
				
			String respuestaFinal = this.ejecutarOperacionAsync(TipoOperacion.TRANSFERENCIA,request);
			// this.ejecutarOperacionAsync2(TipoOperacion.TRANSFERENCIA,request);
			//String respuestaFinal = obtenerRespuesta();
			
			if(respuestaFinal!=null && !respuestaFinal.trim().equalsIgnoreCase("")){
				TramaComercios respuestaRecibida = new TramaComercios(respuestaFinal);
				
				if(respuestaRecibida.getError().equals("0000")){ 
					
					respuestaConsulta.setExitoOperacion(true);
					respuestaConsulta.setMensajeError("Transaccion Exitosa");
					respuestaConsulta.setRespuesta("0000");
					respuestaConsulta.setSaldoDisponible(respuestaRecibida.getSaldoDisponible());
					respuestaConsulta.setSaldoContable(respuestaRecibida.getSaldoContable());
					
					System.out.println("Saldo Disponible actualiza: "+respuestaConsulta.getSaldoDisponible());
					
				}else{
					
					respuestaConsulta.setRespuesta(respuestaRecibida.getError());
					respuestaConsulta.setSaldoDisponible(0);
					respuestaConsulta.setSaldoContable(0);
					
					throw new Exception("Error "+respuestaRecibida.getError()+" de postillion");
				}
			}else{
				
				respuestaConsulta.setRespuesta("00E1");
				respuestaConsulta.setSaldoDisponible(0);
				respuestaConsulta.setSaldoContable(0);
				
				throw new Exception("No se recibio respuesta de postillion");
			}
			
		}catch(Exception ex){
			respuestaConsulta.setExitoOperacion(false);
			respuestaConsulta.setMensajeError(ex.getMessage());
		}
		
		return respuestaConsulta;
	}
	
	
	public static void main(String[] args) {
		try {
			
//			IClientePostillion cliente =  ClientePostillion.getClientePostillion("172.16.30.93",2222);
			for(;;) {
				System.out.println(obtenerSecuencial());	
			}
			
			//String cuenta []= {"0021337051","0021157665","0002943804"};
			 
			//for (String cta : cuenta ) {
			//RespuestaConsulta salida = cliente.consultarSaldoOffline(TipoCuenta.CORRIENTE,"0025024340");
	//		RespuestaConsulta salida = cliente.credito(TipoCuenta.CORRIENTE, "1305020096",1.00, 0.00);
			
	//		if(salida.isExitoOperacion()){
	//			System.out.println("Saldo Disponible: "+salida.getSaldoDisponible());
	//			System.out.println("Saldo Contable: "+salida.getSaldoContable());
	//		}else{
	//			System.out.println(salida.getRespuesta());
	//			System.out.println(salida.getMensajeError());
	//		}
			//}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
