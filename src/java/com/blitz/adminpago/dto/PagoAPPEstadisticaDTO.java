/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

/**
 *
 * @author H. Fabi�n Junco
 */
public class PagoAPPEstadisticaDTO {
private String estado="";
private String totalPagos="0";
private String importe="0";
private String tipo="";


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(String totalPagos) {
        this.totalPagos = totalPagos;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
}
