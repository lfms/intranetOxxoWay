/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.dto;

import java.util.Date;

/**
 *
 * @author SMMIGUEL
 */
public class ConciliacionDTO {

    private long idConciliacion;
    private Date fecha;
    private long totalRegistros;
    private String archivo;
    private String estado;
    private String proceso;
    private String viaCobro;
    private String libreria;
    private String importe;

    /**
     * @return the idConciliacion
     */
    public long getIdConciliacion() {
        return idConciliacion;
    }

    /**
     * @param idConciliacion the idConciliacion to set
     */
    public void setIdConciliacion(long idConciliacion) {
        this.idConciliacion = idConciliacion;
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
     * @return the proceso
     */
    public String getProceso() {
        return proceso;
    }

    /**
     * @param proceso the proceso to set
     */
    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    /**
     * @return the viaCobro
     */
    public String getViaCobro() {
        return viaCobro;
    }

    /**
     * @param viaCobro the viaCobro to set
     */
    public void setViaCobro(String viaCobro) {
        this.viaCobro = viaCobro;
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
     * @return the importe
     */
    public String getImporte() {
        return importe;
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(String importe) {
        this.importe = importe;
    }
}
