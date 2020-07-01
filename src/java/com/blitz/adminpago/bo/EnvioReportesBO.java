/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.dto.PagoAPPEstadisticaDTO;
import com.blitz.adminpago.dto.PagoDTO;
import com.blitz.adminpago.dto.PagoMKTDTO;
import com.blitz.adminpago.dto.PagoPisaIVRDTO;
import com.blitz.adminpago.util.ArchivoVentanilla;
import com.blitz.adminpago.util.ConnectionPropertiesContainer;
import com.blitz.adminpago.util.SendMail_nv;
import com.blitz.adminpago.util.Utilerias;
import com.blitz.adminpago.util.ArchivoUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jxl.*;
import jxl.write.*;
import org.apache.log4j.Logger;

/**
 *
 * @author PGRANDE
 */
public class EnvioReportesBO {
    private PagoBO pagoBO;
    private String rutaArchivo;
    private String rutaArchivoVentanilla;
    private SocketPagoPisaBO socketPagoPisaBO ;
    private LibreriaBO libreriaBO ;
    private BuscaLibPagosPendientesBO buscaLibPagosPendientesBO;

    private String numOfMKT;
    private String numOfAPT;
    private String numOfTWA;

    private Logger log = Logger.getLogger("com.blitz.adminpagoline.bo.EnvioReportesBO");


    public void enviaRepPagosMKT() {


        Utilerias util = new Utilerias();

        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String nombreArchivo = rutaArchivo + "/PagosMKT.xls";
        String lstFechaHoy = util.fechaAyer(null);
        //String lstFechaHoy = "2016/08/15";
        //String nombreArchivo = rutaArchivo + "/PagosMKT_160815.xls";


        generaArchivo(lstFechaHoy, nombreArchivo);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("reporte.cadena.to");
            String asunto = "REPORTE DIARIO DE PAGOS POR CADENA" ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se env&iacute;a reporte diario con el detalle de pagos.";
            sHTML += "<br><br>";


            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);


