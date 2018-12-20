package com.bb.cliente.dto;

import java.util.Date;

import com.bb.enums.CodigoTransaccion;
import com.bb.enums.TipoProceso;
import com.bb.enums.TipoTransaccion;

/**
 * Representacion de estructura de comunicacion del modulo de comercios Postillion
 * @author jvillavc
 *
 */
public class TramaComercios {
	private CodigoTransaccion codigoTransaccion;
	private TipoTransaccion tipoTransaccion;
	private String tarjeta;
	private String cuenta;
	private String error;
	private String errorReal;
	private String moneda;
	private double monto;
	private double comision;
	private String referencia;
	private String clave;
	private String terminal;
	private String operador;
	private String abaEmisor;
	private String codigoServicio;
	private String origenTransaccion;
	private String secuencia;
	private TipoProceso tipoProceso;
	private String pista2;
	private String pista3;
	private String referencia2;
	private String referencia3;
	private String referencia4;
	private String referencia5;
	private double monto2;
	private Date fecha;
	private double saldoDisponible;
	private double saldoContable;
	
	public TramaComercios(){
		
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

	public TramaComercios(String trama){
		this.codigoTransaccion = CodigoTransaccion.get(trama.substring(0,4));
		this.tipoTransaccion = TipoTransaccion.get(trama.substring(4,5));
		this.tarjeta = obtenerCadena(trama,5,24);
		this.cuenta = obtenerCadena(trama,25,49);
		this.error = obtenerCadena(trama,49,53);
		this.errorReal = obtenerCadena(trama,53,59);
		this.moneda = obtenerCadena(trama,59,62);
		this.monto = obtenerValorMonetario(trama,62,79);
		this.comision = obtenerValorMonetario(trama,79,96);
		this.referencia = obtenerCadena(trama,96,136);
		this.clave = obtenerCadena(trama,136,152);
		this.terminal = obtenerCadena(trama,152,157);
		this.operador = obtenerCadena(trama,157,165);
		this.abaEmisor = obtenerCadena(trama,165,175);
		this.codigoServicio = obtenerCadena(trama,175,178);
		this.origenTransaccion = obtenerCadena(trama,178,181);
		this.secuencia = obtenerCadena(trama,181,187);
		this.tipoProceso = TipoProceso.get(obtenerCadena(trama,187,188));
		this.pista2 = obtenerCadena(trama,188,228);
		this.pista3 = obtenerCadena(trama,228,338);
		this.referencia2 = obtenerCadena(trama,338,378); // reutilizada para saldo disponible
		this.referencia3 = obtenerCadena(trama,378,418); // reutilizada para saldo contable
		this.referencia4 = obtenerCadena(trama,418,458);
		this.referencia5 = obtenerCadena(trama,458,498);
		this.monto2 = obtenerValorMonetario(trama,498,515);
		
		this.saldoDisponible = obtenerValorMonetarioSaldo(obtenerCadena(trama,338,378));
		this.saldoContable = obtenerValorMonetarioSaldo(obtenerCadena(trama,378,419));
	}
	
	/**
	 * Obtiene un elemento string de la trama indicada de forma segura
	 * @param trama La trama a evaluar
	 * @param posicionInicial Posicion inicial de palabra
	 * @param posicionFinal Posicion final de palabra
	 * @return La cadena resultante
	 */
	private String obtenerCadena(String trama,int posicionInicial,int posicionFinal){
		String resultado = "";
		
		try{
			resultado = trama.substring(posicionInicial,posicionFinal);
		}catch(Exception e){}
		
		return resultado;
	}
	
	/**
	 * Obtiene el valor monetario de una trama asumiendo formato estandar
	 * @param trama La trama a evaluar
	 * @param posicionInicial Posicion inicial de la palabra
	 * @param posicionFinal Posicion final de la palabra
	 * @return El valor resultante
	 */
	private double obtenerValorMonetario(String trama,int posicionInicial,int posicionFinal){
		double valor = 0;
		
		try{
			valor = Double.parseDouble(trama.substring(posicionInicial,posicionFinal));
		}catch(Exception e){}
		
		return valor;
	}
	
	/**
	 * Convierte el valor monetario de la trama propietaria de postillion en la forma +00000000000000000
	 * @param valorInicial El segmento de la trama de postillion con el valor monetario en formato propietario
	 * @return El valor monetario en formato decimal
	 */
	private double obtenerValorMonetarioSaldo(String valorInicial){
		double valor = 0;
		
		try{
			valor = Double.parseDouble(valorInicial.substring(1,11).replaceFirst("^0+(?!$)", ""));
			double decimales = Double.parseDouble(valorInicial.substring(11,13));
			
			valor = valor + (decimales/100);
		}catch(Exception e){}
		
		return valor;
	}
	

	public TramaComercios(CodigoTransaccion codigoTransaccion, TipoTransaccion tipoTransaccion, String tarjeta,
			String cuenta, String error, String errorReal, String moneda, double monto, double comision,
			String referencia, String clave, String terminal, String operador, String abaEmisor, String codigoServicio,
			String origenTransaccion, String secuencia, TipoProceso tipoProceso, String pista2, String pista3,
			String referencia2, String referencia3, String referencia4, String referencia5, double monto2,
			Date fecha) {
		super();
		this.codigoTransaccion = codigoTransaccion;
		this.tipoTransaccion = tipoTransaccion;
		this.tarjeta = tarjeta;
		this.cuenta = cuenta;
		this.error = error;
		this.errorReal = errorReal;
		this.moneda = moneda;
		this.monto = monto;
		this.comision = comision;
		this.referencia = referencia;
		this.clave = clave;
		this.terminal = terminal;
		this.operador = operador;
		this.abaEmisor = abaEmisor;
		this.codigoServicio = codigoServicio;
		this.origenTransaccion = origenTransaccion;
		this.secuencia = secuencia;
		this.tipoProceso = tipoProceso;
		this.pista2 = pista2;
		this.pista3 = pista3;
		this.referencia2 = referencia2;
		this.referencia3 = referencia3;
		this.referencia4 = referencia4;
		this.referencia5 = referencia5;
		this.monto2 = monto2;
		this.fecha = fecha;
		this.saldoDisponible = 0;
		this.saldoContable = 0;
	}

	public CodigoTransaccion getCodigoTransaccion() {
		return codigoTransaccion;
	}

	public void setCodigoTransaccion(CodigoTransaccion codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}

	public TipoTransaccion getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorReal() {
		return errorReal;
	}

	public void setErrorReal(String errorReal) {
		this.errorReal = errorReal;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getComision() {
		return comision;
	}

	public void setComision(double comision) {
		this.comision = comision;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public String getAbaEmisor() {
		return abaEmisor;
	}

	public void setAbaEmisor(String abaEmisor) {
		this.abaEmisor = abaEmisor;
	}

	public String getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getOrigenTransaccion() {
		return origenTransaccion;
	}

	public void setOrigenTransaccion(String origenTransaccion) {
		this.origenTransaccion = origenTransaccion;
	}

	public String getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

	public TipoProceso getTipoProceso() {
		return tipoProceso;
	}

	public void setTipoProceso(TipoProceso tipoProceso) {
		this.tipoProceso = tipoProceso;
	}

	public String getPista2() {
		return pista2;
	}

	public void setPista2(String pista2) {
		this.pista2 = pista2;
	}

	public String getPista3() {
		return pista3;
	}

	public void setPista3(String pista3) {
		this.pista3 = pista3;
	}

	public String getReferencia2() {
		return referencia2;
	}

	public void setReferencia2(String referencia2) {
		this.referencia2 = referencia2;
	}

	public String getReferencia3() {
		return referencia3;
	}

	public void setReferencia3(String referencia3) {
		this.referencia3 = referencia3;
	}

	public String getReferencia4() {
		return referencia4;
	}

	public void setReferencia4(String referencia4) {
		this.referencia4 = referencia4;
	}

	public String getReferencia5() {
		return referencia5;
	}

	public void setReferencia5(String referencia5) {
		this.referencia5 = referencia5;
	}

	public double getMonto2() {
		return monto2;
	}

	public void setMonto2(double monto2) {
		this.monto2 = monto2;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getMontoTrama() {
		return String.format("%018.2f", this.getMonto()).replace(",", "").replace(".","");
	}
	
	public String getComisionTrama() {
		return String.format("%018.2f", this.getComision()).replace(",", "").replace(".","");
	}
	
	public String getSaldoDisponible1() {
		return String.format("%018.2f", this.saldoDisponible);
	}

	public String getSaldoContable1() {
		return String.format("%018.2f", this.saldoContable);
	}	

	
	
	/*public static void main(String[] args) {
	 //String ejemplo = "0475O5977770000000000    0000000000000000050367160000         0000000000000000000000000C00000000                                                        00998        0000597777      002898C                                                                                                                                                      +000014927412                           +000014927412                                                                                                                           ";
		String ejemplo ="0475O5977770000000000    0000000000000030000253810000         0000000000000000000000000C00000000                                                        00998        0000597777      121844C                                                                                                                                                      +050000520307                           +049999999700                                                                                                                            ";		
		System.out.println(ejemplo.substring(79,96));
		
		TramaComercios trama = new TramaComercios(ejemplo);
		System.out.println(trama.getTipoTransaccion());
		System.out.println(trama.getCodigoTransaccion());
		System.out.println(trama.getTarjeta());
		System.out.println(trama.getCuenta());
		System.out.println(trama.getError());
		System.out.println(trama.getMonto());
		
		System.out.println("#############");
		System.out.println(trama.getReferencia());
		System.out.println(trama.getReferencia2().substring(1,11));
		System.out.println(trama.getReferencia3());
		System.out.println(trama.getReferencia4());
		System.out.println(trama.getReferencia5());
		System.out.println(trama.getSaldoDisponible1());
		System.out.println(trama.getSaldoDisponible());
		System.out.println(trama.getSaldoContable1());
		
	}*/
}
