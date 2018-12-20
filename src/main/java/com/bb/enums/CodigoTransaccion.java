package com.bb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Categorias de transacciones disponibles para interactuar con el modulo de comercios postillion
 * @author jvillavc
 *
 */
public enum CodigoTransaccion {
	TRX_CTA_CTE_CRED("0415"),
	TRX_CTA_AHO_CRED("0515"),
	TRX_CTA_CTE_DEB("0430"),
	TRX_CTA_AHO_DEB("0530"),
	CONSULTA_CORRIENTE("0475"),
	CONSULTA_AHORROS("0575"),
	CONSULTA_VIRTUAL("0375"),
	BLOQUEO_TARJETA("0415");
	
	private String codigo;
	private static final Map<String,CodigoTransaccion>lookup = new HashMap<>();
	
	static{
		for(CodigoTransaccion codigo:CodigoTransaccion.values()){
			lookup.put(codigo.getCodigo(),codigo);
		}
	}
	
	CodigoTransaccion(String codigo){
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public static CodigoTransaccion get(String codigo){
		return lookup.get(codigo);
	}
	
}
