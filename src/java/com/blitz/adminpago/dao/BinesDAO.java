/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dao;

import com.blitz.adminpago.dto.BinesDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PGRANDE
 */
@Repository
public class BinesDAO {
    
    @Resource(name="pagosvtDS")
    private JdbcTemplate jt;
    private SimpleJdbcInsert insertaBin;

    public void setDataSource(DataSource dataSource) {
        this.jt = new JdbcTemplate(dataSource);


        this.insertaBin =
                new SimpleJdbcInsert(dataSource).withTableName("POL_24_C_BINES").usingColumns("PREFIJO", "NOM_PRODUCTO", "EMISOR",
                "BIN_TPO_TARJETA", "PRIORIDAD", "FECHA", "COMENTARIOS", "RESPONSABLE", "ID_BANCO");
                

    }

    public double add(BinesDTO registro) {
        Map<String, Object> parameters = new HashMap<String, Object>(13);

        double ret = 0;

        parameters.put("PREFIJO", registro.getPrefijo());
        parameters.put("NOM_PRODUCTO", registro.getNombreProducto());
        parameters.put("EMISOR", registro.getEmisor());
        parameters.put("BIN_TPO_TARJETA", registro.getTipoTarjeta());
        parameters.put("PRIORIDAD", registro.getPrioridad());
        parameters.put("FECHA", registro.getFecha());
        parameters.put("COMENTARIOS", registro.getComentarios());
        parameters.put("RESPONSABLE", registro.getResponsable());
        parameters.put("ID_BANCO", registro.getIdBanco());

        try
        {
            ret = insertaBin.execute(parameters);
        }
        catch(Exception e)
        {
            ret = -1;
        }

        return ret;
    }


    public List obtenerBines(BinesDTO pobDatos) {

        String sql = "SELECT PREFIJO, NOM_PRODUCTO, NOMBRE_BANCO, BIN_TPO_TARJETA, " +
                "PRIORIDAD, FECHA, COMENTARIOS, RESPONSABLE, B.ID_BANCO " +
                "FROM POL_24_C_BINES, POL_32_C_BANCO B  " +
                "WHERE EMISOR = NOMBRE_BANCO " ;

        if (pobDatos != null) {
            if (pobDatos.getPrefijo() != null && pobDatos.getPrefijo().length() > 0) {
                sql += "AND PREFIJO = " + pobDatos.getPrefijo() + " ";
            }

            // Ya trae la clave del banco
            if (pobDatos.getEmisor() != null && pobDatos.getEmisor().length() > 0 ) {
                //sql += "AND EMISOR LIKE '%" + pobDatos.getEmisor() + "%' ";
                sql += "AND B.ID_BANCO = " + pobDatos.getEmisor() + " ";
            }

            if (pobDatos.getNombreProducto() != null && pobDatos.getNombreProducto().length() > 0) {
                sql += "AND NOM_PRODUCTO LIKE '%" + pobDatos.getNombreProducto() + "%' ";
            }

        }

        RowMapper rowMapper = new RowMapper() {

            public Object mapRow(ResultSet rs, int index) throws SQLException {

                BinesDTO dto = new BinesDTO();
                dto.setPrefijo(rs.getString(1));
                dto.setNombreProducto(rs.getString(2));
                dto.setEmisor(rs.getString(3));
                dto.setTipoTarjeta(rs.getString(4));
                dto.setPrioridad(rs.getString(5));
                dto.setFecha(rs.getString(6));
                dto.setComentarios(rs.getString(7));
                dto.setResponsable(rs.getString(8));
                dto.setIdBanco(rs.getString(9));

                return dto;

            }
        };
        List lobLista = jt.query(sql, rowMapper);

        return lobLista;

    }

    public BinesDTO obtenerBin(String prefijo)
    {
        String sql = "SELECT PREFIJO, NOM_PRODUCTO, NOMBRE_BANCO, BIN_TPO_TARJETA, " +
                "PRIORIDAD, FECHA, COMENTARIOS, RESPONSABLE, B.ID_BANCO " +
                "FROM POL_24_C_BINES, POL_32_C_BANCO B " +
                "WHERE PREFIJO = " + prefijo + " " +
                "AND EMISOR = NOMBRE_BANCO ";

        try {
            BinesDTO dto = (BinesDTO) jt.queryForObject(sql, new RowMapper() {

             public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                BinesDTO dto = new BinesDTO();
                dto.setPrefijo(rs.getString(1));
                dto.setNombreProducto(rs.getString(2));
                dto.setEmisor(rs.getString(3));
                dto.setTipoTarjeta(rs.getString(4));
                dto.setPrioridad(rs.getString(5));
                dto.setFecha(rs.getString(6));
                dto.setComentarios(rs.getString(7));
                dto.setResponsable(rs.getString(8));
                dto.setIdBanco(rs.getString(9));

                return dto;
             }
            });

            return dto;

        }
        catch(Exception e) {
            return null;
        }

    }


    public int actualizarBin(BinesDTO pobDatos)
    {
        int ret = -1;

        String sql = "UPDATE POL_24_C_BINES " +
                     "SET NOM_PRODUCTO = ?, ID_BANCO = ?, BIN_TPO_TARJETA = ?, " +
                     "FECHA = ?, COMENTARIOS = ?, RESPONSABLE = ? , EMISOR = ?" +
                     "WHERE PREFIJO = ?" ;

            ret = jt.update(sql, new Object[] { pobDatos.getNombreProducto(), pobDatos.getIdBanco(), pobDatos.getTipoTarjeta(),
                    pobDatos.getFecha(), pobDatos.getComentarios(), pobDatos.getResponsable(), pobDatos.getEmisor(),
                    pobDatos.getPrefijo()});

        return ret;
    }


    public List obtenerBinesFaltantes(String fechaI, String fechaF) {

        String sql = "SELECT DISTINCT(TARJETA) " +
                "FROM PAGOSVT.MT_PAGO " +
                "WHERE ESTADO = 'T01' ";

        if ( fechaI != null && fechaI.length() > 0 && fechaF != null && fechaF.length() > 0
                && fechaI.equals(fechaF))
                sql += "AND TO_CHAR(FECHAESTADO,'YYYY/MM/DD') = '" + fechaI + "' " ;
        else if ( fechaI != null && fechaI.length() > 0 && fechaF != null && fechaF.length() > 0 )
                sql += "AND TO_CHAR(FECHAESTADO,'YYYY/MM/DD') BETWEEN '" + fechaI + "' AND '" + fechaF + "' " ;

        sql += "AND TARJETA NOT IN ( SELECT PREFIJO FROM POL_24_C_BINES )";

        RowMapper rowMapper = new RowMapper() {

            public Object mapRow(ResultSet rs, int index) throws SQLException {
                BinesDTO dto = new BinesDTO();
                dto.setPrefijo(rs.getString(1));
                return dto;
            }
        };
        List lobLista = jt.query(sql, rowMapper);

        return lobLista;

    }

}
