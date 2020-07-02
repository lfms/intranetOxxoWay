/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.mb;


import com.blitz.adminpago.bo.CargoBO;
import com.blitz.adminpago.bo.ConsultaBO;
import com.blitz.adminpago.bo.EnvioReportesBO;
import com.blitz.adminpago.bo.PagoBO;
import com.blitz.adminpago.bo.SucursalBO;
import com.blitz.adminpago.dto.ConsultaDTO;
import com.blitz.adminpago.dto.ListaSucursalDTO;
import com.blitz.adminpago.dto.PagoDTO;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jxl.*;
import jxl.write.*;
import com.blitz.adminpago.dto.UsuarioDTO;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "ReportesMB")
@ViewScoped
public class ReportesMB implements Serializable{
    @ManagedProperty(value = "#{PagoBO}")
    private PagoBO pagoBO;
    private ConsultaBO consultaBO;
    @ManagedProperty(value = "#{SucursalBO}")
    private SucursalBO sucursalBO;
    private DataScrollerList dataScrollerList;
    private String parSucursal;
    private String parComercio;
    private String parTelefono;
    private String parCorreo;
    private String parAutorizacion;
    private String parEstatus;
    private String parTransaccion;
    private String parIdMtPago;
    private String parImporte;
    private String fechaIO;
    private String fechaFO;
    private String parReferenciaBanco;
    private String parCS;
    private String parLib;
    private String fechaI;
    private String fechaF;
    private String msgError;
    private boolean mostrarPagos;
    private boolean mostrarErrorPagos;
    private boolean mostrarVistaMKT;
    @ManagedProperty(value = "#{estatusPagoMap}")
    private Map estatusPagoMap;
    private Map comercios;    
    @ManagedProperty(value = "#{csMap}")
    private Map csMap;    
    @ManagedProperty(value = "#{libreriaMap}")
    private Map libreriaMap;
    private List pagos;
    private List pagosTel;
    private boolean mostrarPagosTel;
    private boolean mostrarErrorPagosTel;
    private List consTel;
    private boolean mostrarConsTel;
    private boolean mostrarErrorConsTel;
    private String msgErrorConsTel;
    private String adquiriente;
    private String fechaArchivoPisa;
    private String fechaArchivoTercero;
    private PagoDTO totalesFiltro;
    private String filtros;
    private String saldo;
    
    Log log = LogFactory.getLog(this.getClass());

