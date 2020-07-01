/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.dao;

import com.blitz.adminpago.dto.ModuloDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pgrande
 */
@Repository
public class ModuloDAO {
    
    private JdbcTemplate jt;
    Log log = LogFactory.getLog(this.getClass());

    @Resource(name="pagosvtDS")
    public void setDataSource(DataSource dataSource) {
        jt = new JdbcTemplate(dataSource);
    }

    public List obtenerModPerfil(int pnuPerfil, String universal) {
        List respuesta = new ArrayList();        
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT DISTINCT(M.ID_MODULO), M.NOMBRE, M.URL, M.CATEGORIA FROM POL_08_C_MODULO M, POL_09_T_SUBPERFIL_MODULO S ");
        sql.append("WHERE M.ID_MODULO = S.ID_MODULO ");
        if ( pnuPerfil > 0 ){
             sql.append("AND S.ID_PERFIL = ");
             sql.append(pnuPerfil);
        }
        else if(universal != null && !universal.equals("SI")){
             sql.append(" AND S.ID_PERFIL = ");
             sql.append(pnuPerfil);
        }
        log.info(sql.toString());
        sql.append(" ORDER BY M.CATEGORIA, M.NOMBRE ");
        
        List<ModuloDTO> result = jt.query(sql.toString(), (ResultSet rs, int i ) -> {
            ModuloDTO modulo = new ModuloDTO();
            modulo.setIdModulo(rs.getInt(1));
            modulo.setNombre(rs.getString(2));
            modulo.setUrl(rs.getString(3));
            modulo.setCategoria(rs.getString(4).trim());
            return modulo;
        });        
        
        respuesta.add(result.stream().filter(c -> "C".equals(c.getCategoria())).collect(Collectors.toList()));
        respuesta.add(result.stream().filter(c -> "R".equals(c.getCategoria())).collect(Collectors.toList()));
        respuesta.add(result.stream().filter(c -> "M".equals(c.getCategoria())).collect(Collectors.toList()));
        
        return respuesta;
    }

    public List obtenerModDispPerfil(String pnuPerfil, String pnuSubPerfil) {
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ID_MODULO, NOMBRE, URL FROM POL_08_C_MODULO ");
        if (pnuPerfil != null && pnuPerfil.length() > 0) {
            sql.append("WHERE ID_MODULO NOT IN (SELECT ID_MODULO FROM POL_09_T_SUBPERFIL_MODULO WHERE ID_PERFIL = ");
            sql.append(pnuPerfil);
            sql.append(" AND ID_SUBPERFIL = " + pnuSubPerfil + " ) ");
        }
        
       List<ModuloDTO> modulos = jt.query(sql.toString(), (ResultSet rs, int i) -> {
            ModuloDTO modulo = new ModuloDTO();
            modulo.setIdModulo(rs.getInt(1));
            modulo.setNombre(rs.getString(2));
            modulo.setUrl(rs.getString(3));
            return modulo;
       });      

       return modulos;
    }


    public HashMap obtenerMod() {

        String sql = "SELECT ID_MODULO, NOMBRE, URL " +
                "FROM POL_08_C_MODULO ";

        RowMapper rowMapper = new RowMapper() {

            public Object mapRow(ResultSet rs, int index) throws SQLException {
                ModuloDTO modulo = new ModuloDTO();
                modulo.setIdModulo(rs.getInt(1));
                modulo.setNombre(rs.getString(2));
                modulo.setUrl(rs.getString(3));
                return modulo;
            }
        };

        List lobLista = jt.query(sql, rowMapper);

         HashMap lobHashLista = new HashMap();

         if ( lobLista != null && lobLista.size() > 0 )
         {
             Iterator it = lobLista.iterator();
             while (it.hasNext() )
             {
                ModuloDTO dto = (ModuloDTO) it.next();
                lobHashLista.put(dto.getNombre(),dto.getIdModulo());
             }
         }

         return lobHashLista;
    }


}
