/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dao;

import com.blitz.adminpago.dto.PerfilDTO;
import com.blitz.adminpago.dto.PerfilModuloDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
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
public class PerfilDAO {

    
    private JdbcTemplate jt;
    private SimpleJdbcInsert insertaPerfil;
    private OracleSequenceMaxValueIncrementer siguientePerfil;
    private SimpleJdbcInsert insertaPerfilModulo;
    private OracleSequenceMaxValueIncrementer siguientePerMod;

    //private DataSource vtas3DataSource;
    
    @Resource(name = "pagosvtDS")
    public void setDataSource(DataSource dataSource)  {
        jt = new JdbcTemplate(dataSource);

        /*this.insertaPerfil =
                new SimpleJdbcInsert(dataSource)
                        .withTableName("POL_07_T_SUBPERFIL")
                        .usingColumns("ID_SUBPERFIL","ID_PERFIL","NOMBRE_SUBPERFIL","ID_TIENDA" );

        this.siguientePerfil = new OracleSequenceMaxValueIncrementer(dataSource,"S_POL_06_C_PERFIL");

        this.insertaPerfilModulo =
                new SimpleJdbcInsert(dataSource)
                        .withTableName("POL_09_T_SUBPERFIL_MODULO")
                        .usingColumns("ID_SUBPERMOD","ID_PERFIL","ID_SUBPERFIL","ID_MODULO","ORDEN");

        this.siguientePerMod = new OracleSequenceMaxValueIncrementer(dataSource,"S_POL_09_T_SUBPERFIL_MODULO");
         
         */

    }


    /**
     * @param vtas3DataSource the vtas3DataSource to set
     */
    //@Resource(name = "pagosvtDS")
    /*
    public void setVtas3DataSource(DataSource vtas3DataSource) {
        jt = new JdbcTemplate(vtas3DataSource);


        this.insertaPerfil =
                new SimpleJdbcInsert(vtas3DataSource)
                        .withTableName("POL_06_C_PERFIL")
                        .usingColumns("ID_PERFIL","NOMBRE","ID_TIENDA" );

        this.siguientePerfil = new OracleSequenceMaxValueIncrementer(vtas3DataSource,"S_POL_06_C_PERFIL");

         this.insertaPerfilModulo =
                new SimpleJdbcInsert(vtas3DataSource)
                        .withTableName("POL_09_T_SUBPERFIL_MODULO")
                        .usingColumns("ID_SUBPERMOD","ID_PERFIL","ID_MODULO","ORDEN");

        this.siguientePerMod = new OracleSequenceMaxValueIncrementer(vtas3DataSource,"S_POL_09_T_SUBPERFIL_MODULO");

        this.vtas3DataSource = vtas3DataSource;
    }*/


