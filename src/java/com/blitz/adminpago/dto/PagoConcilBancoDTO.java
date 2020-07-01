/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

/**
 *
 * @author PGRANDE
 */
public class PagoConcilBancoDTO {
    private String fecha;
    private String hora;
    private String movimiento;
    private String afiliacion;
    private String autorizacion;
    private String transaccion;
    private String numeroTC;
    private String tipoTC;
    private String monto;
    private String telefono;
    private String comercio;
    private String sucursal;
    private String estatus;
    private String pagoSimilar;
    private String tipoAutorizacion;
    private String autorCobranza;
    private String libreria;
    private String formaPago;
    private String tipoProceso;
    private String foliocpagos; //campo para conciliacion santander

    private String conservarToken;
    private String domiciliarToken;
    private String banco;
    private String debitoCredito;
    private String pagoConToken;

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the hora
     */
    public String getHora() {
        return hora;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * @return the movimiento
     */
    public String getMovimiento() {
        return movimiento;
    }

    /**
     * @param movimiento the movimiento to set
     */
    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    /**
     * @return the afiliacion
     */
    public String getAfiliacion() {
        return afiliacion;
    }

    /**
     * @param afiliacion the afiliacion to set
     */
    public void setAfiliacion(String afiliacion) {
        this.afiliacion = afiliacion;
    }

    /**
     * @return the autorizacion
     */
    public String getAutorizacion() {
        return autorizacion;
    }

    /**
     * @param autorizacion the autorizacion to set
     */
    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    /**
     * @return the transaccion
     */
    public String getTransaccion() {
        return transaccion;
    }

    /**
     * @param transaccion the transaccion to set
     */
    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    /**
     * @return the numeroTC
     */
    public String getNumeroTC() {
        return numeroTC;
    }

    /**
     * @param numeroTC the numeroTC to set
     */
    public void setNumeroTC(String numeroTC) {
        this.numeroTC = numeroTC;
    }

    /**
     * @return the tipoTC
     */
    public String getTipoTC() {
        return tipoTC;
    }

    /**
     * @param tipoTC the tipoTC to set
     */
    public void setTipoTC(String tipoTC) {
        this.tipoTC = tipoTC;
    }

    /**
     * @return the monto
     */
    public String getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(String monto) {
        this.monto = monto;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the comercio
     */
    public String getComercio() {
        return comercio;
    }

    /**
     * @param comercio the comercio to set
     */
    public void setComercio(String comercio) {
        this.comercio = comercio;
    }

    /**
     * @return the estatus
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * @param estatus the estatus to set
     */
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    /**
     * @return the pagoSimilar
     */
    public String getPagoSimilar() {
        return pagoSimilar;
    }

    /**
     * @param pagoSimilar the pagoSimilar to set
     */
    public void setPagoSimilar(String pagoSimilar) {
        this.pagoSimilar = pagoSimilar;
    }

    /**
     * @return the tipoAutorizacion
     */
    public String getTipoAutorizacion() {
        return tipoAutorizacion;
    }

    /**
     * @param tipoAutorizacion the tipoAutorizacion to set
     */
    public void setTipoAutorizacion(String tipoAutorizacion) {
        this.tipoAutorizacion = tipoAutorizacion;
    }

    /**
     * @return the autorCobranza
     */
    public String getAutorCobranza() {
        return autorCobranza;
    }

    /**
     * @param autorCobranza the autorCobranza to set
     */
    public void setAutorCobranza(String autorCobranza) {
        this.autorCobranza = autorCobranza;
    }

    /**
     * @return the libreria
     */
    public String getLibreria() {
        return libreria;
    }

    /**
     * @param libreria the libreria to set
     */
    public void setLibreria(String libreria) {
        this.libreria = libreria;
    }

    /**
     * @return the sucursal
     */
    public String getSucursal() {
        return sucursal;
    }

    /**
     * @param sucursal the sucursal to set
     */
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    /**
     * @return the formaPago
     */
    public String getFormaPago() {
        return formaPago;
    }

    /**
     * @param formaPago the formaPago to set
     */
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    /**
     * @return the tipoProceso
     */
    public String getTipoProceso() {
        return tipoProceso;
    }

    /**
     * @param tipoProceso the tipoProceso to set
     */
    public void setTipoProceso(String tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    /**
     * @return the foliocpagos
     */
    public String getFoliocpagos() {
        return foliocpagos;
    }

    /**
     * @param foliocpagos the foliocpagos to set
     */
    public void setFoliocpagos(String foliocpagos) {
        this.foliocpagos = foliocpagos;
    }

    /**
     * @return the conservarToken
     */
    public String getConservarToken() {
        return conservarToken;
    }

    /**
     * @param conservarToken the conservarToken to set
     */
    public void setConservarToken(String conservarToken) {
        this.conservarToken = conservarToken;
    }

    /**
     * @return the domiciliarToken
     */
    public String getDomiciliarToken() {
        return domiciliarToken;
    }

    /**
     * @param domiciliarToken the domiciliarToken to set
     */
    public void setDomiciliarToken(String domiciliarToken) {
        this.domiciliarToken = domiciliarToken;
    }

    /**
     * @return the banco
     */
    public String getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * @return the debitoCredito
     */
    public String getDebitoCredito() {
        return debitoCredito;
    }

    /**
     * @param debitoCredito the debitoCredito to set
     */
    public void setDebitoCredito(String debitoCredito) {
        this.debitoCredito = debitoCredito;
    }

    /**
     * @return the pagoConToken
     */
    public String getPagoConToken() {
        return pagoConToken;
    }

    /**
     * @param pagoConToken the pagoConToken to set
     */
    public void setPagoConToken(String pagoConToken) {
        this.pagoConToken = pagoConToken;
    }


}
