package com.bb.codecs;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Encoder de trama de postillion
 * @author jvillavc
 *
 */
public class RequestPostillionEncoder implements ProtocolEncoder{

	@Override
	public void dispose(IoSession arg0) throws Exception {}

	@Override
	public void encode(IoSession sesion, Object datos, ProtocolEncoderOutput respuesta) throws Exception {
		datos = datos.toString();
		
		IoBuffer ioBuffer = IoBuffer.allocate(3, false);
        ioBuffer.setAutoExpand(true);
        ioBuffer.setAutoShrink(true);
        byte[] responseByteArr = datos.toString().getBytes();
        ioBuffer.put(responseByteArr);
        ioBuffer.flip();
        respuesta.write(ioBuffer);
        
       // System.out.println("*** Escribiendo buffer ***");
        
        respuesta.flush();
	}
	
}
