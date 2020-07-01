/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.dao;

import com.blitz.adminpago.dto.UsuarioDTO;
import com.blitz.adminpago.util.Utilerias;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


/**
 *
 * @author LFMS
 */
@Repository
public class AccesoDAO {

    private JdbcTemplate jt;
    
    Log log = LogFactory.getLog(this.getClass());

    @Resource(name="pagosvtDS")
    public void setDataSource(DataSource dataSource) {
        jt = new JdbcTemplate(dataSource);
    }
    
    
    public UsuarioDTO buscaUsuario(String usuario) {
        
        String strQuery = "SELECT ID_USUARIO, CVE_ACCESO, ID_TIENDA, ID_PERFIL, UNIVERSAL, ID_SUBPERFIL FROM POL_10_T_USUARIO WHERE ID_USUARIO = ?";
        log.info(strQuery);
       
        return (UsuarioDTO) jt.queryForObject(strQuery, new Object[]{usuario}, (ResultSet rs, int i) -> {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setUsuario(usuario);
            dto.setClave(decrypt(rs.getString("CVE_ACCESO")));
            dto.setIdTienda(rs.getString("ID_TIENDA"));
            dto.setIdPerfil(rs.getString("ID_PERFIL"));
            dto.setUniversal(rs.getString("UNIVERSAL"));
            dto.setIdSubPerfil(rs.getString("ID_SUBPERFIL"));
            return dto;
        });
    }
    
    private String decrypt(String value){
        return Utilerias.desencriptarCadena(value);
    }
}
