/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dao;

import com.blitz.adminpago.dto.BancoDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.stereotype.Repository;

@Repository
public class BancoDAO {
    
    private JdbcTemplate jt;
    private SimpleJdbcInsert insertaBanco;
    private OracleSequenceMaxValueIncrementer siguienteBanco;

    @Resource(name="pagosvtDS")
    public void setDataSource(DataSource dataSource) {
        this.jt = new JdbcTemplate(dataSource);
        this.insertaBanco = new SimpleJdbcInsert(dataSource).withTableName("POL_32_C_BANCO").usingColumns("ID_BANCO", "NOMBRE_BANCO");
        this.siguienteBanco = new OracleSequenceMaxValueIncrementer(dataSource, "S_POL_32_C_BANCO");
    }

    public int add(BancoDTO registro) {
        Map<String, Object> parameters = new HashMap<>(3);

        parameters.put("ID_BANCO", siguienteBanco.nextIntValue());
        parameters.put("NOMBRE_BANCO", registro.getNombreBanco());
        
        return insertaBanco.execute(parameters);       
    }

    public List obtenerBancos(String pstNombre) {
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ID_BANCO, NOMBRE_BANCO FROM POL_32_C_BANCO WHERE ID_BANCO > 0 ");
        if ( pstNombre != null && pstNombre.length() > 0 )
            sql.append("AND NOMBRE_BANCO LIKE '" + pstNombre + "%' ");

        sql.append("ORDER BY NOMBRE_BANCO");
        
        return jt.query(sql.toString(), (ResultSet rs, int index) -> {
            BancoDTO dto = new BancoDTO();
            dto.setIdBanco(rs.getInt(1));
            dto.setNombreBanco(rs.getString(2));
            return dto;
        });

    }


    public Map obtenerBancosH()
    {

        Map lobHashLista = new LinkedHashMap();

        List lobLista = this.obtenerBancos(null);

        if (lobLista != null && lobLista.size() > 0) {

            Iterator it = lobLista.iterator();
            while (it.hasNext()) {
                BancoDTO dto = (BancoDTO) it.next();
                lobHashLista.put(dto.getNombreBanco(), dto.getIdBanco());
            }
        }
        return lobHashLista;
    }

    


    public String obtenerNombreBanco(String pstIdBanco)
    {
        String sql = "SELECT NOMBRE_BANCO FROM POL_32_C_BANCO WHERE ID_BANCO = " + pstIdBanco + " ";
        
        return (String) jt.queryForObject(sql,String.class);
    }


    public BancoDTO obtenerBanco(String pstIdBanco)
    {
        
        String sql = "SELECT ID_BANCO, NOMBRE_BANCO FROM POL_32_C_BANCO WHERE ID_BANCO = " + pstIdBanco + " ";
        
        return jt.queryForObject(sql, (ResultSet rs, int index) -> {
            BancoDTO banco = new BancoDTO();
            banco.setIdBanco(rs.getInt(1));
            banco.setNombreBanco(rs.getString(2));
            return banco;
        });
    }


    public int actualizarBanco(BancoDTO pobDatos)
    {
        String sql = "UPDATE POL_32_C_BANCO SET NOMBRE_BANCO = ? WHERE ID_BANCO = ?" ;        
        return jt.update(sql, new Object[] { pobDatos.getNombreBanco(), pobDatos.getIdBanco()});        
    }


}
