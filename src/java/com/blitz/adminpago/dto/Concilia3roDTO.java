/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.util.Date;

/**
 *
 * @author PGRANDE
 */
public class Concilia3roDTO {

    private long idConciliacionTro;
    private Date fecha;
    private String archivo;
    private String comercio;
    private long totalRegistros;
    private double montoTotal;
    private String estado;
    private Date fechaMin; // la establecemos para acotar la busqueda de los pagos por fecha


    /**
     * @return the idConciliacionTro
     */
    public long getIdConciliacionTro() {
        return idConciliacionTro;
    }

    /**
     * @param idConciliacionTro the idConciliacionTro to set
     */
    public void setIdConciliacionTro(long idConciliacionTro) {
        this.idConciliacionTro = idConciliacionTro;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the archivo
     */
    public String getArchivo() {
        return archivo;
    }

    /**
     * @param archivo the archivo to set
     */
    public void setArchivo(String archivo) {
        this.archivo = archivo;
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
     * @return the totalRegistros
     */
    public long getTotalRegistros() {
        return totalRegistros;
    }

    /**
     * @param totalRegistros the totalRegistros to set
     */
    public void setTotalRegistros(long totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    /**
     * @return the montoTotal
     */
    public double getMontoTotal() {
        return montoTotal;
    }

    /**
     * @param montoTotal the montoTotal to set
     */
    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the fechaMin
     */
    public Date getFechaMin() {
        return fechaMin;
    }

    /**
     * @param fechaMin the fechaMin to set
     */
    public void setFechaMin(Date fechaMin) {
        this.fechaMin = fechaMin;
    }


}
