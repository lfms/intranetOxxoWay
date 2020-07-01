/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author pgrande
 */
public class ListaComercioDTO implements Serializable{

    private Map listaComercio;

    private Map listaComercioXid;
    private Map listaComercioXNom;

    public Map getListaComercio() {
        return listaComercio;
    }

    public void setListaComercio(Map listaComercio) {
        this.listaComercio = listaComercio;
    }

    public Map getListaComercioXid() {
        return listaComercioXid;
    }

    public void setListaComercioXid(Map listaComercioXid) {
        this.listaComercioXid = listaComercioXid;
    }

    public Map getListaComercioXNom() {
        return listaComercioXNom;
    }

    public void setListaComercioXNom(Map listaComercioXNom) {
        this.listaComercioXNom = listaComercioXNom;
    }
}
