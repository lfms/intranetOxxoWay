/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.bo;

import com.blitz.adminpago.dto.UsuarioDTO;
import com.blitz.adminpago.dao.AccesoDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 *
 * @author SMMIGUEL
 */
@Service
public class AccesoBO {

    public static Log log = LogFactory.getLog(AccesoBO.class);
    @Autowired
    private AccesoDAO accesoDAO;

    public UsuarioDTO validarUsuario(String usuario, String clave) {
        log.info(usuario + "----" + clave);
        UsuarioDTO resultado = null;
        UsuarioDTO usuarioDTO = null;
        try {
            usuarioDTO = accesoDAO.buscaUsuario(usuario);
            log.info("ABO; " + usuarioDTO);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            log.error("Error al validar el usuario: " + dae.toString());
        }
        if (usuarioDTO != null) {
            String claveUsuario = usuarioDTO.getClave();
            if (clave.equals(claveUsuario)) {
                resultado = usuarioDTO;
            }
        }
        return resultado;
    }


}
