/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dao;

import com.blitz.adminpago.dto.BinesDTO;
import com.blitz.adminpago.dto.DatosCuerpoDTO;
import com.blitz.adminpago.dto.PagoAPPEstadisticaDTO;
import com.blitz.adminpago.dto.PagoConciliacionDTO;
import com.blitz.adminpago.dto.PagoDTO;
import com.blitz.adminpago.dto.PagoMKTDTO;
import com.blitz.adminpago.dto.TelefonoNegroDTO;
import com.blitz.adminpago.util.UtilConciliaPago;
import com.blitz.adminpago.util.Utilerias;
import com.blitz.adminpago.util.Rijndael_Algorithm;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author pgrande
 */
public class PagoDAO {

    private JdbcTemplate jt;
    private Map csMap;
    private String usrTC;
    private String comercioBD1;
    private BinesDAO binesDAO;

    private Logger log = Logger.getLogger("com.blitz.adminpagoline.dao.PagoDAO");

    public void setDataSource(DataSource dataSource)  {
        jt = new JdbcTemplate(dataSource);
    }

    public List obtenerPagos(String pstFechaI,String pstFechaF, String pstComercio, String pstSucursal,
            String pstEstatus, String pstAutorizacion, String pstTelefono, String pstTransaccion,
            String pstTarjeta, String pstRefBanco, String pstCS, String pstLib,
            String pstFechaIO,String pstFechaFO, String pstImporte, String pstLogin, int reporte)
    {


        String sql = "SELECT P.ID_PAGO, P.TELEFONO, P.MONTO_PAGAR, FECHA_SOL, " +
                "FECHA_RESP_PISA, FECHA_RESP_TRO, FECHA_CONTABLE, " +
                "FECHA_CAPTURA, HORA, DIA, MONEDA, TIPO_INGRESO, CODIGO_RESP," +
                "TIPO_RESP, AUDIT_NUMBER, ADQUIRIENTE, TIENDA_TERMINAL, UNIDAD, " +
                "SECUENCIA_PISA, SALDO_VENCIDO, MIN_REANUDACION, MONTO_PAGADO, " +
                "TRANSACCION, P.ESTATUS, P.CAJA, P.ESTATUS_CONCILPISA, P.ESTATUS_CONCILTRO " ;

        if ( pstComercio != null && (pstComercio.equals("MKT")||pstComercio.equals("APT"))) {
            String fechaIC = pstFechaI.replaceAll("/", "-");
            String fechaFC = pstFechaF.replaceAll("/", "-");

            sql +=  ",MP.TARJETA, MP.TIPOTARJETA, MP.REFERENCIA, MP.SECURE, P.CS, P.LIBRERIA, MP.CORREO, '" + pstLogin +
                    "' FROM POL_04_C_COMERCIO C , " ;


            if ( pstComercio != null && comercioBD1.indexOf(pstComercio) != -1 && Utilerias.esFechaHistorico(fechaIC))
             sql +=  " POL_02_T_PAGO_BK P LEFT OUTER JOIN MT_PAGO_BK MP ON P.TRANSACCION =  TO_CHAR(MP.ID) " ;
            else
             sql +=  " POL_02_T_PAGO P LEFT OUTER JOIN MT_PAGO MP ON P.TRANSACCION =  TO_CHAR(MP.ID) " ;


            if ( fechaIC.equals(fechaFC) == true )
                sql+=   "AND TO_CHAR(MP.FECHAESTADO,'YYYY-MM-DD') = '" + fechaIC + "' " ;
            else
                sql+=   "AND TO_CHAR(MP.FECHAESTADO,'YYYY-MM-DD') BETWEEN '" + fechaIC + "' AND '" + fechaFC + "' " ;

            sql +=  "AND MP.ESTADO IN ( 'T55', 'T54', 'T59', 'T60', 'T61') " +
                    "WHERE P.ADQUIRIENTE = C.CVE_COMERCIO " ;
        }
        else {
            sql +=  ",'', '', '', '', P.CS, P.LIBRERIA, '' , '" + pstLogin +
                    "' FROM POL_04_C_COMERCIO C , " ;
            if ( pstComercio != null && pstComercio.equals("BSN") && Utilerias.esFechaHistorico(pstFechaI))
                sql += " POL_02_T_PAGO P  " ;
            else
                sql += " POL_02_T_PAGO P  " ;

            sql += "WHERE P.ADQUIRIENTE = C.CVE_COMERCIO " ;
                    //"FROM POL_02_T_PAGO P " +
                    //"WHERE ID_PAGO IS NOT NULL " ;
        }
        
        if( pstComercio != null && pstComercio.length() > 0 && !pstComercio.equals("-") && !pstComercio.equals("*") )
            //sql += "AND C.ID_COMERCIO = '" + pstComercio + "' ";
            sql += "AND P.ADQUIRIENTE = '" + pstComercio + "' ";

        if( pstFechaI != null && pstFechaI.length() > 0 && pstFechaF != null && pstFechaF.length() > 0 )
        {
                sql += "AND FECHA_SOL BETWEEN '" + pstFechaI + " 00:00:00' AND '" + pstFechaF + " 24:00:00' " ;
            //sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') >= TO_DATE('" + pstFechaI + "','YYYY/MM/DD') ";
        }
        else
        {
            if( pstFechaI != null && pstFechaI.length() > 0 )
                sql += "AND FECHA_SOL >= '" + pstFechaI + " 00:00:00' ";

            if( pstFechaF != null && pstFechaF.length() > 0 )
                sql += "AND FECHA_SOL <= '" + pstFechaF + " 24:00:00' ";
            //sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') <= TO_DATE('" + pstFechaF + "','YYYY/MM/DD') ";

        }


        if( pstFechaIO != null && pstFechaIO.length() > 0 )
            sql += "AND TO_DATE(SUBSTR(MP.FECHAPAGO ,0, 10),'YYYY/MM/DD') >= TO_DATE('" + pstFechaIO + "','YYYY/MM/DD') ";

        if( pstFechaFO != null && pstFechaFO.length() > 0 )
            sql += "AND TO_DATE(SUBSTR(MP.FECHAPAGO ,0, 10),'YYYY/MM/DD') <= TO_DATE('" + pstFechaFO + "','YYYY/MM/DD') ";

        if( pstImporte != null && pstImporte.length() > 0 && !pstEstatus.equals("-") && pstComercio!=null && pstComercio.trim().equals("MKT") )
            sql += "AND MP.IMPORTE = " + pstImporte + " ";


        if( pstSucursal != null && pstSucursal.length() > 0 && !pstSucursal.equals("-") && !pstSucursal.equals("*") )
        {
            if ( pstComercio!=null && pstComercio.trim().equals("BSN"))
                sql += "AND TIENDA_TERMINAL LIKE '%" + pstSucursal + "' ";
            else
                sql += "AND TIENDA_TERMINAL LIKE '" + pstSucursal + "%' ";
        }

        if( pstEstatus != null && pstEstatus.length() > 0 && !pstEstatus.equals("-") && !pstEstatus.equals("*") )
            sql += "AND P.ESTATUS = '" + pstEstatus + "' ";

        if( pstAutorizacion != null && pstAutorizacion.length() > 0 )
            sql += "AND P.ID_PAGO = " + pstAutorizacion + " ";

        if( pstTransaccion != null && pstTransaccion.length() > 0 )
            sql += "AND P.TRANSACCION = '" + pstTransaccion + "' ";

        if( pstTelefono != null && pstTelefono.length() > 0 )
            sql += "AND P.TELEFONO = " + pstTelefono + " ";

        if( pstTarjeta != null && pstTarjeta.length() > 0 && 
                pstComercio!=null && (pstComercio.trim().equals("MKT")||pstComercio.trim().equals("APT") ))
        {
            Rijndael_Algorithm util = new Rijndael_Algorithm();
            sql += "AND (MP.TARJETA = '" + pstTarjeta + "' OR MP.TARJETA = '" + util.Encriptar(pstTarjeta) + "' ) " ;
        }

        if( pstRefBanco != null && pstRefBanco.length() > 0 && 
                pstComercio!=null && (pstComercio.trim().equals("MKT") ||pstComercio.trim().equals("APT") ) )
            sql += "AND MP.REFERENCIA = '" + pstRefBanco + "' ";

        if( pstCS != null && pstCS.length() > 0 && !pstCS.equals("*")  )
            sql += "AND P.CS IN (" + pstCS + ")  ";

        if( pstLib != null && pstLib.length() > 0 && !pstLib.equals("*") )
            sql += "AND P.LIBRERIA = '" + pstLib + "'  ";

        //sql += "ORDER BY P.ID_PAGO ";



        RowMapper rowMapper = new RowMapper() {
                DecimalFormat myFormatter = new DecimalFormat("###,##0.00");

                public Object mapRow(ResultSet rs, int index) throws SQLException {
                    
                        String tarjetaDesencriptada="";
//                        String subtarjeta="";
//                        String subtarjeta2="";
//                        EncriptadoAsterisco encriptadoAsterisco = new EncriptadoAsterisco();
                        
                        Rijndael_Algorithm util = new Rijndael_Algorithm();
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setMontoPagar(rs.getString(3));
                        pago.setFechaSol(rs.getString(4));
                        pago.setFechaRespPisa(rs.getString(5));
                        pago.setFechaRespTro(rs.getString(6));
                        pago.setFechaContable(rs.getString(7));
                        pago.setFechaCaptura(rs.getString(8));
                        pago.setHora(rs.getString(9));
                        pago.setDia(rs.getString(10));
                        pago.setMoneda(rs.getString(11));
                        pago.setTipoIngreso(rs.getString(12));
                        pago.setCodigoResp(rs.getString(13));
                        pago.setTipoResp(rs.getString(14));
                        pago.setAuditNumber(rs.getString(15));
                        pago.setAdquiriente(rs.getString(16));
                        pago.setTiendaTerm(rs.getString(17));
                        pago.setUnidad(rs.getString(18));
                        pago.setSecuenciaPisa(rs.getString(19));
                        pago.setSaldoVencido(rs.getString(20));
                        pago.setMinReanudacion(rs.getString(21));
                        try
                        {
                            double ld = rs.getDouble(22);
                            pago.setMontoPagado(myFormatter.format(ld));
                        } catch(Exception e){;}
                        pago.setTransaccion(rs.getString(23));
                        pago.setEstatus(rs.getString(24));
                        pago.setCaja(rs.getString(25));
                        pago.setEstatusConcilPisa(rs.getString(26));
                        pago.setEstatusConcilTro(rs.getString(27));
                        String tarjeta = rs.getString(28);

                        try {
                            if (tarjeta != null && !tarjeta.isEmpty()&& tarjeta.length()> 16) {
                                 tarjetaDesencriptada=util.Desencriptar(tarjeta);
                                 pago.setNumeroTC(tarjetaDesencriptada);

                                 BinesDTO bin = binesDAO.obtenerBin(tarjetaDesencriptada.substring(0, 6));
                                 if (bin != null) {
                                     pago.setUnidad(bin.getTipoTarjeta());
                                     pago.setMoneda(bin.getEmisor());
                                 }
                            }
                            else
                                pago.setNumeroTC(tarjeta);
                            }
                        catch(Exception e) {
                            pago.setNumeroTC(tarjeta);
                        }
                        if (pago.getAdquiriente() != null && pago.getAdquiriente().startsWith("MKT")
                                && (rs.getString(29)== null || rs.getString(29).length()==0))
                            pago.setTipoTC("A"); //tipo de tarjeta american express
                        else
                            pago.setTipoTC(rs.getString(29)); //tipo de tarjeta
                        pago.setAutorizacion(rs.getString(30));
                        pago.setSecure(rs.getString(31));
                        pago.setClaseServicio(rs.getString(32));
                        pago.setLibreria(rs.getString(33));
                        pago.setAvs(rs.getString(34));

                        String login = rs.getString(35);
                        if ( login != null && usrTC.indexOf(login) == -1 )
                        {
                            //cortamos la tarjeta a la terminacion
                            String tarj = pago.getNumeroTC();
                            if ( tarj != null && tarj.length() > 3 )
                                pago.setNumeroTC("************"+tarj.substring(tarj.length()-4));
                        }

                        String csHogar = (String)csMap.get("Hogar");
                        String csNegocio = (String)csMap.get("Negocio");
                        if ( pago.getClaseServicio() != null && csHogar.indexOf(pago.getClaseServicio().trim()) != -1)
                            pago.setTipotel("H");
                        else if(pago.getClaseServicio() != null && csNegocio.indexOf(pago.getClaseServicio().trim()) != -1)
                            pago.setTipotel("N");

                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }

    public List obtenerPagosT11(String pstFechaI,String pstComercio) {
        String sql = "SELECT ID, TELEFONO, ESTADO, FECHAESTADO, IMPORTE, TIPOTARJETA, CREDITOINBURSA " +
                "FROM MT_PAGO WHERE ESTADO = 'T11' " +
                "AND TO_CHAR(FECHAESTADO,'YYYY/MM/DD' ) = TO_CHAR( SYSDATE-1, 'YYYY/MM/DD') " ;
                //"AND ( CREDITOINBURSA = 'E' OR CREDITOINBURSA IS NULL ) ";

        RowMapper rowMapper = new RowMapper() {
                DecimalFormat myFormatter = new DecimalFormat("###,##0.00");

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setEstatus(rs.getString(3));
                        pago.setFechaSol(rs.getString(4));
                        pago.setMontoPagar(rs.getString(5));
                        pago.setTipoTC(rs.getString(6));

                        pago.setTipoIngreso("MKT");
                        String origen = rs.getString(7);
                        if ( origen != null && origen.startsWith("M"))
                            pago.setTipoIngreso("APT");

                        

                        return pago;
                }
        };

         return jt.query(sql,rowMapper);

    }

    public List obtenerPagosT11TelUnicos(String pstFechaI,String pstComercio) {
        String sql = "SELECT DISTINCT(TELEFONO) " +
                "FROM MT_PAGO WHERE ESTADO = 'T11' " +
                "AND TO_CHAR(FECHAESTADO,'YYYY/MM/DD' ) = TO_CHAR( SYSDATE-1, 'YYYY/MM/DD') " ;
                //"AND ( CREDITOINBURSA = 'E' OR CREDITOINBURSA IS NULL ) ";

        RowMapper rowMapper = new RowMapper() {

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoDTO pago = new PagoDTO();
                        pago.setTelefono(rs.getString(1));
                        return pago;
                }
        };

         return jt.query(sql,rowMapper);

    }




