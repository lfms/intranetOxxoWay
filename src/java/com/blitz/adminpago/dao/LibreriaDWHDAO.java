/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dao;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.log4j.Logger;

/**
 *
 * @author PGRANDE
 */
public class LibreriaDWHDAO {

    private JdbcTemplate jt;
    private Logger log = Logger.getLogger("LibreriaDWHDAO");


    public void setDwhDataSource(DataSource dwhDataSource) {
        jt = new JdbcTemplate(dwhDataSource);
    }


    public String obtenerLibCS(String pstTelefono) {

        String sql = "SELECT origen||'|'||cve_servicio||'|' " +
                "FROM ctes_pisa " +
                "WHERE telefono_pisa = '" + pstTelefono + "' " +
                "AND status = '0' " ;
                //Se quedan muchos sin aplicar por esta condicion: PGM 11Mar
                //"AND fecha_desconexion = '00000000'" ;

            return (String) jt.queryForObject(sql,String.class);
    }

    public String obtenerLibreria(String pstTelefono) {

        String sql = "SELECT origen " +
                "FROM ctes_pisa " +
                "WHERE telefono_pisa = '" + pstTelefono + "' " +
                "AND status = '0' " +
                "AND fecha_desconexion = '00000000'" ;

        return (String) jt.queryForObject(sql,String.class);
      
    }




}
