/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

/**
 *
 * @author pgrande
 */
public class PerfilModuloDTO {
    private int idPerMod;
    private int idPerfil;
    private int idModulo;
    private int orden;
    private String nombreModulo;

    public int getIdPerMod() {
        return idPerMod;
    }

    public void setIdPerMod(int idPerMod) {
        this.idPerMod = idPerMod;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

}
