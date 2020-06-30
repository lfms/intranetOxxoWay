/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;

/**
 *
 * @author pgrande
 */
public class ModuloDTO implements Serializable{

    private int idModulo;
    private String nombre;
    private String url;
    private int orden;
    private String categoria;
    
    public int getIdModulo() {
        return idModulo;
    }
    
    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getUrl() {
        return url;
    }
 
    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrden() {
        return orden;
    }
 
    public void setOrden(int orden) {
        this.orden = orden;
    }
   
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
