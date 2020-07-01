/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.ConsultaDAO;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author pgrande
 */
public class ConsultaBO {

    private Logger log = Logger.getLogger("com.blitz.adminpagoline.bo.ConsultaBO");
    private ConsultaDAO consultaDAO;
    private ConsultaDAO consultaDAO2;
    private String comercioBD1;


    public List obtenerConsultasTelefono(String pstFechaI,String pstFechaF, String pstTelefono, String pstComercio)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return getConsultaDAOBD(lnuBD).obtenerConsultasTel(pstFechaI, pstFechaF, pstTelefono);
    }

    public int eliminarConsultasTelefono(String pstTelefonos)
    {
        return consultaDAO.eliminarConsultasTel(pstTelefonos);
    }

    /**
     * @param consultaDAO the consultaDAO to set
     */
    public void setConsultaDAO(ConsultaDAO consultaDAO) {
        this.consultaDAO = consultaDAO;
    }

    /**
     * @param consultaDAO2 the consultaDAO2 to set
     */
    public void setConsultaDAO2(ConsultaDAO consultaDAO2) {
        this.consultaDAO2 = consultaDAO2;
    }


    public ConsultaDAO getConsultaDAOBD(int pnuBD)
    {
        if ( pnuBD == 1 )
            return consultaDAO;
        else
            return consultaDAO2;
    }

    /**
     * @param comercioBD1 the comercioBD1 to set
     */
    public void setComercioBD1(String comercioBD1) {
        this.comercioBD1 = comercioBD1;
    }

}
