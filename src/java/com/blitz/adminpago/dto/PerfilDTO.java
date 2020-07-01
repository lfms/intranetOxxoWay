/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;
import java.util.List;

public class PerfilDTO implements Serializable{

    private String idPerfil;
    private String idSubPerfil;
    private String nombre;
    private String tienda;
    private List perfilModulo;
    private String nomModulos;
    private List perfilModDisponibles;
    private int ultOrden;

    private String idComercio;

    public String getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(String idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public List getPerfilModulo() {
        return perfilModulo;
    }

    public void setPerfilModulo(List perfilModulo) {
        this.perfilModulo = perfilModulo;
    }

    public String getNomModulos() {
        return nomModulos;
    }

    public void setNomModulos(String nomModulos) {
        this.nomModulos = nomModulos;
    }

    public List getPerfilModDisponibles() {
        return perfilModDisponibles;
    }

    public void setPerfilModDisponibles(List perfilModDisponibles) {
        this.perfilModDisponibles = perfilModDisponibles;
    }

    public int getUltOrden() {
        return ultOrden;
    }

    public void setUltOrden(int ultOrden) {
        this.ultOrden = ultOrden;
    }

    public String getIdSubPerfil() {
        return idSubPerfil;
    }

    public void setIdSubPerfil(String idSubPerfil) {
        this.idSubPerfil = idSubPerfil;
    }

    public String getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(String idComercio) {
        this.idComercio = idComercio;
    }
}