    public List obtenerPerfiles(PerfilDTO pobPerfil)
    {

        String sql = "select p.id_perfil, p.nombre,p.id_tienda, c.nombre as tienda "+
                    "from pol_06_c_perfil p, pol_04_c_comercio c "+
                    "where p.id_tienda = c.id_comercio ";

        if ( pobPerfil != null && pobPerfil.getNombre()!= null && pobPerfil.getNombre().length() > 0 )
            sql += "and upper(p.nombre) like upper('%"+pobPerfil.getNombre()+"%')";

        sql += "ORDER BY ID_PERFIL ";


        

        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                PerfilDTO dto = new PerfilDTO();                
                dto.setIdPerfil(rs.getString(1));
                dto.setNombre(rs.getString(2));
                dto.setIdComercio(rs.getString(3));
                dto.setTienda(rs.getString(4));
                return dto;
            }
        };

        return jt.query(sql,rowMapper);

    }

    
    /**
     *
     * @param pstIdPerfil
     * @return
     */
    public PerfilDTO obtenerPerfil(String pstIdPerfil)
    {

          String sql = "select id_perfil, nombre "+
                    "from pol_06_c_perfil  "+
                    "where id_perfil = "+pstIdPerfil;
        
        PerfilDTO dto = (PerfilDTO) jt.queryForObject(sql, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PerfilDTO perfil = new PerfilDTO();
                    perfil.setIdPerfil(rs.getString(1));
                    perfil.setNombre(rs.getString(2));
                    return perfil;
                }
        });

        return dto;

    }


    /**
     * 
     * @param pstIdPerfil
     * @return
     */
    public List obtenerPerfilModulo(String pstIdPerfil)
    {

        String sql = "SELECT PM.ID_SUBPERMOD, PM.ID_SUBPERFIL, PM.ID_MODULO, PM.ORDEN, M.NOMBRE " +
                      "FROM POL_09_T_SUBPERFIL_MODULO PM, POL_08_C_MODULO M " +
                     "WHERE PM.ID_PERFIL = " + pstIdPerfil +
                     " AND PM.ID_MODULO = M.ID_MODULO " +
                      "ORDER BY ORDEN ";


        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                PerfilModuloDTO dto = new PerfilModuloDTO();
                dto.setIdPerMod(rs.getInt(1));
                dto.setIdPerfil(rs.getInt(2));
                dto.setIdModulo(rs.getInt(3));
                dto.setOrden(rs.getInt(4));
                dto.setNombreModulo(rs.getString(5));

                return dto;
            }
        };

        return jt.query(sql,rowMapper);

    }

    public int eliminarModPerfil(String pstIdPerMod)
    {
        int lnuError = -1;

        String sql = "DELETE FROM POL_09_T_SUBPERFIL_MODULO " +
                "WHERE ID_PERFIL = ?  " ;

        if ( pstIdPerMod != null && pstIdPerMod.length() > 0 )
            lnuError = jt.update(sql, new Object[] { pstIdPerMod });

        return lnuError;
    }


    public int eliminarModulosPerfil(String pstIdPerMod)
    {
        int lnuError = -1;

        String sql = "DELETE FROM POL_09_T_SUBPERFIL_MODULO " +
                "WHERE ID_PERFIL= ?  " ;

        if ( pstIdPerMod != null && pstIdPerMod.length() > 0 )
            lnuError = jt.update(sql, new Object[] { pstIdPerMod });

        return lnuError;
    }


    /**
     *
     * @param perfil
     * @return
     */
    public int actualizarPerfil(PerfilDTO perfil)
    {
        int lnuError = -1;

         String sql = "update pol_06_c_perfil set nombre = '"+perfil.getNombre()+"' "+
                       "WHERE ID_PERFIL= "+ perfil.getIdPerfil();


        if ( perfil != null  )
            lnuError = jt.update(sql);

        return lnuError;
    }

    /**
     * 
     * @param pstIdPerfil
     * @param pstIdModulo
     * @return
     */
    public int agregarModuloPerfil(String pstIdPerfil, String pstIdModulo) {
        Map<String, Object> parameters = new HashMap<String, Object>(4);

        int  orden=1;
        double ret = siguientePerMod.nextLongValue();
        parameters.put("ID_SUBPERMOD", ret);
        parameters.put("ID_PERFIL", pstIdPerfil);
        parameters.put("ID_MODULO", pstIdModulo);
        parameters.put("ORDEN",orden);
        int lnuRet = insertaPerfilModulo.execute(parameters);

        return lnuRet;
    }


    /**
     * 
     * @return
     */
    public int obtenerUltOrdenPerMod()
    {
        String sql = "SELECT MAX(id_subpermod) FROM POL_09_T_SUBPERFIL_MODULO ";
        return jt.queryForInt(sql);

    }



    public int actualizarOrdenPerMod(String pstIdPerMod, String pstOrden)
    {
        String sql = "UPDATE POL_09_T_SUBPERFIL_MODULO " +
                     "SET ORDEN = ?  " +
                     "WHERE ID_SUBPERMOD = ? " ;

        return jt.update(sql, new Object[] { pstOrden, pstIdPerMod });

    }

    public String obtenerPerModAntA(String pstIdPerfil, String pstOrden, String pstIdSubPerfil)
    {
        String sql = "SELECT ID_SUBPERMOD||':'||ORDEN " +
                    "FROM POL_09_T_SUBPERFIL_MODULO " +
                    "WHERE ID_PERFIL = " + pstIdPerfil +
                    " AND ID_SUBPERFIL = " + pstIdSubPerfil + " " +
                    "AND ORDEN = ( " +
                        "SELECT MAX(ORDEN) " +
                        "FROM POL_09_T_SUBPERFIL_MODULO " +
                        "WHERE ID_PERFIL = " + pstIdPerfil + 
                        " AND ID_SUBPERFIL = " + pstIdSubPerfil +
                        " AND ORDEN < " + pstOrden + " ) " ;

        try
        {
            return (String)jt.queryForObject(sql, String.class);
        }
        catch(Exception e)
        {
            return null;
        }

    }

    public String obtenerPerModPosA(String pstIdPerfil, String pstOrden, String pstIdSubPerfil)
    {

        String sql = "SELECT ID_SUBPERMOD||':'||ORDEN " +
                    "FROM POL_09_T_SUBPERFIL_MODULO " +
                    "WHERE ID_PERFIL = " + pstIdPerfil +
                    " AND ID_SUBPERFIL = " + pstIdSubPerfil + " " +
                    "AND ORDEN = ( " +
                        "SELECT MIN(ORDEN) " +
                        "FROM POL_09_T_SUBPERFIL_MODULO " +
                        "WHERE ID_PERFIL = " + pstIdPerfil +
                        " AND ID_SUBPERFIL = " + pstIdSubPerfil +
                        " AND ORDEN > " + pstOrden + " ) " ;


        try
        {
            return (String)jt.queryForObject(sql, String.class);
        }
        catch(Exception e)
        {
            return null;
        }

    }
    

    /**
     *
     * @param pobPerfil
     * @return
     */
    public int insertarPerfil(PerfilDTO pobPerfil) {
        Map<String, Object> parameters = new HashMap<String, Object>(3);

        int ret = (int) siguientePerfil.nextLongValue();
        //parameters.put("ID_SUBPERFIL", ret);
        parameters.put("ID_PERFIL", ret);
        parameters.put("NOMBRE", pobPerfil.getNombre());
        parameters.put("ID_TIENDA", 1);
        int lnuRet = insertaPerfil.execute(parameters);

        return ret;
    }


    /**
     * Obtiene el ultimo registro den perfiles
     * @return
     */
    public int obtenerUltOrdenPerfil()
    {
        String sql = "SELECT MAX(id_perfil) FROM pol_06_c_perfil ";

        return jt.queryForInt(sql);
    }

    public int eliminarSubPerfilMod(String pstIdPerfil)
    {
        String sql = "DELETE FROM POL_09_T_SUBPERFIL_MODULO " +
                     "WHERE ID_PERFIL = ? " ;
        return jt.update(sql, new Object[] { pstIdPerfil});

    }

    /**
     *
     * @param pstIdPerfil
     * @return
     */
    public int eliminarPerfil(String pstIdPerfil)
    {
        String sql = "DELETE FROM POL_06_C_PERFIL " +
                     "WHERE ID_PERFIL = ? " ;
        return jt.update(sql, new Object[] { pstIdPerfil});
    }

    public int eliminarSubPerfil(String pstIdPerfil, String pstIdSubPerfil)
    {
        String sql = "DELETE FROM POL_07_T_SUBPERFIL " +
                     "WHERE ID_PERFIL = ? AND ID_SUBPERFIL = ? " ;

        return jt.update(sql, new Object[] { pstIdPerfil, pstIdSubPerfil });

    }



    /**
     *
     * @return
     */
     public HashMap obtenerPerfiles() {

        String sql = "select p.id_perfil, p.nombre,p.id_tienda, c.nombre as tienda "+
                    "from pol_06_c_perfil p, pol_04_c_comercio c "+
                    "where p.id_tienda = c.id_comercio "+
                    "and  p.id_perfil <> 0 "+
                    "ORDER BY ID_PERFIL ";

        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                PerfilDTO dto = new PerfilDTO();
                dto.setIdPerfil(rs.getString(1));
                dto.setNombre(rs.getString(2));
                dto.setIdComercio(rs.getString(3));
                dto.setTienda(rs.getString(4));
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
                PerfilDTO dto = (PerfilDTO) it.next();
                lobHashLista.put(dto.getNombre(),dto.getIdPerfil());
             }
         }

         return lobHashLista;

    }


    /**
     * @return the vtas3DataSource
     */
    /*public DataSource getVtas3DataSource() {
        return vtas3DataSource;
    }*/

    

}
