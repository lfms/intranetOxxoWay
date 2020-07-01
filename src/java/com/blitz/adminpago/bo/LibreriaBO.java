/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.LibreriaDAO;


/**
 *
 * @author PGRANDE
 */
public class LibreriaBO {

    
    private LibreriaDAO libreriaDAO;


    public String obtenerLibreria(String pstTelefono)
    {
        String lstLibreria = null;

        if (pstTelefono != null && pstTelefono.length() == 10 )
        {
            ;//lstLibreria = libreriaDAO.obtenerLibreria(pstTelefono);
        }


        return lstLibreria;
    }


    /**
     * @param libreriaDAO the libreriaDAO to set
     */
    public void setLibreriaDAO(LibreriaDAO libreriaDAO) {
        this.libreriaDAO = libreriaDAO;
    }

}
