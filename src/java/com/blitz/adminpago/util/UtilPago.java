/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.util;
import com.blitz.adminpago.dto.ConsultaDTO;
import com.blitz.adminpago.dto.PagoConciliacionDTO;
import com.blitz.adminpago.dto.PagoDTO;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;



/**
 *
 * @author PGRANDE
 */
public class UtilPago {


    private Logger log = Logger.getLogger("com.blitz.adminpagoline.util.UtilPago");

    // Invocaciones WS al puerto 8010

    //Se usa en el trigger de consultas programadas a las librerias para asegurar conexion
    public String enviaPeticionConsulta(String pstXMLConsulta)
    {
        String respuesta = null;
        /*
        try {

            //EnvioPeticionWSPago enviaPeticion = new EnvioPeticionWSPago();
            respuesta = pagows.EnvioPeticionWSPago.enviaPeticionWS(pstXMLConsulta,2);

        } catch (Exception ex) {
                log.error("consulta: " + ex);
                respuesta = null;
        }
        */
        return respuesta;
    }


    public String enviaPeticionPago(String pstXMLPago)
    {
        String respuesta = null;


        try {
            
            //respuesta = pagows.EnvioPeticionWSPago.enviaPeticionWS(pstXMLPago,3);
            respuesta = "";

        } catch (Exception ex) {
                log.error("envio pago FL: " + ex);
        }

        return respuesta;
    }


    public String enviaPeticionReenvio(String pstXMLPago)
    {
        String transaccion = null;



        try {           
            //String respuesta = pagows.EnvioPeticionWSPago.enviaPeticionWS(pstXMLPago,4);
            String respuesta = "";
            if (respuesta != null )
                transaccion = this.obtenerTransacccion(respuesta);
            
        } catch (Exception ex) {
                log.error("reenvio: " + ex);
                transaccion = null;
        }

        return transaccion;
    }





    // Invocaciones WS al puerto 8005
    public String enviaPeticionConsulta2(String pstXMLConsulta)
    {
        String respuesta = null;

        try {            
            //respuesta = pagows2.EnvioPeticionWSPago.enviaPeticionWS(pstXMLConsulta,2);
            respuesta = "";

        } catch (Exception ex) {
                log.error("consulta2: " + ex);
                respuesta = null;
        }

        return respuesta;
    }


    
    public String enviaPeticionPago2(String pstXMLPago)
    {
        String respuesta = null;


        try {            
            respuesta = "";
        } catch (Exception ex) {
                log.error("envio pago FL: " + ex);
        }

        return respuesta;
    }




    public String enviaPeticionReenvio2(String pstXMLPago)
    {
        String transaccion = null;


        try {
            //Reenvio al WS alterno :8005            
            //String respuesta = pagows2.EnvioPeticionWSPago.enviaPeticionWS(pstXMLPago,4);
            String respuesta = "";
            if (respuesta != null )
                transaccion = this.obtenerTransacccion(respuesta);

        } catch (Exception ex) {
                log.error("reenvio: " + ex);
                transaccion = null;
        }

        return transaccion;
    }



    public String obtenerTransacccion(String solicitudXML) {
        /*
        SAXBuilder builder = new SAXBuilder(false);
        ByteArrayInputStream inputStreamCadenaPisa = null;
        String transaccion = null;
        Document doc = null;

        try {
            inputStreamCadenaPisa = new ByteArrayInputStream(solicitudXML.getBytes("ISO-8859-1"));
            doc = builder.build(inputStreamCadenaPisa);
        } catch (UnsupportedEncodingException encodingException) {
            log.error("---> Error al crear el InputStream");
            return null;
        } catch (IOException ioException) {
            log.error("---> Error al crear el Document" + ioException.toString());
            return null;
        } catch (JDOMException jdomException) {
            log.error("---> Error al crear el documento" + jdomException.toString());
            return null;
        }


        String accion = null;
        Element nodoOS = doc.getRootElement().getChild("accion");
        accion = nodoOS.getText();

        if(accion != null && accion.equals("TransaccionFL"))
        {

            nodoOS = doc.getRootElement().getChild("foliodetransaccion");
            if ( nodoOS.getText() != null )
                transaccion = nodoOS.getText();

            return transaccion;

        }
        else */
            return null;
    }