    public List obtenerAnalisisFraude(String pstAgrupa,String pstAgrupaCount,String pstFechaI,String pstFechaF,
            String pstEstatus, String pstTelefono, String pstTelPrincipal,
            String pstTarjeta, String pstCorreo, String pstTipoTel)
    {


        String sql = "SELECT "+ pstAgrupa + " ,COUNT(*) AS NOPAGOS " ;

        if ( Utilerias.esFechaHistorico(pstFechaI))
            sql += "FROM PAGOSVT.MT_PAGO_BK " ;
        else
            sql += "FROM PAGOSVT.MT_PAGO " ;

        sql += "WHERE "+ pstAgrupa + " IS NOT NULL " ;



        if( pstFechaI != null && pstFechaI.length() > 0  &&
             pstFechaF != null && pstFechaF.length() > 0   )
        {
            //Obtenemos el numero de dias a partir de hoy
            Utilerias util = new Utilerias();
            Date lobFIni = util.obtenerFechaHoraString(pstFechaI+" 12:00:00");
            int numDias = util.obtenerHaceCuantosDias(lobFIni);

            Date lobFFin = util.obtenerFechaHoraString(pstFechaF+" 12:00:00");
            int numDiasFin = util.obtenerHaceCuantosDias(lobFFin);

            sql += "AND FECHAESTADO BETWEEN SYSDATE - " + numDias + " AND SYSDATE - " + numDiasFin + " ";


        }
        else 
        {
            if( pstFechaI != null && pstFechaI.length() > 0 )
            {
                //Obtenemos el numero de dias a partir de hoy
                Utilerias util = new Utilerias();
                Date lobFIni = util.obtenerFechaHoraString(pstFechaI+" 12:00:00");
                int numDias = util.obtenerHaceCuantosDias(lobFIni);
                sql += "AND FECHAESTADO >= SYSDATE - " + numDias + " ";

            }

            if( pstFechaF != null && pstFechaF.length() > 0 )
            {
                //Obtenemos el numero de dias a partir de hoy
                Utilerias util = new Utilerias();
                Date lobFFin = util.obtenerFechaHoraString(pstFechaF+" 12:00:00");
                int numDias = util.obtenerHaceCuantosDias(lobFFin);
                sql += "AND FECHAESTADO <= SYSDATE - " + numDias + " ";
            }

        }
        if( pstEstatus != null && pstEstatus.length() > 0 && !pstEstatus.equals("*") )
            sql += "AND ESTADO = '" + pstEstatus + "' ";


        if( pstTelefono != null && pstTelefono.length() == 10 )
            sql += "AND TELEFONO = " + pstTelefono + " ";

        if( pstTelPrincipal != null && pstTelPrincipal.length() == 10 )
            sql += "AND PRINCIPAL = " + pstTelPrincipal + " ";

        if( pstCorreo != null && pstCorreo.length() > 0 )
            sql += "AND CORREO = " + pstCorreo + " ";

        if( pstTarjeta != null && pstTarjeta.length() > 0 )
        {
            if ( pstTarjeta.length() > 15)
            {
                Rijndael_Algorithm util = new Rijndael_Algorithm();
                sql += "AND TARJETA = '" + util.Encriptar(pstTarjeta) + "' " ;
            }
            else
                sql += "AND TARJETA = '" + pstTarjeta + "' " ;

        }
        if( pstTipoTel != null && pstTipoTel.length() > 0 && !pstTipoTel.equals("*") )
            sql += "AND TIPO = '" + pstTipoTel + "' ";


        sql += "GROUP BY " + pstAgrupa + " " +
               "HAVING COUNT(*) > " +  pstAgrupaCount;


        RowMapper rowMapper = new RowMapper() {
                DecimalFormat myFormatter = new DecimalFormat("###,##0.00");

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        Rijndael_Algorithm util = new Rijndael_Algorithm();
                        PagoDTO pago = new PagoDTO();
                        pago.setTransaccion(rs.getString(1));
                        pago.setMoneda(rs.getString(2));
                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }


    public List obtenerDetalleMovimientos(String pstFechaI,String pstFechaF,
            String pstEstatus, String pstTelefono, String pstTelPrincipal,
            String pstTarjeta, String pstCorreo, String pstTipoTel)
    {


        String sql = "SELECT ID, TELEFONO, TARJETA, IMPORTE, TO_CHAR(FECHAESTADO,'YYYY/MM/DD'),  TIPO, ESTADO, " +
                "FECHAPAGO, SECURE, REFERENCIA, NOMBRE, CORREO, " +
                "PRINCIPAL, NOMBRESESION, ESTADOPISA, TO_CHAR(FECHAEDOPISA,'YYYY/MM/DD'), OTRALINEA, TIPOTARJETA " ;

        if ( Utilerias.esFechaHistorico(pstFechaI))
            sql += "FROM PAGOSVT.MT_PAGO_BK " ;
        else
            sql += "FROM PAGOSVT.MT_PAGO " ;


        sql += "WHERE ID > 0 " ;



        if( pstFechaI != null && pstFechaI.length() > 0 )
        {
            //Obtenemos el numero de dias a partir de hoy
            Utilerias util = new Utilerias();
            Date lobFIni = util.obtenerFechaHoraString(pstFechaI+" 12:00:00");
            int numDias = util.obtenerHaceCuantosDias(lobFIni);
            sql += "AND FECHAESTADO >= SYSDATE - " + numDias + " ";

        }

        if( pstFechaF != null && pstFechaF.length() > 0 )
        {
            //Obtenemos el numero de dias a partir de hoy
            Utilerias util = new Utilerias();
            Date lobFFin = util.obtenerFechaHoraString(pstFechaF+" 12:00:00");
            int numDias = util.obtenerHaceCuantosDias(lobFFin);
            sql += "AND FECHAESTADO <= SYSDATE - " + numDias + " ";
        }

        if( pstEstatus != null && pstEstatus.length() > 0 && !pstEstatus.equals("*") )
            sql += "AND ESTADO = '" + pstEstatus + "' ";


        if( pstTelefono != null && pstTelefono.length() == 10 )
            sql += "AND TELEFONO = '" + pstTelefono + "' ";

        if( pstTelPrincipal != null && pstTelPrincipal.length() == 10 )
            sql += "AND PRINCIPAL = '" + pstTelPrincipal + "' ";

        if( pstCorreo != null && pstCorreo.length() > 0 )
            sql += "AND CORREO = '" + pstCorreo + "' ";

        if( pstTarjeta != null && pstTarjeta.length() > 0 )
        {
            if ( pstTarjeta.length() > 15)
            {
                Rijndael_Algorithm util = new Rijndael_Algorithm();
                sql += "AND TARJETA = '" + util.Encriptar(pstTarjeta) + "' " ;
            }
            else
                sql += "AND TARJETA = '" + pstTarjeta + "' " ;

        }
        if( pstTipoTel != null && pstTipoTel.length() > 0 && !pstTipoTel.equals("*") )
            sql += "AND TIPO = '" + pstTipoTel + "' ";


        RowMapper rowMapper = new RowMapper() {
                DecimalFormat myFormatter = new DecimalFormat("###,##0.00");

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        Rijndael_Algorithm util = new Rijndael_Algorithm();
                        PagoMKTDTO pago = new PagoMKTDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        String tarjeta = rs.getString(3);
                        if (tarjeta != null && tarjeta.length() > 15)
                        {
                            pago.setTarjeta(util.Desencriptar(tarjeta));
                        }
                        else
                            pago.setTarjeta(tarjeta);
                        pago.setMontoPagar(rs.getString(4));
                        pago.setFechaEstatus(rs.getString(5));
                        pago.setTipoTelefono(rs.getString(6));
                        pago.setEstatus(rs.getString(7));
                        pago.setFechaSol(rs.getString(8));
                        pago.setSecure(rs.getString(9));
                        pago.setReferencia(rs.getString(10));
                        pago.setNombre(rs.getString(11));
                        pago.setCorreo(rs.getString(12));
                        pago.setPrincipal(rs.getString(13));
                        pago.setNombreSesion(rs.getString(14));
                        pago.setEdoPisa(rs.getString(15));
                        pago.setFechaEdoPisa(rs.getString(16));
                        pago.setOtraLinea(rs.getString(17));
                        pago.setTipoTarjeta(rs.getString(18));
                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }


    public List obtenerCoincidenciasListasNegras(String pstAgrupa, String pstLista)
    {
        List listaNegra = new ArrayList();
        String agrupa[] = {"TARJETA","CORREO","TELEFONO","PRINCIPAL"};
        String query[] = {"SELECT TARJETA FROM POL_25_C_TARJETA_NEGRA WHERE TARJETA IN ",
                "SELECT EMAIL FROM PAGOSVT.POL_26_C_EMAIL_NEGRO WHERE EMAIL IN ",
                "SELECT TELEFONO FROM PAGOSVT.POL_27_C_TELEFONO_NEGRO WHERE TELEFONO IN ",
                "SELECT TELEFONO FROM PAGOSVT.POL_27_C_TELEFONO_NEGRO WHERE TELEFONO IN "};

        

        for (int i=0; i<agrupa.length; i++)
        {
            if ( pstAgrupa!=null && pstAgrupa.equals(agrupa[i]))
            {
                String sql = query[i] + "(" + pstLista + ")";
                RowMapper rowMapper = new RowMapper() {
                    public Object mapRow(ResultSet rs, int index) throws SQLException {

                        return new DatosCuerpoDTO(rs.getString(1));
                    }
               };

               listaNegra =  jt.query(sql,rowMapper);

            }

        }

        return listaNegra;

    }


    public HashMap obtenerCoincidencias(String pstAgrupa, String pstFechaI,String pstFechaF,
            String pstEstatus, String pstTelefono, String pstTelPrincipal,
            String pstTarjeta, String pstCorreo, String pstTipoTel)
    {
        String tablas[] = {"TARJETA","CORREO","TELEFONO","PRINCIPAL"};
        HashMap coincidencias = new HashMap();


        String sql = "WHERE "+ pstAgrupa + " IS NOT NULL " ;

        //Obtenemos las condiciones de la busqueda que son iguales para todos los casos
        if( pstFechaI != null && pstFechaI.length() > 0 )
        {
            //Obtenemos el numero de dias a partir de hoy
            Utilerias util = new Utilerias();
            Date lobFIni = util.obtenerFechaHoraString(pstFechaI+" 12:00:00");
            int numDias = util.obtenerHaceCuantosDias(lobFIni);
            sql += "AND FECHAESTADO >= SYSDATE - " + numDias + " ";

        }

        if( pstFechaF != null && pstFechaF.length() > 0 )
        {
            //Obtenemos el numero de dias a partir de hoy
            Utilerias util = new Utilerias();
            Date lobFFin = util.obtenerFechaHoraString(pstFechaF+" 12:00:00");
            int numDias = util.obtenerHaceCuantosDias(lobFFin);
            sql += "AND FECHAESTADO <= SYSDATE - " + numDias + " ";
        }

        if( pstEstatus != null && pstEstatus.length() > 0 && !pstEstatus.equals("*") )
            sql += "AND ESTADO = '" + pstEstatus + "' ";


        if( pstTelefono != null && pstTelefono.length() == 10 )
            sql += "AND TELEFONO = '" + pstTelefono + "' ";

        if( pstTelPrincipal != null && pstTelPrincipal.length() == 10 )
            sql += "AND PRINCIPAL = '" + pstTelPrincipal + "' ";

        if( pstCorreo != null && pstCorreo.length() > 0 )
            sql += "AND CORREO = '" + pstCorreo + "' ";

        if( pstTarjeta != null && pstTarjeta.length() > 0 )
        {
            if ( pstTarjeta.length() > 15)
            {
                Rijndael_Algorithm util = new Rijndael_Algorithm();
                sql += "AND TARJETA = '" + util.Encriptar(pstTarjeta) + "' " ;
            }
            else
                sql += "AND TARJETA = '" + pstTarjeta + "' " ;

        }
        if( pstTipoTel != null && pstTipoTel.length() > 0 && !pstTipoTel.equals("*") )
            sql += "AND TIPO = '" + pstTipoTel + "' ";




        for (int i=0; i<tablas.length; i++)
        {
            if ( pstAgrupa != null && !pstAgrupa.equals(tablas[i]))
            {

                String sql1 = "SELECT DISTINCT "+ tablas[i] + " " +
                    "FROM PAGOSVT.MT_PAGO " + sql ;

                RowMapper rowMapper;
                if ( tablas[i].equals("TARJETA"))
                {
                    rowMapper = new RowMapper() {
                            public Object mapRow(ResultSet rs, int index) throws SQLException {
                                    Rijndael_Algorithm util = new Rijndael_Algorithm();

                                    String tarjeta = rs.getString(1);
                                    if (tarjeta != null && tarjeta.length() > 15)
                                        return new DatosCuerpoDTO(util.Desencriptar(tarjeta));
                                    else
                                        return new DatosCuerpoDTO(tarjeta);
                            }
                    };

                }
                else
                {
                    rowMapper = new RowMapper() {
                            public Object mapRow(ResultSet rs, int index) throws SQLException {

                                    return new DatosCuerpoDTO(rs.getString(1));
                            }
                    };

                }

                coincidencias.put(tablas[i],jt.query(sql1,rowMapper));
            }
        }

         return coincidencias;
    }




    public PagoDTO obtenerTotalesPagos(String pstFechaI,String pstFechaF, String pstComercio, String pstSucursal,
            String pstEstatus, String pstAutorizacion, String pstTelefono, String pstTransaccion,
            String pstTarjeta, String pstRefBanco, String pstCS, String pstLib)
    {


        String sql = "SELECT COUNT(*) , SUM(MONTO_PAGAR) " +
                //"FROM POL_02_T_PAGO P, POL_04_C_COMERCIO C " +
                //"WHERE P.ADQUIRIENTE = C.CVE_COMERCIO " ;
                "FROM POL_04_C_COMERCIO C ," ;

        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) != -1 && Utilerias.esFechaHistorico(pstFechaI))
            sql += "POL_02_T_PAGO_BK P LEFT OUTER JOIN MT_PAGO_BK MP ON P.TRANSACCION = MP.ID  " ;
        else
            sql += "POL_02_T_PAGO P LEFT OUTER JOIN MT_PAGO MP ON P.TRANSACCION = MP.ID  " ;

        sql += "WHERE P.ADQUIRIENTE = C.CVE_COMERCIO " ;



        if( pstFechaI != null && pstFechaI.length() > 0 && pstFechaF != null && pstFechaF.length() > 0 )
        {
            sql += "AND FECHA_SOL BETWEEN '" + pstFechaI + " 00:00:00' AND '" + pstFechaF + " 24:00:00' " ;

        }
        else
        {
            if( pstFechaI != null && pstFechaI.length() > 0 )
                sql += "AND FECHA_SOL >= '" + pstFechaI + " 00:00:00' ";
                //sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') >= TO_DATE('" + pstFechaI + "','YYYY/MM/DD') ";

            if( pstFechaF != null && pstFechaF.length() > 0 )
                sql += "AND FECHA_SOL <= '" + pstFechaF + " 24:00:00' ";
                //sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') <= TO_DATE('" + pstFechaF + "','YYYY/MM/DD') ";
        }


        if( pstComercio != null && pstComercio.length() > 0 && !pstComercio.equals("-") && !pstComercio.equals("*") )
            sql += "AND C.CVE_COMERCIO = '" + pstComercio + "' ";
            //sql += "AND C.ID_COMERCIO = '" + pstComercio + "' ";

        if( pstSucursal != null && pstSucursal.length() > 0 && !pstSucursal.equals("-") && !pstSucursal.equals("*") )
            sql += "AND TIENDA_TERMINAL LIKE '" + pstSucursal + "%' ";

        if( pstEstatus != null && pstEstatus.length() > 0 && !pstEstatus.equals("-") && !pstEstatus.equals("*") )
            sql += "AND P.ESTATUS = '" + pstEstatus + "' ";

        if( pstAutorizacion != null && pstAutorizacion.length() > 0 )
            sql += "AND P.ID_PAGO = " + pstAutorizacion + " ";

        if( pstTransaccion != null && pstTransaccion.length() > 0 )
            sql += "AND P.TRANSACCION = " + pstTransaccion + " ";

        if( pstTelefono != null && pstTelefono.length() > 0 )
            sql += "AND P.TELEFONO = " + pstTelefono + " ";

        if( pstTarjeta != null && pstTarjeta.length() > 0 )
        {
            Rijndael_Algorithm util = new Rijndael_Algorithm();
            sql += "AND (MP.TARJETA = '" + pstTarjeta + "' OR MP.TARJETA = '" + util.Encriptar(pstTarjeta) + "' ) " ;
        }

        if( pstRefBanco != null && pstRefBanco.length() > 0 )
            sql += "AND MP.REFERENCIA = '" + pstRefBanco + "' ";

        if( pstCS != null && pstCS.length() > 0 && !pstCS.equals("*") )
            sql += "AND P.CS IN (" + pstCS + ")  ";

        if( pstLib != null && pstLib.length() > 0 && !pstLib.equals("*"))
            sql += "AND P.LIBRERIA = '" + pstLib + "'  ";

        sql += "ORDER BY P.ID_PAGO ";

        try {
        PagoDTO dto = (PagoDTO) jt.queryForObject(sql, new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        DecimalFormat myFormatter = new DecimalFormat("###,##0.00");

                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        try
                        {
                            double ld = rs.getDouble(2);
                            pago.setMontoPagado(myFormatter.format(ld));
                        } catch(Exception e){;}

                        return pago;
            }}
            );

