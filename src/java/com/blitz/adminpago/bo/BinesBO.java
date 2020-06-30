/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.BinesDAO;
import com.blitz.adminpago.dto.BinesDTO;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PGRANDE
 */
@Service
public class BinesBO {

    public static Log log = LogFactory.getLog(ComercioBO.class);
    
    @Autowired
    private BinesDAO binesDAO;

    public void setBinesDAO(BinesDAO binesDAO) {
        this.binesDAO = binesDAO;
    }

    public List obtenerBines(BinesDTO pobDatos)
    {
        return binesDAO.obtenerBines(pobDatos);
    }


    public List obtenerBinesFaltantes(String fechaI, String fechaF)
    {
        return binesDAO.obtenerBinesFaltantes(fechaI, fechaF);
    }

    public BinesDTO obtenerBin(String prefijo)
    {
        return binesDAO.obtenerBin(prefijo);
    }

    public double registrarBin(BinesDTO pobDatos)
    {
        return binesDAO.add(pobDatos);
    }

    public int actualizarBin(BinesDTO pobDatos)
    {
        return binesDAO.actualizarBin(pobDatos);
    }

}