            enviaRepPagosMKTT11(lstFechaHoy,null);

         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }

    public void enviaRepPagosMKTOndemand(String fecha) {


        Utilerias util = new Utilerias();

        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String nombreArchivo = rutaArchivo + "/PagosMKT.xls";
        String lstFechaHoy = util.fechaAyer(null);
        if ( fecha != null && fecha.length() > 0)
            lstFechaHoy = fecha;
        //String lstFechaHoy = "2016/08/15";
        //String nombreArchivo = rutaArchivo + "/PagosMKT_160815.xls";


        generaArchivo(lstFechaHoy, nombreArchivo);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("reporte.cadena.to");
            String asunto = "REPORTE DIARIO DE PAGOS POR CADENA" ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se env&iacute;a reporte diario con el detalle de pagos.";
            sHTML += "<br><br>";


            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);

         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }



    public void generaArchivo(String fecha, String archivo) {

        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(archivo));
            WritableSheet sheet = workbook.createSheet("Reporte de Pagos", 0);
            int fila = 0;

            List lobDatos = pagoBO.obtenerPagos(fecha, fecha, "MKT", null, null, null, null, null,null,
                    null,null,null,null, null, null, "AMMALAGO", 1 );

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
                encabezado = new Label(6, fila, "CODIGO_RESP");
                sheet.addCell(encabezado);
                encabezado = new Label(7, fila, "TIPO_RESP");
                sheet.addCell(encabezado);
                encabezado = new Label(8, fila, "ADQUIRIENTE");
                sheet.addCell(encabezado);
                encabezado = new Label(9, fila, "TIENDA_TERMINAL");
                sheet.addCell(encabezado);
                encabezado = new Label(10, fila, "SECUENCIA_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(11, fila, "SALDO_VENCIDO");
                sheet.addCell(encabezado);
                encabezado = new Label(12, fila, "MIN_REANUDACION");
                sheet.addCell(encabezado);
                encabezado = new Label(13, fila, "MONTO_PAGADO");
                sheet.addCell(encabezado);
                encabezado = new Label(14, fila, "TRANSACCION");
                sheet.addCell(encabezado);
                encabezado = new Label(15, fila, "CAJA");
                sheet.addCell(encabezado);
                encabezado = new Label(16, fila, "TARJETA");
                sheet.addCell(encabezado);
                encabezado = new Label(17, fila, "TIPOTC");
                sheet.addCell(encabezado);
                encabezado = new Label(18, fila, "REFERENCIA");
                sheet.addCell(encabezado);
                encabezado = new Label(19, fila, "SECURE");
                sheet.addCell(encabezado);
                encabezado = new Label(20, fila, "CLASE_SERV");
                sheet.addCell(encabezado);
                encabezado = new Label(21, fila, "TIPO_TEL");
                sheet.addCell(encabezado);
                encabezado = new Label(22, fila, "LIBRERIA");
                sheet.addCell(encabezado);
                encabezado = new Label(23, fila, "TC_TIPO");
                sheet.addCell(encabezado);
                encabezado = new Label(24, fila, "EMISOR");
                sheet.addCell(encabezado);


                Iterator it = null;

                DateFormat customDateFormat = new DateFormat("dd MMM yyyy hh:mm:ss");
                WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);

                PagoDTO pago;
                it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {
                    try {
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
                    accion = new Label(5, fila, pago.getFechaRespTro());
                    sheet.addCell(accion);
                    accion = new Label(6, fila, pago.getCodigoResp());
                    sheet.addCell(accion);
                    accion = new Label(7, fila, pago.getTipoResp());
                    sheet.addCell(accion);
                    accion = new Label(8, fila, pago.getAdquiriente());
                    sheet.addCell(accion);
                    accion = new Label(9, fila, pago.getTiendaTerm());
                    sheet.addCell(accion);
                    accion = new Label(10, fila, pago.getSecuenciaPisa());
                    sheet.addCell(accion);
                    accion = new Label(11, fila, pago.getSaldoVencido());
                    sheet.addCell(accion);
                    accion = new Label(12, fila, pago.getMinReanudacion());
                    sheet.addCell(accion);
                    accion = new Label(13, fila, pago.getMontoPagado());
                    sheet.addCell(accion);
                    accion = new Label(14, fila, pago.getTransaccion());
                    sheet.addCell(accion);
                    accion = new Label(15, fila, pago.getCaja());
                    sheet.addCell(accion);
                    accion = new Label(16, fila, pago.getNumeroTC());
                    sheet.addCell(accion);
                    accion = new Label(17, fila, pago.getTipoTC());
                    sheet.addCell(accion);
                    accion = new Label(18, fila, pago.getAutorizacion());
                    sheet.addCell(accion);
                    accion = new Label(19, fila, pago.getSecure());
                    sheet.addCell(accion);
                    accion = new Label(20, fila, pago.getClaseServicio());
                    sheet.addCell(accion);
                    accion = new Label(21, fila, pago.getTipotel());
                    sheet.addCell(accion);
                    accion = new Label(22, fila, pago.getLibreria());
                    sheet.addCell(accion);
                    accion = new Label(23, fila, pago.getUnidad());
                    sheet.addCell(accion);
                    accion = new Label(24, fila, pago.getMoneda());
                    sheet.addCell(accion);


                    } catch(Exception e){;}
                    fila++;

                }
                fila++;
            }


            workbook.write();
            workbook.close();



        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }



    }


    public void enviaRepPagosMKTAPT() {


        Utilerias util = new Utilerias();
        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String nombreArchivo = rutaArchivo + "/PagosMKTAPTMen.xls";
        String lstFechaHoy = util.fechaHoy();
        //String lstFechaHoy = "2016/08/15";
        //String nombreArchivo = rutaArchivo + "/PagosMKTAPTMen_160815.xls";


        generaArchivoMKTAPT(lstFechaHoy, nombreArchivo);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("correo.aviso.default");
            String asunto = "REPORTE MENSUAL DE PAGOS TELMEX.COM" ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Favor de bajar el reporte mensual generado en la ruta " + nombreArchivo;
            sHTML += "<br><br>";


            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);

         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }

    public void generaArchivoMKTAPT(String fecha, String archivo) {

        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(archivo));
            WritableSheet sheet = workbook.createSheet("RepPagosMens", 0);
            int fila = 0;

            List lobDatos = pagoBO.obtenerPagosMKTAPT(fecha);

            if ( lobDatos != null )
            {
                Label encabezado = new Label(0, fila, "TELEFONO PAGADO");
                sheet.addCell(encabezado);
                encabezado = new Label(1, fila, "CORREO");
                sheet.addCell(encabezado);
                encabezado = new Label(2, fila, "IMPORTE");
                sheet.addCell(encabezado);
                encabezado = new Label(3, fila, "FECHAESTADO");
                sheet.addCell(encabezado);
                encabezado = new Label(4, fila, "PRINCIPAL");
                sheet.addCell(encabezado);
                encabezado = new Label(5, fila, "ORIGEN");
                sheet.addCell(encabezado);


                Iterator it = null;

                PagoMKTDTO pago;
                it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {

                    pago = (PagoMKTDTO) it.next();
                    Label accion = new Label(0, fila, pago.getTelefono());
                    sheet.addCell(accion);
                    accion = new Label(1, fila, pago.getCorreo());
                    sheet.addCell(accion);
                    accion = new Label(2, fila, pago.getMontoPagar());
                    sheet.addCell(accion);
                    accion = new Label(3, fila, pago.getFechaEstatus());
                    sheet.addCell(accion);
                    accion = new Label(4, fila, pago.getPrincipal());
                    sheet.addCell(accion);
                    accion = new Label(5, fila, pago.getTipoTarjeta());
                    sheet.addCell(accion);

                    fila++;

                }
                fila++;
            }


            workbook.write();
            workbook.close();



        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }



    }


    public void enviaRepPagosMKTConPend() {

        Utilerias util = new Utilerias();
        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String nombreArchivo = rutaArchivo + "/PagosMKTConPendDiario.xls";
        //String lstFechaAyer = util.fechaAyer(null);  //algunos pagos reportados pendientes al dia siguiente se aplican
        String lstFechaAyer = util.fechaHaceDias(1);

        //lstFechaAyer = "2017/12/30";
        //String nombreArchivo = rutaArchivo + "/PagosMKTAPTMen_160815.xls";
        //List pendientes = generaArchivoAPT(lstFechaAyer, nombreArchivo, false);
        List pendientes = generaArchivoAPT(null, nombreArchivo, false,"MKT",0);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("correo.aviso.mkt");
            String asunto = "REPORTE DIARIO DE PAGOS MKT" ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se envia reporte de pagos recibidos desde MKT  del dia " + lstFechaAyer;
            sHTML += "<br><br>";

            if ( pendientes != null && pendientes.size() > 0 ) {

            sHTML += "Pagos sin aplicar:<br><br>";

                Iterator it = pendientes.iterator();
                while(it.hasNext()) {
//                    PagoMKTDTO pago = (PagoMKTDTO) it.next();
//                    sHTML += pago.getTelefono() + "|" + pago.getMontoPagar() + "|"+ pago.getTarjeta() +  "|<br>";
                    String trama = (String) it.next();
                    sHTML += trama +  "<br>";
                }
            }

            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);

            //Enviamos el reporte de TWA
            this.enviaRepPagosTWAConPend();


         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }

    public void enviaRepPagosTWAConPend() {

        Utilerias util = new Utilerias();
        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String nombreArchivo = rutaArchivo + "/PagosTWADiario.xls";
        //String lstFechaAyer = util.fechaAyer(null);  //algunos pagos reportados pendientes al dia siguiente se aplican
        String lstFechaAyer = util.fechaHaceDias(1);

        //lstFechaAyer = "2017/12/30";
        //String nombreArchivo = rutaArchivo + "/PagosMKTAPTMen_160815.xls";
        //List pendientes = generaArchivoAPT(lstFechaAyer, nombreArchivo, false);
        List pendientes = generaArchivoAPT(null, nombreArchivo, false,"TWA",0);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("correo.aviso.mkt");
            String asunto = "REPORTE DIARIO DE PAGOS TWA" ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se envia reporte de pagos recibidos desde TWA  del dia " + lstFechaAyer;
            sHTML += "<br><br>";

            if ( pendientes != null && pendientes.size() > 0 ) {

            sHTML += "Pagos sin aplicar:<br><br>";

                Iterator it = pendientes.iterator();
                while(it.hasNext()) {
//                    PagoMKTDTO pago = (PagoMKTDTO) it.next();
//                    sHTML += pago.getTelefono() + "|" + pago.getMontoPagar() + "|"+ pago.getTarjeta() +  "|<br>";
                    String trama = (String) it.next();
                    sHTML += trama +  "<br>";
                }
            }

            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);


         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }

    //Reporte diario de pagos APT
    public void enviaRepPagosAPT() {

        //Envia cifras pagos Bety
        this.enviaRepPagosAPPEstadistica();


        Utilerias util = new Utilerias();
        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String nombreArchivo = rutaArchivo + "/PagosAPTDiario.xls";
        //String lstFechaAyer = util.fechaAyer(null);  //algunos pagos reportados pendientes al dia siguiente se aplican
        String lstFechaAyer = util.fechaHaceDias(1);
        //lstFechaAyer = "2019/10/29";
        //String nombreArchivo = rutaArchivo + "/PagosMKTAPTMen_160815.xls";
        //List pendientes = generaArchivoAPT(lstFechaAyer, nombreArchivo, false);
        List pendientes = generaArchivoAPT(null, nombreArchivo, false,"APT",0);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("correo.aviso.mkt"); //solo a gente de cobranza sep 2019
            //correo = "grandel@globalhitss.com";
            String asunto = "REPORTE DIARIO DE PAGOS APP" ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se envia reporte de pagos recibidos desde el APP  del dia " + lstFechaAyer;
            sHTML += "<br><br>";

            if ( pendientes != null && pendientes.size() > 0 ) {

            sHTML += "Pagos sin aplicar:<br><br>";

                Iterator it = pendientes.iterator();
                while(it.hasNext()) {
//                    PagoMKTDTO pago = (PagoMKTDTO) it.next();
//                    sHTML += pago.getTelefono() + "|" + pago.getMontoPagar() + "|"+ pago.getTarjeta() +  "|<br>";
                    String trama = (String) it.next();
                    sHTML += trama +  "<br>";
                }
            }

            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);


            //Enviamos el reporte con pendeintes de MKT
            this.enviaRepPagosMKTConPend();
            

         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }




    public List generaArchivoAPT(String fecha, String archivo, boolean generaTrama, String oficina, int soloPendientes) {

        List lobPendientes = new ArrayList();
        List<String> lobPagosVent = new ArrayList();
        int pendientes = 0;
        double importePendientes=0;
        String numOfVentanilla = numOfMKT; //MKT
        boolean servicioConsultaOK = true;
        if ( oficina != null && oficina.startsWith("APT"))
            numOfVentanilla = numOfAPT;
        if ( oficina != null && oficina.startsWith("TWA"))
            numOfVentanilla = numOfTWA;
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(archivo));
            WritableSheet sheet = workbook.createSheet("Pagos", 0);
            int fila = 0;
            WritableSheet sheet2 = workbook.createSheet("Pendientes", 1);
            int fila2 = 0;
            WritableSheet sheet3 = workbook.createSheet("Ventanilla", 2);

            List lobDatos = pagoBO.obtenerPagosAPTPorReferencia(fecha,oficina,soloPendientes); //cero trae todos

            String fechaVentanilla = Utilerias.fechaAyer(null);
            if (fechaVentanilla.indexOf("/") > 0 ) {
                fechaVentanilla = fechaVentanilla.replaceAll("/", "");
            }
            String archivoVentanilla = rutaArchivoVentanilla + "/TRANTMX." + oficina + ".A0376.PV.D"+ fechaVentanilla;

            if ( lobDatos != null  && lobDatos.size() > 0)
            {
                Label encabezado = new Label(0, fila, "ID");
                sheet.addCell(encabezado);
                encabezado = new Label(1, fila, "TELEFONO ");
                sheet.addCell(encabezado);
                encabezado = new Label(2, fila, "TIPO");
                sheet.addCell(encabezado);
                encabezado = new Label(3, fila, "ESTADO");
                sheet.addCell(encabezado);
                encabezado = new Label(4, fila, "FECHAESTADO");
                sheet.addCell(encabezado);
                encabezado = new Label(5, fila, "HORA");
                sheet.addCell(encabezado);
                encabezado = new Label(6, fila, "TARJETA");
                sheet.addCell(encabezado);
                encabezado = new Label(7, fila, "IMPORTE");
                sheet.addCell(encabezado);
                encabezado = new Label(8, fila, "REFERENCIA");
                sheet.addCell(encabezado);
                encabezado = new Label(9, fila, "TIPOTARJETA");
                sheet.addCell(encabezado);
                encabezado = new Label(10, fila, "ID_PAGO");
                sheet.addCell(encabezado);
                encabezado = new Label(11, fila, "MONTO_PAGADO");
                sheet.addCell(encabezado);
                encabezado = new Label(12, fila, "ESTATUS");
                sheet.addCell(encabezado);
                encabezado = new Label(13, fila, "LIBRERIA");
                sheet.addCell(encabezado);
                encabezado = new Label(14, fila, "CAJA");
                sheet.addCell(encabezado);
                encabezado = new Label(15, fila, "TOKEN/FFACT");
                sheet.addCell(encabezado);
                encabezado = new Label(16, fila, "EMISOR");
                sheet.addCell(encabezado);
                encabezado = new Label(17, fila, "FECHA_REP_PISA");
                sheet.addCell(encabezado);
                encabezado = new Label(18, fila, "FECHA_APLICACION_PAGO");
                sheet.addCell(encabezado);


                //Encabezados hoja pendientes
                Label encabezado2 = new Label(0, fila2, "ID");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(1, fila2, "TELEFONO ");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(2, fila2, "TIPO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(3, fila2, "ESTADO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(4, fila2, "FECHAESTADO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(5, fila2, "HORA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(6, fila2, "TARJETA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(7, fila2, "IMPORTE");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(8, fila2, "REFERENCIA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(9, fila2, "TIPOTARJETA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(10, fila2, "ID_PAGO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(11, fila2, "MONTO_PAGADO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(12, fila2, "ESTATUS");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(13, fila2, "LIBRERIA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(14, fila2, "CAJA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(15, fila2, "TOKEN/FFACT");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(16, fila, "FECHA_REP_PISA");
                sheet2.addCell(encabezado2);



                //Encabezados hoja pendientes
                Label encabezado3 = new Label(0, fila2, "VENTANILLA");
                sheet3.addCell(encabezado3);
                fila2++;


                Iterator it = null;

                PagoMKTDTO pago;
                it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {

                    boolean esToken = false;
                    pago = (PagoMKTDTO) it.next();
                    Label accion = new Label(0, fila, pago.getIdPago());
                    sheet.addCell(accion);
                    accion = new Label(1, fila, pago.getTelefono());
                    sheet.addCell(accion);
                    accion = new Label(2, fila, pago.getTipoTelefono());
                    sheet.addCell(accion);
                    accion = new Label(3, fila, pago.getEstatus());
                    sheet.addCell(accion);
                    accion = new Label(4, fila, pago.getFechaEstatus());
                    sheet.addCell(accion);
                    accion = new Label(5, fila, pago.getNombre());
                    sheet.addCell(accion);
                    
                    accion = new Label(6, fila, pago.getTarjeta());
                    sheet.addCell(accion);
                    accion = new Label(7, fila, pago.getMontoPagar());
                    sheet.addCell(accion);
                    accion = new Label(8, fila, pago.getReferencia());
                    sheet.addCell(accion);
                    accion = new Label(9, fila, pago.getTipoTarjeta());
                    sheet.addCell(accion);
                    accion = new Label(10, fila, pago.getPrincipal());
                    sheet.addCell(accion);
                    accion = new Label(11, fila, pago.getOtraLinea());
                    sheet.addCell(accion);
                    accion = new Label(12, fila, pago.getEstatusDes());
                    sheet.addCell(accion);
                    accion = new Label(13, fila, pago.getSecure());
                    sheet.addCell(accion);
                    accion = new Label(14, fila, pago.getCorreo());
                    sheet.addCell(accion);
                    accion = new Label(15, fila, pago.getFechaSol());
                    sheet.addCell(accion);
                    
                    
                    if (pago.getTipoTarjeta() != null && pago.getTipoTarjeta().startsWith("A")) {
                        //Pueden entrar por el esquema anterior o sea sin mit
                        if ( pago.getFechaSol()!= null ) {
                            String number = pago.getFechaSol().substring(0,1);
                            if (number.matches("[0-9]+")) {
                                accion = new Label(16, fila, "TOKEN");
                                sheet.addCell(accion);
                                esToken = true;
                            }
                            else {
                                esToken = false;
                                accion = new Label(16, fila, "TARJETA");
                                sheet.addCell(accion);
                            }
                        }
                        else {
                                esToken = false;
                                accion = new Label(16, fila, "TARJETA");
                                sheet.addCell(accion);
                            
                        }
                    }
                    else if ( pago.getFechaSol()!= null ) {
                        String number = pago.getFechaSol().substring(0,1);
                        if (number.matches("[0-9]+")) {
                            accion = new Label(16, fila, "TOKEN");
                            sheet.addCell(accion);
                            esToken = true;
                        }
                    }

                    
                    accion = new Label(17, fila, pago.getNombreSesion()); // fecha de reporte de pendientes a pisa
                    sheet.addCell(accion);
                    accion = new Label(18, fila, pago.getFechaEdoPisa()); // fecha estado. en la que se aplico el pago
                    sheet.addCell(accion);
                    
                    
                    //Escribimos los pendientes en una hoja aparte

                    if ( pago.getEstatusDes() == null || pago.getEstatusDes().length()== 0
                            || pago.getEstatusDes().startsWith("AS")
                            || pago.getEstatusDes().startsWith("RI")
                            || pago.getEstatusDes().startsWith("TI")
                            || pago.getEstatusDes().startsWith("IV")
                            || pago.getEstatus().startsWith("T61")
                            || pago.getEstatus().startsWith("T62") ) {


                        //Obtenemos la libreria si no la tiene
                        String libreria = pago.getSecure();
                        if (libreria == null || libreria.trim().length() <= 0) {
                            libreria = this.buscaLibPagosPendientesBO.obtenerLibreriaCJPobla(pago.getTelefono());
                            if ( libreria != null )
                                pago.setSecure(libreria);
                        }

                        //Validamos si ya existe con el nuevo servicio de pisa Sep 2019

                        int existePago = 0;
                        if ( servicioConsultaOK = true ) 
                            existePago = this.buscaLibPagosPendientesBO.consultaPago(pago, oficina, esToken);
                                    
                                    
                        if ( existePago == -1 ) //No esta disponible el servicio de validacion lo omitimos para los posteriores
                            servicioConsultaOK = false;
                        
                        if ( existePago == 1 ) { //el pago si existe
                            //Es necesario abanderarlo
                            log.info("Pago si se aplico.. se abandera como atendido: " +  pago.getIdPago());
                            //Se marca como AP en estatus de pisa y la fecha
                            pagoBO.actEstatusPisaManual(pago.getPrincipal());
                            accion = new Label(12, fila, "AP");
                            sheet.addCell(accion);
                            accion = new Label(17, fila, Utilerias.fechaHoy()+":ConsOK"); // fecha de reporte de pendientes a pisa
                            sheet.addCell(accion);


                        }
                        else {

                            if ( pendientes == 0 ) {
                                //Cifras de control para archivo ventanilla

                                String header = ArchivoVentanilla.formateaHead(fechaVentanilla, numOfVentanilla);
                                lobPagosVent.add(0, header); //0
                                Label accion3 = new Label(0, pendientes+1, header);
                                sheet3.addCell(accion3);


                            }
                            pendientes++;
                            importePendientes += Double.parseDouble(pago.getMontoPagar());


                            //Cuerpo del correo
                            String trama = pago.getTelefono() + "|"
                                    + pago.getTarjeta() + "|"
                                    + pago.getMontoPagar()+  "|"
                                    + pago.getReferencia()+  "|" ;
                            if ( esToken == true ) {
                                trama += pago.getFechaSol();
                                trama += "|TOKEN";
                            }
                            else
                                trama += pago.getTarjeta();

                            lobPendientes.add(trama);


                            //Escribimos los pagos en la hoja 2
                            Label accion2 = new Label(0, fila2, pago.getIdPago());
                            sheet2.addCell(accion2);
                            accion2 = new Label(1, fila2, pago.getTelefono());
                            sheet2.addCell(accion2);
                            accion2 = new Label(2, fila2, pago.getTipoTelefono());
                            sheet2.addCell(accion2);
                            accion2 = new Label(3, fila2, pago.getEstatus());
                            sheet2.addCell(accion2);
                            accion2 = new Label(4, fila2, pago.getFechaEstatus());
                            sheet2.addCell(accion2);
                            accion2 = new Label(5, fila2, pago.getNombre());
                            sheet2.addCell(accion2);
                            if ( esToken == true )
                                accion2 = new Label(6, fila2, pago.getFechaSol()); //El token
                            else
                                accion2 = new Label(6, fila2, pago.getTarjeta());
                            sheet2.addCell(accion2);
                            accion2 = new Label(7, fila2, pago.getMontoPagar());
                            sheet2.addCell(accion2);
                            accion2 = new Label(8, fila2, pago.getReferencia());
                            sheet2.addCell(accion2);
                            accion2 = new Label(9, fila2, pago.getTipoTarjeta());
                            sheet2.addCell(accion2);
                            accion2 = new Label(10, fila2, pago.getPrincipal());
                            sheet2.addCell(accion2);
                            accion2 = new Label(11, fila2, pago.getOtraLinea());
                            sheet2.addCell(accion2);
                            accion2 = new Label(12, fila2, pago.getEstatusDes());
                            sheet2.addCell(accion2);
                            accion2 = new Label(13, fila2, pago.getSecure());
                            sheet2.addCell(accion2);
                            accion2 = new Label(14, fila2, pago.getCorreo());
                            sheet2.addCell(accion2);

                            if ( esToken == true )
                                accion2 = new Label(15, fila2, "TOKEN");
                            else
                                accion2 = new Label(15, fila2, "TARJETA");
                            sheet2.addCell(accion2);
                            accion2 = new Label(16, fila2, pago.getNombreSesion()); // fecha de reporte de pendientes a pisa
                            sheet2.addCell(accion2);


                            fila2++;

                            //Escribimos la trama en la hoja3
                            //String trama2 = obtenerTramaPagoPendiente(pago, esToken);
                            //Preparamos el registro del pago ventanilla
                            String trama2 = ArchivoVentanilla.formateaDetail(pago, numOfVentanilla);
                            lobPagosVent.add(trama2);
                            Label accion3 = new Label(0, fila2, trama2);
                            sheet3.addCell(accion3);

                        }
                    }

                    fila++;

                }
                fila++;

            }
            else 
                log.info("Sin datos");





            //Solo enviamos el correo si la validacion de consulta estaba activa
            if ( pendientes > 0 ) {
            //Cifras de control para archivo ventanilla
                String trail = ArchivoVentanilla.formateaTrail(String.valueOf(pendientes),importePendientes, numOfVentanilla);
                lobPagosVent.add(trail);
                Label accion4 = new Label(0, fila2+1, trail);
                sheet3.addCell(accion4);
                
                workbook.write();
                workbook.close();

                if ( servicioConsultaOK == true) {

                    double MEG = (Math.pow(1024, 2));
                    String nomArchivo = rutaArchivoVentanilla + "/TRANTMX." + oficina + ".A0376.PV.D"+ fechaVentanilla;
                    ArchivoUtil.writeBuffered(lobPagosVent, (int)(Math.pow(1024, 2)), nomArchivo);

                    try {
                    //Enviamos por correo el archivo por ventanilla
                        String cifras = "Archivo a recuperar: " + nomArchivo + " <br/> "  +
                                "Total de pagos: " + pendientes + " <br/> "  +
                                "Importe total: " +  importePendientes + " <br/> "  ;

                        envioCorreoVentanilla(oficina,nomArchivo,fechaVentanilla, cifras);

                    }
                    catch(Exception e) {
                        log.info("Error al enviar archivo ventanilla:" + e.toString());
                    }

                }
                else { //Notificamos que no se pudo validar los pagos
                    envioCorreoSinVentanilla(oficina,fechaVentanilla);
                    
                }


            }
            else {
                workbook.write();
                workbook.close();

            }




        } catch (Exception ex) {
            //System.out.println(ex)
            log.equals("generarExcel: " + ex);
        }

        return lobPendientes;

    }

    public void envioCorreoVentanilla(String oficina, String archivo, String fecha, String texto) {

         log.info("Enviando correo ventanilla: " + oficina);

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("reporteventanilla.to"); //solo a gente de cobranza sep 2019
            String asunto = "REPORTE DIARIO DE PAGOS POR VENTANILLA " + oficina ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se envia archivo ventanilla con pagos recibidos el dia " + fecha;
            sHTML += "<br><br>";
            sHTML += texto;
            sHTML += "<br><br>";

            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, archivo);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo + " archivo:" + archivo);


         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + archivo);
         }


    }

    public void envioCorreoSinVentanilla(String oficina, String fecha) {

         log.info("Enviando correo ventanilla: " + oficina);

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("reporteventanilla.to"); //solo a gente de cobranza sep 2019
            String asunto = "REPORTE DIARIO DE PAGOS POR VENTANILLA OMITIDO" + oficina ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se informa que no fu� posible la validaci�n de pagos del d�a " + fecha;
            sHTML += "<br>No se gener� elarchivo correspondiente<br>";
            sHTML += "<br><br>";

            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, "");
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo );


         }
         catch(Exception e) {
        	 log.error("Error en enviar el correo:" + e.toString());
         }


    }
    
    public List generaArchivoSoloPendientes(String fecha, String archivo, boolean generaTrama, String oficina) {

        List lobPendientes = new ArrayList();
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(archivo));
            WritableSheet sheet2 = workbook.createSheet("Pendientes", 0);
            int fila2 = 0;

            List lobDatos = pagoBO.obtenerPagosAPTPorReferenciaCorte(fecha,oficina);

            if ( lobDatos != null )
            {

                //Encabezados hoja pendientes
                Label encabezado2 = new Label(0, fila2, "ID");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(1, fila2, "TELEFONO ");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(2, fila2, "TIPO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(3, fila2, "ESTADO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(4, fila2, "FECHAESTADO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(5, fila2, "HORA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(6, fila2, "TARJETA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(7, fila2, "IMPORTE");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(8, fila2, "REFERENCIA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(9, fila2, "TIPOTARJETA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(10, fila2, "ID_PAGO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(11, fila2, "MONTO_PAGADO");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(12, fila2, "ESTATUS");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(13, fila2, "LIBRERIA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(14, fila2, "CAJA");
                sheet2.addCell(encabezado2);
                encabezado2 = new Label(15, fila2, "TOKEN/FFACT");
                sheet2.addCell(encabezado2);

                fila2++;

                Iterator it = null;

                StringBuffer sbIdentificadores = new StringBuffer();

                PagoMKTDTO pago;
                int existePago = 0;
                it = lobDatos.iterator();
                while (it.hasNext()) {

                    boolean esToken = false;
                    pago = (PagoMKTDTO) it.next();


                    if ( pago.getEstatus() != null && pago.getEstatus().startsWith("T54") == false ) {
                        if (pago.getTipoTarjeta() != null && pago.getTipoTarjeta().startsWith("A")) {
                            
                            
                            if ( pago.getFechaSol()!= null ) {
                                String number = pago.getFechaSol().substring(0,1);
                                if (number.matches("[0-9]+"))
                                    esToken = true;
                                else
                                    esToken = false;

                            }
                            else
                               esToken = false;
                        
                        }
                        else if ( pago.getFechaSol()!= null ) {
                            String number = pago.getFechaSol().substring(0,1);
                            if (number.matches("[0-9]+")) {
                                esToken = true;
                            }
                        }

                    //Escribimos los pendientes en una hoja aparte
                    if ( pago.getEstatusDes() == null || pago.getEstatusDes().length()== 0
                            || pago.getEstatusDes().startsWith("AS")
                            || pago.getEstatusDes().startsWith("RI")
                            || pago.getEstatusDes().startsWith("TI")
                            || pago.getEstatusDes().startsWith("IV")
                            || pago.getEstatus().startsWith("T61")
                            || pago.getEstatus().startsWith("T62") ) {


                        //Obtenemos la libreria si no la tiene
                        String libreria = pago.getSecure();
                        if (libreria == null || libreria.trim().length() <= 0) {
                            libreria = this.buscaLibPagosPendientesBO.obtenerLibreriaCJPobla(pago.getTelefono());
                            if ( libreria != null )
                                pago.setSecure(libreria);
                        }

                        //Validamos si ya existe con el nuevo servicio de pisa Sep 2019
                        
                        existePago = this.buscaLibPagosPendientesBO.consultaPago(pago, oficina, esToken);
                        if ( existePago == 1 ) { //el pago si existe
                            //Es necesario abanderarlo
                            log.info("Pago si se aplico.. se abandera como atendido: " +  pago.getIdPago());
                            //Se marca como AP en estatus de pisa y la fecha
                            pagoBO.actEstatusPisaManual(pago.getPrincipal());

                        }
                        else  { //no existe el pago o el servicio de consulta no esta arriba

                            //Guardamos el id_pgo
                            sbIdentificadores.append(pago.getPrincipal());
                            sbIdentificadores.append(",");

                            //Cuerpo del correo
                            String trama = pago.getTelefono() + "|"
                                    + pago.getTarjeta() + "|"
                                    + pago.getMontoPagar()+  "|"
                                    + pago.getReferencia()+  "|" ;
                            if ( esToken == true ) {
                                trama += pago.getFechaSol();
                                trama += "|TOKEN";
                            }
                            else
                                trama += pago.getTarjeta();

                            lobPendientes.add(trama);


                            //Escribimos los pagos en la hoja 2
                            Label accion2 = new Label(0, fila2, pago.getIdPago());
                            sheet2.addCell(accion2);
                            accion2 = new Label(1, fila2, pago.getTelefono());
                            sheet2.addCell(accion2);
                            accion2 = new Label(2, fila2, pago.getTipoTelefono());
                            sheet2.addCell(accion2);
                            accion2 = new Label(3, fila2, pago.getEstatus());
                            sheet2.addCell(accion2);
                            accion2 = new Label(4, fila2, pago.getFechaEstatus());
                            sheet2.addCell(accion2);
                            accion2 = new Label(5, fila2, pago.getNombre());
                            sheet2.addCell(accion2);
                            if ( esToken == true )
                                accion2 = new Label(6, fila2, pago.getFechaSol()); //El token
                            else
                                accion2 = new Label(6, fila2, pago.getTarjeta());
                            sheet2.addCell(accion2);
                            accion2 = new Label(7, fila2, pago.getMontoPagar());
                            sheet2.addCell(accion2);
                            accion2 = new Label(8, fila2, pago.getReferencia());
                            sheet2.addCell(accion2);
                            accion2 = new Label(9, fila2, pago.getTipoTarjeta());
                            sheet2.addCell(accion2);
                            accion2 = new Label(10, fila2, pago.getPrincipal()); //Id_pago en pol_02_t_pago
                            sheet2.addCell(accion2);
                            accion2 = new Label(11, fila2, pago.getOtraLinea());
                            sheet2.addCell(accion2);
                            accion2 = new Label(12, fila2, pago.getEstatusDes());
                            sheet2.addCell(accion2);

                            accion2 = new Label(13, fila2, libreria);
                            sheet2.addCell(accion2);

                            accion2 = new Label(14, fila2, pago.getCorreo());
                            sheet2.addCell(accion2);


                            if ( esToken == true )
                                accion2 = new Label(15, fila2, "TOKEN");
                            else
                                accion2 = new Label(15, fila2, "TARJETA");
                            sheet2.addCell(accion2);


                            fila2++;


                        }//fin consulta de pago

                    }

                    } //fin if T54
                }

                //Actualizamos los estatus en pol_02_t_pago fecha_concilmanual y estatus = AP
                if ( sbIdentificadores != null && sbIdentificadores.toString() != null &&
                        sbIdentificadores.toString().length() > 0) {

                    pagoBO.actEstatusPisaManual(sbIdentificadores.substring(0, sbIdentificadores.length()-1));
                    
                }


            }


            workbook.write();
            workbook.close();



        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }

        return lobPendientes;

    }



    
    private String obtenerTramaPagoPendiente(PagoMKTDTO pagoAPT, boolean esToken) {

        String trama = null;

        if ( pagoAPT != null ) {

            PagoPisaIVRDTO pago = new PagoPisaIVRDTO();
            pago.setAdeudo("0");
            pago.setTicket(pagoAPT.getIdPago());
            pago.setTelefono(pagoAPT.getTelefono());
            pago.setFechaPago(pagoAPT.getFechaEstatus());//yyyymmdd
            pago.setOficinaComercial("APT"); //APT
            pago.setCaja("APT"+pagoAPT.getFechaEstatus().substring(2)); //APTaammdd
            pago.setImportePago(pagoAPT.getMontoPagar());
            pago.setHoraPago(pagoAPT.getNombre());
            pago.setNumTarjeta(pagoAPT.getTarjeta());
            if ( esToken == true)
                pago.setNumTarjeta(pagoAPT.getFechaSol()); //El token
            if ( pagoAPT.getReferencia() != null && pagoAPT.getReferencia().length()>0 )
                pago.setAutorizacion(pagoAPT.getReferencia());
            else
                pago.setAutorizacion("");

            pago.setFechaVenc("0000"); //no la conocemos
            if ( pagoAPT.getSecure() != null && pagoAPT.getSecure().length() > 0)
                pago.setBiblioteca(pagoAPT.getSecure());
            //else ya no hay conexion a pisa en esta version. no se usa el metodo
                //pago.setBiblioteca(libreriaBO.obtenerLibreria(pagoAPT.getTelefono()));

            try {
            trama = socketPagoPisaBO.formatoBuffer07String(pago);
            }catch(Exception e1) {;}

        }

        return trama;
    }

    public void generarReporteSoloPendientesOnDemand(String fecha, String correo) {
        //Reporte para gente de pisa

        obtenerReporteSoloPendientes("MKT", fecha, correo);
        obtenerReporteSoloPendientes("APT", fecha, correo);

    }


    public void generarReporteSoloPendientes() {
        //Reporte para gente de pisa

        Utilerias util = new Utilerias();
        String fecha = util.fechaHaceDias(0);

        obtenerReporteSoloPendientes("MKT", fecha, null);
        obtenerReporteSoloPendientes("APT", fecha, null);

    }


    public void obtenerReporteSoloPendientes(String oficina, String fecha, String enviaCorreo) {

        String fechaArch = "Diario";
        if ( fechaArch != null ) {
            fechaArch = fecha.replaceAll("/", "_");
            fechaArch = fechaArch.replaceAll("'", "");
        }

        String nombreArchivo = rutaArchivo + "/Pendientes"+oficina+"_"+ fechaArch + ".xls";
        String lstFechaAyer = fecha;

        List pendientes = generaArchivoSoloPendientes(lstFechaAyer, nombreArchivo, false, oficina);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("correo.aviso.app");
            String asunto = "REPORTE DIARIO DE PAGOS PENDIENTES " + oficina ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se envia reporte de pagos pendientes recibidos desde el " + oficina + " del dia " + lstFechaAyer;
            sHTML += "<br><br>";

            if ( pendientes != null && pendientes.size() > 0 ) {

            sHTML += "Pagos sin aplicar:<br><br>";

                Iterator it = pendientes.iterator();
                while(it.hasNext()) {
//                    PagoMKTDTO pago = (PagoMKTDTO) it.next();
//                    sHTML += pago.getTelefono() + "|" + pago.getMontoPagar() + "|"+ pago.getTarjeta() +  "|<br>";
                    String trama = (String) it.next();
                    sHTML += trama +  "<br>";
                }

                sHTML += "</HTML>";

            }
            SendMail_nv send = null;
            if ( enviaCorreo != null && enviaCorreo.length() > 0) {
                send = new SendMail_nv(remitente, enviaCorreo, asunto, sHTML, nombreArchivo);
                send.enviaMailHTML();
                log.info("Correo enviado:" + enviaCorreo + " archivo:" + nombreArchivo);
            }
            else {
                send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
                send.enviaMailHTML();
                log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);
             }

         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }

    public void enviaRepPagosAPTOndemand(String fecha, String oficina, String enviaCorreo,int soloPendientes) {


        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String fechaArch = "Diario";
        if ( fechaArch != null ) {
            fechaArch = fecha.replaceAll("/", "_");
            fechaArch = fechaArch.replaceAll("'", "");
        }

        String nombreArchivo = rutaArchivo + "/Pagos"+oficina+"_"+ fechaArch + ".xls";
        String lstFechaAyer = fecha;

        List pendientes = generaArchivoAPT(lstFechaAyer, nombreArchivo, false, oficina,soloPendientes);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("correo.aviso.app");
            String asunto = "REPORTE DIARIO DE PAGOS " + oficina ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se envia reporte de pagos recibidos desde el " + oficina + " del dia " + lstFechaAyer;
            sHTML += "<br><br>";

            if ( pendientes != null && pendientes.size() > 0 ) {

            sHTML += "Pagos sin aplicar:<br><br>";

                Iterator it = pendientes.iterator();
                while(it.hasNext()) {
//                    PagoMKTDTO pago = (PagoMKTDTO) it.next();
//                    sHTML += pago.getTelefono() + "|" + pago.getMontoPagar() + "|"+ pago.getTarjeta() +  "|<br>";
                    String trama = (String) it.next();
                    sHTML += trama +  "<br>";
                }

                sHTML += "</HTML>";

            }
            SendMail_nv send = null;
            if ( enviaCorreo != null && enviaCorreo.length() > 0) {
                send = new SendMail_nv(remitente, enviaCorreo, asunto, sHTML, nombreArchivo);
                send.enviaMailHTML();
                log.info("Correo enviado:" + enviaCorreo + " archivo:" + nombreArchivo);
            }
            else {
                send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
                send.enviaMailHTML();
                log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);
             }

         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }

        //Metodo para enviar reporte estadistico diario de los pagos APP
    public void enviaRepPagosAPPEstadistica() {

        Utilerias util = new Utilerias();
        String correo="";
         try {

        String lstFechaAyer = util.fechaAyer("EEEEEEEEE dd 'de' MMMMM 'del' yyyy");
        List<PagoAPPEstadisticaDTO> estadistica=pagoBO.obtenerPagosAPPEstadistica();
        String sHTML="";

         log.info("Enviando correo:");

            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            correo = props.getProperty("correo.aviso.default");
            String asunto = "REPORTE DIARIO DE PAGOS APP-ESTAD�STICAS" ;
            sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se envia reporte de pagos recibidos desde el APP, estad�sticas del dia " + lstFechaAyer;
            sHTML += "<br><br>";

            sHTML +="<table>";

            sHTML +="<tr><td>TIPO</td><td>TOTAL</td><td>IMPORTE</td><td>ESTADO</td></tr>";
            
            if (estadistica!=null){
                Iterator it = estadistica.iterator();
                while (it.hasNext()) {
                    PagoAPPEstadisticaDTO dto = (PagoAPPEstadisticaDTO)it.next();
                        sHTML +="<tr>";
                        sHTML += "<td>"+dto.getTipo()+"</td>";
                        sHTML += "<td>"+dto.getTotalPagos()+"</td>";
                        sHTML += "<td>"+dto.getImporte()+"</td>";
                        sHTML += "<td>"+dto.getEstado()+"</td>";
                        sHTML +="</tr>";
                }
             }
            else{
            sHTML +="<tr>";
            sHTML +="<td>"+"0"+"</td>"+"<td>"+"0"+"</td>"+"<td>"+"0"+"</td><td>"+"0"+"</td>";
            sHTML +="</tr>";
            }

            sHTML +="</table>";
            sHTML += "</HTML>";
            log.info("HTML:" + sHTML);


            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo);

         }
         catch(Exception e) {
             e.printStackTrace();
        	 log.error("Error en enviar el correo a " + correo);
         }

    }


    public void enviaRepPagosMKTT11(String fecha, String correoD) {


        Utilerias util = new Utilerias();

        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String nombreArchivo = rutaArchivo + "/T11MKT.xls";
        String lstFechaHoy = util.fechaAyer(null);
        if ( fecha != null && fecha.length() > 0)
            lstFechaHoy = fecha;
        //String lstFechaHoy = "2016/08/15";
        //String nombreArchivo = rutaArchivo + "/PagosMKT_160815.xls";


        generaArchivoT11(lstFechaHoy, nombreArchivo);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("reporte.cadena.to");
            String asunto = "REPORTE DIARIO DE PAGOS T11" ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se env&iacute;a reporte diario con el detalle de pagos.";
            sHTML += "<br><br>";

            SendMail_nv send = null;
            if ( correoD != null && correoD.length() > 0) {

                send = new SendMail_nv(remitente, correoD, asunto, sHTML, nombreArchivo);
                send.enviaMailHTML();
                log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);
             }
            else {
                send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
                send.enviaMailHTML();
                log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);
            }

         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }


    public void generaArchivoT11(String fecha, String archivo) {

        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(archivo));
            WritableSheet sheet = workbook.createSheet("T11", 0);
            int fila = 0;
            WritableSheet sheet2 = workbook.createSheet("Unicos", 1);

            List lobDatos = pagoBO.obtenerPagosT11(fecha, "MKT");
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
                encabezado = new Label(4, fila, "TIPOTC");
                sheet.addCell(encabezado);
                encabezado = new Label(5, fila, "OFICINACOMERCIAL");
                sheet.addCell(encabezado);

                Iterator it = null;

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
                    accion = new Label(4, fila, pago.getTipoTC());
                    sheet.addCell(accion);
                    accion = new Label(5, fila, pago.getTipoIngreso());
                    sheet.addCell(accion);

                    fila++;

                }
                fila++;
            }

            fila = 0;

            lobDatos = pagoBO.obtenerPagosT11TelUnicos(fecha, "MKT");
            if ( lobDatos != null )
            {
                //Encabezados hoja unicos
                Label encabezado3 = new Label(0, fila, "TELEFONO");
                sheet2.addCell(encabezado3);

                Iterator it = null;

                PagoDTO pago;
                it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {

                    pago = (PagoDTO) it.next();
                    Label accion = new Label(0, fila, String.valueOf(pago.getTelefono()));
                    sheet2.addCell(accion);
                    fila++;

                }
                fila++;
            }


            workbook.write();
            workbook.close();



        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }

    }

    public void enviaRepPagosBSN() {

        enviaRepPagosCadena("BSN", null);
        
    }

    public void enviaRepPagosCadenaOndemand(String fecha, String comercio) {
        if ( comercio != null && fecha != null)
            enviaRepPagosCadena(comercio, fecha);
    }


    public void enviaRepPagosCadena(String pstComercio, String fecha) {


        Utilerias util = new Utilerias();

        //String nombreArchivo = rutaArchivo + "/PagosMKT_" + util.fechaAyer("yyMMdd") + ".xls";
        String nombreArchivo = rutaArchivo + "/Pagos"+ pstComercio + ".xls";
        String lstFechaHoy = util.fechaAyer(null);
        if ( fecha != null )
            lstFechaHoy = fecha;
        //String lstFechaHoy = "2016/08/15";
        //String nombreArchivo = rutaArchivo + "/PagosMKT_160815.xls";

        if (lstFechaHoy != null && lstFechaHoy.indexOf("'") != -1)
            lstFechaHoy = lstFechaHoy.replaceAll("'", "");


        generaArchivoCadena(lstFechaHoy, nombreArchivo, pstComercio);

         log.info("Enviando correo:");

         try {
            ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
            String remitente = props.getProperty("reporte.from");
            String correo = props.getProperty("reporte.cadena.BSN.to");
            String asunto = "REPORTE DIARIO DE PAGOS POR CADENA" ;
            String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
            sHTML += "Estimado Usuario:</b>";
            sHTML += "<br><br>Se env&iacute;a reporte diario con el detalle de pagos.";
            sHTML += "<br><br>";


            SendMail_nv send = new SendMail_nv(remitente, correo, asunto, sHTML, nombreArchivo);
            send.enviaMailHTML();
            log.info("Correo enviado:" + correo + " archivo:" + nombreArchivo);


         }
         catch(Exception e) {
        	 log.error("Error en enviar el archivo por correo:" + nombreArchivo);
         }

    }

    public void generaArchivoCadena(String fecha, String archivo, String pstComercio) {

        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(archivo));
            WritableSheet sheet = workbook.createSheet("Reporte de Pagos", 0);
            int fila = 0;

            List lobDatos = pagoBO.obtenerPagos(fecha, fecha, pstComercio, null, null, null, null, null, null,
                    null, null,null, null, null, null, null, 0);


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


        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }



    }



    /**
     * @param pagoBO the pagoBO to set
     */
    public void setPagoBO(PagoBO pagoBO) {
        this.pagoBO = pagoBO;
    }

    /**
     * @param rutaArchivo the rutaArchivo to set
     */
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * @param socketPagoPisaBO the socketPagoPisaBO to set
     */
    public void setSocketPagoPisaBO(SocketPagoPisaBO socketPagoPisaBO) {
        this.socketPagoPisaBO = socketPagoPisaBO;
    }

    /**
     * @param libreriaBO the libreriaBO to set
     */
    public void setLibreriaBO(LibreriaBO libreriaBO) {
        this.libreriaBO = libreriaBO;
    }

    /**
     * @param buscaLibPagosPendientesBO the buscaLibPagosPendientesBO to set
     */
    public void setBuscaLibPagosPendientesBO(BuscaLibPagosPendientesBO buscaLibPagosPendientesBO) {
        this.buscaLibPagosPendientesBO = buscaLibPagosPendientesBO;
    }

    /**
     * @param rutaArchivoVentanilla the rutaArchivoVentanilla to set
     */
    public void setRutaArchivoVentanilla(String rutaArchivoVentanilla) {
        this.rutaArchivoVentanilla = rutaArchivoVentanilla;
    }

    /**
     * @return the numOfMKT
     */
    public String getNumOfMKT() {
        return numOfMKT;
    }

    /**
     * @param numOfMKT the numOfMKT to set
     */
    public void setNumOfMKT(String numOfMKT) {
        this.numOfMKT = numOfMKT;
    }

    /**
     * @return the numOfAPT
     */
    public String getNumOfAPT() {
        return numOfAPT;
    }

    /**
     * @param numOfAPT the numOfAPT to set
     */
    public void setNumOfAPT(String numOfAPT) {
        this.numOfAPT = numOfAPT;
    }

    /**
     * @param numOfTWA the numOfTWA to set
     */
    public void setNumOfTWA(String numOfTWA) {
        this.numOfTWA = numOfTWA;
    }



}