    public String generalXMLPago(PagoConciliacionDTO pobPago)
    {

        /*
            peticion = "<root><accion>AplicacionPago</accion>" +
                       "<telefono>5512720147</telefono>" +
                       //"<telefono>5555123470</telefono>" +
                       "<oficinaComercial>BSN</oficinaComercial>" +
                       "<sucursal>1064</sucursal>" +
                       "<transaccion>666111342</transaccion>" +
                       "<estrategia>200203</estrategia>" +
                       "<FormaDePago>1</FormaDePago>" +
                       "<MontoAPagar>1.00</MontoAPagar>" +
                       "<numeroTC>1234567890123456<numeroTC>" +
                       "</root>";
         */

        String lstDato = null;
        StringBuffer lstXmlErr = null;
        lstXmlErr = new StringBuffer();

        lstXmlErr.append("<root>");
        lstXmlErr.append("<accion>AplicacionPago</accion>");

        lstXmlErr.append("<telefono>");
        lstXmlErr.append(pobPago.getTelefono());
        lstXmlErr.append("</telefono>");

        lstXmlErr.append("<oficinaComercial>");
        lstXmlErr.append(pobPago.getComercio().trim());
        lstXmlErr.append("</oficinaComercial>");

        lstXmlErr.append("<sucursal>");
        lstXmlErr.append(pobPago.getSucursal());
        lstXmlErr.append("</sucursal>");

        lstXmlErr.append("<transaccion>");
        lstXmlErr.append(pobPago.getTransaccion());
        lstXmlErr.append("</transaccion>");

        lstXmlErr.append("<estrategia>");
        if ( pobPago.getEstrategia()!= null )
            lstXmlErr.append(pobPago.getEstrategia());
        lstXmlErr.append("</estrategia>");

        lstXmlErr.append("<FormaDePago>");
        lstXmlErr.append(pobPago.getFormaPago());
        lstXmlErr.append("</FormaDePago>");

        lstXmlErr.append("<MontoAPagar>");
        lstXmlErr.append(pobPago.getMonto());
        lstXmlErr.append("</MontoAPagar>");

        lstXmlErr.append("<numeroTC>");
        if ( pobPago.getNumeroTC() != null )
            lstXmlErr.append(pobPago.getNumeroTC());
        lstXmlErr.append("</numeroTC>");

        lstXmlErr.append("<idPago>");
        lstXmlErr.append("</idPago>");


        lstXmlErr.append("</root>");


        return lstXmlErr.toString();



    }

    public String generalXMLReenvio(PagoDTO pobPago)
    {

        /*
            peticion = "<root><accion>AplicacionPago</accion>" +
                       "<telefono>5512720147</telefono>" +
                       "<oficinaComercial>BSN</oficinaComercial>" +
                       "<sucursal>1064</sucursal>" +
                       "<transaccion>666111342</transaccion>" +
                       "<FormaDePago>1</FormaDePago>" +
                       "<MontoAPagar>1.00</MontoAPagar>" +
                       "<numeroTC>1234567890123456<numeroTC>" +
                       "<idPago>1963</idPago>" +
                       "</root>";
         */

        String lstDato = null;
        StringBuffer lstXmlErr = null;
        lstXmlErr = new StringBuffer();

        lstXmlErr.append("<root>");
        lstXmlErr.append("<accion>AplicacionPago</accion>");

        lstXmlErr.append("<telefono>");
        lstXmlErr.append(pobPago.getTelefono());
        lstXmlErr.append("</telefono>");

        lstXmlErr.append("<oficinaComercial>");
        lstXmlErr.append(pobPago.getAdquiriente().trim());
        lstXmlErr.append("</oficinaComercial>");

        lstXmlErr.append("<sucursal>");
        lstXmlErr.append(pobPago.getTiendaTerm());
        lstXmlErr.append("</sucursal>");

        lstXmlErr.append("<transaccion>");
        lstXmlErr.append(pobPago.getTransaccion());
        lstXmlErr.append("</transaccion>");

        lstXmlErr.append("<FormaDePago>");
        lstXmlErr.append(pobPago.getFormaPago());
        lstXmlErr.append("</FormaDePago>");

        lstXmlErr.append("<MontoAPagar>");
        lstXmlErr.append(pobPago.getMontoPagado());
        //lstXmlErr.append("1");
        lstXmlErr.append("</MontoAPagar>");

        lstXmlErr.append("<numeroTC>");
        if ( pobPago.getNumeroTC() != null )
            lstXmlErr.append(pobPago.getNumeroTC());
        lstXmlErr.append("</numeroTC>");

        lstXmlErr.append("<idPago>");
        lstXmlErr.append(pobPago.getIdPago());
        lstXmlErr.append("</idPago>");

        lstXmlErr.append("</root>");


        return lstXmlErr.toString();



    }

    public String generalXMLConsulta(ConsultaDTO datos)
    {

        /*
        String peticion = "<root><accion>Consulta</accion>" +
                       "<telefono>6649043053</telefono>" +
                       "<oficinaComercial>BSN</oficinaComercial>" +
                       "<sucursal>1001</sucursal>" +
                       "<transaccion>1182315</transaccion>" +
                       "<estrategia></estrategia>" +
                       "</root>";
         */

        String lstDato = null;
        StringBuffer lstXmlErr = null;
        lstXmlErr = new StringBuffer();

        lstXmlErr.append("<root>");
        lstXmlErr.append("<accion>Consulta</accion>");

        lstXmlErr.append("<telefono>");
        lstXmlErr.append(datos.getTelefono());
        lstXmlErr.append("</telefono>");

        lstXmlErr.append("<oficinaComercial>");
        lstXmlErr.append(datos.getAdquiriente().trim());
        lstXmlErr.append("</oficinaComercial>");

        lstXmlErr.append("<sucursal>");
        lstXmlErr.append(datos.getTiendaTerminal());
        lstXmlErr.append("</sucursal>");

        lstXmlErr.append("<transaccion>");
        lstXmlErr.append(datos.getTransaccion());
        lstXmlErr.append("</transaccion>");


        lstXmlErr.append("<estrategia>");
        lstXmlErr.append("</estrategia>");

        lstXmlErr.append("</root>");


        return lstXmlErr.toString();


    }




}
