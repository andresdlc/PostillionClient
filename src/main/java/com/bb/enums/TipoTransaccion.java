package com.bb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Tipo de transaccion postillion
 * @author jvillavc
 *
 */
public enum TipoTransaccion {
	ORIGINAL("O"),REVERSO("R");
	
	private String tipo;
	private static final Map<String,TipoTransaccion>lookup = new HashMap<>();
	
	static{
		for(TipoTransaccion tipo:TipoTransaccion.values()){
			lookup.put(tipo.getTipo(),tipo);
		}
	}
	
	TipoTransaccion(String tipo){
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}
	
	public static TipoTransaccion get(String codigo){
		return lookup.get(codigo);
	}
}
