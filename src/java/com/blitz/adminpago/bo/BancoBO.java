/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.BancoDAO;
import com.blitz.adminpago.dto.BancoDTO;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PGRANDE
 */
@Service
public class BancoBO {
    
    @Autowired
    private BancoDAO bancoDAO;


    public int add(BancoDTO registro)
    {
        return bancoDAO.add(registro);
    }

    public List obtenerBancos(String pstNombre)
    {
        return bancoDAO.obtenerBancos(pstNombre);
    }

    public Map obtenerBancosH()
    {
        return bancoDAO.obtenerBancosH();
    }

    public String obtenerNombreBanco(String pstIdBanco)
    {
        return bancoDAO.obtenerNombreBanco(pstIdBanco);
    }

    public BancoDTO obtenerBanco(String pstIdBanco)
    {
        return bancoDAO.obtenerBanco(pstIdBanco);
    }

    public int actualizarBanco(BancoDTO pobDatos)
    {
       return bancoDAO.actualizarBanco(pobDatos);
    }
    
    /**
     * @param bancoDAO the bancoDAO to set
     */
    public void setBancoDAO(BancoDAO bancoDAO) {
        this.bancoDAO = bancoDAO;
    }

}
