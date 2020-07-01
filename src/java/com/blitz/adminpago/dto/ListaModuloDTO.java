/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;
import java.util.Map;

public class ListaModuloDTO implements Serializable{

    private Map listaModulos;
     
    public Map getListaModulos() {
        return listaModulos;
    }

    public void setListaModulos(Map listaModulos) {
        this.listaModulos = listaModulos;
    }
}
