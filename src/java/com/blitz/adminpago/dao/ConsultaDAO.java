/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dao;

import com.blitz.adminpago.dto.ConsultaDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author pgrande
 */
public class ConsultaDAO {

    private JdbcTemplate jt;

    public void setDataSource(DataSource dataSource)  {
        jt = new JdbcTemplate(dataSource);
    }

    public List obtenerConsultasTel(String pstFechaI,String pstFechaF, String pstTelefono)
    {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ID_CONSULTA, TELEFONO, MONTO_PAGAR, FECHA_SOL, ");
        sql.append("FECHA_RESP_PISA, FECHA_RESP_TRO, CODIGO_RESP, TIPO_INGRESO, AUDIT_NUMBER, ADQUIRIENTE, ");
        sql.append("TIENDA_TERMINAL, UNIDAD, SALDO_VENCIDO, MIN_REANUDACION, TRANSACCION  ");
        sql.append("FROM POL_01_T_CONSULTA ");
        sql.append("WHERE TELEFONO = '" + pstTelefono + "'");
        if( pstFechaI != null && pstFechaI.length() > 0 )
            sql.append("AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') >= TO_DATE('" + pstFechaI + "','YYYY/MM/DD') ");
        if( pstFechaF != null && pstFechaF.length() > 0 )
            sql.append("AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') <= TO_DATE('" + pstFechaF + "','YYYY/MM/DD') ");

        sql.append(" ORDER BY FECHA_SOL ");

         return jt.query(sql.toString(),(ResultSet rs, int index) -> {
            ConsultaDTO consulta = new ConsultaDTO();
            consulta.setIdConsulta(rs.getString(1));
            consulta.setTelefono(rs.getString(2));
            consulta.setMontoaPagar(rs.getString(3));
            consulta.setFechaSol(rs.getString(4));
            consulta.setFechaRespPisa(rs.getString(5));
            consulta.setFechaRespTro(rs.getString(6));
            consulta.setCodigoResp(rs.getString(7));
            consulta.setTipoIngreso(rs.getString(8));
            consulta.setAuditNumber(rs.getString(9));
            consulta.setAdquiriente(rs.getString(10));
            consulta.setTiendaTerminal(rs.getString(11));
            consulta.setUnidad(rs.getString(12));
            consulta.setSaldoVencido(rs.getString(13));
            consulta.setMinReanudacion(rs.getString(14));
            consulta.setTransaccion(rs.getString(15));

            return consulta;
         });


    }


    //Borra los registros de las consultas generadas por el batch
    public int eliminarConsultasTel(String pstTelefonos)
    {  
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM POL_01_T_CONSULTA ");
        sql.append("WHERE TELEFONO IN (" + pstTelefonos + ") ");
        sql.append("AND SYSDATE - TO_DATE(FECHA_SOL,'YYYY/MM/DD HH24:MI:SS') BETWEEN (1/1440) AND (10/1440) ");

        return jt.update(sql.toString());        
    }

}