    public String muestraPagos()
    {
        mostrarVistaMKT = false;


        if( parComercio == null || parComercio.length() == 0 )
        {
            mostrarPagos = false;
            mostrarErrorPagos = true;
            msgError = "SELECCIONE UN COMERCIO";
            return null;

        }

        try
        {
            filtros = this.obtenerRotuloFiltros();

            HttpSession session =
                    (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            UsuarioDTO usuario = (UsuarioDTO)session.getAttribute("usuario");

            //Validamos el perfil del usuario y limitamos la visualizacion de la TC a toda o la terminacion segun el caso
            String login = usuario.getUsuario();


            pagos = pagoBO.obtenerPagos(fechaI, fechaF, parComercio, parSucursal, parEstatus, parAutorizacion, parTelefono, parTransaccion, parIdMtPago, 
                    parReferenciaBanco, parCS,parLib, fechaIO, fechaFO, parImporte, login, 0);
            if ( parComercio != null && (parComercio.equals("MKT")|| parComercio.equals("APT") ))
                mostrarVistaMKT = true;

            if( pagos != null && pagos.size() > 0 )
            {
                totalesFiltro = pagoBO.obtenerTotalesPagos(fechaI, fechaF, parComercio, parSucursal, parEstatus, parAutorizacion, parTelefono, parTransaccion, parIdMtPago, parReferenciaBanco, parCS,parLib);
                mostrarPagos = true;
                mostrarErrorPagos = false;
                setMsgError(null);
                dataScrollerList = new DataScrollerList(pagos);
            }
            else
            {
                mostrarPagos = false;
                mostrarErrorPagos = true;
                setMsgError("NO EXISTEN PAGOS CON LOS DATOS DE BUSQUEDA SELECCIONADOS");
            }

        }
        catch(Exception e)
        {
            return null;
        }

        return null;

    }


    public String muestraPagosSinBSN()
    {
        log.info("muestra pagos sin bsn");
        mostrarVistaMKT = false;

        if( parComercio == null || parComercio.length() == 0 ){
            mostrarPagos = false;
            mostrarErrorPagos = true;
            msgError = "SELECCIONE UN COMERCIO";
            return null;

        }

        try
        {
            filtros = this.obtenerRotuloFiltros();

            HttpSession session =
                    (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            UsuarioDTO usuario = (UsuarioDTO)session.getAttribute("usuario");

            //Validamos el perfil del usuario y limitamos la visualizacion de la TC a toda o la terminacion segun el caso
            String login = usuario.getUsuario();


            pagos = pagoBO.obtenerPagos(fechaI, fechaF, parComercio, parSucursal, parEstatus, parAutorizacion, parTelefono, parTransaccion, parIdMtPago,
                    parReferenciaBanco, parCS,parLib, fechaIO, fechaFO, parImporte, login, 0);
            if ( parComercio != null && (parComercio.equals("MKT")|| parComercio.equals("APT") ))
                mostrarVistaMKT = true;

            if( pagos != null && pagos.size() > 0 )
            {
                totalesFiltro = pagoBO.obtenerTotalesPagos(fechaI, fechaF, parComercio, parSucursal, parEstatus, parAutorizacion, parTelefono, parTransaccion, parIdMtPago, parReferenciaBanco, parCS,parLib);
                mostrarPagos = true;
                mostrarErrorPagos = false;
                setMsgError(null);
                dataScrollerList = new DataScrollerList(pagos);
            }
            else
            {
                mostrarPagos = false;
                mostrarErrorPagos = true;
                setMsgError("NO EXISTEN PAGOS CON LOS DATOS DE BUSQUEDA SELECCIONADOS");
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return null;

    }

    public String muestraPagosTelefono()
    {
        mostrarVistaMKT = false;

        if( adquiriente == null || adquiriente.length() == 0 )
        {
            mostrarPagos = false;
            mostrarErrorPagosTel = true;
            msgError = "SELECCIONE UNA VIA DE COBRO";
            return null;

        }

        if( (fechaI == null || fechaI.length() == 0 ) && (fechaF == null || fechaF.length() == 0))
        {
            mostrarPagos = false;
            mostrarErrorPagosTel = true;
            msgError = "SELECCIONE UN RANGO DE FECHAS";
            return null;

        }

        try
        {

            if ( parTelefono == null || parTelefono.length() != 10 )
            {
                mostrarPagosTel = false;
                mostrarErrorPagosTel = true;
                setMsgError("PROPORCIONE UN NUMERO DE TELEFONO VALIDO");
                return null;
            }

            pagosTel = pagoBO.obtenerPagosTelefono(fechaI, fechaF, parTelefono, adquiriente, fechaArchivoPisa, fechaArchivoTercero);

            if( pagosTel != null && pagosTel.size() > 0 )
            {
                mostrarPagosTel = true;
                mostrarErrorPagosTel = false;
                setMsgError(null);
                dataScrollerList = new DataScrollerList(pagosTel);
            }
            else
            {
                mostrarPagosTel = false;
                mostrarErrorPagosTel = true;
                setMsgError("NO EXISTEN PAGOS CON LOS DATOS DE BUSQUEDA SELECCIONADOS");
            }


            /*PGM: YA NO SE ALMACENARAN LAS CONSULTAS.
            consTel = consultaBO.obtenerConsultasTelefono(fechaI, fechaF, parTelefono, adquiriente);

            if( consTel != null && consTel.size() > 0 )
            {
                mostrarConsTel = true;
                mostrarErrorConsTel = false;
                setMsgErrorConsTel(null);
                dataScrollerList2 = new DataScrollerList(consTel);
            }
            else
            {
                mostrarConsTel = false;
                mostrarErrorConsTel = true;
                setMsgErrorConsTel("NO EXISTEN CONSULTAS CON LOS DATOS DE BUSQUEDA SELECCIONADOS");
            }
            */
            //this.limpiaParametros();

        }
        catch(Exception e)
        {
            return null;
        }

        return null;

    }
    
    public void obtenerSucursal(ValueChangeEvent event)
    {
        log.info("Obteniendo sucursales");
        String lstCveCom = (String) event.getNewValue();
        //Ya no trae el id_comercio sino la cve_comercio

        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();

        ListaSucursalDTO listaSuc = (ListaSucursalDTO) elContext.getELResolver().getValue(elContext,null,"ListaSucursalDTO");
        if ( lstCveCom == null  || lstCveCom.length() > 0 )
        {
            Map lista = sucursalBO.obtenerSucursalesComCve(lstCveCom);
            if ( lista != null )
                listaSuc.setListaSucursal(lista);
        }

       /*Render the response*/
       context.renderResponse();
    
    }

    public String obtenerRotuloFiltros()
    {
        String lstCadena = "";

         if (parComercio != null && parComercio.length()> 0 && !parComercio.equals("*"))
            lstCadena += parComercio;

        if (parSucursal != null && parSucursal.length()> 0 && !parSucursal.equals("*"))
            lstCadena += " SUC=" + parSucursal;

        if (parEstatus != null && parEstatus.length()> 0 && !parEstatus.equals("*"))
            lstCadena += " EST=" + parEstatus;

        if (fechaI != null && fechaI.length()> 0)
            lstCadena += " DEL " + fechaI;

        if (fechaF != null && fechaF.length()> 0)
            lstCadena += " AL " + fechaF;

        if (fechaIO != null && fechaIO.length()> 0)
            lstCadena += "Fecha Pago DEL " + fechaIO;

        if (fechaFO != null && fechaFO.length()> 0)
            lstCadena += " AL " + fechaFO;


        if (parAutorizacion != null && parAutorizacion.length()> 0)
            lstCadena += " TX=" + parAutorizacion;

        if (parTelefono != null && parTelefono.length()> 0)
            lstCadena += " TEL=" + parTelefono;

         if (parTransaccion != null && parTransaccion.length()> 0)
            lstCadena += " TROFOL= " + parTransaccion;

         if (parIdMtPago != null && parIdMtPago.length()> 0)
            lstCadena += " TC= " + parIdMtPago;

         if (parReferenciaBanco != null && parReferenciaBanco.length()> 0)
            lstCadena += " REFBCO= " + parReferenciaBanco;

        return lstCadena;
    }

    
    public String limpiaParametros()
    {
        fechaI = null;
        fechaF = null;
        fechaIO = null;
        fechaFO = null;
        parAutorizacion=null;
        parEstatus=null;
        parSucursal=null;
        parComercio=null;
        parTelefono=null ;
        adquiriente = null;
        mostrarErrorConsTel = false;
        totalesFiltro = null;
        mostrarVistaMKT=false;
        parIdMtPago=null;
        parReferenciaBanco=null;
        parCS=null;
        parLib=null;
        parImporte=null;
        
        return null;

    }

    public String borrarCampos()
    {
        setFechaI(null);
        setFechaF(null);
        setFechaIO(null);
        setFechaFO(null);
        setMsgError(null);
        setMsgErrorConsTel(null);
        pagos = null;
        mostrarPagos = false;
        mostrarPagosTel = false;
        mostrarConsTel = false;
        adquiriente=null;
        parAutorizacion=null;
        parEstatus=null;
        parSucursal=null;
        parComercio=null;
        parTelefono=null;
        parTransaccion=null;
        totalesFiltro = null;
        mostrarVistaMKT=false;
        parIdMtPago=null;
        parReferenciaBanco=null;
        parCS=null;
        parLib=null;
        parImporte=null;
        saldo = null;
        parCorreo=null;
        return null;
    }




    public void generarReporte(FacesContext fc) {

        OutputStream out = null;

        HttpServletResponse resp = (HttpServletResponse) fc.getExternalContext().getResponse();


        resp.setContentType("application/vnd.ms-excel");
        resp.addHeader("Content-Disposition", "attachment;filename=ReportePagos.xls");

        try {

            out = resp.getOutputStream();

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Reporte de Pagos", 0);
            int fila = 0;

            HttpSession session =
                    (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            UsuarioDTO usuario = (UsuarioDTO)session.getAttribute("usuario");

            //Validamos el perfil del usuario y limitamos la visualizacion de la TC a toda o la terminacion segun el caso
            String login = usuario.getUsuario();

            List lobDatos = pagoBO.obtenerPagos(fechaI, fechaF, parComercio, parSucursal, parEstatus, parAutorizacion, parTelefono, parTransaccion,parIdMtPago,
                    parReferenciaBanco,parCS,parLib,fechaIO, fechaFO, parImporte, login, 0 );

            if ( lobDatos != null )
            {
                Label encabezado = new Label(0, fila, "PAGO");
                sheet.addCell(encabezado);
                encabezado = new Label(1, fila, "TELEFONO");
                sheet.addCell(encabezado);
                encabezado = new Label(2, fila, "MONTO_PAGAR");
                sheet.addCell(encabezado);
                encabezado = new Label(3, fila, "FECHA_SOL");
                sheet.addCell(encabezado);
                encabezado = new Label(4, fila, "FECHA_RESP_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(5, fila, "FECHA_RESP_TRO");
                sheet.addCell(encabezado);
                encabezado = new Label(6, fila, "FECHA_CONTABLE");
                sheet.addCell(encabezado);
                encabezado = new Label(7, fila, "FECHA_CAPTURA");
                sheet.addCell(encabezado);
                encabezado = new Label(8, fila, "HORA");
                sheet.addCell(encabezado);
                encabezado = new Label(9, fila, "DIA");
                sheet.addCell(encabezado);
                encabezado = new Label(10, fila, "MONEDA");
                sheet.addCell(encabezado);
                encabezado = new Label(11, fila, "TIPO_INGRESO");
                sheet.addCell(encabezado);
                encabezado = new Label(12, fila, "CODIGO_RESP");
                sheet.addCell(encabezado);
                encabezado = new Label(13, fila, "TIPO_RESP");
                sheet.addCell(encabezado);
                encabezado = new Label(14, fila, "AUDIT_NUMBER");
                sheet.addCell(encabezado);
                encabezado = new Label(15, fila, "ADQUIRIENTE");
                sheet.addCell(encabezado);
                encabezado = new Label(16, fila, "TIENDA_TERMINAL");
                sheet.addCell(encabezado);
                encabezado = new Label(17, fila, "UNIDAD");
                sheet.addCell(encabezado);
                encabezado = new Label(18, fila, "SECUENCIA_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(19, fila, "SALDO_VENCIDO");
                sheet.addCell(encabezado);
                encabezado = new Label(20, fila, "MIN_REANUDACION");
                sheet.addCell(encabezado);
                encabezado = new Label(21, fila, "MONTO_PAGADO");
                sheet.addCell(encabezado);
                encabezado = new Label(22, fila, "TRANSACCION");
                sheet.addCell(encabezado);
                encabezado = new Label(23, fila, "CAJA");
                sheet.addCell(encabezado);
                encabezado = new Label(24, fila, "TARJETA");
                sheet.addCell(encabezado);
                encabezado = new Label(25, fila, "TIPOTC");
                sheet.addCell(encabezado);
                encabezado = new Label(26, fila, "REFERENCIA");
                sheet.addCell(encabezado);
                encabezado = new Label(27, fila, "SECURE");
                sheet.addCell(encabezado);
                encabezado = new Label(28, fila, "CLASE_SERV");
                sheet.addCell(encabezado);
                encabezado = new Label(29, fila, "TIPO_TEL");
                sheet.addCell(encabezado);
                encabezado = new Label(30, fila, "LIBRERIA");
                sheet.addCell(encabezado);


                Iterator it = null;

                DateFormat customDateFormat = new DateFormat("dd MMM yyyy hh:mm:ss");
                WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);

                PagoDTO pago;
                it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {

                    pago = (PagoDTO) it.next();
                    Label accion = new Label(0, fila, String.valueOf(pago.getIdPago()));
                    sheet.addCell(accion);
                    accion = new Label(1, fila, pago.getTelefono());
                    sheet.addCell(accion);
                    accion = new Label(2, fila, pago.getMontoPagar());
                    sheet.addCell(accion);
                    accion = new Label(3, fila, pago.getFechaSol());
                    sheet.addCell(accion);
                    accion = new Label(4, fila, pago.getFechaRespPisa());
                    sheet.addCell(accion);
                    //DateTime fecha = new DateTime(5, fila, pedido.getFechaPedido(), dateFormat);
                    //sheet.addCell(fecha);
                    accion = new Label(5, fila, pago.getFechaRespTro());
                    sheet.addCell(accion);


                    accion = new Label(6, fila, pago.getFechaContable());
                    sheet.addCell(accion);
                    accion = new Label(7, fila, pago.getFechaCaptura());
                    sheet.addCell(accion);
                    accion = new Label(8, fila, pago.getHora());
                    sheet.addCell(accion);
                    accion = new Label(9, fila, pago.getDia());
                    sheet.addCell(accion);
                    accion = new Label(10, fila, pago.getMoneda());
                    sheet.addCell(accion);
                    accion = new Label(11, fila, pago.getTipoIngreso());
                    sheet.addCell(accion);

                    accion = new Label(12, fila, pago.getCodigoResp());
                    sheet.addCell(accion);
                    accion = new Label(13, fila, pago.getTipoResp());
                    sheet.addCell(accion);
                    accion = new Label(14, fila, pago.getAuditNumber());
                    sheet.addCell(accion);
                    accion = new Label(15, fila, pago.getAdquiriente());
                    sheet.addCell(accion);
                    accion = new Label(16, fila, pago.getTiendaTerm());
                    sheet.addCell(accion);
                    accion = new Label(17, fila, pago.getUnidad());
                    sheet.addCell(accion);
                    accion = new Label(18, fila, pago.getSecuenciaPisa());
                    sheet.addCell(accion);
                    accion = new Label(19, fila, pago.getSaldoVencido());
                    sheet.addCell(accion);
                    accion = new Label(20, fila, pago.getMinReanudacion());
                    sheet.addCell(accion);
                    accion = new Label(21, fila, pago.getMontoPagado());
                    sheet.addCell(accion);
                    accion = new Label(22, fila, pago.getTransaccion());
                    sheet.addCell(accion);
                    accion = new Label(23, fila, pago.getCaja());
                    sheet.addCell(accion);
                    accion = new Label(24, fila, pago.getNumeroTC());
                    sheet.addCell(accion);
                    accion = new Label(25, fila, pago.getTipoTC());
                    sheet.addCell(accion);
                    accion = new Label(26, fila, pago.getAutorizacion());
                    sheet.addCell(accion);
                    accion = new Label(27, fila, pago.getSecure());
                    sheet.addCell(accion);
                    accion = new Label(28, fila, pago.getClaseServicio());
                    sheet.addCell(accion);
                    accion = new Label(29, fila, pago.getTipotel());
                    sheet.addCell(accion);
                    accion = new Label(30, fila, pago.getLibreria());
                    sheet.addCell(accion);

                    fila++;

                }
                fila++;
            }


            workbook.write();
            workbook.close();
            out.flush();



        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }
        finally {
            if (out != null)
                try {
                out.close();
                }catch(Exception e) { ; }

        }
        fc.responseComplete();

    }



    public void generarReporteTel(FacesContext fc) {

        OutputStream out = null;

        HttpServletResponse resp = (HttpServletResponse) fc.getExternalContext().getResponse();


        resp.setContentType("application/vnd.ms-excel");
        resp.addHeader("Content-Disposition", "attachment;filename=ReportePagosTel.xls");

        try {

            out = resp.getOutputStream();

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Reporte de Pagos", 0);
            int fila = 0;

            List lobDatos = pagoBO.obtenerPagosTelefono(fechaI, fechaF, parTelefono, adquiriente, fechaArchivoPisa, fechaArchivoTercero);

            if ( lobDatos != null )
            {
                Label encabezado = new Label(0, fila, "PAGO");
                sheet.addCell(encabezado);
                encabezado = new Label(1, fila, "TELEFONO");
                sheet.addCell(encabezado);
                encabezado = new Label(2, fila, "MONTO_PAGAR");
                sheet.addCell(encabezado);
                encabezado = new Label(3, fila, "FECHA_SOL");
                sheet.addCell(encabezado);
                encabezado = new Label(4, fila, "FECHA_RESP_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(5, fila, "FECHA_RESP_TRO");
                sheet.addCell(encabezado);
                encabezado = new Label(6, fila, "FECHA_CONTABLE");
                sheet.addCell(encabezado);
                encabezado = new Label(7, fila, "FECHA_CAPTURA");
                sheet.addCell(encabezado);
                encabezado = new Label(8, fila, "HORA");
                sheet.addCell(encabezado);
                encabezado = new Label(9, fila, "DIA");
                sheet.addCell(encabezado);
                encabezado = new Label(10, fila, "MONEDA");
                sheet.addCell(encabezado);
                encabezado = new Label(11, fila, "TIPO_INGRESO");
                sheet.addCell(encabezado);
                encabezado = new Label(12, fila, "CODIGO_RESP");
                sheet.addCell(encabezado);
                encabezado = new Label(13, fila, "TIPO_RESP");
                sheet.addCell(encabezado);
                encabezado = new Label(14, fila, "AUDIT_NUMBER");
                sheet.addCell(encabezado);
                encabezado = new Label(15, fila, "VIA DE COBRO");
                sheet.addCell(encabezado);
                encabezado = new Label(16, fila, "TIENDA_TERMINAL");
                sheet.addCell(encabezado);
                encabezado = new Label(17, fila, "UNIDAD");
                sheet.addCell(encabezado);
                encabezado = new Label(18, fila, "SECUENCIA_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(19, fila, "SALDO_VENCIDO");
                sheet.addCell(encabezado);
                encabezado = new Label(20, fila, "MIN_REANUDACION");
                sheet.addCell(encabezado);
                encabezado = new Label(21, fila, "MONTO_PAGADO");
                sheet.addCell(encabezado);
                encabezado = new Label(22, fila, "TRANSACCION");
                sheet.addCell(encabezado);
                encabezado = new Label(23, fila, "FECHA _POSTEO");
                sheet.addCell(encabezado);
                encabezado = new Label(24, fila, "ARCHIVO_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(25, fila, "ARCHIVO_TERCERO");
                sheet.addCell(encabezado);
                encabezado = new Label(26, fila, "CAJA");
                sheet.addCell(encabezado);


                Iterator it = null;

                DateFormat customDateFormat = new DateFormat("dd MMM yyyy hh:mm:ss");
                WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);

                PagoDTO pago;
                it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {

                    pago = (PagoDTO) it.next();
                    Label accion = new Label(0, fila, String.valueOf(pago.getIdPago()));
                    sheet.addCell(accion);
                    accion = new Label(1, fila, pago.getTelefono());
                    sheet.addCell(accion);
                    accion = new Label(2, fila, pago.getMontoPagar());
                    sheet.addCell(accion);
                    accion = new Label(3, fila, pago.getFechaSol());
                    sheet.addCell(accion);
                    accion = new Label(4, fila, pago.getFechaRespPisa());
                    sheet.addCell(accion);
                    //DateTime fecha = new DateTime(5, fila, pedido.getFechaPedido(), dateFormat);
                    //sheet.addCell(fecha);
                    accion = new Label(5, fila, pago.getFechaRespTro());
                    sheet.addCell(accion);


                    accion = new Label(6, fila, pago.getFechaContable());
                    sheet.addCell(accion);
                    accion = new Label(7, fila, pago.getFechaCaptura());
                    sheet.addCell(accion);
                    accion = new Label(8, fila, pago.getHora());
                    sheet.addCell(accion);
                    accion = new Label(9, fila, pago.getDia());
                    sheet.addCell(accion);
                    accion = new Label(10, fila, pago.getMoneda());
                    sheet.addCell(accion);
                    accion = new Label(11, fila, pago.getTipoIngreso());
                    sheet.addCell(accion);

                    accion = new Label(12, fila, pago.getCodigoResp());
                    sheet.addCell(accion);
                    accion = new Label(13, fila, pago.getTipoResp());
                    sheet.addCell(accion);
                    accion = new Label(14, fila, pago.getAuditNumber());
                    sheet.addCell(accion);
                    accion = new Label(15, fila, pago.getAdquiriente());
                    sheet.addCell(accion);
                    accion = new Label(16, fila, pago.getTiendaTerm());
                    sheet.addCell(accion);
                    accion = new Label(17, fila, pago.getUnidad());
                    sheet.addCell(accion);
                    accion = new Label(18, fila, pago.getSecuenciaPisa());
                    sheet.addCell(accion);
                    accion = new Label(19, fila, pago.getSaldoVencido());
                    sheet.addCell(accion);
                    accion = new Label(20, fila, pago.getMinReanudacion());
                    sheet.addCell(accion);
                    accion = new Label(21, fila, pago.getMontoPagado());
                    sheet.addCell(accion);
                    accion = new Label(22, fila, pago.getTransaccion());
                    sheet.addCell(accion);
                    accion = new Label(23, fila, pago.getFechaPosteo());
                    sheet.addCell(accion);
                    accion = new Label(24, fila, pago.getSecPisaArchivo());
                    sheet.addCell(accion);
                    accion = new Label(25, fila, pago.getArchivoRespTro());
                    sheet.addCell(accion);
                    accion = new Label(26, fila, pago.getCaja());
                    sheet.addCell(accion);

                    fila++;

                }
                fila++;
            }


            //Creamos la hoja dos con las consultas
            WritableSheet sheet2 = workbook.createSheet("Reporte de Consultas", 1);
            fila = 0;
            lobDatos = consultaBO.obtenerConsultasTelefono(fechaI, fechaF, parTelefono, adquiriente);
            if ( lobDatos != null )
            {
                Label encabezado = new Label(0, fila, "CONSULTAS DE SALDO");
                sheet2.addCell(encabezado);
                fila++;
                encabezado = new Label(0, fila, "TELEFONO");
                sheet2.addCell(encabezado);
                encabezado = new Label(1, fila, "FECHA_SOL");
                sheet2.addCell(encabezado);
                encabezado = new Label(2, fila, "FECHA_RESP_PISA");
                sheet2.addCell(encabezado);
                encabezado = new Label(3, fila, "FECHA_RESP_TRO");
                sheet2.addCell(encabezado);
                encabezado = new Label(4, fila, "SALDO_VENCIDO");
                sheet2.addCell(encabezado);
                encabezado = new Label(5, fila, "MIN_REANUDACION");
                sheet2.addCell(encabezado);
                encabezado = new Label(6, fila, "CODIGO_RESP");
                sheet2.addCell(encabezado);
                encabezado = new Label(7, fila, "ADQUIRIENTE");
                sheet2.addCell(encabezado);
                encabezado = new Label(8, fila, "TIENDA_TERMINAL");
                sheet2.addCell(encabezado);
                encabezado = new Label(9, fila, "UNIDAD");
                sheet2.addCell(encabezado);
                encabezado = new Label(10, fila, "TRANSACCION");
                sheet2.addCell(encabezado);


                ConsultaDTO consulta;
                Iterator it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {

                    consulta = (ConsultaDTO) it.next();
                    Label accion = new Label(0, fila, String.valueOf(consulta.getTelefono()));
                    sheet2.addCell(accion);
                    accion = new Label(1, fila, consulta.getFechaSol());
                    sheet2.addCell(accion);
                    accion = new Label(2, fila, consulta.getFechaRespPisa());
                    sheet2.addCell(accion);
                    accion = new Label(3, fila, consulta.getFechaRespTro());
                    sheet2.addCell(accion);
                    accion = new Label(4, fila, consulta.getSaldoVencido());
                    sheet2.addCell(accion);
                    accion = new Label(5, fila, consulta.getMinReanudacion());
                    sheet2.addCell(accion);
                    accion = new Label(6, fila, consulta.getCodigoResp());
                    sheet2.addCell(accion);
                    accion = new Label(7, fila, consulta.getAdquiriente());
                    sheet2.addCell(accion);
                    accion = new Label(8, fila, consulta.getTiendaTerminal());
                    sheet2.addCell(accion);
                    accion = new Label(9, fila, consulta.getUnidad());
                    sheet2.addCell(accion);
                    accion = new Label(10, fila, consulta.getTransaccion());
                    sheet2.addCell(accion);

                    fila++;

                }
                fila++;

            }
            workbook.write();
            workbook.close();
            out.flush();



        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }
        finally {
            if (out != null)
                try {
                out.close();
                }catch(Exception e) { ; }

        }
        fc.responseComplete();

    }


    public String muestraPagosCaja()
    {
        log.info("Buscando Pagos ----------------->");
        mostrarVistaMKT = false;

        if( parComercio == null || parComercio.length() == 0 )
        {
            mostrarPagos = false;
            mostrarErrorPagos = true;
            msgError = "SELECCIONE UN COMERCIO";
            return null;
        }
        log.info("Buscando Pagos ----------------->");
        try
        {
            filtros = this.obtenerRotuloFiltros();
            pagos = pagoBO.obtenerPagosCaja(fechaI, parComercio, parEstatus);
            if( pagos != null && pagos.size() > 0 )
            {
                log.info("Buscando Pagos ----------------->");
                totalesFiltro = pagoBO.obtenerTotalesPagosCaja(fechaI, parComercio, parEstatus);
                mostrarPagos = true;
                mostrarErrorPagos = false;
                setMsgError(null);
                dataScrollerList = new DataScrollerList(pagos);
            }
            else
            {
                mostrarPagos = false;
                mostrarErrorPagos = true;
                setMsgError("NO EXISTEN PAGOS CON LOS DATOS DE BUSQUEDA SELECCIONADOS");
            }

        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return null;
    }
    
    public void generarReporteCaja(FacesContext fc) {

        OutputStream out = null;

        HttpServletResponse resp = (HttpServletResponse) fc.getExternalContext().getResponse();


        resp.setContentType("application/vnd.ms-excel");
        resp.addHeader("Content-Disposition", "attachment;filename=ReportePagos.xls");

        try {

            out = resp.getOutputStream();

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Pagos Caja", 0);
            int fila = 0;

            List lobDatos = pagos;

            if ( lobDatos == null )
                lobDatos = pagoBO.obtenerPagosCaja(fechaI, parComercio, parEstatus);

            if ( lobDatos != null )
            {
                Label encabezado = new Label(0, fila, "PAGO");
                sheet.addCell(encabezado);
                encabezado = new Label(1, fila, "TELEFONO");
                sheet.addCell(encabezado);
                encabezado = new Label(2, fila, "MONTO_PAGAR");
                sheet.addCell(encabezado);
                encabezado = new Label(3, fila, "FECHA_SOL");
                sheet.addCell(encabezado);
                encabezado = new Label(4, fila, "FECHA_RESP_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(5, fila, "ADQUIRIENTE");
                sheet.addCell(encabezado);
                encabezado = new Label(6, fila, "ESTATUS");
                sheet.addCell(encabezado);
                encabezado = new Label(7, fila, "SECUENCIA_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(8, fila, "MONTO_PAGADO");
                sheet.addCell(encabezado);
                encabezado = new Label(9, fila, "TRANSACCION");
                sheet.addCell(encabezado);
                encabezado = new Label(10, fila, "CAJA");
                sheet.addCell(encabezado);
                encabezado = new Label(11, fila, "LIBRERIA");
                sheet.addCell(encabezado);
                encabezado = new Label(12, fila, "ARCHIVO_CONCIL");
                sheet.addCell(encabezado);


                Iterator it = null;

                DateFormat customDateFormat = new DateFormat("dd MMM yyyy hh:mm:ss");
                WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);

                PagoDTO pago;
                it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {

                    pago = (PagoDTO) it.next();
                    Label accion = new Label(0, fila, String.valueOf(pago.getIdPago()));
                    sheet.addCell(accion);
                    accion = new Label(1, fila, pago.getTelefono());
                    sheet.addCell(accion);
                    accion = new Label(2, fila, pago.getMontoPagar());
                    sheet.addCell(accion);
                    accion = new Label(3, fila, pago.getFechaSol());
                    sheet.addCell(accion);
                    accion = new Label(4, fila, pago.getFechaRespPisa());
                    sheet.addCell(accion);
                    accion = new Label(5, fila, pago.getAdquiriente());
                    sheet.addCell(accion);
                    accion = new Label(6, fila, pago.getEstatus());
                    sheet.addCell(accion);
                    accion = new Label(7, fila, pago.getSecuenciaPisa());
                    sheet.addCell(accion);
                    accion = new Label(8, fila, pago.getMontoPagado());
                    sheet.addCell(accion);
                    accion = new Label(9, fila, pago.getTransaccion());
                    sheet.addCell(accion);
                    accion = new Label(10, fila, pago.getCaja());
                    sheet.addCell(accion);
                    accion = new Label(11, fila, pago.getLibreria());
                    sheet.addCell(accion);
                    accion = new Label(12, fila, pago.getAuditNumber());
                    sheet.addCell(accion);

                    fila++;

                }
                fila++;
            }


            workbook.write();
            workbook.close();
            out.flush();



        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }
        finally {
            if (out != null)
                try {
                out.close();
                }catch(Exception e) { ; }

        }
        fc.responseComplete();

    }

    public DataScrollerList getDataScrollerList() {
        return dataScrollerList;
    }

    public void setDataScrollerList(DataScrollerList dataScrollerList) {
        this.dataScrollerList = dataScrollerList;
    }

    public String getParSucursal() {
        return parSucursal;
    }

    public void setParSucursal(String parSucursal) {
        this.parSucursal = parSucursal;
    }

    public String getParComercio() {
        return parComercio;
    }

    public void setParComercio(String parComercio) {
        this.parComercio = parComercio;
    }

    public String getParTelefono() {
        return parTelefono;
    }

    public void setParTelefono(String parTelefono) {
        this.parTelefono = parTelefono;
    }

    public String getFechaI() {
        return fechaI;
    }

    public void setFechaI(String fechaI) {
        this.fechaI = fechaI;
    }

    public String getFechaF() {
        return fechaF;
    }

    public void setFechaF(String fechaF) {
        this.fechaF = fechaF;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public boolean isMostrarPagos() {
        return mostrarPagos;
    }

    public void setMostrarPagos(boolean mostrarPagos) {
        this.mostrarPagos = mostrarPagos;
    }

    public boolean isMostrarErrorPagos() {
        return mostrarErrorPagos;
    }

    public void setMostrarErrorPagos(boolean mostrarErrorPagos) {
        this.mostrarErrorPagos = mostrarErrorPagos;
    }

    public Map getComercios() {
        return comercios;
    }

    public void setComercios(Map comercios) {
        this.comercios = comercios;
    }

    public List getPagos() {
        return pagos;
    }

    public void setPagos(List pagos) {
        this.pagos = pagos;
    }

    public void setPagoBO(PagoBO pagoBO) {
        this.pagoBO = pagoBO;
    }

    public List getPagosTel() {
        return pagosTel;
    }

    public void setPagosTel(List pagosTel) {
        this.pagosTel = pagosTel;
    }

    public boolean isMostrarPagosTel() {
        return mostrarPagosTel;
    }

    public void setMostrarPagosTel(boolean mostrarPagosTel) {
        this.mostrarPagosTel = mostrarPagosTel;
    }

    public boolean isMostrarErrorPagosTel() {
        return mostrarErrorPagosTel;
    }

    public void setMostrarErrorPagosTel(boolean mostrarErrorPagosTel) {
        this.mostrarErrorPagosTel = mostrarErrorPagosTel;
    }

    public List getConsTel() {
        return consTel;
    }

    public void setConsTel(List consTel) {
        this.consTel = consTel;
    }

    public boolean isMostrarConsTel() {
        return mostrarConsTel;
    }

    public void setMostrarConsTel(boolean mostrarConsTel) {
        this.mostrarConsTel = mostrarConsTel;
    }

    public boolean isMostrarErrorConsTel() {
        return mostrarErrorConsTel;
    }

    public void setMostrarErrorConsTel(boolean mostrarErrorConsTel) {
        this.mostrarErrorConsTel = mostrarErrorConsTel;
    }

    public String getMsgErrorConsTel() {
        return msgErrorConsTel;
    }

    public void setMsgErrorConsTel(String msgErrorConsTel) {
        this.msgErrorConsTel = msgErrorConsTel;
    }

    public void setConsultaBO(ConsultaBO consultaBO) {
        this.consultaBO = consultaBO;
    }

    public void setSucursalBO(SucursalBO sucursalBO) {
        this.sucursalBO = sucursalBO;
    }

    public String getAdquiriente() {
        return adquiriente;
    }

    public void setAdquiriente(String adquiriente) {
        this.adquiriente = adquiriente;
    }

    public String getFechaArchivoPisa() {
        return fechaArchivoPisa;
    }

    public void setFechaArchivoPisa(String fechaArchivoPisa) {
        this.fechaArchivoPisa = fechaArchivoPisa;
    }

    public String getFechaArchivoTercero() {
        return fechaArchivoTercero;
    }

    public void setFechaArchivoTercero(String fechaArchivoTercero) {
        this.fechaArchivoTercero = fechaArchivoTercero;
    }

    public Map getEstatusPagoMap() {
        return estatusPagoMap;
    }

    public void setEstatusPagoMap(Map estatusPagoMap) {
        this.estatusPagoMap = estatusPagoMap;
    }

    public String getParAutorizacion() {
        return parAutorizacion;
    }

    public void setParAutorizacion(String parAutorizacion) {
        this.parAutorizacion = parAutorizacion;
    }

    public String getParEstatus() {
        return parEstatus;
    }

    public void setParEstatus(String parEstatus) {
        this.parEstatus = parEstatus;
    }

    public String getParTransaccion() {
        return parTransaccion;
    }

    public void setParTransaccion(String parTransaccion) {
        this.parTransaccion = parTransaccion;
    }

    public PagoDTO getTotalesFiltro() {
        return totalesFiltro;
    }

    public void setTotalesFiltro(PagoDTO totalesFiltro) {
        this.totalesFiltro = totalesFiltro;
    }

    public String getFiltros() {
        return filtros;
    }

    public void setFiltros(String filtros) {
        this.filtros = filtros;
    }

    public boolean isMostrarVistaMKT() {
        return mostrarVistaMKT;
    }

    public void setMostrarVistaMKT(boolean mostrarVistaMKT) {
        this.mostrarVistaMKT = mostrarVistaMKT;
    }

    public String getParIdMtPago() {
        return parIdMtPago;
    }

    public void setParIdMtPago(String parIdMtPago) {
        this.parIdMtPago = parIdMtPago;
    }

    public String getParReferenciaBanco() {
        return parReferenciaBanco;
    }

    public void setParReferenciaBanco(String parReferenciaBanco) {
        this.parReferenciaBanco = parReferenciaBanco;
    }

    public String getParCS() {
        return parCS;
    }

    public void setParCS(String parCS) {
        this.parCS = parCS;
    }

    public String getParLib() {
        return parLib;
    }

    public void setParLib(String parLib) {
        this.parLib = parLib;
    }

    public Map getCsMap() {
        return csMap;
    }

    public void setCsMap(Map csMap) {
        this.csMap = csMap;
    }

    public Map getLibreriaMap() {
        return libreriaMap;
    }

    public void setLibreriaMap(Map libreriaMap) {
        this.libreriaMap = libreriaMap;
    }

    public String getParImporte() {
        return parImporte;
    }

    public void setParImporte(String parImporte) {
        this.parImporte = parImporte;
    }

    public String getFechaIO() {
        return fechaIO;
    }

    public void setFechaIO(String fechaIO) {
        this.fechaIO = fechaIO;
    }

    public String getFechaFO() {
        return fechaFO;
    }

    public void setFechaFO(String fechaFO) {
        this.fechaFO = fechaFO;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public void setParCorreo(String parCorreo) {
        this.parCorreo = parCorreo;
    }

    public String getParCorreo() {
        return parCorreo;
    }



}
