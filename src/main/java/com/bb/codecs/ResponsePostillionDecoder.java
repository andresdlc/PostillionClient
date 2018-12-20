package com.bb.codecs;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * Decoder de trama de postillion
 * @author jvillavc
 *
 */
public class ResponsePostillionDecoder extends CumulativeProtocolDecoder{

	private int bitsLeidos = 0;
	private String bufferSalida = "";
	
	@Override
	protected boolean doDecode(IoSession sesion, IoBuffer buffer, ProtocolDecoderOutput salida) throws Exception {
		
		boolean continuarDecodificacion = false;
		
		if(bitsLeidos < 515){ //515 caracteres exceptuando fecha, por algun motivo no la envian
			char bitRespuesta = (char)buffer.get();
			bufferSalida = bufferSalida + Character.toString(bitRespuesta);
			bitsLeidos++;
			continuarDecodificacion = true;
		}else{
			//System.out.println("### La respuesta fue: "+ bufferSalida +" ###");
			salida.write(bufferSalida);
		}
		
		return continuarDecodificacion;
	}

}
