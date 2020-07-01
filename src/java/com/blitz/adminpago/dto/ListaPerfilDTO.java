/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author EHJIMENE
 */
public class ListaPerfilDTO implements Serializable{
    
    private Map listaPerfil;

    public Map getListaPerfil() {
        return listaPerfil;
    }

    public void setListaPerfil(Map listaPerfil) {
        this.listaPerfil = listaPerfil;
    }

}
