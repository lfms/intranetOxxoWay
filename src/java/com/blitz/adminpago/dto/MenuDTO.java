/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author pgrande
 */
public class MenuDTO implements Serializable{

    private List menu;
  
    public List getMenu() {
        return menu;
    }

    public void setMenu(List menu) {
        this.menu = menu;
    }

}
