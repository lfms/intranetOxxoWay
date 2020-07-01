/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dao;
import com.blitz.adminpago.dto.LibreriaDTO;
import com.blitz.adminpago.util.Utilerias;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author PGRANDE
 */

public class LibreriaDAO {

    private Logger log = Logger.getLogger("com.blitz.adminpagoline.LibreriaDAO");
    private JdbcTemplate jt;

    /** Creates a new instance of LibreriaDAO
    public LibreriaDAO() {
    }
     */

    public void setDb2DataSource(DataSource db2DataSource) {
        jt = new JdbcTemplate(db2DataSource);
    }


    public String obtenerLibreria(String pstTelefono) {

        String lstLibreria = null;
        /* Antes en AS400
        String sql = " SELECT  TRIM(SEPNML) " +
                "FROM  MEXV1.CJPOBLA  " +
                "WHERE  SEPEXC = " + pstTelefono.substring(0, 6) + " " +  //555676
                "AND " + Integer.parseInt(pstTelefono.substring(6)) + " BETWEEN SEPFRM AND SEPTO "; //1234
        */
        String sql = " SELECT  TRIM(SEPFMF) " +
                "FROM  CJPOBLA  " +
                "WHERE  SEPEXC = " + pstTelefono.substring(0, 6) + " " +  //555676
                "AND " + Integer.parseInt(pstTelefono.substring(6)) + " BETWEEN SEPFRM AND SEPTO "; //1234


        try
        {

            String lstSEPNML = (String) jt.queryForObject(sql, String.class);
            if ( lstSEPNML != null && lstSEPNML.length() > 0 )
            {
                Utilerias util = new Utilerias();
                //Obtenemos la libreria
                //FUENTE DE HERMES 1            MEXV1
                lstLibreria = util.traduceLibreria(lstSEPNML.substring(lstSEPNML.lastIndexOf(" ")+1));
            }


        }
        catch(Exception e)
        {
            log.info("CJPOBLA->" + e.toString());
            lstLibreria = null;
        }

        return lstLibreria;
    }


}