                return dto;


        }
        catch(Exception e) {
            return null;
        }

    }





    public List obtenerPagosTelefono(String pstFechaI,String pstFechaF, String pstTelefono, String pstAdquiriente, String pstFechaAPisa, String pstFechaATro)
    {


        String sql = "SELECT P.ID_PAGO, P.TELEFONO, P.MONTO_PAGAR, P.FECHA_SOL, " +
                "P.FECHA_RESP_PISA, P.FECHA_RESP_TRO, P.FECHA_CONTABLE, " +
                "P.FECHA_CAPTURA, P.HORA, P.DIA, P.MONEDA, P.TIPO_INGRESO, P.CODIGO_RESP," +
                "P.TIPO_RESP, P.AUDIT_NUMBER, P.ADQUIRIENTE, P.TIENDA_TERMINAL, P.UNIDAD, " +
                "P.SECUENCIA_PISA, P.SALDO_VENCIDO, P.MIN_REANUDACION, P.MONTO_PAGADO, P.TRANSACCION, " +
                "P.FECHA_POSTEO, CO.ARCHIVO, CT.ARCHIVO AS ARCHTRO, P.ESTATUS, P.CAJA, P.FECHA_CONCIL_MAN ";

        if ( pstAdquiriente != null && comercioBD1.indexOf(pstAdquiriente) != -1 && Utilerias.esFechaHistorico(pstFechaI))
            sql += "FROM POL_02_T_PAGO_BK P," ;
        else
            sql += "FROM POL_02_T_PAGO P," ;

        sql += "POL_11_T_CONCILIACION CO, POL_13_T_CONCILTERCERO CT " +
                "WHERE P.TELEFONO = '" + pstTelefono + "' " +
                "AND P.ID_CONCILIACION = CO.ID_CONCILIACION (+) AND P.ID_CONCILTRO = CT.ID_CONCILTRO (+) " ;

        if( pstAdquiriente != null && pstAdquiriente.length() > 0 )
            sql += "AND P.ADQUIRIENTE = '"+pstAdquiriente+"' ";

        if( pstFechaI != null && pstFechaI.length() > 0 && pstFechaF != null && pstFechaF.length() > 0 )
        {
            sql += "AND FECHA_SOL BETWEEN '" + pstFechaI + " 00:00:00' AND '" + pstFechaF + " 24:00:00' " ;

        }
        else
        {
            if( pstFechaI != null && pstFechaI.length() > 0 )
                sql += "AND FECHA_SOL >= '" + pstFechaI + " 00:00:00' ";
                //sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') >= TO_DATE('" + pstFechaI + "','YYYY/MM/DD') ";

            if( pstFechaF != null && pstFechaF.length() > 0 )
                sql += "AND FECHA_SOL <= '" + pstFechaF + " 24:00:00' ";
                //sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') <= TO_DATE('" + pstFechaF + "','YYYY/MM/DD') ";
        }

        
        if( pstFechaAPisa != null && pstFechaAPisa.length() > 0 )
            sql += "AND CO.FECHA BETWEEN to_timestamp('" + pstFechaAPisa+ " 00:00:00','YYYY/MM/DD HH24:MI:SS') AND " +
                "to_timestamp('" + pstFechaAPisa+ " 23:59:59','YYYY/MM/DD HH24:MI:SS') " ;

        if( pstFechaATro != null && pstFechaATro.length() > 0 )
            sql += "AND to_char(CT.FECHA,'YYYY/MM/DD')= '"+pstFechaATro+"' ";

      
        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setMontoPagar(rs.getString(3));
                        pago.setFechaSol(rs.getString(4));
                        pago.setFechaRespPisa(rs.getString(5));
                        pago.setFechaRespTro(rs.getString(6));
                        pago.setFechaContable(rs.getString(7));
                        pago.setFechaCaptura(rs.getString(8));
                        pago.setHora(rs.getString(9));
                        pago.setDia(rs.getString(10));
                        pago.setMoneda(rs.getString(11));
                        pago.setTipoIngreso(rs.getString(12));
                        pago.setCodigoResp(rs.getString(13));
                        pago.setTipoResp(rs.getString(14));
                        pago.setAuditNumber(rs.getString(15));
                        pago.setAdquiriente(rs.getString(16));
                        pago.setTiendaTerm(rs.getString(17));
                        pago.setUnidad(rs.getString(18));
                        pago.setSecuenciaPisa(rs.getString(19));
                        pago.setSaldoVencido(rs.getString(20));
                        pago.setMinReanudacion(rs.getString(21));
                        pago.setMontoPagado(rs.getString(22));
                        pago.setTransaccion(rs.getString(23));
                        pago.setFechaPosteo(rs.getString(24));
                        pago.setSecPisaArchivo(rs.getString(25));
                        pago.setArchivoRespTro(rs.getString(26));
                        pago.setEstatus(rs.getString(27));
                        pago.setCaja(rs.getString(28));
                        pago.setFechaConcilManual(rs.getString(29));

                        if ( pago.getFechaConcilManual() != null &&
                                (pago.getMontoPagado() == null || pago.getMontoPagado().length() == 0) )
                        {
                            pago.setMontoPagado("AP:" + pago.getFechaConcilManual() + " " + pago.getMontoPagar());
                        }

                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }

    /**
     * Obtiene el pago para ser comparado con el archivo de conciliaci�n. Solo se recupera el registro si se encuentra en estado PE 
     * (por aplicar)
     * @param pstTransaccion El identificador del pago generado por PISA.
     * @return Un objeto tipo java.util.List con el pago recuperado.
     */
    public List obtenerPagoXConciliar(String pstTransaccion)
    {


        String sql = "SELECT ID_PAGO, TELEFONO, MONTO_PAGAR, FECHA_SOL, " +
                "FECHA_RESP_PISA, FECHA_RESP_TRO, FECHA_CONTABLE, " +
                "FECHA_CAPTURA, HORA, DIA, MONEDA, TIPO_INGRESO, CODIGO_RESP," +
                "TIPO_RESP, AUDIT_NUMBER, ADQUIRIENTE, TIENDA_TERMINAL, UNIDAD, " +
                "SECUENCIA_PISA, SALDO_VENCIDO, MIN_REANUDACION, MONTO_PAGADO, TRANSACCION " +
                "FROM POL_02_T_PAGO P " +
                "WHERE P.AUTORIZACION = '" + pstTransaccion + "' ";


        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setMontoPagar(rs.getString(3));
                        pago.setFechaSol(rs.getString(4));
                        pago.setFechaRespPisa(rs.getString(5));
                        pago.setFechaRespTro(rs.getString(6));
                        pago.setFechaContable(rs.getString(7));
                        pago.setFechaCaptura(rs.getString(8));
                        pago.setHora(rs.getString(9));
                        pago.setDia(rs.getString(10));
                        pago.setMoneda(rs.getString(11));
                        pago.setTipoIngreso(rs.getString(12));
                        pago.setCodigoResp(rs.getString(13));
                        pago.setTipoResp(rs.getString(14));
                        pago.setAuditNumber(rs.getString(15));
                        pago.setAdquiriente(rs.getString(16));
                        pago.setTiendaTerm(rs.getString(17));
                        pago.setUnidad(rs.getString(18));
                        pago.setSecuenciaPisa(rs.getString(19));
                        pago.setSaldoVencido(rs.getString(20));
                        pago.setMinReanudacion(rs.getString(21));
                        pago.setMontoPagado(rs.getString(22));
                        pago.setTransaccion(rs.getString(23));

                        return pago;
                }
        };
         return jt.query(sql,rowMapper);
    }

    /**
     * Obtiene la lista de pagos para ser comparados con el archivo de conciliaci�n. Solo se recupera el registro si se encuentra en estado PE
     * (por aplicar)
     * @param  pstTransaccion El identificador del pago generado por PISA.
     * @return Un objeto tipo java.util.List con el pago recuperado.
     */
    public List obtenerPagosXConciliar()
    {


        String sql = "SELECT ID_PAGO, TELEFONO, AUTORIZACION " +
                "FROM POL_02_T_PAGO P " +
                "WHERE P.ESTATUS in ('RR','AA') ";

        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setAutorizacion(rs.getString(3));
                        return pago;
                }
        };
         return jt.query(sql,rowMapper);
    }

    public int cambiarEstadoConciliacion(PagoDTO pobPago){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET ESTATUS = ?, ID_CONCILIACION = ? " +
        "WHERE AUTORIZACION = '" + pobPago.getAutorizacion() + "'";

            lnuError = jt.update(sql, new Object[] { pobPago.getEstatus(), pobPago.getIdConciliacion() });

        return lnuError;
    }


    public HashMap obtenerPagosXConciliarPisaCom(String pstLibreria)
    {

        HashMap pobPagos = new HashMap();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        //Acotamos las busquedas en pagos en una semana
        Calendar lobFecha = Calendar.getInstance();
        lobFecha.add(Calendar.DAY_OF_MONTH, 1); //adelantamos un dia por si acaso

        String lstFechaFinConcil = sdf.format(lobFecha.getTime()) + " 24:00:00";
        //Nos regresamos 60 dias mas
        lobFecha.add(Calendar.DAY_OF_MONTH, -61);
        String lstFechaIniConcil = sdf.format(lobFecha.getTime()) + " 00:00:00";



        String sql = "SELECT ID_PAGO, TELEFONO, AUTORIZACION, TRANSACCION, " +
                "FECHA_SOL, MONTO_PAGADO, TIENDA_TERMINAL, ESTATUS " +
                "FROM POL_02_T_PAGO P " +
                //"WHERE ( P.ID_CONCILIACION IS NULL OR ESTATUS <> 'AP' ) " +
                "WHERE  P.ID_CONCILIACION IS NULL " +
                //los pagos anteriores no los podremos conciliar ya
                "AND FECHA_SOL BETWEEN '" + lstFechaIniConcil + "' AND '" + lstFechaFinConcil + "' " +
                "AND LIBRERIA = '" + pstLibreria + "' ";

        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        UtilConciliaPago util = new UtilConciliaPago();
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setAutorizacion(rs.getString(3));

                        //Completamos la transaccion con ceros a la izq a 15 posiciones
                        //para que empate con lo que envia pisa en la conciliacion
                        pago.setTransaccion(util.obtenTransaccionXpos(rs.getString(4),15));
                        pago.setFechaSol(rs.getString(5));
                        pago.setMontoPagado(rs.getString(6));
                        pago.setTiendaTerm(rs.getString(7));
                        pago.setEstatus(rs.getString(8));

                        return pago;
                }
        };
         List pobDatos =  jt.query(sql,rowMapper);
         if ( pobDatos != null )
         {
             Iterator it = pobDatos.iterator();
             while ( it.hasNext() )
             {
                 PagoDTO pago = (PagoDTO)it.next();
                 pobPagos.put(pago.getIdPago(), pago);

             }

         }
         return pobPagos;

    }


    public int actualizarConcilPISA(PagoDTO pobPago){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET ID_CONCILIACION = ? , ESTATUS = ? ," +
                     "FECHA_POSTEO = ?, ESTATUS_CONCILPISA = ? , MONTO_PAGADO = ? " ;

        if ( pobPago.getEstatusConcilTro() != null && pobPago.getEstatusConcilTro().length() > 0 )
            sql += ", ESTATUS_CONCILTRO = '" + pobPago.getEstatusConcilTro() + "' ";

                sql += "WHERE ID_PAGO = " + pobPago.getIdPago() + " " +
                     "AND TELEFONO = '" + pobPago.getTelefono() + "' ";

        try
        {
            lnuError = jt.update(sql, new Object[] { pobPago.getIdConciliacion(),pobPago.getEstatus(),
                                        pobPago.getFechaPosteo(),
                                        pobPago.getEstatusConcilPisa(), pobPago.getMontoPagado()});

        }
        catch(Exception e)
        {
            ;
        }
        return lnuError;
    }




    public int actualizarConcilTerceroEL(PagoDTO pobPago){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET ID_CONCILTRO = ?, ARCHIVO_RESP_TRO = ? , " +
                     "FECHA_CONCIL_TRO = ?, TIPO_PROCESO = ? , ESTATUS_CONCILTRO = ? " +
                     "WHERE ID_PAGO = " + pobPago.getIdPago() + " " +
                     "AND TELEFONO = '" + pobPago.getTelefono() + "' ";

            lnuError = jt.update(sql, new Object[] { pobPago.getIdConcilTro(), pobPago.getArchivoRespTro(),
                                        pobPago.getFechaConcilTro(), pobPago.getTipoProceso(),
                                        pobPago.getEstatusConcilTro()});

        return lnuError;
    }


    public int actualizarConcilTerceroFL(PagoDTO pobPago){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET ID_CONCILTRO = ?, ARCHIVO_RESP_TRO = ? , " +
                     "FECHA_CONCIL_TRO = ?, TIPO_PROCESO = ? , " +
                     "ESTRATEGIA = ? , ESTATUS_CONCILTRO = ? " +
                     "WHERE ID_PAGO = " + pobPago.getIdPago() + " " +
                     "AND TELEFONO = '" + pobPago.getTelefono() + "' ";

            lnuError = jt.update(sql, new Object[] { pobPago.getIdConcilTro(), pobPago.getArchivoRespTro(),
                                        pobPago.getFechaConcilTro(), pobPago.getTipoProceso(),
                                        pobPago.getEstrategia() , pobPago.getEstatusConcilTro()});

        return lnuError;
    }


    //public HashMap obtenerPagosXConciliarCom(String pstComercio,String pstTelefono,
    public List obtenerPagosXConciliarCom(String pstComercio,String pstTelefono,
            String pstFechaSol, String pstMontoPagado, String pstFechaSolMin, int ordenIdPago )
    {
        List pobPagos = new ArrayList();
        HashMap pobPagosTran = new HashMap();
        HashMap pobPagosIdPago = new HashMap();

        String sql = "SELECT ID_PAGO, TELEFONO, AUTORIZACION, TRANSACCION, " +
                "FECHA_SOL, MONTO_PAGADO, TIENDA_TERMINAL, ESTATUS, MONTO_PAGAR, FECHA_CONCIL_MAN " +
                "FROM POL_02_T_PAGO P " +
                "WHERE ADQUIRIENTE = '" + pstComercio + "' ";
            sql += "AND ( P.ID_CONCILTRO IS NULL ) " ;



        if ( pstFechaSol != null && pstFechaSol.length() > 0 && pstFechaSolMin != null && pstFechaSolMin.length() > 0 )
            sql += "AND FECHA_SOL BETWEEN '" + pstFechaSolMin + "' AND '" + pstFechaSol + "' " ;

        if ( pstTelefono != null && pstTelefono.length() > 0 )
            sql += "AND TELEFONO = '" + pstTelefono + "' ";


        if ( pstMontoPagado != null && pstMontoPagado.length() > 0 )
            sql += "AND MONTO_PAGADO = " + pstMontoPagado + " ";


        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setAutorizacion(rs.getString(3));
                        pago.setTransaccion(rs.getString(4));
                        pago.setFechaSol(rs.getString(5));
                        pago.setMontoPagado(rs.getString(6));
                        pago.setTiendaTerm(rs.getString(7));
                        pago.setEstatus(rs.getString(8));
                        pago.setMontoPagar(rs.getString(9));
                        pago.setFechaConcilManual(rs.getString(10));

                        return pago;
                }
        };
         List pobDatos =  jt.query(sql,rowMapper);
         if ( pobDatos != null )
         {
             Iterator it = pobDatos.iterator();
             while ( it.hasNext() )
             {
                 PagoDTO pago = (PagoDTO)it.next();
                 pobPagosTran.put(pago.getTransaccion(), pago);
                 pobPagosIdPago.put(pago.getIdPago(), pago);

             }

             pobPagos.add(pobPagosTran);//0
             pobPagos.add(pobPagosIdPago);//1

         }
         return pobPagos;

    }


    public PagoDTO obtenerPagoTxCom(String pstComercio, String pstTransaccion, String pstTelefono, 
            String pstFechaSol, int pnuConcilPisa, String pstReferencia)
    {

        String sql = "SELECT ID_PAGO, TELEFONO, AUTORIZACION, TRANSACCION, " +
                "FECHA_SOL, MONTO_PAGADO, TIENDA_TERMINAL, ESTATUS, FECHA_POSTEO, " +
                "ID_CONCILIACION, ESTATUS_CONCILPISA, SECUENCIA_PISA, ID_CONCILTRO, MONTO_PAGAR, FECHA_CONCIL_MAN " +
                "FROM POL_02_T_PAGO " +
                "WHERE ADQUIRIENTE = '" + pstComercio + "' " ;

        if ( pstTelefono != null && pstTelefono.length() > 0 )
                sql += "AND TELEFONO = '" + pstTelefono + "' " ;

        /*
        if ( pstReferencia != null && pstReferencia.length() > 0 )
                sql += "AND TELEFONO = '" + pstReferencia + "' " ;
         *
         */

        if ( pstFechaSol != null && pstFechaSol.length() > 0 )
            sql += "AND FECHA_SOL BETWEEN '" + pstFechaSol + " 00:00:00' AND '"+ pstFechaSol + " 24:00:00' ";
            //sql += "AND SUBSTR(FECHA_SOL,1,10) = '" + pstFechaSol + "' ";

        if ( pstTransaccion!=null && pstTransaccion.length()>0 )
        {
            long transaccion = 0;

            //Datalogic enviara una transaccion hasta de 26 posiciones
            //el dato long aguanta hasta 18 posiciones
            if ( pstTransaccion.length() < 18 )
                transaccion = Long.parseLong(pstTransaccion);
            else
                transaccion = 1;

            if ( transaccion > 0 ) //Sanborns envia 000000000000 en los casos de los pagos FL
            {
                if ( pnuConcilPisa == 1 )
                    sql += "AND TRANSACCION LIKE '%" + pstTransaccion + "' " ;
                    //esto porque pisa completa la transaccion con ceros a la izquierda :S
                else
                    sql += "AND TRANSACCION = '" + pstTransaccion + "' " ;
            }

        }

        try {
        PagoDTO dto = (PagoDTO) jt.queryForObject(sql, new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                            PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                            pago.setAutorizacion(rs.getString(3));
                            pago.setTransaccion(rs.getString(4));
                            pago.setFechaSol(rs.getString(5));
                            pago.setMontoPagado(rs.getString(6));
                            pago.setTiendaTerm(rs.getString(7));
                            pago.setEstatus(rs.getString(8));
                            pago.setFechaPosteo(rs.getString(9));
                            pago.setIdConciliacion(rs.getLong(10));
                            pago.setEstatusConcilPisa(rs.getString(11));
                            pago.setSecuenciaPisa(rs.getString(12));
                            pago.setIdConcilTro(rs.getLong(13));
                            pago.setMontoPagar(rs.getString(14));
                            pago.setFechaConcilManual(rs.getString(15));
                            return pago;
                        }
                });

                return dto;


        }
        catch(Exception e) {
            return null;
        }

    }


    public List obtenerPagosPendientes()
    {
        //los pagos que por conciliacion con el tercero no existen
        //o los que envio fuera de linea
        //y que estan autorizados por MARCIA

        String sql = "SELECT ID_PAGONREG, TELEFONO, ADQUIRIENTE, TIENDA_TERMINAL, " +
                "TRANSACCION, MONTO_PAGADO,'NREG', ID_CONCILTRO, TARJETACREDITO, TIPOPAGO " +
                "FROM POL_19_T_PAGONOREGISTRADO " +
                "WHERE  ESTATUS IN ('EN','EE') " +
                "AND TIPO_AUTORIZACION IN ('NVOF','NVOE') " + //solo los que se consideran como nuevos, i.e no resgistrados
                "AND AUTOR_COBRANZA = '1' " +
                "AND ESTATUS_ENVIO = '0' " + //que no se ha atendido la peticion, 1 son historicos
                // mas todos los que no se pudieron enviar desde el ws en forma normal
                "UNION " +
                "SELECT ID_PAGO, TELEFONO, ADQUIRIENTE, TIENDA_TERMINAL, " +
                "TRANSACCION, MONTO_PAGAR, 'REG', 0, TARJETACREDITO, TIPOPAGO " +
                "FROM POL_02_T_PAGO " +
                //ya no se envian los sin secuencia porque pisa los vuelve a aplicar n veces
                //"WHERE  ESTATUS IN ('RR','AS','EE' ) " +
                "WHERE  ESTATUS IN ('RR','EE' ) " +
                //PGM: le damos mas tiempo a pisa a que conteste, tomamos los pagos de 15 min hacia atras
                //"AND SYSDATE - TO_DATE(FECHA_SOL,'YYYY/MM/DD HH24:MI:SS') > (15/1440) " ;
                //ESTA ES LA BUENA
                "AND FECHA_SOL <= to_char(current_timestamp - (15/1440), 'YYYY/MM/DD HH24:MI:SS') ";
                //"AND FECHA_SOL > '2018/01/03 17:00:00' ";

                //por mientras
                //"AND SUBSTR(FECHA_SOL,1,10) = '2012/02/22' " ;

                //cuando entre MKT se necesitan encolar aquellos pagos que por
                //algun motivo el conector no pudo establecer cx con el ws del pago en linea
                //estos pagos estaran con el estatuspisa = AS, se necesitan registrar del lado del pago
                sql +="UNION " +
                "SELECT ID, TELEFONO, 'MKT', '0001', " +
                "TO_CHAR(ID), IMPORTE, 'NREGMKT', 0, TARJETA, '2' " +
                "FROM MT_PAGO " +
                "WHERE  ESTADOPISA = 'SC' " +
                //"AND ID > 45590001 " +
                "AND FECHAESTADO > SYSDATE - 4 " +
                "AND ID NOT IN ( select transaccion from POL_02_T_PAGO where adquiriente = 'MKT' ) " ;
                //"AND ( TO_CHAR(FECHAESTADO,'YYYY/MM/DD') = TO_CHAR(SYSDATE,'YYYY/MM/DD') " +
                //"OR TO_CHAR(FECHAESTADO,'YYYY/MM/DD') = TO_CHAR(SYSDATE-1,'YYYY/MM/DD') ) ";

                // Aumentamos los casos de american express equivalentes a T11 que no pueden ser conciliados como eglobal
                //27 de junio 2013
                sql +="UNION " +
                "SELECT ID, TELEFONO, 'MKT', '0001', " +
                "TO_CHAR(ID), IMPORTE, 'NREGMKT', 0, TARJETA, '2' " +
                "FROM MT_PAGO " +
                "WHERE  ESTADOPISA = 'SD' AND ESTADO = 'T55' " +
                //"AND ID > 45590001 " +
                "AND FECHAESTADO > SYSDATE - 4 " +
                "AND ID NOT IN ( select transaccion from POL_02_T_PAGO where adquiriente = 'MKT' ) " ;

                //Esperar confirmacion Ana Laura casos 12 y 29 Junio 2015
                //pagos de eglobal se quedaron en T54 y no hubo registro en pol_02 correspondiente
                //como si el conector ya no pudo conectarse al WS del pago en linea ni tampoco
                //regresar a actualizar el estatus en mt_pago con el estatus de SC
                sql +="UNION " +
                "SELECT ID, TELEFONO, 'MKT', '0001', " +
                "TO_CHAR(ID), IMPORTE, 'NREGMKT', 0, TARJETA, '2' " +
                "FROM MT_PAGO " +
                "WHERE  ESTADOPISA IS NULL AND ESTADO = 'T54' " +
                "AND TIPOTARJETA IS NOT NULL AND RESPUESTA = '000' " +
                "AND FECHAESTADO > SYSDATE - 4 " +
                "AND ID NOT IN ( select transaccion from POL_02_T_PAGO where adquiriente = 'MKT' ) " ;
                
                
                
                
                

        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        String telefono = rs.getString(2);
                        if (telefono != null )
                            pago.setTelefono(telefono.trim());
                        else
                            pago.setTelefono(telefono);

                        pago.setAdquiriente(rs.getString(3));
                        pago.setTiendaTerm(rs.getString(4));
                        pago.setTransaccion(rs.getString(5));
                        pago.setMontoPagado(rs.getString(6));
                        pago.setTipoProceso(rs.getString(7));
                        pago.setIdConcilTro(rs.getLong(8));
                        String tarjeta = rs.getString(9);
                        if (tarjeta != null )
                            pago.setNumeroTC(tarjeta.trim());
                        else
                            pago.setNumeroTC(tarjeta);

                        pago.setFormaPago(rs.getString(10));
                        return pago;
                }
        };
         return jt.query(sql,rowMapper);
    }


    public List obtenerPagosXIdConcilFecha(long pnuIdConcilTro, String pstComercio)
    {


        String sql = "SELECT ID_PAGO, TELEFONO, ADQUIRIENTE, TIENDA_TERMINAL," +
                "FECHA_SOL, HORA, TRANSACCION, AUTORIZACION, " +
                "TIPO_INGRESO, MONTO_PAGADO, TIPO_PROCESO, ESTATUS, ESTATUS_CONCILTRO " +
                "FROM POL_02_T_PAGO " +
                "WHERE ADQUIRIENTE = '" + pstComercio + "' " +
                "AND ID_CONCILTRO = " + pnuIdConcilTro + " ";
        //Aumentamos los que siguen pendientes de aplicar
         sql += "UNION SELECT ID_PAGONREG, TELEFONO, ADQUIRIENTE, TIENDA_TERMINAL, " +
                "FECHA_SOL, HORA, TRANSACCION, AUTORIZACION, " +
                "TIPO_INGRESO, MONTO_PAGADO, 'F', ESTATUS, to_nchar('RNA') " +
                "FROM POL_19_T_PAGONOREGISTRADO " +
                "WHERE  ESTATUS IN ('EN','EE','ER') " + //aumentamos los estatus que estan duplicados
                "AND ( ESTATUS_ENVIO IS NULL OR ESTATUS_ENVIO <> 'OK' ) " +
                "AND ID_CONCILTRO = " + pnuIdConcilTro + " " +
                "AND ADQUIRIENTE = '" + pstComercio + "' " +
                "AND ESTATUS_ENVIO = '0' " + //que no se ha atendido la peticion, 1 son historicos
                "ORDER BY FECHA_SOL";



        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setAdquiriente(rs.getString(3));
                        pago.setTiendaTerm(rs.getString(4));
                        pago.setFechaSol(rs.getString(5));
                        pago.setHora(rs.getString(6));
                        pago.setTransaccion(rs.getString(7));
                        pago.setAutorizacion(rs.getString(8));
                        pago.setTipoIngreso(rs.getString(9));
                        pago.setMontoPagado(rs.getString(10));
                        pago.setTipoProceso(rs.getString(11));
                        pago.setEstatus(rs.getString(12));
                        pago.setEstatusConcilTro(rs.getString(13));
                        return pago;
                }
        };
         return jt.query(sql,rowMapper);
    }

    public List obtenerPagosTiendaTelMonto(PagoConciliacionDTO pobPago )
    {


        String sql = "SELECT ID_PAGO, TELEFONO, AUTORIZACION, TRANSACCION, " +
                "FECHA_SOL, MONTO_PAGADO, TIENDA_TERMINAL, ESTATUS, RESPTIMEOUT, FECHA_CONCIL_MAN  " ;

        sql += "FROM POL_02_T_PAGO P " ;

        sql += "WHERE P.ID_CONCILTRO IS NULL  " + //se buscan los que no han sido conciliados
                "AND ADQUIRIENTE = '" + pobPago.getComercio() + "' ";

        if ( pobPago.getTelefono() != null && pobPago.getTelefono().length() > 0 )
            sql += "AND TELEFONO = '" + pobPago.getTelefono() + "' ";

        if ( pobPago.getMonto() != null && pobPago.getMonto().length() > 0 )
            sql += "AND MONTO_PAGAR = " + pobPago.getMonto() + " ";

        //2012/01/20 18:02:37 en BD , en el ojbeto la fecha y hora estan separadas
        if ( pobPago.getFecha() != null && pobPago.getFecha().length() > 0 )
            sql += "AND FECHA_SOL BETWEEN '" + pobPago.getFecha() + " 00:00:00' AND '"+ pobPago.getFecha() + " 24:00:00' ";
            //sql += "AND SUBSTR(FECHA_SOL,1,10) = '" + pobPago.getFecha() + "' ";

        sql += " ORDER BY RESPTIMEOUT ";

        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setAutorizacion(rs.getString(3));
                        pago.setTransaccion(rs.getString(4));
                        pago.setFechaSol(rs.getString(5));
                        pago.setMontoPagado(rs.getString(6));
                        pago.setTiendaTerm(rs.getString(7));
                        pago.setEstatus(rs.getString(8));
                        pago.setRespTimeout(rs.getString(9));
                        pago.setFechaConcilManual(rs.getString(10));

                        return pago;
                }
        };
         return  jt.query(sql,rowMapper);

    }


    public PagoDTO obtenerPagoXId(String pstIdPago)
    {

        String sql = "SELECT ID_PAGO, TELEFONO, AUTORIZACION, TRANSACCION, " +
                "FECHA_SOL, MONTO_PAGADO, TIENDA_TERMINAL, ESTATUS, FECHA_POSTEO, " +
                "ID_CONCILIACION, ESTATUS_CONCILPISA, SECUENCIA_PISA, ID_CONCILTRO, ADQUIRIENTE, FECHA_CONCIL_MAN " +
                "FROM POL_02_T_PAGO " +
                "WHERE ID_PAGO = " + pstIdPago ;

        try {
        PagoDTO dto = (PagoDTO) jt.queryForObject(sql, new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                            PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                            pago.setAutorizacion(rs.getString(3));
                            pago.setTransaccion(rs.getString(4));
                            pago.setFechaSol(rs.getString(5));
                            pago.setMontoPagado(rs.getString(6));
                            pago.setTiendaTerm(rs.getString(7));
                            pago.setEstatus(rs.getString(8));
                            pago.setFechaPosteo(rs.getString(9));
                            pago.setIdConciliacion(rs.getLong(10));
                            pago.setEstatusConcilPisa(rs.getString(11));
                            pago.setSecuenciaPisa(rs.getString(12));
                            pago.setIdConcilTro(rs.getLong(13));
                            pago.setAdquiriente(rs.getString(14));
                            pago.setFechaConcilManual(rs.getString(15));
                            return pago;
                        }
                });

                return dto;


        }
        catch(Exception e) {
            return null;
        }

    }

    public PagoDTO obtenerPagoXTransCom(String pstTransaccion, String pstComercio, String pstTelefono)
    {

        String sql = "SELECT ID_PAGO, TELEFONO, AUTORIZACION, TRANSACCION, " +
                "FECHA_SOL, MONTO_PAGADO, TIENDA_TERMINAL, ESTATUS, FECHA_POSTEO, " +
                "ID_CONCILIACION, ESTATUS_CONCILPISA, SECUENCIA_PISA, ID_CONCILTRO, ADQUIRIENTE " +
                "FROM POL_02_T_PAGO " +
                "WHERE ADQUIRIENTE = '" + pstComercio + "' " +
                "AND TRANSACCION = '" + pstTransaccion + "' " +
                "AND TELEFONO = '" + pstTelefono + "' " ;

        try {
        PagoDTO dto = (PagoDTO) jt.queryForObject(sql, new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                            PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                            pago.setAutorizacion(rs.getString(3));
                            pago.setTransaccion(rs.getString(4));
                            pago.setFechaSol(rs.getString(5));
                            pago.setMontoPagado(rs.getString(6));
                            pago.setTiendaTerm(rs.getString(7));
                            pago.setEstatus(rs.getString(8));
                            pago.setFechaPosteo(rs.getString(9));
                            pago.setIdConciliacion(rs.getLong(10));
                            pago.setEstatusConcilPisa(rs.getString(11));
                            pago.setSecuenciaPisa(rs.getString(12));
                            pago.setIdConcilTro(rs.getLong(13));
                            pago.setAdquiriente(rs.getString(14));
                            return pago;
                        }
                });

                return dto;


        }
        catch(Exception e) {
            return null;
        }

    }



    public List obtenerPagoXIdSinConciliar(String pstIdPago)
    {

        String sql = "SELECT ID_PAGO, TELEFONO, AUTORIZACION, TRANSACCION, " +
                "FECHA_SOL, MONTO_PAGADO, TIENDA_TERMINAL, ESTATUS, FECHA_POSTEO, " +
                "ID_CONCILIACION, ESTATUS_CONCILPISA, SECUENCIA_PISA, ID_CONCILTRO, TIPO_PROCESO, MONTO_PAGAR  " +
                "FROM POL_02_T_PAGO " +
                "WHERE ID_CONCILTRO IS NULL " +
                "AND ID_PAGO = " + pstIdPago ;



        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                            pago.setAutorizacion(rs.getString(3));
                            pago.setTransaccion(rs.getString(4));
                            pago.setFechaSol(rs.getString(5));
                            pago.setMontoPagado(rs.getString(6));
                            pago.setTiendaTerm(rs.getString(7));
                            pago.setEstatus(rs.getString(8));
                            pago.setFechaPosteo(rs.getString(9));
                            pago.setIdConciliacion(rs.getLong(10));
                            pago.setEstatusConcilPisa(rs.getString(11));
                            pago.setSecuenciaPisa(rs.getString(12));
                            pago.setIdConcilTro(rs.getLong(13));
                            pago.setTipoProceso(rs.getString(14));
                            pago.setMontoPagar(rs.getString(15));
                        return pago;
                }
        };
         return  jt.query(sql,rowMapper);
    }




    public int actualizarIdConcilTroPago(String pstIdPago, long idConcilTro, String lstTipoProc){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET ID_CONCILTRO = " + idConcilTro + ", " +
                     "TIPO_PROCESO = '" + lstTipoProc + "' " +
                     "WHERE ID_PAGO = " + pstIdPago ;

            lnuError =  jt.update(sql);

        return lnuError;
    }

    public int actualizarLibreria(String pstIdPago, String pstLibreria){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET LIBRERIA = '" + pstLibreria + "' " +
                     "WHERE ID_PAGO = " + pstIdPago ;

            lnuError =  jt.update(sql);

        return lnuError;
    }

    public int actualizarLibCS(String pstIdPago, String pstLibreria, String pstCS ){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET LIBRERIA = '" + pstLibreria + "', " +
                     "CS = '" + pstCS + "' " +
                     "WHERE ID_PAGO = " + pstIdPago ;

            lnuError =  jt.update(sql);

        return lnuError;
    }

    public int actualizarCS(String pstIdPago, String pstCS ){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET CS = '" + pstCS + "' " +
                     "WHERE ID_PAGO = " + pstIdPago ;

            lnuError =  jt.update(sql);

        return lnuError;
    }


    public List obtenerPagosSinLibreria()
    {
        String sql = "SELECT ID_PAGO, TELEFONO " +
                "FROM POL_02_T_PAGO " +
                "WHERE LIBRERIA IS NULL " +
                "AND ESTATUS NOT IN ('AA','AP','CA') " +
                //"AND TELEFONO NOT LIKE ' 0%' " ;
                //"AND SYSDATE - TO_DATE (SUBSTR(FECHA_SOL,0,10),'YYYY/MM/DD')  <= 3 " +
                "AND FECHA_SOL >= to_char(current_timestamp - 3, 'YYYY/MM/DD HH24:MI:SS') " ;
        /*        Los separamos porque como ya no la actualizamos se esta enviando a lo menso
                "UNION " +
                "SELECT ID_PAGO, TELEFONO " +
                "FROM POL_02_T_PAGO " +
                "WHERE CS IS NULL " +
                "AND SUBSTR(FECHA_SOL,0,10) = TO_CHAR(SYSDATE,'YYYY/MM/DD')  " +
                "AND ESTATUS IN ('AA','AP','AS','RR')" +
                "AND TELEFONO NOT LIKE ' 0%' " ;
*/



        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                        return pago;
                }
        };
         return  jt.query(sql,rowMapper);

        
    }

    public List obtenerPagosSinCS()
    {
        String sql =
                "SELECT ID_PAGO, TELEFONO " +
                "FROM POL_02_T_PAGO " +
                "WHERE CS IS NULL " +
                //"AND SYSDATE - TO_DATE (SUBSTR(FECHA_SOL,0,10),'YYYY/MM/DD')  <= 2 " +
                "AND FECHA_SOL >= to_char(current_timestamp - 2, 'YYYY/MM/DD HH24:MI:SS') " +
                //"AND SUBSTR(FECHA_SOL,0,10) = TO_CHAR(SYSDATE,'YYYY/MM/DD')  " +
                "AND ESTATUS IN ('AA','AP','AS','RR')" ;
                //"AND TELEFONO NOT LIKE ' 0%' " ;
                //"and adquiriente ='MKT' ";




        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                        return pago;
                }
        };
         return  jt.query(sql,rowMapper);


    }



    public List obtenerPagosSinLibCS()
    {
        String sql = "SELECT ID_PAGO, TELEFONO " +
                "FROM POL_02_T_PAGO " +
                "WHERE CS IS NULL " +
                "AND SUBSTR(FECHA_SOL,0,10) = TO_CHAR(SYSDATE,'YYYY/MM/DD')  " +
                "AND ESTATUS IN ('AA','AP','AS','RR') ";



        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                        return pago;
                }
        };
         return  jt.query(sql,rowMapper);


    }


    public HashMap obtenerPagosXConciliarBanco(String pstComercio,String pstTelefono,
            String pstFechaSol, String pstMontoPagado )
    {

        HashMap pobPagos = new HashMap();

        //Buscamos los pagos que faltan por conciliar y que se encuentran registrados
        //en mkt_pago es decir entraron por telmex.com
        String sql = "SELECT P.ID_PAGO, P.TELEFONO, P.AUTORIZACION, P.TRANSACCION, " +
                "P.FECHA_SOL, P.MONTO_PAGADO, P.TIENDA_TERMINAL, P.ESTATUS, P.MONTO_PAGAR " +
                "FROM POL_02_T_PAGO P, MT_PAGO MT " +
                "WHERE P.ID_CONCILTRO IS NULL  " +
                "AND P.ADQUIRIENTE = '" + pstComercio + "' " +
                "AND P.TRANSACCION = MT.ID " +
                "AND P.TELEFONO = MT.TELEFONO " +
                "AND P.MONTO_PAGAR = MT.IMPORTE " +
                "AND P.FECHA_SOL >= TO_CHAR(SYSDATE-30,'YYYY/MM/DD HH24:MI:SS') " ; //limitamos la busqueda a un mes

        if ( pstTelefono != null && pstTelefono.length() > 0 )
            sql += "AND P.TELEFONO = '" + pstTelefono + "' ";

        if ( pstFechaSol != null && pstFechaSol.length() > 0 )
            sql += "AND P.FECHA_SOL BETWEEN '" + pstFechaSol + " 00:00:00' AND '"+ pstFechaSol + " 24:00:00' ";
            //sql += "AND SUBSTR(P.FECHA_SOL,0,10) = '" + pstFechaSol + "' ";

        if ( pstMontoPagado != null && pstMontoPagado.length() > 0 )
            sql += "AND P.MONTO_PAGADO = " + pstMontoPagado + " ";


        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setAutorizacion(rs.getString(3));
                        pago.setTransaccion(rs.getString(4));
                        pago.setFechaSol(rs.getString(5));
                        pago.setMontoPagado(rs.getString(6));
                        pago.setTiendaTerm(rs.getString(7));
                        pago.setEstatus(rs.getString(8));
                        pago.setMontoPagar(rs.getString(9));

//                        try {
//                            pago.setIdConcilTro(rs.getInt(10));
//                        }catch(Exception e1){;}

                        return pago;
                }
        };
         List pobDatos =  jt.query(sql,rowMapper);
         if ( pobDatos != null )
         {
             Iterator it = pobDatos.iterator();
             while ( it.hasNext() )
             {
                 PagoDTO pago = (PagoDTO)it.next();
                 pobPagos.put(pago.getTransaccion(), pago);

             }

         }
         return pobPagos;

    }

    public HashMap obtenerPagosXConciliarSantander(String pstComercio)
    {

        HashMap pobPagos = new HashMap();

        //Buscamos los pagos que faltan por conciliar y que se encuentran registrados
        //en mkt_pago es decir entraron por el app
        String sql = "SELECT P.ID_PAGO, P.TELEFONO, P.AUTORIZACION, P.TRANSACCION, " +
                "P.FECHA_SOL, P.MONTO_PAGADO, P.TIENDA_TERMINAL, P.ESTATUS, P.MONTO_PAGAR, MT.FIRMA, MT.CREDITOINBURSA " +
                "FROM POL_02_T_PAGO P, MT_PAGO MT " +
                "WHERE P.ID_CONCILTRO IS NULL  " +
                //Quitamos la condicion porque desde enero 2 2018 se recibiran pagos de MKT y APT
                //"AND P.ADQUIRIENTE = '" + pstComercio + "' " +
                "AND P.TRANSACCION = TO_CHAR(MT.ID) " +
                "AND P.TELEFONO = MT.TELEFONO " +
                "AND P.MONTO_PAGAR = MT.IMPORTE " +
                "AND P.FECHA_SOL >= TO_CHAR(SYSDATE-10,'YYYY/MM/DD HH24:MI:SS') " ; //limitamos la busqueda a 10 DIAS


        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setAutorizacion(rs.getString(3));
                        pago.setTransaccion(rs.getString(4));
                        pago.setFechaSol(rs.getString(5));
                        pago.setMontoPagado(rs.getString(6));
                        pago.setTiendaTerm(rs.getString(7));
                        pago.setEstatus(rs.getString(8));
                        pago.setMontoPagar(rs.getString(9));
                        pago.setSecure(rs.getString(10));//mt.Firma correspondo al foliocpagos

                        pago.setAdquiriente("MKT");
                        String comercio = rs.getString(11);
                        if (comercio != null && comercio.equals("M"))
                            pago.setAdquiriente("APT");

//                        try {
//                            pago.setIdConcilTro(rs.getInt(10));
//                        }catch(Exception e1){;}

                        return pago;
                }
        };
         List pobDatos =  jt.query(sql,rowMapper);
         if ( pobDatos != null )
         {
             Iterator it = pobDatos.iterator();
             while ( it.hasNext() )
             {
                 PagoDTO pago = (PagoDTO)it.next();
                 pobPagos.put(pago.getTransaccion(), pago);

             }

         }
         return pobPagos;

    }

    public HashMap obtenerPagosT11Santander(String pstComercio)
    {

        HashMap pobPagos = new HashMap();

        //Buscamos los pagos que faltan por conciliar y que se encuentran registrados
        //en mkt_pago es decir entraron por telmex.com
        String sql = "SELECT ID, TELEFONO, FECHAESTADO, IMPORTE, CREDITOINBURSA  " +
                "FROM MT_PAGO  " +
                "WHERE ESTADO = 'T11' " +
                "AND FECHAESTADO > SYSDATE - 5 " +
                //"AND ID NOT IN ( SELECT TRANSACCION FROM POL_02_T_PAGO WHERE ADQUIRIENTE = '" + pstComercio + "' " +
                "AND ID NOT IN ( SELECT TRANSACCION FROM POL_02_T_PAGO WHERE ADQUIRIENTE IN ('APT','MKT') " +
                "AND FECHA_SOL >= TO_CHAR(SYSDATE-5,'YYYY/MM/DD HH24:MI:SS') ) " ; //limitamos la busqueda a 10 DIAS


        /*
        if ( pstComercio != null && pstComercio.equals("APT"))
            sql += "AND CREDITOINBURSA = 'M'";
        else // es MKT que aun no se utiliza
            sql += "AND (CREDITOINBURSA IS NULL OR CREDITOINBURSA <> 'M' ) ";
            */

        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setFechaSol(rs.getString(3));
                        pago.setMontoPagado(rs.getString(4));
                        pago.setTiendaTerm(rs.getString(5)); //indica de que comercio se trata

                        return pago;
                }
        };
         List pobDatos =  jt.query(sql,rowMapper);
         if ( pobDatos != null )
         {
             Iterator it = pobDatos.iterator();
             while ( it.hasNext() )
             {
                 PagoDTO pago = (PagoDTO)it.next();
                 pobPagos.put(pago.getIdPago(), pago);

             }

         }
         return pobPagos;

    }



    public int actEstatusReintentosYaConciliados()
    {
        int lnuError = 0;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET ESTATUS = 'AP' " +
                     "WHERE ESTATUS = 'AA' AND CODIGO_RESP = '13' " +
                     "AND ID_CONCILIACION IS NOT NULL " ;

        return jt.update(sql);

    }

    public int actEstatusReintentos()
    {

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET MONTO_PAGADO = MONTO_PAGAR " +
                     "WHERE ESTATUS = 'AA' AND CODIGO_RESP = '13' " +
                     "AND MONTO_PAGADO IS NULL " ;

        return jt.update(sql);

    }



    public PagoDTO obtenerPagoMKT02Pago(String pstComercio, String pstTransaccion, String pstReferencia)
    {

        String sql = "SELECT ID_PAGO, MT.TELEFONO, MT.REFERENCIA, P.TRANSACCION, " +
                "P.FECHA_SOL, P.MONTO_PAGADO, P.TIENDA_TERMINAL, P.ESTATUS, P.FECHA_POSTEO, " +
                "P.ID_CONCILIACION, P.ESTATUS_CONCILPISA, P.SECUENCIA_PISA, P.ID_CONCILTRO, P.MONTO_PAGAR " ;
        sql += "FROM POL_02_T_PAGO P, MT_PAGO MT " ;

        if ( pstComercio != null && pstComercio.equals("APT")) {
            //Significa que es la conciliacion con Santander y necesitamos buscar independiente del comercio
            //porque puede ser de MKT tambien
            sql += "WHERE MT.ID = " + pstTransaccion + " " +
                   // "WHERE P.ADQUIRIENTE = '" + pstComercio + "' " +
                    "AND P.TRANSACCION = TO_CHAR(MT.ID) ";

            
        }
        else {
            sql += "WHERE P.ADQUIRIENTE = '" + pstComercio + "' " +
                    "AND MT.ID = " + pstTransaccion + " " +
                    "AND P.TRANSACCION = TO_CHAR(MT.ID) ";
        }


        if ( pstReferencia != null && pstReferencia.length() > 0 )
                sql += "AND MT.REFERENCIA = '" + pstReferencia + "' " ;


        try {
        PagoDTO dto = (PagoDTO) jt.queryForObject(sql, new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                            PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                            pago.setAutorizacion(rs.getString(3));
                            pago.setTransaccion(rs.getString(4));
                            pago.setFechaSol(rs.getString(5));
                            pago.setMontoPagado(rs.getString(6));
                            pago.setTiendaTerm(rs.getString(7));
                            pago.setEstatus(rs.getString(8));
                            pago.setFechaPosteo(rs.getString(9));
                            pago.setIdConciliacion(rs.getLong(10));
                            pago.setEstatusConcilPisa(rs.getString(11));
                            pago.setSecuenciaPisa(rs.getString(12));
                            pago.setIdConcilTro(rs.getLong(13));
                            pago.setMontoPagar(rs.getString(14));
                            return pago;
                        }
                });

                return dto;


        }
        catch(Exception e) {
            return null;
        }

    }


    public List obtenerPagosLibCom(String pstFechaI,String pstFechaF, String pstComercio, String pstEstatus, String pstAcumMensual )
    {

        String lstFecha = "SUBSTR(FECHA_SOL,0,10)";
        if ( pstAcumMensual!=null && pstAcumMensual.equals("1"))
            lstFecha = "SUBSTR(FECHA_SOL,0,7)";



        String sql = "SELECT " + lstFecha + " AS FECHA, LIBRERIA, COUNT(*), SUM(MONTO_PAGADO)" +
                "FROM POL_02_T_PAGO P " +
                "WHERE ID_PAGO > 0 " ;


        if( pstComercio != null && pstComercio.length() > 0 && !pstComercio.equals("-") && !pstComercio.equals("*") )
            sql += "AND P.ADQUIRIENTE = '" + pstComercio + "' ";


        if( pstFechaI != null && pstFechaI.length() > 0 && pstFechaF != null && pstFechaF.length() > 0 )
        {
            sql += "AND FECHA_SOL BETWEEN '" + pstFechaI + " 00:00:00' AND '" + pstFechaF + " 24:00:00' " ;

        }
        else
        {
            if( pstFechaI != null && pstFechaI.length() > 0 )
                sql += "AND FECHA_SOL >= '" + pstFechaI + " 00:00:00' ";
                //sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') >= TO_DATE('" + pstFechaI + "','YYYY/MM/DD') ";

            if( pstFechaF != null && pstFechaF.length() > 0 )
                sql += "AND FECHA_SOL <= '" + pstFechaF + " 24:00:00' ";
                //sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') <= TO_DATE('" + pstFechaF + "','YYYY/MM/DD') ";

        }

        if( pstEstatus != null && pstEstatus.length() > 0 && !pstEstatus.equals("-") && !pstEstatus.equals("*") )
        {
            if ( pstEstatus.length() > 3) // trae una lista
                sql += "AND P.ESTATUS IN (" + pstEstatus + ") ";
            else
                sql += "AND P.ESTATUS = '" + pstEstatus + "' ";
        }

        sql += "GROUP BY " + lstFecha + ", LIBRERIA " +
                "ORDER BY LIBRERIA, " + lstFecha ;



        RowMapper rowMapper = new RowMapper() {
                DecimalFormat myFormatter = new DecimalFormat("###,##0.00");

                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                        pago.setFechaSol(rs.getString(1));
                        pago.setLibreria(rs.getString(2));
                        pago.setIdPago(rs.getString(3));
                        try
                        {
                            double ld = rs.getDouble(4);
                            pago.setMontoPagado(myFormatter.format(ld));
                        } catch(Exception e){;}
                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }

    public List obtenerPagosAS()
    {
        String sql = "SELECT ID_PAGO, TELEFONO, MONTO_PAGAR, TRANSACCION, ADQUIRIENTE  " +
                "FROM POL_02_T_PAGO " +
                "WHERE FECHA_SOL BETWEEN TO_CHAR(SYSDATE-1,'YYYY/MM/DD HH24:MI:SS') AND TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS') " +
                "AND ADQUIRIENTE = 'MKT' " +
                "AND ESTATUS = 'AS' " +
                "AND SUBSTR(TELEFONO,0,1) <> '0' " ;


        RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int index) throws SQLException {
                        PagoDTO pago = new PagoDTO();
                            pago.setIdPago(rs.getString(1));
                            pago.setTelefono(rs.getString(2));
                            pago.setMontoPagar(rs.getString(3));
                            pago.setTransaccion(rs.getString(4));
                            pago.setAdquiriente(rs.getString(5));
                        return pago;
                }
        };

        try
        {
         return  jt.query(sql,rowMapper);

        }
        catch(Exception e)
        {
            return null;
        }

    }

    public int cambiarEstado(String pstIdePago, String pstEstatus){
        int lnuError = -1;

        String sql = "UPDATE POL_02_T_PAGO " +
                     "SET ESTATUS = ? " +
        "WHERE ID_PAGO = " + pstIdePago + " ";

            lnuError = jt.update(sql, new Object[] { pstEstatus });

        return lnuError;
    }

    public List obtenerPagosCaja(String pstFechaI, String pstComercio, String pstEstatus)
    {

        String sql = "SELECT ID_PAGO, TELEFONO, FECHA_SOL, " +
                "FECHA_RESP_PISA, ADQUIRIENTE, SECUENCIA_PISA, MONTO_PAGADO, " +
                "TRANSACCION, ESTATUS, CAJA, C.ARCHIVO , LIBRERIA, MONTO_PAGAR ";

        if ( pstComercio != null && (pstComercio.equals("MKT")||pstComercio.equals("APT")) && Utilerias.esFechaHistorico(pstFechaI))
            sql +=  "FROM POL_02_T_PAGO_BK P ";
        else
            sql +=  "FROM POL_02_T_PAGO P ";

        sql += "LEFT OUTER JOIN  POL_13_T_CONCILTERCERO C ON P.ID_CONCILTRO = C.ID_CONCILTRO " +
                "WHERE ID_PAGO > 0 " ;

        if( pstFechaI != null && pstFechaI.length() > 0 ) // La caja del dia seleccionado empieza desde el dia anterior a las 19:00hr
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar lobFecha = Calendar.getInstance();
            lobFecha.setTime(Utilerias.obtenerObjetoFecha(pstFechaI+ " 00:00:00"));
            lobFecha.add(Calendar.DAY_OF_MONTH, -1);
            String lstFechaIni = sdf.format(lobFecha.getTime());


            sql += "AND FECHA_SOL BETWEEN '" + lstFechaIni + " 00:00:00' AND '" + pstFechaI +  " 24:00:00' " +
                "AND CAJA = TRIM(ADQUIRIENTE)||TO_CHAR(TO_DATE('" + pstFechaI + "','YYYY/MM/DD'),'YYMMDD')" ;
        }


        if( pstComercio != null && pstComercio.length() > 0 && !pstComercio.equals("-") && !pstComercio.equals("*") )
            sql += "AND ADQUIRIENTE = '" + pstComercio + "' ";

        if( pstEstatus != null && pstEstatus.length() > 0 && !pstEstatus.equals("-") && !pstEstatus.equals("*") )
            sql += "AND ESTATUS = '" + pstEstatus + "' ";

        sql += "ORDER BY ID_PAGO ";


        RowMapper rowMapper = new RowMapper() {
                DecimalFormat myFormatter = new DecimalFormat("###,##0.00");

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setFechaSol(rs.getString(3));
                        pago.setFechaRespPisa(rs.getString(4));
                        pago.setAdquiriente(rs.getString(5));
                        pago.setSecuenciaPisa(rs.getString(6));
                        try
                        {
                            double ld = rs.getDouble(7);
                            pago.setMontoPagado(myFormatter.format(ld));
                        } catch(Exception e){;}
                        pago.setTransaccion(rs.getString(8));
                        pago.setEstatus(rs.getString(9));
                        pago.setCaja(rs.getString(10));
                        pago.setAuditNumber(rs.getString(11));
                        pago.setLibreria(rs.getString(12));
                        pago.setMontoPagar(rs.getString(13));

                        return pago;
                }
        };

         return jt.query(sql,rowMapper);



    }


    public PagoDTO obtenerTotalesPagosCaja(String pstFechaI, String pstComercio, String pstEstatus)
    {


        String sql = "SELECT COUNT(*) , SUM(MONTO_PAGADO) " ;

        if ( pstComercio != null && (pstComercio.equals("MKT")||pstComercio.equals("APT")) && Utilerias.esFechaHistorico(pstFechaI))
            sql +=  "FROM POL_02_T_PAGO_BK P ";
        else
            sql +=  "FROM POL_02_T_PAGO P ";


        sql += "WHERE ID_PAGO > 0 " ;

        if( pstFechaI != null && pstFechaI.length() > 0 ) // La caja del dia seleccionado empieza desde el dia anterior a las 19:00hr
        {
            sql += "AND TO_DATE(SUBSTR(FECHA_SOL ,0, 10),'YYYY/MM/DD') " +
                "BETWEEN TO_DATE('" + pstFechaI + "','YYYY/MM/DD')-1 " +
                "AND TO_DATE('" + pstFechaI + "','YYYY/MM/DD') " +
                "AND CAJA = TRIM(ADQUIRIENTE)||TO_CHAR(TO_DATE('" + pstFechaI + "','YYYY/MM/DD'),'YYMMDD')";
        }

        if( pstComercio != null && pstComercio.length() > 0 && !pstComercio.equals("-") && !pstComercio.equals("*") )
            sql += "AND ADQUIRIENTE = '" + pstComercio + "' ";

        if( pstEstatus != null && pstEstatus.length() > 0 && !pstEstatus.equals("-") && !pstEstatus.equals("*") )
            sql += "AND ESTATUS = '" + pstEstatus + "' ";


        try {
        PagoDTO dto = (PagoDTO) jt.queryForObject(sql, new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        DecimalFormat myFormatter = new DecimalFormat("###,##0.00");

                        PagoDTO pago = new PagoDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setImportePagado(rs.getDouble(2));
                        return pago;
            }}
            );

                return dto;


        }
        catch(Exception e) {
            return null;
        }

    }


    public List obtenerPagosMKTAPT(String pstFecha)
    {


 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar lobFecha = Calendar.getInstance();
        lobFecha.setTime(Utilerias.obtenerObjetoFecha(pstFecha+ " 00:00:00"));
        lobFecha.add(Calendar.DAY_OF_MONTH, -1);
        String lstFechaIni = sdf.format(lobFecha.getTime());


       String sql = "SELECT TELEFONO, IMPORTE, TO_CHAR(FECHAESTADO, 'YYYY/MM/DD HH24:MI:SS'), " +
                "CORREO, PRINCIPAL, CREDITOINBURSA " +
                "FROM PAGOSVT.MT_PAGO " +
                "WHERE TO_CHAR(FECHAESTADO, 'YYYY/MM/DD') >= '" +lstFechaIni + "' " +
                "AND TO_CHAR(FECHAESTADO, 'YYYY/MM/DD') < '" + pstFecha + "' " +
                "AND ( ESTADO IN ('T55','T54') OR REFERENCIA IS NOT NULL ) ";


        RowMapper rowMapper = new RowMapper() {

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoMKTDTO pago = new PagoMKTDTO();
                        pago.setTelefono(rs.getString(1));
                        pago.setMontoPagar(rs.getString(2));
                        pago.setFechaEstatus(rs.getString(3));
                        pago.setCorreo(rs.getString(4));
                        pago.setPrincipal(rs.getString(5));
                        pago.setTipoTarjeta(rs.getString(6));
                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }


    public List obtenerPagosAPTPorReferencia(String fecha, String oficina, int soloPendientes)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar lobFecha = Calendar.getInstance();
        String lstFechaIni = sdf.format(lobFecha.getTime());

       String sql = "SELECT MT.ID, MT.TELEFONO, MT.TIPO, MT.ESTADO, TO_CHAR(MT.FECHAPAGO,'YYYYMMDD' ), " +
            "TO_CHAR(MT.FECHAPAGO,'HH24MISS' ) , MT.TARJETA, " +
            "MT.IMPORTE, MT.REFERENCIA, MT.TIPOTARJETA, P.ID_PAGO, " +
            "P.MONTO_PAGADO, P.ESTATUS, P.LIBRERIA, P.CAJA, " +
            "MT.FECHAFACTURA, P.FECHA_CONCIL_MAN, TO_CHAR(MT.FECHAESTADO,'YYYYMMDD' ) " +
            "FROM PAGOSVT.MT_PAGO MT LEFT OUTER JOIN POL_02_T_PAGO P ON TO_CHAR(MT.ID) = P.TRANSACCION " ;

       if ( oficina != null && oficina.equals("APT")) {
           sql += "AND P.ADQUIRIENTE = 'APT' " +
//            "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-41,'YYYY/MM/DD' ) " +
            "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-1,'YYYY/MM/DD' ) " +
            "AND MT.CREDITOINBURSA = 'M' " +
            "AND MT.REFERENCIA IS NOT NULL " ;

       }
       else if ( oficina != null && oficina.equals("TWA")) {
           sql += "AND P.ADQUIRIENTE = 'TWA' " +
//            "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-41,'YYYY/MM/DD' ) " +
            "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-1,'YYYY/MM/DD' ) " +
            "AND MT.CREDITOINBURSA = 'E' " +
            "AND MT.REFERENCIA IS NOT NULL " ;

       }
       else {
           sql += "AND P.ADQUIRIENTE = 'MKT' " +
            "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-1,'YYYY/MM/DD' ) " +
//            "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-14,'YYYY/MM/DD' ) " +
            "AND MT.CREDITOINBURSA IS NULL " +
            "AND MT.REFERENCIA IS NOT NULL " ;

       }



        if ( fecha != null ) {
            sql = "SELECT MT.ID, MT.TELEFONO, MT.TIPO, MT.ESTADO, TO_CHAR(MT.FECHAPAGO,'YYYYMMDD' ), " +
            "TO_CHAR(MT.FECHAPAGO,'HH24MISS' ) , MT.TARJETA, " +
            "MT.IMPORTE, MT.REFERENCIA, MT.TIPOTARJETA, P.ID_PAGO, " +
            "P.MONTO_PAGADO, P.ESTATUS, P.LIBRERIA, P.CAJA, MT.FECHAFACTURA, " +
            "P.FECHA_CONCIL_MAN , TO_CHAR(MT.FECHAESTADO,'YYYYMMDD' ) " +
            "FROM PAGOSVT.MT_PAGO MT LEFT OUTER JOIN POL_02_T_PAGO P ON TO_CHAR(MT.ID) = P.TRANSACCION " ;

            //Para cambiar el filtro de fecha
            String filtro = "=";
            if ( fecha.indexOf("mq") != -1) {
                filtro = " >=" ;
                fecha = fecha.substring(0,fecha.indexOf("mq"));
            }
            
            
            if ( oficina != null && oficina.equals("APT")) {
                sql += "AND P.ADQUIRIENTE = 'APT' " +
                "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) " + filtro + " " + fecha + " " +
                "AND MT.CREDITOINBURSA = 'M' " +
                "AND MT.REFERENCIA IS NOT NULL " ;

            }
            else if ( oficina != null && oficina.equals("TWA")) {
                sql += "AND P.ADQUIRIENTE = 'TWA' " +
                "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) " + filtro + " " + fecha + " " +
                "AND MT.CREDITOINBURSA = 'E' " +
                "AND MT.REFERENCIA IS NOT NULL " ;

            }
            else {
                sql += "AND P.ADQUIRIENTE = 'MKT' " +
                "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) " + filtro + " " + fecha + " " +
                "AND MT.CREDITOINBURSA IS NULL  " +
                "AND MT.REFERENCIA IS NOT NULL " ;

            }


        }

        if ( soloPendientes == 1)
            sql += " AND MT.ESTADO IN ('T61','T62') ";
            
        log.info(sql);
        
        RowMapper rowMapper = new RowMapper() {

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoMKTDTO pago = new PagoMKTDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setTipoTelefono(rs.getString(3));
                        pago.setEstatus(rs.getString(4));
                        pago.setFechaEstatus(rs.getString(5));
                        pago.setNombre(rs.getString(6)); //Hora

                        try {
                            pago.setTarjeta(Rijndael_Algorithm.Desencriptar(rs.getString(7)));
                        }catch(Exception e) {
                            pago.setTarjeta(rs.getString(7));

                        }
                        pago.setMontoPagar(rs.getString(8));
                        pago.setReferencia(rs.getString(9));
                        pago.setTipoTarjeta(rs.getString(10));
                        pago.setPrincipal(rs.getString(11)); //pol_02.id_pago
                        pago.setOtraLinea(rs.getString(12)); //pol_02.monto_pagadp
                        pago.setEstatusDes(rs.getString(13)); //pol_02.estatus
                        pago.setSecure(rs.getString(14)); //pol_02.libreria
                        pago.setCorreo(rs.getString(15)); //pol_02.caja
                        pago.setFechaSol(rs.getString(16));//MT.FECHAFACTURA
                        pago.setNombreSesion(rs.getString(17));//MT.FECHA_CONCIL_MAN en la que se envio reporte a pisa
                        pago.setFechaEdoPisa(rs.getString(18));//MT.FECHAESTADO que es diferente a la fecha de pago
                        

                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }

    public List obtenerPagosAPTPorReferenciaCorte(String fecha, String oficina)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar lobFecha = Calendar.getInstance();
        String lstFechaIni = sdf.format(lobFecha.getTime());

       String sql = "SELECT MT.ID, MT.TELEFONO, MT.TIPO, MT.ESTADO, TO_CHAR(MT.FECHAPAGO,'YYYYMMDD' ), " +
            "TO_CHAR(MT.FECHAPAGO,'HH24MISS' ) , MT.TARJETA, " +
            "MT.IMPORTE, MT.REFERENCIA, MT.TIPOTARJETA, P.ID_PAGO, " +
            "P.MONTO_PAGADO, P.ESTATUS, P.LIBRERIA, P.CAJA, MT.FECHAFACTURA " +
            "FROM PAGOSVT.MT_PAGO MT LEFT OUTER JOIN POL_02_T_PAGO P ON MT.ID = P.TRANSACCION " ;

       if ( oficina != null && oficina.equals("APT")) {
           sql += "AND P.ADQUIRIENTE = 'APT' " +
//            "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-2,'YYYY/MM/DD' ) " +
            " WHERE MT.CREDITOINBURSA = 'M' " +
            "AND MT.REFERENCIA IS NOT NULL " ;

       }
       else if ( oficina != null && oficina.equals("TWA")) {
           sql += "AND P.ADQUIRIENTE = 'TWA' " +
//            "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-2,'YYYY/MM/DD' ) " +
            " WHERE MT.CREDITOINBURSA = 'E' " +
            "AND MT.REFERENCIA IS NOT NULL " ;

       }
       else {
           sql += "AND P.ADQUIRIENTE = 'MKT' " +
            //"WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = TO_CHAR(SYSDATE-2,'YYYY/MM/DD' ) " +
            " WHERE MT.CREDITOINBURSA IS NULL  " +
            "AND MT.REFERENCIA IS NOT NULL " ;

       }


        if ( fecha != null ) {
            sql = "SELECT MT.ID, MT.TELEFONO, MT.TIPO, MT.ESTADO, TO_CHAR(MT.FECHAPAGO,'YYYYMMDD' ), " +
            "TO_CHAR(MT.FECHAPAGO,'HH24MISS' ) , MT.TARJETA, " +
            "MT.IMPORTE, MT.REFERENCIA, MT.TIPOTARJETA, P.ID_PAGO, " +
            "P.MONTO_PAGADO, P.ESTATUS, P.LIBRERIA, P.CAJA, MT.FECHAFACTURA " +
            "FROM PAGOSVT.MT_PAGO MT LEFT OUTER JOIN POL_02_T_PAGO P ON TO_CHAR(MT.ID) = P.TRANSACCION " ;

            if ( oficina != null && oficina.equals("APT")) {
                sql += "AND P.ADQUIRIENTE = 'APT' " +
                //"WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = " + fecha + " " +
                " WHERE MT.CREDITOINBURSA = 'M' " +
                "AND MT.REFERENCIA IS NOT NULL " ;

            }
            else if ( oficina != null && oficina.equals("TWA")) {
                sql += "AND P.ADQUIRIENTE = 'TWA' " +
                //"WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = " + fecha + " " +
                " WHERE MT.CREDITOINBURSA = 'E' " +
                "AND MT.REFERENCIA IS NOT NULL " ;

            }
            else {
                sql += "AND P.ADQUIRIENTE = 'MKT' " +
                //"WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = " + fecha + " " +
                " WHERE MT.CREDITOINBURSA IS NULL  " +
                "AND MT.REFERENCIA IS NOT NULL " ;

            }


        }


       //Adicionar el corte del horario
       //int horaActual = Utilerias.horaActual();
       int horaActual = 0;
       //
       if ( horaActual >= 9 && horaActual < 12 ) //rango uno
       {//del dia anterior y antes de las 9
           String fecha2 = Utilerias.fechaHaceDias(1);
            sql += " AND TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD HH24:MI:SS') between '" + fecha2 + " 20:00:00' AND '" + fecha + " 08:59:59' ";

       }
       else if( horaActual >= 12 && horaActual < 17 ) //rango dos
       {
            sql += " AND TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD HH24:MI:SS') between '" + fecha + " 09:00:00' AND '" + fecha + " 11:59:59' ";

       }
       else if( horaActual >= 17 && horaActual < 20 ) //rango tres
       {
            sql += " AND TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD HH24:MI:SS') between '" + fecha + " 12:00:00' AND '" + fecha + " 16:59:59' ";
       }
       else if( horaActual >= 20 ) //rango tres
       {
            sql += " AND TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD HH24:MI:SS') between '" + fecha + " 17:00:00' AND '" + fecha + " 19:59:59' ";

       }
       else {
            sql += " AND TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD') = '" + fecha + "' ";

       }


        log.info("SQL_PENDIENTES_HORA=" + sql);


        RowMapper rowMapper = new RowMapper() {

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoMKTDTO pago = new PagoMKTDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setTipoTelefono(rs.getString(3));
                        pago.setEstatus(rs.getString(4));
                        pago.setFechaEstatus(rs.getString(5));
                        pago.setNombre(rs.getString(6)); //Hora

                        try {
                            pago.setTarjeta(Rijndael_Algorithm.Desencriptar(rs.getString(7)));
                        }catch(Exception e) {
                            pago.setTarjeta(rs.getString(7));

                        }
                        pago.setMontoPagar(rs.getString(8));
                        pago.setReferencia(rs.getString(9));
                        pago.setTipoTarjeta(rs.getString(10));
                        pago.setPrincipal(rs.getString(11)); //pol_02.id_pago
                        pago.setOtraLinea(rs.getString(12)); //pol_02.monto_pagadp
                        pago.setEstatusDes(rs.getString(13)); //pol_02.estatus
                        pago.setSecure(rs.getString(14)); //pol_02.libreria
                        pago.setCorreo(rs.getString(15)); //pol_02.caja
                        pago.setFechaSol(rs.getString(16));//MT.FECHAFACTURA

                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }




    /** Metodo para obtener el la estadistica diaria de los pagos APP y enviarla por correo*/
    public List obtenerPagosAPPEstadistica(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar hoy = Calendar.getInstance();
        hoy.add(Calendar.DAY_OF_MONTH, -1);
        String fecha=sdf.format(hoy.getTime());

        String sql="SELECT 'T53', MT.TIPO, count(*), 0 FROM MT_PAGO MT "+
                   "WHERE  TO_CHAR(MT.FECHAESTADO,'YYYY/MM/DD' ) = '" + fecha + "' AND MT.CREDITOINBURSA = 'M' "+
                   "AND MT.ESTADO = 'T53' GROUP BY mt.estado, MT.TIPO "+
                   "UNION "+
                   "SELECT 'T55',MT.TIPO,count(*), SUM(MT.IMPORTE) FROM MT_PAGO MT "+
                   "WHERE  TO_CHAR(MT.FECHAESTADO,'YYYY/MM/DD' ) = '" + fecha + "' "+
                   "AND MT.CREDITOINBURSA = 'M' AND MT.REFERENCIA IS NOT NULL GROUP BY MT.TIPO ORDER BY 2,1";



          RowMapper rowMapper = new RowMapper() {

        public Object mapRow(ResultSet rs, int index) throws SQLException {
   
            PagoAPPEstadisticaDTO estadistica= new PagoAPPEstadisticaDTO();
             try{

            String estado = rs.getString(1);
            if ( estado != null && estado.startsWith("T53"))
                estado = "RECHAZO";
            else
                estado = "PAGOS OK";
            estadistica.setEstado(estado);


            String tipo = rs.getString(2);
            if ( tipo != null && tipo.startsWith("H"))
                tipo = "HOGAR";
            else
                tipo = "NEGOCIO";
            estadistica.setTipo(tipo);


            estadistica.setTotalPagos    (rs.getString(3).toString());
            estadistica.setImporte       (rs.getString(4).toString());
                }catch (Exception e){

                    e.printStackTrace();

              }
              return estadistica;
          }
      };

      return jt.query(sql,rowMapper);
    }


    public List obtenerTelsTCFraude(){

        //Eliminamos los telefono previo

        String sql= "DELETE FROM FRAUDE";
        jt.update(sql);


        sql= "INSERT INTO FRAUDE " +
            "SELECT TELEFONO FROM MT_PAGO WHERE TELEFONO in ( SELECT TELEFONO FROM MT_PAGO " +
			"WHERE TO_CHAR(FECHAESTADO,'YYYY/MM/DD') = TO_CHAR(SYSDATE-1,'YYYY/MM/DD') " +
			"AND ESTADO = 'T05') AND FECHAESTADO BETWEEN SYSDATE-30 AND SYSDATE " +
                        "GROUP BY TELEFONO HAVING COUNT(DISTINCT(TARJETA)) > 8";

        jt.update(sql);


        sql = "SELECT distinct(principal) FROM PAGOSVT.MT_PAGO " +
                "WHERE TELEFONO IN (SELECT TELEFONO FROM FRAUDE) UNION " +
                "SELECT distinct(tarjeta) FROM PAGOSVT.MT_PAGO " +
                "WHERE principal IN (SELECT distinct(principal) " +
		" FROM PAGOSVT.MT_PAGO WHERE TELEFONO IN (SELECT TELEFONO FROM FRAUDE))	" +
                " UNION SELECT distinct(tarjeta) FROM PAGOSVT.MT_PAGO " +
                " WHERE TELEFONO IN (SELECT TELEFONO FROM FRAUDE)" +
                " UNION SELECT TELEFONO FROM FRAUDE ";


          RowMapper rowMapper = new RowMapper() {

        public Object mapRow(ResultSet rs, int index) throws SQLException {

            TelefonoNegroDTO lista = new TelefonoNegroDTO();
             try{
                lista.setTelefono(rs.getString(1));

             }catch (Exception e){
                    e.printStackTrace();
              }
              return lista;
          }
      };

      return jt.query(sql,rowMapper);
    }


    public List obtenerPagosPendientesPISA(String fecha, String oficina)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar lobFecha = Calendar.getInstance();
        String lstFechaIni = sdf.format(lobFecha.getTime());

        String sql = null;

        if ( fecha != null ) {
            sql = "SELECT MT.ID, MT.TELEFONO, MT.TIPO, MT.ESTADO, TO_CHAR(MT.FECHAPAGO,'YYYYMMDD' ), " +
            "TO_CHAR(MT.FECHAPAGO,'HH24MISS' ) , MT.TARJETA, " +
            "MT.IMPORTE, MT.REFERENCIA, MT.TIPOTARJETA, P.ID_PAGO, " +
            "P.MONTO_PAGADO, P.ESTATUS, P.LIBRERIA, P.CAJA, MT.FECHAFACTURA " +
            "FROM PAGOSVT.MT_PAGO MT LEFT OUTER JOIN POL_02_T_PAGO P ON MT.ID = P.TRANSACCION " ;

            if ( oficina != null && oficina.equals("APT")) {
                sql += "AND P.ADQUIRIENTE = 'APT' " +
                "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = '" + fecha + "' " +
                "AND MT.CREDITOINBURSA = 'M' " +
                "AND MT.REFERENCIA IS NOT NULL " +
                "AND P.ESTATUS IN ('AS','RI','TI','IV')";

            }
            else {
                sql += "AND P.ADQUIRIENTE = 'MKT' " +
                "WHERE  TO_CHAR(MT.FECHAPAGO,'YYYY/MM/DD' ) = '" + fecha + "' " +
                "AND (MT.CREDITOINBURSA IS NULL OR  MT.CREDITOINBURSA = 'E') " +
                "AND MT.REFERENCIA IS NOT NULL " +
                "AND P.ESTATUS IN ('AS','RI','TI','IV')";

            }


        }

        RowMapper rowMapper = new RowMapper() {

                public Object mapRow(ResultSet rs, int index) throws SQLException {

                        PagoMKTDTO pago = new PagoMKTDTO();
                        pago.setIdPago(rs.getString(1));
                        pago.setTelefono(rs.getString(2));
                        pago.setTipoTelefono(rs.getString(3));
                        pago.setEstatus(rs.getString(4));
                        pago.setFechaEstatus(rs.getString(5));
                        pago.setNombre(rs.getString(6)); //Hora

                        try {
                            pago.setTarjeta(Rijndael_Algorithm.Desencriptar(rs.getString(7)));
                        }catch(Exception e) {
                            pago.setTarjeta(rs.getString(7));

                        }
                        pago.setMontoPagar(rs.getString(8));
                        pago.setReferencia(rs.getString(9));
                        pago.setTipoTarjeta(rs.getString(10));
                        pago.setPrincipal(rs.getString(11)); //pol_02.id_pago
                        pago.setOtraLinea(rs.getString(12)); //pol_02.monto_pagadp
                        pago.setEstatusDes(rs.getString(13)); //pol_02.estatus
                        pago.setSecure(rs.getString(14)); //pol_02.libreria
                        pago.setCorreo(rs.getString(15)); //pol_02.caja
                        pago.setFechaSol(rs.getString(16));//MT.FECHAFACTURA

                        return pago;
                }
        };

         return jt.query(sql,rowMapper);


    }


    public int actEstatusPisaManual(String idPago)
    {

        if ( idPago != null ) {

            String sql = "UPDATE POL_02_T_PAGO " +
                         "SET ESTATUS = 'AP', FECHA_CONCIL_MAN = SYSDATE " ;

            if ( idPago.indexOf(",") != -1 )
                       sql +=  "WHERE ID_PAGO IN ( " + idPago  + ") ";
            else
                sql += "WHERE ID_PAGO = " + idPago ;


            return jt.update(sql);

        }
        else return -1;
    }



    /**
     * @param csMap the csMap to set
     */
    public void setCsMap(Map csMap) {
        this.csMap = csMap;
    }

    /**
     * @param usrTC the usrTC to set
     */
    public void setUsrTC(String usrTC) {
        this.usrTC = usrTC;
    }

    /**
     * @param comercioBD1 the comercioBD1 to set
     */
    public void setComercioBD1(String comercioBD1) {
        this.comercioBD1 = comercioBD1;
    }

    /**
     * @param binesDAO the binesDAO to set
     */
    public void setBinesDAO(BinesDAO binesDAO) {
        this.binesDAO = binesDAO;
    }


}
