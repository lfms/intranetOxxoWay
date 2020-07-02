/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.dao;

import com.blitz.adminpago.dto.ComercioDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pgrande
 */
@Repository
public class ComercioDAO {
    
    
    private JdbcTemplate jt;
    private SimpleJdbcInsert insertaComercio;
    private OracleSequenceMaxValueIncrementer siguienteComercio;
    
    @Resource(name="pagosvtDS")
    public void setDataSource(DataSource dataSource) {
        this.jt = new JdbcTemplate(dataSource);
        this.insertaComercio = new SimpleJdbcInsert(dataSource).withTableName("POL_04_C_COMERCIO").usingColumns("ID_COMERCIO", "CVE_COMERCIO", "DIAS_HABILES", "HORA_INI",
                "HORA_FIN", "NOMBRE", "CVE_COM_ORIGEN", "CORREO_CONCIL", "ESTATUS", "TIMEOUT", "TIMEOUTREIN");
        this.siguienteComercio = new OracleSequenceMaxValueIncrementer(dataSource, "S_POL_04_C_COMERCIO");
    }

    public double add(ComercioDTO registro) {
        
        Map<String, Object> parameters = new HashMap<>(13);

        double ret = 0;
        if ( registro.getIdComercio() != null && registro.getIdComercio().length() > 0 )
            ret = Double.parseDouble(registro.getIdComercio());
        else
            ret = siguienteComercio.nextLongValue();

        
        parameters.put("ID_COMERCIO", ret);
        parameters.put("CVE_COMERCIO", registro.getCveComercio());
        parameters.put("DIAS_HABILES", registro.getDiasHabiles());
        parameters.put("HORA_INI", registro.getHoraIni());
        parameters.put("HORA_FIN", registro.getHoraFin());
        parameters.put("NOMBRE", registro.getNombre());
        parameters.put("CVE_COM_ORIGEN", registro.getCveComercioOrigen());
        parameters.put("CORREO_CONCIL", registro.getCorreoConcil());
        parameters.put("ESTATUS", registro.getEstatus());
        parameters.put("TIMEOUT", registro.getTimeout());
        parameters.put("TIMEOUTREIN", registro.getTimeoutrein());

        return insertaComercio.execute(parameters);
    }

    public Map obtenerComercios() {

        String sql = "SELECT ID_COMERCIO, NOMBRE, CVE_COM_ORIGEN FROM POL_04_C_COMERCIO order by NOMBRE";

        List lobLista = jt.query(sql, (ResultSet rs, int index) -> {
            ComercioDTO dto = new ComercioDTO();
            dto.setIdComercio(rs.getString(1));
            dto.setNombre(rs.getString(2));
            dto.setCveComercioOrigen(rs.getString(3));

            return dto;
        });
        
        
        /*
        if (lobLista != null && lobLista.size() > 0) {
            Iterator it = lobLista.iterator();
            while (it.hasNext()) {
                ComercioDTO dto = (ComercioDTO) it.next();
                lobHashLista.put(dto.getNombre(), dto.getIdComercio());
            }
        }*/


        return (Map) lobLista.stream().collect(Collectors.toMap(ComercioDTO::getNombre, comercio -> comercio.getIdComercio()));
    }

    /**
     *
     * @return
     */
    public Map obtenerComerciosXcve() {

        String sql = "SELECT ID_COMERCIO, NOMBRE, CVE_COMERCIO " +
                "FROM POL_04_C_COMERCIO  order by ID_COMERCIO";

    
        List lobLista = jt.query(sql, (ResultSet rs, int index)->{
            ComercioDTO dto = new ComercioDTO();
            dto.setIdComercio(rs.getString(1));
            dto.setNombre(rs.getString(2));
            dto.setCveComercio(rs.getString(3));
            return dto;
        });
        
        /*
        HashMap lobHashLista = new HashMap();
        
        if (lobLista != null && lobLista.size() > 0) {
            Iterator it = lobLista.iterator();
            while (it.hasNext()) {
                ComercioDTO dto = (ComercioDTO) it.next();
                lobHashLista.put(dto.getCveComercio(), dto.getIdComercio());
            }
        }*/

        return (Map) lobLista.stream().collect(Collectors.toMap(ComercioDTO::getCveComercio, comercio -> comercio.getIdComercio())) ;
    }

