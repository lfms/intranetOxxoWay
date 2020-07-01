/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.ComercioDAO;
import com.blitz.adminpago.dto.ComercioDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ComercioBO implements Serializable{

    public static Log log = LogFactory.getLog(ComercioBO.class);
    @Autowired
    private ComercioDAO comercioDAO;

    public ComercioDAO getComercioDAO() {
        return comercioDAO;
    }


    public HashMap obtenerComercios() {
        return comercioDAO.obtenerComercios();
    }

    public HashMap obtenerComerciosXcve() {
        return comercioDAO.obtenerComerciosXcve();
    }

    public HashMap obtenerComerciosXNomCve() {
        return comercioDAO.obtenerComerciosXNomCve();
    }

    public List obtenerComercios(ComercioDTO pobDatos) {
        List lobListaComercios = null;
        try {
            lobListaComercios = comercioDAO.obtenerComercios(pobDatos);
        } catch (DataAccessException dae) {
            log.error("Error al obtener lista de comercios: " + dae.toString());
            if (dae.toString().contains("")) {
                lobListaComercios = new ArrayList();
            }
        }
        return lobListaComercios;
    }

    public ComercioDTO obtenerComercio(String pstIdComercio) {
        return comercioDAO.obtenerComercio(pstIdComercio);
    }

    public ComercioDTO obtenerComercioCve(String pstCveComercio) {
        return comercioDAO.obtenerComercioCve(pstCveComercio);
    }


    public int actualizarComercio(ComercioDTO pobDatos) {
        int lnuResultado = 0;
        try {
            lnuResultado = comercioDAO.actualizarComercio(pobDatos);
        } catch (DataAccessException dae) {
            log.error("Error al actualizar el comercio. " + dae.toString());
            lnuResultado = 0;
        }
        return lnuResultado;
    }

    public double registrarComercio(ComercioDTO pobDatos) {
        double lnuRespuesta = 0;
        try {
            lnuRespuesta = comercioDAO.add(pobDatos);
            if ( lnuRespuesta != -1 )
            {
                pobDatos.setIdComercio(String.valueOf(lnuRespuesta));
            }
        } catch (DataAccessException dae) {
            log.error("Error al dar de alta comercio: " + dae.toString());
            lnuRespuesta = -1;
            if (dae.toString().contains("unique")) {
                lnuRespuesta = 0;
            }
        }
        return lnuRespuesta;
    }

    public List obtenerComerciosOfCobro()
    {
        return comercioDAO.obtenerComerciosOfCobro();
    }

    public void setComercioDAO(ComercioDAO comercioDAO) {
        this.comercioDAO = comercioDAO;
    }

}
