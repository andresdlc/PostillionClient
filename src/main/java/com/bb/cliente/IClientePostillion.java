package com.bb.cliente;

import com.bb.enums.TipoCuenta;

/**
 * Contrato de cliente postillion
 * @author jvillavc
 *
 */
public interface IClientePostillion {
	
	/**
	 * Permite consultar el saldo de una cuenta en offline
	 * 
	 * @param tipo El tipo de cuenta
	 * @param cuenta La cuenta a consultar
	 * @return El saldo disponible
	 * @throws Exception Error en caso de ocurrir
	 */
	public RespuestaConsulta consultarSaldoOffline(TipoCuenta tipo, String cuenta);
	
	public RespuestaConsulta debito(TipoCuenta tipo, String cuenta,double saldo,double comision);
	public RespuestaConsulta credito(TipoCuenta tipo, String cuenta,double saldo,double comision);
}
