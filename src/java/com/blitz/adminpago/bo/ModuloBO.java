/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.ModuloDAO;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 *
 * @author pgrande
 */
@Service
public class ModuloBO implements Serializable{

    private Logger log = Logger.getLogger("com.blitz.negocio.ModuloBO");
    @Autowired
    private ModuloDAO moduloDAO;

    public List obtenerModPerfil(int pnuPerfil, String universal)
    {
        log.info("Obteniendo perfil");
        return moduloDAO.obtenerModPerfil(pnuPerfil, universal);

    }

    public List obtenerModDispPerfil(String pnuPerfil, String pnuSubPerfil)
    {
        return moduloDAO.obtenerModDispPerfil(pnuPerfil, pnuSubPerfil);
    }

    public HashMap obtenerModulo()
    {
        return moduloDAO.obtenerMod();
    }


}
