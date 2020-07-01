/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

/**
 *
 * @author PGRANDE
 */
public class PagoConcilPISADTO {

    private String telefono;
    private String transaccion;
    private String secuencia;
    private String fechaOperacion;
    private String horaOperacion;
    private String fechaPosteo;
    private String horaPosteo;
    private String importePagado;
    private String caja;
    private String oficina;
    private String idPago;
    private int noBD; // para identificar si ya fue conciliado en la otra BD

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
     * @return the secuencia
     */
    public String getSecuencia() {
        return secuencia;
    }

    /**
     * @param secuencia the secuencia to set
     */
    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    /**
     * @return the fechaOperacion
     */
    public String getFechaOperacion() {
        return fechaOperacion;
    }

    /**
     * @param fechaOperacion the fechaOperacion to set
     */
    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    /**
     * @return the horaOperacion
     */
    public String getHoraOperacion() {
        return horaOperacion;
    }

    /**
     * @param horaOperacion the horaOperacion to set
     */
    public void setHoraOperacion(String horaOperacion) {
        this.horaOperacion = horaOperacion;
    }

    /**
     * @return the fechaPosteo
     */
    public String getFechaPosteo() {
        return fechaPosteo;
    }

    /**
     * @param fechaPosteo the fechaPosteo to set
     */
    public void setFechaPosteo(String fechaPosteo) {
        this.fechaPosteo = fechaPosteo;
    }

    /**
     * @return the horaPosteo
     */
    public String getHoraPosteo() {
        return horaPosteo;
    }

    /**
     * @param horaPosteo the horaPosteo to set
     */
    public void setHoraPosteo(String horaPosteo) {
        this.horaPosteo = horaPosteo;
    }

    /**
     * @return the importePagado
     */
    public String getImportePagado() {
        return importePagado;
    }

    /**
     * @param importePagado the importePagado to set
     */
    public void setImportePagado(String importePagado) {
        this.importePagado = importePagado;
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
     * @return the oficina
     */
    public String getOficina() {
        return oficina;
    }

    /**
     * @param oficina the oficina to set
     */
    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    /**
     * @return the idPago
     */
    public String getIdPago() {
        return idPago;
    }

    /**
     * @param idPago the idPago to set
     */
    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    /**
     * @return the noBD
     */
    public int getNoBD() {
        return noBD;
    }

    /**
     * @param noBD the noBD to set
     */
    public void setNoBD(int noBD) {
        this.noBD = noBD;
    }

}
