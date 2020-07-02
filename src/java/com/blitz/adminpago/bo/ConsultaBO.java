/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.ConsultaDAO;
import java.util.List;
import java.util.logging.Logger;

public class ConsultaBO {

    private Logger log = Logger.getLogger("com.blitz.adminpagoline.bo.ConsultaBO");
    private ConsultaDAO consultaDAO;
    private String comercioBD1;


    public List obtenerConsultasTelefono(String pstFechaI,String pstFechaF, String pstTelefono, String pstComercio)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return consultaDAO.obtenerConsultasTel(pstFechaI, pstFechaF, pstTelefono);
    }

    public int eliminarConsultasTelefono(String pstTelefonos)
    {
        return consultaDAO.eliminarConsultasTel(pstTelefonos);
    }

    public void setConsultaDAO(ConsultaDAO consultaDAO) {
        this.consultaDAO = consultaDAO;
    }

    public void setComercioBD1(String comercioBD1) {
        this.comercioBD1 = comercioBD1;
    }

}
