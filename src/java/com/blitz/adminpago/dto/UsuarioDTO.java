/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;

/**
 *
 * @author SMMIGUEL
 */
public class UsuarioDTO implements Serializable{

    private String usuario;
    private String clave;
    private String dato;
    private int tipoDato;
    private String estrategia;
    private String promotor;
    private String idTienda;
    private String frontera;
    private String nombre;
    private String paterno;
    private String materno;
    private String universal;
    private String idPerfil;
    private String idSubPerfil;
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getClave() {
        return clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public String getDato() {
        return dato;
    }
    
    public void setDato(String dato) {
        this.dato = dato;
    }
    
    public int getTipoDato() {
        return tipoDato;
    }
    
    public void setTipoDato(int tipoDato) {
        this.tipoDato = tipoDato;
    }
    
    public String getEstrategia() {
        return estrategia;
    }
    
    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }
    
    public String getPromotor() {
        return promotor;
    }
    
    public void setPromotor(String promotor) {
        this.promotor = promotor;
    }
    
    public String getIdTienda() {
        return idTienda;
    }
   
    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }
    
    public String getFrontera() {
        return frontera;
    }
    
    public void setFrontera(String frontera) {
        this.frontera = frontera;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getPaterno() {
        return paterno;
    }
    
    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }
    
    public String getMaterno() {
        return materno;
    }
    
    public void setMaterno(String materno) {
        this.materno = materno;
    }
    
    public String getUniversal() {
        return universal;
    }
   
    public void setUniversal(String universal) {
        this.universal = universal;
    }
    
    public String getIdPerfil() {
        return idPerfil;
    }
   
    public void setIdPerfil(String idPerfil) {
        this.idPerfil = idPerfil;
    }
    
    public String getIdSubPerfil() {
        return idSubPerfil;
    }
    
    public void setIdSubPerfil(String idSubPerfil) {
        this.idSubPerfil = idSubPerfil;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" + "usuario=" + usuario + ", clave=" + clave + ", dato=" + dato + ", tipoDato=" + tipoDato + ", estrategia=" + estrategia + ", promotor=" + promotor + ", idTienda=" + idTienda + ", frontera=" + frontera + ", nombre=" + nombre + ", paterno=" + paterno + ", materno=" + materno + ", universal=" + universal + ", idPerfil=" + idPerfil + ", idSubPerfil=" + idSubPerfil + '}';
    }    
}
