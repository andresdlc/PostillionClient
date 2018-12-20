package com.bb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Tipos de procesos postillion
 * @author jvillavc
 *
 */
public enum TipoProceso {
	CUENTA("C"),
	TARJETA("T");
	
	private String tipo;
	private static final Map<String,TipoProceso>lookup = new HashMap<>();
	
	static{
		for(TipoProceso tipo:TipoProceso.values()){
			lookup.put(tipo.getTipo(),tipo);
		}
	}
	
	TipoProceso(String tipo){
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}
	
	public static TipoProceso get(String codigo){
		return lookup.get(codigo);
	}
	
}
