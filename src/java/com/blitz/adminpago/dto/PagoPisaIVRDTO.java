/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.dto;

/**
 * clase de que mapea los parametros para enviar el pago a pisa via IVR
 * @author PGRANDE
 */
public class PagoPisaIVRDTO {

    private String bibliotecaObj;
    private String biblioteca;
    private String ticket; //secuencia = idPago
    private String telefono;
    private String fechaPago;
    private String horaPago;
    private String caja;
    private String adeudo;
    private String importePago;
    private String numTarjeta;
    private String fechaVenc;
    private String autorizacion;
    
    private String programa;
    private String servicio;
    private String clavePago;
    private String tipoPago;
    private String oficinaComercial;
    private String monedaCambio;
    private String importeCambio;
    private String tipoCambio;
    

    
    public PagoPisaIVRDTO() {
    	this.bibliotecaObj = "PREACJ0099";
        this.adeudo = "0000000000000";
        this.programa = "CJ0099";
        this.servicio = "07";
        this.clavePago = "INP";
        this.oficinaComercial = "APT";
        this.tipoPago = "NOT";
        //this.tipoPago = "ASD";
        this.monedaCambio = "P";
        this.importeCambio = "000000000";
        this.tipoCambio = "000000000";   
    }
    
    //utilizarse para pruebas
    public PagoPisaIVRDTO(String telefono) {
    	this.bibliotecaObj = "PREACJ0099";
        this.adeudo = "0000000095020";
        this.programa = "CJ0099";
        this.servicio = "07";
        //APE 10 INP 07/09
        this.clavePago = "INP";
        this.oficinaComercial = "APT";
        this.tipoPago = "ASD";
        this.monedaCambio = "P";
        this.importeCambio = "000000000";
        this.tipoCambio = "000000000";   
        //no estaticos
        this.biblioteca = "MEXV9";
        this.telefono = telefono;
//        this.ticket = "123456789012";
//        this.fechaPago = "20160826";
//        this.caja = "APT160826";
//        this.importePago = "0000000030000";
//        this.horaPago = "160031";
//        this.numTarjeta = "5290280047725192";
//        this.autorizacion = "456789";
//        this.fechaVenc = "1805";
    }

    /**
     * @return the bibliotecaObj
     */
    public String getBibliotecaObj() {
        return bibliotecaObj;
    }

    /**
     * @param bibliotecaObj the bibliotecaObj to set
     */
    public void setBibliotecaObj(String bibliotecaObj) {
        this.bibliotecaObj = bibliotecaObj;
    }

    /**
     * @return the biblioteca
     */
    public String getBiblioteca() {
        return biblioteca;
    }

    /**
     * @param biblioteca the biblioteca to set
     */
    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    /**
     * @return the ticket
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * @param ticket the ticket to set
     */
    public void setTicket(String ticket) {
        this.ticket = ticket;
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
     * @return the fechaPago
     */
    public String getFechaPago() {
        return fechaPago;
    }

    /**
     * @param fechaPago the fechaPago to set
     */
    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    /**
     * @return the horaPago
     */
    public String getHoraPago() {
        return horaPago;
    }

    /**
     * @param horaPago the horaPago to set
     */
    public void setHoraPago(String horaPago) {
        this.horaPago = horaPago;
    }

    /**
     * @return the caja
     */
    public String getCaja() {
        return caja;
    }

    /**
     * @param caja the caja to set
     */
    public void setCaja(String caja) {
        this.caja = caja;
    }

    /**
     * @return the adeudo
     */
    public String getAdeudo() {
        return adeudo;
    }

    /**
     * @param adeudo the adeudo to set
     */
    public void setAdeudo(String adeudo) {
        this.adeudo = adeudo;
    }

    /**
     * @return the importePago
     */
    public String getImportePago() {
        return importePago;
    }

    /**
     * @param importePago the importePago to set
     */
    public void setImportePago(String importePago) {
        this.importePago = importePago;
    }

    /**
     * @return the numTarjeta
     */
    public String getNumTarjeta() {
        return numTarjeta;
    }

    /**
     * @param numTarjeta the numTarjeta to set
     */
    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    /**
     * @return the fechaVenc
     */
    public String getFechaVenc() {
        return fechaVenc;
    }

    /**
     * @param fechaVenc the fechaVenc to set
     */
    public void setFechaVenc(String fechaVenc) {
        this.fechaVenc = fechaVenc;
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
     * @return the programa
     */
    public String getPrograma() {
        return programa;
    }

    /**
     * @param programa the programa to set
     */
    public void setPrograma(String programa) {
        this.programa = programa;
    }

    /**
     * @return the servicio
     */
    public String getServicio() {
        return servicio;
    }

    /**
     * @param servicio the servicio to set
     */
    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    /**
     * @return the clavePago
     */
    public String getClavePago() {
        return clavePago;
    }

    /**
     * @param clavePago the clavePago to set
     */
    public void setClavePago(String clavePago) {
        this.clavePago = clavePago;
    }

    /**
     * @return the oficinaComercial
     */
    public String getOficinaComercial() {
        return oficinaComercial;
    }

    /**
     * @param oficinaComercial the oficinaComercial to set
     */
    public void setOficinaComercial(String oficinaComercial) {
        this.oficinaComercial = oficinaComercial;
    }

    /**
     * @return the tipoPago
     */
    public String getTipoPago() {
        return tipoPago;
    }

    /**
     * @param tipoPago the tipoPago to set
     */
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    /**
     * @return the monedaCambio
     */
    public String getMonedaCambio() {
        return monedaCambio;
    }

    /**
     * @param monedaCambio the monedaCambio to set
     */
    public void setMonedaCambio(String monedaCambio) {
        this.monedaCambio = monedaCambio;
    }

    /**
     * @return the importeCambio
     */
    public String getImporteCambio() {
        return importeCambio;
    }

    /**
     * @param importeCambio the importeCambio to set
     */
    public void setImporteCambio(String importeCambio) {
        this.importeCambio = importeCambio;
    }

    /**
     * @return the tipoCambio
     */
    public String getTipoCambio() {
        return tipoCambio;
    }

    /**
     * @param tipoCambio the tipoCambio to set
     */
    public void setTipoCambio(String tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    
    
}
