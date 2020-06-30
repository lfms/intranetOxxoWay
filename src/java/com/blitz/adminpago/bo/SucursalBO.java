/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.SucursalDAO;
import com.blitz.adminpago.dto.SucursalDTO;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SucursalBO {

    public static Log log = LogFactory.getLog(SucursalBO.class);
    @Autowired
    private SucursalDAO sucursalDAO;
    @Autowired
    private SucursalDAO sucursalDAO2;
    private String comercioBD1;

    public HashMap obtenerSucursales() {
        //Cuando se realice el cambio es necesario llenar la lista con la busqueda en los dos dao
        return sucursalDAO.obtenerSucursales();
    }

    public HashMap obtenerSucursalesCom(String pstCveCom) {
        return sucursalDAO.obtenerSucursalesCom(pstCveCom);
    }

    public HashMap obtenerSucursalesComCve(String pstCveCom) {
        return sucursalDAO.obtenerSucursalesComCve(pstCveCom);
    }


    public List obtenerSucCom(SucursalDTO pobDatos) {
        return sucursalDAO.obtenerSucCom(pobDatos);
    }

    /**
     * Inserta sucursal
     * @param pobDatos
     * @return
     */
    public int insertaSucursale(SucursalDTO pobDatos) {
        long lnuResultado = 0;
         int lnuBD = 1; //BSN, MKT, APT
        try {

            if (pobDatos!=null && pobDatos.getIdComercio()!=null &&
                    comercioBD1.indexOf(pobDatos.getIdComercio()) == -1)
                lnuBD = 2;


//            lnuResultado = this.getSucursalDAOBD(lnuBD).insertaSucursal(pobDatos);

            lnuResultado = sucursalDAO.insertaSucursal(pobDatos);
            if ( lnuResultado > 0 )
            {
                pobDatos.setIdSucursal(String.valueOf(lnuResultado));
                sucursalDAO2.insertaSucursal(pobDatos);
            }
        } catch (DataAccessException dae) {
            log.error("Error al dar de alta sucursal: " + dae.toString());
            lnuResultado = -1;
            if (dae.toString().contains("unique")) {
                lnuResultado = 0;
            }
        }
        return (int)lnuResultado;
    }

    public int actualizarSucursal(SucursalDTO pobDatos) {
        int lnuRespuesta = 0;
        int lnuBD = 1; //BSN, MKT, APT

        try {
            if (pobDatos!=null && pobDatos.getIdComercio()!=null &&
                    comercioBD1.indexOf(pobDatos.getIdComercio()) == -1)
                lnuBD = 2;


//            lnuRespuesta = this.getSucursalDAOBD(lnuBD).actualizarSucursal(pobDatos);

            lnuRespuesta = sucursalDAO.actualizarSucursal(pobDatos);
            sucursalDAO2.actualizarSucursal(pobDatos);
        } catch (DataAccessException dae) {
            log.error("Error al actualizar la sucursal. " + dae.toString());
            lnuRespuesta = 0;
        }
        return lnuRespuesta;
    }

    public SucursalDTO obtenSucursal(String lstIdSucursal) {
        return sucursalDAO.obtenSucursal(lstIdSucursal);
    }

    /**
     * @param sucursalDAO the sucursalDAO to set
     */
    public void setSucursalDAO(SucursalDAO sucursalDAO) {
        this.sucursalDAO = sucursalDAO;
    }

    /**
     * @param sucursalDAO2 the sucursalDAO2 to set
     */
    public void setSucursalDAO2(SucursalDAO sucursalDAO2) {
        this.sucursalDAO2 = sucursalDAO2;
    }

    public SucursalDAO getSucursalDAOBD(int pnuBD)
    {
        if ( pnuBD == 1 )
            return sucursalDAO;
        else
            return sucursalDAO2;

    }


    /**
     * @param comercioBD1 the comercioBD1 to set
     */
    public void setComercioBD1(String comercioBD1) {
        this.comercioBD1 = comercioBD1;
    }
}
