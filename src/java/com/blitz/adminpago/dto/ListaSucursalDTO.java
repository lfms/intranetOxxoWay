/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;
import java.util.Map;

public class ListaSucursalDTO implements Serializable{

    private Map listaSucursal;

    public Map getListaSucursal() {
        return listaSucursal;
    }

    public void setListaSucursal(Map listaSucursal) {
        this.listaSucursal = listaSucursal;
    }
}
