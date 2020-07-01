/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PGRANDE
 */
@XmlRootElement (name="root")
public class ReqConsultaPagoDTO {

    private String accion;
    private String biblioteca;
    private String telefono;
    private String oficinaComercial;
    private String caja;
    private String importePago;
    private String fechaPago;
    private String ticket;
    private String numTarjeta;
    private String error;
    private String descerror;

    /**
     * @return the accion
     */
    public String getAccion() {
        return accion;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
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
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the descerror
     */
    public String getDescerror() {
        return descerror;
    }

    /**
     * @param descerror the descerror to set
     */
    public void setDescerror(String descerror) {
        this.descerror = descerror;
    }

    @Override
    public String toString() {
        return "ReqConsultaPagoDTO{" + "accion=" + accion + "biblioteca=" + biblioteca + "telefono=" + telefono + "oficinaComercial=" + oficinaComercial + "caja=" + caja + "importePago=" + importePago + "fechaPago=" + fechaPago + "ticket=" + ticket + "numTarjeta=" + numTarjeta + "error=" + error + "descerror=" + descerror + '}';
    }

    public String toCSV() {
        return accion + "," + biblioteca + "," + telefono + "," + oficinaComercial + "," +
                caja + "," + importePago + "," + fechaPago + "," + ticket + "," +
                numTarjeta ;
    }

    public String toCSV2() {
        return accion + "," + biblioteca + "," + telefono + "," + oficinaComercial + "," +
                caja + "," + importePago + "," + fechaPago + "," + ticket + "," +
                numTarjeta + ","+ error + "," + descerror;
    }


}
