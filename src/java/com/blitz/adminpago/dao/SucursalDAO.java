/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dao;


import com.blitz.adminpago.dto.SucursalDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.stereotype.Repository;

@Repository
public class SucursalDAO {
    
    
    private JdbcTemplate jt;
    private SimpleJdbcInsert insertaConsulta;
    private OracleSequenceMaxValueIncrementer siguienteSucursal;


    @Resource(name="pagosvtDS")
    public void setDataSource(DataSource dataSource) {
        this.jt = new JdbcTemplate(dataSource);

        this.siguienteSucursal = new OracleSequenceMaxValueIncrementer(dataSource, "S_POL_05_C_SUCURSAL");


    }

    public HashMap obtenerSucursalesCom(String pstCveCom)
    {
        String sql = "SELECT CVE_SUC_ORIGEN, NOMBRE " +
                    "FROM POL_05_C_SUCURSAL " ;

        if (pstCveCom !=null && pstCveCom.length() > 0 && !pstCveCom.equals("-"))
                sql += "WHERE ID_COMERCIO = '" + pstCveCom + "'";


        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                SucursalDTO dto = new SucursalDTO();
                dto.setIdSucursal(rs.getString(1));
                dto.setNombre(rs.getString(2));

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
                SucursalDTO dto = (SucursalDTO) it.next();
                lobHashLista.put(dto.getNombre(),dto.getIdSucursal());
             }
         }         
         return lobHashLista;
    }


    public HashMap obtenerSucursalesComCve(String pstCveCom)
    {
        String sql = "SELECT S.CVE_SUC_ORIGEN, S.NOMBRE " +
                    "FROM POL_05_C_SUCURSAL S, POL_04_C_COMERCIO C " +
                    "WHERE S.ID_COMERCIO = C.ID_COMERCIO " ;

        if (pstCveCom !=null && pstCveCom.length() > 0 && !pstCveCom.equals("-"))
                sql += "AND C.CVE_COMERCIO = '" + pstCveCom + "'";


        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                SucursalDTO dto = new SucursalDTO();
                dto.setIdSucursal(rs.getString(1));
                dto.setNombre(rs.getString(2));

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
                SucursalDTO dto = (SucursalDTO) it.next();
                lobHashLista.put(dto.getNombre(),dto.getIdSucursal());
             }
         }
         return lobHashLista;
    }


    public HashMap obtenerSucursales() {

        String sql = "SELECT CVE_SUC_ORIGEN, NOMBRE " +
                    "FROM POL_05_C_SUCURSAL WHERE ID_COMERCIO IN (30,250) " ;


        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                SucursalDTO dto = new SucursalDTO();
                dto.setIdSucursal(rs.getString(1));
                dto.setNombre(rs.getString(2));

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
                SucursalDTO dto = (SucursalDTO) it.next();
                lobHashLista.put(dto.getNombre(),dto.getIdSucursal());
             }
         }
         
         return lobHashLista;

    }


    public List obtenerSucCom(SucursalDTO pobDatos)
    {

        String sql = "SELECT SU.ID_SUCURSAL, SU.ID_COMERCIO, SU.ESTATUS, SU.HORA_INI,SU.HORA_FIN, "+
                    "SU.NOMBRE, SU.CVE_SUC_ORIGEN, SU.DIAS_HABILES,SU.LATITUD,SU.LONGITUD,CO.NOMBRE AS NOMBRE_COMERCIO, " +
                    "SU.CVE_SUCURSAL, SU.ESTADO, SU.LOCALIDAD, SU.MUNICIPIO, SU.CODIGO_POSTAL  " +
                    "FROM POL_05_C_SUCURSAL SU, POL_04_C_COMERCIO CO " +
                    "WHERE SU.ID_COMERCIO > 0 "+
                    "AND SU.ID_COMERCIO = CO.ID_COMERCIO AND SU.ID_COMERCIO IN (30,250) " ;


        if (pobDatos !=null && pobDatos.getIdComercio()!= null && !pobDatos.getIdComercio().equals("*"))
                sql += "AND SU.ID_COMERCIO = '" + pobDatos.getIdComercio() + "' ";

        if (pobDatos !=null && pobDatos.getNombre() != null && pobDatos.getNombre().length() > 0 )
                sql += "AND upper (SU.NOMBRE) like upper('%" + pobDatos.getNombre() + "%') ";

        if (pobDatos !=null && pobDatos.getCveSucursalOrigen()!=null && pobDatos.getCveSucursalOrigen().length() > 0 )
                sql += "AND SU.CVE_SUC_ORIGEN = '" + pobDatos.getCveSucursalOrigen() + "' ";

        if (pobDatos !=null && pobDatos.getEstado()!=null && pobDatos.getEstado().length() > 0 && pobDatos.getEstado().equals("*") ==false )
                sql += "AND SU.ESTADO = '" + pobDatos.getEstado() + "' ";

        if (pobDatos !=null && pobDatos.getCveSucursal()!=null && pobDatos.getCveSucursal().length() > 0 )
                sql += "AND SU.CVE_SUCURSAL = '" + pobDatos.getCveSucursal() + "' ";

        sql += "ORDER BY SU.ID_COMERCIO, SU.NOMBRE ";


        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                SucursalDTO dto = new SucursalDTO();
                dto.setIdSucursal(rs.getString("ID_SUCURSAL"));
                dto.setNombre(rs.getString("NOMBRE"));
                dto.setNombreComercio(rs.getString("NOMBRE_COMERCIO"));
                dto.setIdComercio(rs.getString("ID_COMERCIO"));
                dto.setEstatus(rs.getString("ESTATUS"));
                dto.setHoraIni(rs.getString("HORA_INI"));
                dto.setHoraFin(rs.getString("HORA_FIN"));
                dto.setCveSucursalOrigen(rs.getString("CVE_SUC_ORIGEN"));
                dto.setDiasHabiles(rs.getString("DIAS_HABILES"));
                dto.setLatitud(rs.getString("LATITUD"));
                dto.setLongitud(rs.getString("LONGITUD"));
                dto.setCveSucursal(rs.getString("CVE_SUCURSAL"));
                dto.setEstado(rs.getString("ESTADO"));
                dto.setMunicipio(rs.getString("MUNICIPIO"));
                dto.setLocalidad(rs.getString("LOCALIDAD"));
                dto.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
                return dto;
             }

        };
         List lobLista =  jt.query(sql,rowMapper);

         return lobLista;
    }


    /**
     * Inserta Sucursal 
     * @param pobDatos
     * @return
     */
    public long insertaSucursal(SucursalDTO pobDatos){

            int resp=0;
            String sql ="INSERT INTO POL_05_C_SUCURSAL "+
                        "(ID_SUCURSAL,ID_COMERCIO, ESTATUS, HORA_INI,HORA_FIN, NOMBRE, CVE_SUC_ORIGEN, DIAS_HABILES, LATITUD, LONGITUD, CODIGO_POSTAL, " +
                        "MUNICIPIO, LOCALIDAD, ESTADO, CVE_SUCURSAL ) "+
                        "values (" ;
            long ret = 0;

            if (pobDatos.getIdSucursal() == null || pobDatos.getIdSucursal().length() == 0)
                ret = siguienteSucursal.nextLongValue();
            else
                ret = Long.parseLong(pobDatos.getIdSucursal());


                sql += String.valueOf(ret) + "," +pobDatos.getIdComercio()+",'"+pobDatos.getEstatus()+"','"+pobDatos.getHoraIni()+"'"+
                        ",'"+pobDatos.getHoraFin()+"','"+pobDatos.getNombre()+"','"+pobDatos.getCveSucursalOrigen()+"','"+pobDatos.getDiasHabiles()+"','"
                        +pobDatos.getLatitud()+"','"+pobDatos.getLongitud()+"','" + pobDatos.getCodigoPostal() + "','" + pobDatos.getLocalidad() + "','" + 
                        pobDatos.getMunicipio() + "','" + pobDatos.getEstado() + "','" + pobDatos.getCveSucursal() + "')";

            resp = jt.update(sql);


        return ret;
    }


    /**
     * Obtiene una sucursal
     * @param lstIdSucursal
     * @return
     */
     public SucursalDTO obtenSucursal(String lstIdSucursal){

        String sql = "SELECT S.ID_SUCURSAL, S.ID_COMERCIO, S.CVE_SUCURSAL, S.HORA_INI, " +
            "S.HORA_FIN, S.NOMBRE, S.CVE_SUC_ORIGEN, S.DIAS_HABILES, S.LATITUD, " +
            "S.LONGITUD, S.ESTATUS, S.CODIGO_POSTAL, S.MUNICIPIO,  " +
            "S.LOCALIDAD, S.ESTADO , C.NOMBRE " +
            "FROM POL_05_C_SUCURSAL S, POL_04_C_COMERCIO C " +
            "WHERE S.ID_COMERCIO = C.ID_COMERCIO " +
            "AND S.ID_SUCURSAL = " + lstIdSucursal;

        SucursalDTO sucursal = (SucursalDTO) jt.queryForObject(sql,new RowMapper() {

                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SucursalDTO dto = new SucursalDTO();
                    dto.setIdSucursal(rs.getString(1));
                    dto.setIdComercio(rs.getString(2));
                    dto.setCveSucursal(rs.getString(3));
                    dto.setHoraIni(rs.getString(4));
                    dto.setHoraFin(rs.getString(5));
                    dto.setNombre(rs.getString(6));
                    dto.setCveSucursalOrigen(rs.getString(7));
                    dto.setDiasHabiles(rs.getString(8));
                    dto.setLatitud(rs.getString(9));
                    dto.setLongitud(rs.getString(10));
                    dto.setEstatus(rs.getString(11));
                    dto.setCodigoPostal(rs.getString(12));
                    dto.setMunicipio(rs.getString(13));
                    dto.setLocalidad(rs.getString(14));
                    dto.setEstado(rs.getString(15));
                    dto.setNombreComercio(rs.getString(16));

                    return dto;
                }
            }
        );

        return sucursal;
     }

     /**
      * Actualiza una Sucursal
      * @param pobDatos
      * @return
      */
     public int actualizarSucursal(SucursalDTO pobDatos)
    {
        int lnuError = -1;


        String sql = "UPDATE POL_05_C_SUCURSAL SET "+
                    "ID_COMERCIO = "+pobDatos.getIdComercio()+", ESTATUS='"+pobDatos.getEstatus()+"', "+
                    "NOMBRE='"+pobDatos.getNombre()+"', CVE_SUC_ORIGEN='"+pobDatos.getCveSucursalOrigen()+"', " +
                    "CODIGO_POSTAL='" + pobDatos.getCodigoPostal() + "', " +
                    "MUNICIPIO='" + pobDatos.getMunicipio() + "', " +
                    "LOCALIDAD='" + pobDatos.getLocalidad() + "', " +
                    "ESTADO='" + pobDatos.getEstado() + "', " +
                    "CVE_SUCURSAL='" + pobDatos.getCveSucursal() + "' " ;


        if ( pobDatos != null && pobDatos.getDiasHabiles() != null && pobDatos.getDiasHabiles().length() > 0 ){
            sql += ", DIAS_HABILES = '" + pobDatos.getDiasHabiles() + "' ";
        }else{
            sql += ", DIAS_HABILES = '' ";
        }

        if ( pobDatos != null && !pobDatos.getHoraIni().equals("00:00") )
            sql += ", HORA_INI = '" + pobDatos.getHoraIni() + "' ";

        if ( pobDatos != null && !pobDatos.getHoraFin().equals("00:00") )
            sql += ", HORA_FIN = '" + pobDatos.getHoraFin() + "' ";

        if(pobDatos != null && pobDatos.getLatitud().length() > 0 )
            sql += ", LATITUD = '" + pobDatos.getLatitud() + "' ";

        if(pobDatos != null && pobDatos.getLongitud().length() > 0 )
            sql += ", LONGITUD = '" + pobDatos.getLongitud() + "' ";


        sql += "WHERE ID_SUCURSAL = " + pobDatos.getIdSucursal() ;


        if ( pobDatos != null  )
            //lnuError = jt.update(sql, new Object[] { pobDatos.getIdComercio(), pobDatos.getCveSucursal(), pobDatos.getNombre(), pobDatos.getCveSucursalOrigen() });
            lnuError = jt.update(sql);
        return lnuError;
    }


}
