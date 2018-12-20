package com.bb.cliente;

import java.util.concurrent.Callable;

import java.util.concurrent.ConcurrentMap;

//import org.apache.log4j.Logger;

public class ResponseTask implements Callable<String> {    
        long correlationId;
//        private static Logger logger = Logger.getLogger(ResponseTask.class);
        private String respuesta = null;
        @SuppressWarnings("rawtypes")
		private ConcurrentMap responseMap;
            
        public ResponseTask(long correlationId) {
            this.correlationId = correlationId;
        }    
        
        public String getResponse(){
            return respuesta;
        }
        
        public String call() {
          //System.out.println("<RespuestaTask><run>begin - correlationId="+correlationId);
            int timeout = 30000;
            int contador = 0;
            while(respuesta == null && contador<=(timeout*5)/1000){
                respuesta = (String)responseMap.remove(correlationId);
                try {
                    Thread.sleep(200);
                    //System.out.println("<RespuestaTask><run>.....finding correlationId="+correlationId);
                    contador ++;
                } catch (InterruptedException e) {
                }
            }
            //System.out.println("<RespuestaTask><run>end - correlationId="+correlationId+"- respuesta="+respuesta);    
            return respuesta;        
        }


    public void setResponseMap(@SuppressWarnings("rawtypes") ConcurrentMap responseMap) {
        this.responseMap = responseMap;
    }
}
