package com.bb.cliente;

import java.io.Serializable;

/**
 * Representacion de respuesta de consulta de saldo
 * @author jvillavc
 *
 */
public class RespuestaConsulta implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5619899969765954060L;
	private boolean exitoOperacion;
	private String respuesta;
	private String mensajeError;
	private double saldoDisponible;
	private double saldoContable;
	
	public RespuestaConsulta(){
		exitoOperacion = false;
		respuesta = "001";
		mensajeError = "No definido";
		saldoDisponible = 0;
		saldoContable = 0;
	}
	
	public boolean isExitoOperacion() {
		return exitoOperacion;
	}

	public void setExitoOperacion(boolean exitoOperacion) {
		this.exitoOperacion = exitoOperacion;
	}

	public String getRespuesta() {
		return respuesta;
	}
	
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	public String getMensajeError() {
		return mensajeError;
	}
	
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	
	public double getSaldoDisponible() {
		return saldoDisponible;
	}
	
	public void setSaldoDisponible(double saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}
	
	public double getSaldoContable() {
		return saldoContable;
	}
	
	public void setSaldoContable(double saldoContable) {
		this.saldoContable = saldoContable;
	}
}
