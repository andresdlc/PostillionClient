package com.bb.codecs;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Factory de codec de postillion
 * @author jvillavc
 *
 */
public class PostillionCodecFactory implements ProtocolCodecFactory{

	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return new ResponsePostillionDecoder();
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return new RequestPostillionEncoder();
	}

}