    public Map obtenerComerciosXNomCve() {

        String sql = "SELECT ID_COMERCIO, NOMBRE, CVE_COMERCIO FROM POL_04_C_COMERCIO  order by ID_COMERCIO";
        
        List lobLista = jt.query(sql, (ResultSet rs, int index) -> {
            ComercioDTO dto = new ComercioDTO();
            dto.setIdComercio(rs.getString(1));
            dto.setNombre(rs.getString(2));
            dto.setCveComercio(rs.getString(3));
            return dto;
        });
        
        /*
        HashMap lobHashLista = new HashMap();

        if (lobLista != null && lobLista.size() > 0) {
            Iterator it = lobLista.iterator();
            while (it.hasNext()) {
                ComercioDTO dto = (ComercioDTO) it.next();
                lobHashLista.put(dto.getNombre().trim(), dto.getCveComercio().trim());
            }
        } */

        return (Map) lobLista.stream().collect(Collectors.toMap(ComercioDTO::getNombre, comercio -> comercio.getCveComercio().trim()));
    }



/*
    public HashMap obtenerHorariosComercio() {

        String sql = "SELECT ID_COMERCIO, CVE_COMERCIO, DIAS_HABILES, HORA_INI," +
                "HORA_FIN, NOMBRE, CVE_COM_ORIGEN " +
                "FROM POL_04_C_COMERCIO ";

        RowMapper rowMapper = new RowMapper() {

            public Object mapRow(ResultSet rs, int index) throws SQLException {
                ComercioDTO dto = new ComercioDTO();
                dto.setIdComercio(rs.getString(1));
                dto.setCveComercio(rs.getString(2));
                dto.setDiasHabiles(rs.getString(3));
                dto.setHoraIni(rs.getString(4));
                dto.setHoraFin(rs.getString(5));
                dto.setNombre(rs.getString(6));
                dto.setCveComercioOrigen(rs.getString(7));

                return dto;


            }
        };
        List lobLista = jt.query(sql, rowMapper);
        HashMap lobHashLista = new HashMap();

        if (lobLista != null && lobLista.size() > 0) {
            Iterator it = lobLista.iterator();
            while (it.hasNext()) {
                ComercioDTO dto = (ComercioDTO) it.next();
                lobHashLista.put(dto.getCveComercioOrigen(), dto);
            }
        }

        return lobHashLista;

    }

 *
 */
    public HashMap obtenerHorariosComercio() {

        String sql = "SELECT ID_COMERCIO, CVE_COMERCIO, DIAS_HABILES, HORA_INI, HORA_FIN, NOMBRE, CVE_COM_ORIGEN, ESTATUS, TIMEOUT, TIMEOUTREIN " +
                    "FROM POL_04_C_COMERCIO " ;

        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                ComercioDTO dto = new ComercioDTO();
                dto.setIdComercio(rs.getString(1));
                String cveCom = rs.getString(2);
                if (cveCom!=null)
                    cveCom = rs.getString(2).trim();
                dto.setCveComercio(cveCom);
                dto.setDiasHabiles(rs.getString(3));

                String lstHoraI = rs.getString(4);
                dto.setHoraIni(lstHoraI);
                String lstHora, lstMin;
                if ( lstHoraI != null && lstHoraI.length() > 4 && lstHoraI.indexOf(":") != -1)
                {
                    lstHora = lstHoraI.substring(0, lstHoraI.indexOf(":"));
                    lstMin = lstHoraI.substring(lstHoraI.indexOf(":")+1);
                    Calendar hoy = Calendar.getInstance();
                    hoy.set(Calendar.HOUR_OF_DAY, Integer.parseInt(lstHora));
                    hoy.set(Calendar.MINUTE, Integer.parseInt(lstMin));
                    dto.setdHoraIni(hoy.getTime());
                }

                lstHoraI = rs.getString(5);
                dto.setHoraFin(lstHoraI);
                if ( lstHoraI != null && lstHoraI.length() > 4 && lstHoraI.indexOf(":") != -1)
                {
                    lstHora = lstHoraI.substring(0, lstHoraI.indexOf(":"));
                    lstMin = lstHoraI.substring(lstHoraI.indexOf(":")+1);
                    Calendar hoy = Calendar.getInstance();
                    hoy.set(Calendar.HOUR_OF_DAY, Integer.parseInt(lstHora));
                    hoy.set(Calendar.MINUTE, Integer.parseInt(lstMin));
                    dto.setdHoraFin(hoy.getTime());
                }

                dto.setNombre(rs.getString(6));
                dto.setCveComercioOrigen(rs.getString(7));
                dto.setEstatus(rs.getString(8));
                dto.setTimeout(rs.getString(9));
                dto.setTimeoutrein(rs.getString(10));


                return dto;


             }

        };
         List lobLista =  jt.query(sql,rowMapper);
         HashMap lobHashLista = new HashMap();

         if ( lobLista != null && lobLista.size() > 0 )
         {
             Iterator it = lobLista.iterator();
             while (it.hasNext() )
             {
                ComercioDTO dto = (ComercioDTO) it.next();
                //lobHashLista.put(dto.getCveComercioOrigen(),dto);
                //Cambiamos la entrada por cvecom = oficinatmx
                lobHashLista.put(dto.getCveComercio(),dto);
             }
         }

         return lobHashLista;

    }



    public List obtenerComercios(ComercioDTO pobDatos) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ID_COMERCIO, CVE_COMERCIO, DIAS_HABILES, HORA_INI,");
        sql.append("HORA_FIN, NOMBRE, CVE_COM_ORIGEN, ESTATUS, CORREO_CONCIL ");
        sql.append("FROM POL_04_C_COMERCIO ");
        sql.append("WHERE ID_COMERCIO > 0 ");

        if (pobDatos != null) {
            if (pobDatos.getCveComercioOrigen() != null && pobDatos.getCveComercioOrigen().length() > 0)
                sql.append("AND CVE_COM_ORIGEN = " + pobDatos.getCveComercioOrigen() + " ");
            if (pobDatos.getCveComercio() != null && pobDatos.getCveComercio().length() > 0 && !pobDatos.getCveComercio().equals("-"))
                sql.append("AND ID_COMERCIO = " + pobDatos.getCveComercio() + " ");
            if (pobDatos.getNombre() != null && pobDatos.getNombre().length() > 0) 
                sql.append("AND NOMBRE LIKE '%" + pobDatos.getNombre() + "%' ");
        }

        RowMapper rowMapper = new RowMapper() {

            public Object mapRow(ResultSet rs, int index) throws SQLException {
                Map lobDiasSemana = new HashMap();
                lobDiasSemana.put("1", "D");
                lobDiasSemana.put("2", "L");
                lobDiasSemana.put("3", "M");
                lobDiasSemana.put("4", "Mc");
                lobDiasSemana.put("5", "J");
                lobDiasSemana.put("6", "V");
                lobDiasSemana.put("7", "S");
                char[] lobDiasLab = rs.getString(3).toCharArray();
                StringBuffer lobDiasHabiles = new StringBuffer();
                for (int i = 0; i < lobDiasLab.length; i++) {
                    lobDiasHabiles.append(lobDiasSemana.get(String.valueOf(lobDiasLab[i])));
                }
                ComercioDTO dto = new ComercioDTO();
                dto.setIdComercio(rs.getString(1));
                dto.setCveComercio(rs.getString(2));
                dto.setDiasHabiles(lobDiasHabiles.toString());
                dto.setHoraIni(rs.getString(4));
                dto.setHoraFin(rs.getString(5));
                dto.setNombre(rs.getString(6));
                dto.setCveComercioOrigen(rs.getString(7));
                dto.setEstatus(rs.getString(8));
                dto.setCorreoConcil(rs.getString(9));

                return dto;


            }
        };
        List lobLista = jt.query(sql.toString(), rowMapper);

        return lobLista;

    }

    public ComercioDTO obtenerComercio(String pstIdComercio) {

        String sql = "SELECT ID_COMERCIO, CVE_COMERCIO, DIAS_HABILES, HORA_INI," +
                "HORA_FIN, NOMBRE, CVE_COM_ORIGEN, CORREO_CONCIL, ESTATUS " +
                "FROM POL_04_C_COMERCIO " +
                "WHERE ID_COMERCIO =  " + pstIdComercio;


        ComercioDTO comercio = (ComercioDTO) jt.queryForObject(sql, (ResultSet rs, int index) -> {           
                ComercioDTO dto = new ComercioDTO();
                dto.setIdComercio(rs.getString(1));
                dto.setCveComercio(rs.getString(2));
                dto.setDiasHabiles(rs.getString(3));
                dto.setHoraIni(rs.getString(4));
                dto.setHoraFin(rs.getString(5));
                dto.setNombre(rs.getString(6));
                dto.setCveComercioOrigen(rs.getString(7));
                dto.setCorreoConcil(rs.getString(8));
                dto.setEstatus(rs.getString(9));

                return dto;

            
        });

        return comercio;

    }

    public int actualizarComercio(ComercioDTO pobDatos) {
        int lnuError = -1;
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE POL_04_C_COMERCIO SET CVE_COMERCIO = ? , NOMBRE = ? , CVE_COM_ORIGEN = ? ");


        if (pobDatos != null && pobDatos.getDiasHabiles() != null && pobDatos.getDiasHabiles().length() > 0)
            sql.append(", DIAS_HABILES = '" + pobDatos.getDiasHabiles() + "' ");        

        if (pobDatos != null && !pobDatos.getHoraIni().equals("00:00"))
            sql.append(", HORA_INI = '" + pobDatos.getHoraIni() + "' ");        

        if (pobDatos != null && !pobDatos.getHoraFin().equals("00:00"))
            sql.append(", HORA_FIN = '" + pobDatos.getHoraFin() + "' ");        

        if (pobDatos != null && pobDatos.getCorreoConcil()!=null && pobDatos.getCorreoConcil().length()>0)
            sql.append(", CORREO_CONCIL = '" + pobDatos.getCorreoConcil() + "' ");

        if (pobDatos != null && pobDatos.getEstatus()!=null && pobDatos.getEstatus().length()>0)
            sql.append(", ESTATUS = '" + pobDatos.getEstatus() + "' ");
        
        sql.append("WHERE ID_COMERCIO = " + pobDatos.getIdComercio());


        if (pobDatos != null) {
            lnuError = jt.update(sql.toString(), new Object[]{pobDatos.getCveComercio(), pobDatos.getNombre(), pobDatos.getCveComercioOrigen()});
        }

        return lnuError;
    }

    public ComercioDTO obtenerComercioCve(String pstCveComercio) {

        String sql = "SELECT ID_COMERCIO, CVE_COMERCIO, DIAS_HABILES, HORA_INI," +
                "HORA_FIN, NOMBRE, CVE_COM_ORIGEN, CORREO_CONCIL " +
                "FROM POL_04_C_COMERCIO " +
                "WHERE CVE_COMERCIO =  '" + pstCveComercio + "'";


        ComercioDTO comercio = (ComercioDTO) jt.queryForObject(sql, (ResultSet rs, int index) -> {
                ComercioDTO dto = new ComercioDTO();
                dto.setIdComercio(rs.getString(1));
                dto.setCveComercio(rs.getString(2));
                dto.setDiasHabiles(rs.getString(3));
                dto.setHoraIni(rs.getString(4));
                dto.setHoraFin(rs.getString(5));
                dto.setNombre(rs.getString(6));
                dto.setCveComercioOrigen(rs.getString(7));
                dto.setCorreoConcil(rs.getString(8));
                return dto;

            
        });

        return comercio;

    }


    public List obtenerComerciosOfCobro() {
        String sql = "SELECT CVE_COMERCIO, NOMBRE FROM POL_04_C_COMERCIO " +
                     "ORDER BY NOMBRE ";
        return jt.query(sql,(ResultSet rs, int index) -> {
            ComercioDTO dto = new ComercioDTO();
            dto.setCveComercio(rs.getString(1).trim());
            dto.setNombre(rs.getString(2));
            return dto;
        });
    }


}
