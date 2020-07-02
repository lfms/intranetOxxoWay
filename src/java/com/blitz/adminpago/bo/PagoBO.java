/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.PagoDAO;
import com.blitz.adminpago.dto.DatosCuerpoDTO;
import com.blitz.adminpago.dto.PagoDTO;
import com.blitz.adminpago.util.Rijndael_Algorithm;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class PagoBO {

    private Logger log = Logger.getLogger("com.blitz.adminpagoline.bo.PagoBO");
    @Autowired
    private PagoDAO pagoDAO;
    private PagoDAO pagoDAO2;
    private String comercioBD1;


    public List obtenerPagos(String pstFechaI,String pstFechaF, String pstComercio, String pstSucursal, 
            String pstEstatus, String pstAutorizacion, String pstTelefono, String pstTransaccion, String pstTarjeta, String pstRefBanco, String pstCS, 
            String pstLib, String pstFechaIO,String pstFechaFO, String pstImporte, String pstLogin, int reporte)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return pagoDAO.obtenerPagos(pstFechaI, pstFechaF, pstComercio, pstSucursal, pstEstatus, pstAutorizacion, pstTelefono, pstTransaccion, pstTarjeta,
                pstRefBanco, pstCS, pstLib, pstFechaIO,pstFechaFO, pstImporte, pstLogin, reporte);
    }

    public List obtenerPagosT11(String pstFechaI, String pstComercio)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return getPagoDAOBD(lnuBD).obtenerPagosT11(pstFechaI, pstComercio);
    }

    public List obtenerPagosT11TelUnicos(String pstFechaI, String pstComercio)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return getPagoDAOBD(lnuBD).obtenerPagosT11TelUnicos(pstFechaI, pstComercio);
    }

    public List obtenerPagosCaja(String pstFechaI, String pstComercio, String pstEstatus)
    {
        List pagos = null;
        int lnuBD = 1; //BSN, MKT

        if ( pstComercio != null && pstComercio.equals("*"))
        {
            log.info("obtener pagos - dentro del comercio");
            // BSN, MKT
            pagos = getPagoDAOBD(lnuBD).obtenerPagosCaja(pstFechaI, null, pstEstatus);

            lnuBD = 2; //Los demas terceros

        }
        else
        {
            log.info("obtener pagos - no commercio");
            lnuBD = 1; //BSN, MKT
            if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
                lnuBD = 2;

             pagos = getPagoDAOBD(lnuBD).obtenerPagosCaja(pstFechaI, pstComercio, pstEstatus);
        }

        return pagos;
    }


    public List obtenerPagosPendientesPISA(String pstFechaI, String pstComercio)
    {
        List pagos = null;
        int lnuBD = 1; //BSN, MKT

        lnuBD = 1; //APT, MKT
        pagos = getPagoDAOBD(lnuBD).obtenerPagosPendientesPISA(pstFechaI, pstComercio);

        return pagos;
    }



    public List obtenerAnalisisFraude(String pstAgrupa,String pstAgrupaCount,String pstFechaI,String pstFechaF,
            String pstEstatus, String pstTelefono, String pstTelPrincipal,
            String pstTarjeta, String pstCorreo, String pstTipoTel)
    {
        List pagos = getPagoDAOBD(1).obtenerAnalisisFraude(pstAgrupa,pstAgrupaCount,pstFechaI, pstFechaF, pstEstatus, pstTelefono,
                pstTelPrincipal, pstTarjeta,pstCorreo, pstTipoTel);
        

        if (pagos!= null && pagos.size() > 0  &&
                pstAgrupa != null && pstAgrupa.startsWith("TARJETA"))
        {
            Iterator it = pagos.iterator();
            while (it.hasNext())
            {
                PagoDTO pago = new PagoDTO();
                pago = (PagoDTO)it.next();
                String tarjeta = pago.getTransaccion();
                if (tarjeta!= null && tarjeta.length() > 15)
                {
                    Rijndael_Algorithm util = new Rijndael_Algorithm();
                    pago.setTransaccion(util.Desencriptar(tarjeta));

                }
            }

        }
         return pagos;
    }

    public List obtenerDetalleMovimientos(String pstFechaI,String pstFechaF,
            String pstEstatus, String pstTelefono, String pstTelPrincipal,
            String pstTarjeta, String pstCorreo, String pstTipoTel)
    {
        return getPagoDAOBD(1).obtenerDetalleMovimientos(pstFechaI, pstFechaF, pstEstatus, pstTelefono,
                pstTelPrincipal, pstTarjeta,pstCorreo, pstTipoTel);

    }


    public HashMap obtenerCoincidencias(String pstAgrupa, String pstFechaI,String pstFechaF,
            String pstEstatus, String pstTelefono, String pstTelPrincipal,
            String pstTarjeta, String pstCorreo, String pstTipoTel)
    {
        List coincidencias = new ArrayList();

        HashMap lista = getPagoDAOBD(1).obtenerCoincidencias(pstAgrupa, pstFechaI, pstFechaF,
                pstEstatus, pstTelefono, pstTelPrincipal, pstTarjeta, pstCorreo, pstTipoTel);


        //Buscamos las coincidencias en listas negras
        if (lista != null)
        {
            List lnegra = new ArrayList();
            Iterator it = lista.keySet().iterator();
            while (it.hasNext())
            {
                String llave = (String)it.next();
                List coin = (List)lista.get(llave);
                if ( coin != null && coin.size()> 0)
                {
                    Iterator it2 = coin.iterator();
                    String registro = "";
                    while (it2.hasNext())
                    {
                        registro += "'" + ((DatosCuerpoDTO)it2.next()).getTexto() + "',";
                    }
                    if ( registro != null && registro.length() > 0)
                        registro = registro.substring(0, registro.length()-1);

                    lnegra.addAll(getPagoDAOBD(1).obtenerCoincidenciasListasNegras(llave, registro));


                }
            }
            if (lnegra != null && lnegra.size()>0)
                lista.put("LNEGRAS", lnegra);
        }
        
        return lista;
    }



    public PagoDTO obtenerTotalesPagos(String pstFechaI,String pstFechaF, String pstComercio, String pstSucursal,
            String pstEstatus, String pstAutorizacion, String pstTelefono, String pstTransaccion, String pstTarjeta, String pstRefBanco, String pstCS, String pstLib)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return getPagoDAOBD(lnuBD).obtenerTotalesPagos(pstFechaI, pstFechaF, pstComercio, pstSucursal, pstEstatus, pstAutorizacion, pstTelefono, pstTransaccion, pstTarjeta, pstRefBanco, pstCS, pstLib);
    }


    public List obtenerPagosTelefono(String pstFechaI,String pstFechaF, String pstTelefono, String pstAdquiriente, String pstFechaAPisa, String pstFechaATro)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstAdquiriente != null && comercioBD1.indexOf(pstAdquiriente) == -1 )
            lnuBD = 2;

        return getPagoDAOBD(lnuBD).obtenerPagosTelefono(pstFechaI, pstFechaF, pstTelefono, pstAdquiriente, pstFechaAPisa, pstFechaATro);
    }

    /*
    public HashMap obtenerPagosXConciliarCom(String pstComercio,String pstTelefono, String pstFechaSol, String pstMontoPagado, String pstFechaSolMin)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;
        
        return getPagoDAOBD(lnuBD).obtenerPagosXConciliarCom(pstComercio,pstTelefono, pstFechaSol, pstMontoPagado, pstFechaSolMin,0);
    }
*/
    public PagoDTO obtenerPagoXId(String pstIdPago, String pstComercio)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return getPagoDAOBD(lnuBD).obtenerPagoXId(pstIdPago);
    }

    public int actualizarLibreria(String pstIdPago, String pstLibreria, String pstComercio)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return getPagoDAOBD(lnuBD).actualizarLibreria(pstIdPago, pstLibreria);
    }

    /**
     * @param pagoDAO the pagoDAO to set
     */
    public void setPagoDAO(PagoDAO pagoDAO) {
        this.pagoDAO = pagoDAO;
    }

    public List obtenerPagosLibCom(String fechaI, String fechaF, String pstComercio, String estatus, String pstAcumMensual)
    {
        int lnuBD = 1; //BSN, MKT
        if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
            lnuBD = 2;

        return getPagoDAOBD(lnuBD).obtenerPagosLibCom(fechaI, fechaF, pstComercio, estatus, pstAcumMensual);
    }

    public PagoDTO obtenerPagoMKT02Pago(String pstComercio, String pstTransaccion, String pstReferencia)
    {
        return pagoDAO.obtenerPagoMKT02Pago(pstComercio, pstTransaccion, pstReferencia);
    }


    public PagoDTO obtenerTotalesPagosCaja(String pstFechaI, String pstComercio, String pstEstatus)
    {
        int lnuBD = 1; //BSN, MKT
        PagoDTO pago1 = null;

        if ( pstComercio != null && pstComercio.equals("*"))
        {
            log.info("dentro del commercio");
            // BSN, MKT
            pago1 = getPagoDAOBD(lnuBD).obtenerTotalesPagosCaja(pstFechaI, null, pstEstatus);
            lnuBD = 2; //Los demas terceros
            PagoDTO pago2 = getPagoDAOBD(lnuBD).obtenerTotalesPagosCaja(pstFechaI, null, pstEstatus);
            if ( pago2 != null )
            {
                int totalPagos = Integer.parseInt(pago1.getIdPago());
                totalPagos += Integer.parseInt(pago2.getIdPago());
                pago1.setIdPago(String.valueOf(totalPagos));
                double totalImporte = pago1.getImportePagado();
                totalImporte += pago2.getImportePagado();                
                try
                {
                    pago1.setMontoPagado(this::FormatMonto);
                    
                } catch(Exception e){}
            }
        }else {
            log.info("fuera del comercio");
            lnuBD = 1; //BSN, MKT
            if ( pstComercio != null && comercioBD1.indexOf(pstComercio) == -1 )
                lnuBD = 2;
            pago1 = getPagoDAOBD(lnuBD).obtenerTotalesPagosCaja(pstFechaI, pstComercio, pstEstatus);            
            try
            {
                pago1.setMontoPagado(this::FormatMonto);

            } catch(Exception e){
            }

        }

        return pago1;

    }


    public List obtenerPagosMKTAPT(String pstFecha)
    {
        return pagoDAO.obtenerPagosMKTAPT(pstFecha);
    }
    
    public List obtenerPagosAPTPorReferencia(String fecha, String oficina, int soloPendientes)
    {
        return pagoDAO.obtenerPagosAPTPorReferencia(fecha, oficina, soloPendientes);
    }
    
    public List obtenerPagosAPTPorReferenciaCorte(String fecha, String oficina)
    {
        return pagoDAO.obtenerPagosAPTPorReferenciaCorte(fecha, oficina);
    }

    public int actEstatusPisaManual(String idPago) {
        return pagoDAO.actEstatusPisaManual(idPago);
    }


    public List obtenerPagosAPPEstadistica()
    {
        return pagoDAO.obtenerPagosAPPEstadistica();
    }


    public PagoDAO getPagoDAOBD(int pnuBD)
    {
        if ( pnuBD == 1 )
            return pagoDAO;
        else
            return pagoDAO;

    }

    /**
     * @param pagoDAO2 the pagoDAO2 to set
     */
    public void setPagoDAO2(PagoDAO pagoDAO2) {
        this.pagoDAO2 = pagoDAO2;
    }

    /**
     * @param comercioBD1 the comercioBD1 to set
     */
    public void setComercioBD1(String comercioBD1) {
        this.comercioBD1 = comercioBD1;
    }

    public String FormatMonto(String s){
        DecimalFormat myFormatter = new DecimalFormat("###,##0.00");
        return myFormatter.format(s);
    }

}
