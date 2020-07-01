/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;


import com.blitz.adminpago.dao.LibreriaDAO;
import com.blitz.adminpago.dao.LibreriaDWHDAO;
import com.blitz.adminpago.dao.PagoDAO;
import com.blitz.adminpago.dto.PagoDTO;
import com.blitz.adminpago.dto.PagoMKTDTO;
import com.blitz.adminpago.dto.ReqConsultaPagoDTO;
import com.blitz.adminpago.util.ConnectionPropertiesContainer;
import com.blitz.adminpago.util.Utilerias;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
/**
 *
 * @author PGRANDE
 */
public class BuscaLibPagosPendientesBO {

    private PagoDAO pagoDAO;
    private PagoDAO pagoDAO2;
    private LibreriaDWHDAO libreriaDWHDAO;
    private LibreriaDAO libreriaDAO;
    private Logger log = Logger.getLogger("com.blitz.adminpagoline.bo.BuscaLibPagosPendientesBO");
    private ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();


    public void buscaLibPagosPend()
    {
        buscaLibPagosPendBD(1); //BSN y MKT
        //buscaLibPagosPendBD(2); //demas terceros
    }


    public void buscaCSPagosHoy()
    {
        buscaCSPagos(1); //BSN y MKT
        //buscaCSPagos(2); //demas terceros
    }


    public void buscaLibPagosPendBD(int pnuBD)
    {

        List lobPagos = null;

        if ( pnuBD == 1 )
            lobPagos = pagoDAO.obtenerPagosSinLibreria();
        else
            lobPagos = pagoDAO2.obtenerPagosSinLibreria();


        //List lobPagos = pagoDAO.obtenerPagosSinLibCS();
        if( lobPagos != null )
        {
            log.info("Iniciando proceso de act Libreria");
            Iterator it = lobPagos.iterator();
            while (it.hasNext() )
            {
                String lstLibreria = null;
                String lstCS = null;
                PagoDTO pago = (PagoDTO)it.next();
                //String lstLib = libreriaDWHDAO.obtenerLibreria(pago.getTelefono());

                //ya no hay conexion y no se usa en esata version
                //lstLibreria = libreriaDAO.obtenerLibreria(pago.getTelefono());
                if ( lstLibreria != null )
                {
                    log.info("Actualizando LIB-PISAMEXV1 para BD:tel,pago-->" + pnuBD + ":" + pago.getTelefono() + "," + pago.getIdPago());

                    if ( pnuBD == 1 )
                        pagoDAO.actualizarLibreria(pago.getIdPago(), lstLibreria);
                    else
                        pagoDAO2.actualizarLibreria(pago.getIdPago(), lstLibreria);

                }
                else
                {
                    String lstLib = libreriaDWHDAO.obtenerLibCS(pago.getTelefono());
                    if ( lstLib != null )
                    {
                        if ( lstLib != null )
                        {
                            StringTokenizer st = new StringTokenizer(lstLib,"|");
                            if ( st != null && st.hasMoreTokens())
                            {
                                lstLibreria = st.nextToken();
                                lstCS = st.nextToken();
                                log.info("Lib"+ lstLibreria);
                                log.info("CS"+ lstCS);
                            }
                        }

                        log.info("Actualizando LIB para BD: tel,pago-->" + pnuBD + ":" + pago.getTelefono() + "," + pago.getIdPago());
                        if ( pnuBD == 1 )
                            pagoDAO.actualizarLibCS(pago.getIdPago(), lstLibreria, lstCS);
                        else
                            pagoDAO2.actualizarLibCS(pago.getIdPago(), lstLibreria, lstCS);
                    }
                }


                /* No hemos pasado funcionalidad al conector
                else //intentamos con el cjpobla a traves del conector
                {
                    
                    lstLibreria = obtenerLibreriaCJPobla(pago.getTelefono());
                    if ( lstLibreria != null )
                    {
                        log.info("Actualizando LIB-CJPOBLA para tel,pago-->" + pago.getTelefono() + "," + pago.getIdPago());
                        pagoDAO.actualizarLibreria(pago.getIdPago(), lstLibreria);

                    }
                }
                 */


            }
            log.info("Fin de proceso de act Libreria");

        }



        
    }


    public void buscaCSPagos(int pnuBD)
    {

        List lobPagos = null;

        if ( pnuBD == 1 )
            lobPagos = pagoDAO.obtenerPagosSinCS();
        else
            lobPagos = pagoDAO2.obtenerPagosSinCS();


        if( lobPagos != null )
        {
            log.info("Iniciando proceso de act Clase de servicio BD-->" + pnuBD + " : " + lobPagos.size());
            Iterator it = lobPagos.iterator();
            while (it.hasNext() )
            {
                String lstLibreria = null;
                String lstCS = null;
                PagoDTO pago = (PagoDTO)it.next();

                String lstLib = libreriaDWHDAO.obtenerLibCS(pago.getTelefono());
                if ( lstLib != null )
                {
                    if ( lstLib != null )
                    {
                         StringTokenizer st = new StringTokenizer(lstLib,"|");
                         if ( st != null && st.hasMoreTokens())
                         {
                            lstLibreria = st.nextToken();
                            lstCS = st.nextToken();
                            //log.info("Lib"+ lstLibreria);
                            //log.info("CS"+ lstCS);
                         }
                     }

                     log.info("Act CS para BD: tel,pago-->" + pnuBD + ":" + pago.getTelefono() + "," + pago.getIdPago());
                     if ( pnuBD == 1 )
                        pagoDAO.actualizarCS(pago.getIdPago(),  lstCS);
                    else
                        pagoDAO2.actualizarCS(pago.getIdPago(),  lstCS);
                }
                

            }
            log.info("Fin de proceso de act Clase de servicio BD -->" +  pnuBD);

        }




    }






    public String obtenerLibreriaCJPobla(String telefono) {
        //Verificamos el codigo de la imagen
        /*
        String respuesta = null;
        String libreria = null;

        String lstURL = props.getProperty("conector.libreria.cjpobla");

        ClienteHTTP lobUtil = new ClienteHTTP();
        try
        {
            respuesta = lobUtil.llamarURLget(lstURL+telefono);

            if( respuesta != null && respuesta.length() > 0)
            {
                int idx = respuesta.indexOf("Biblioteca>") ;
                int idx2 = respuesta.indexOf("</Biblioteca>") ;
                if ( idx > 0 ) {
                    libreria = respuesta.substring(idx+11, idx2);
                }
            }
            else
                libreria = null;

        }
        catch(Exception e){;}

        return libreria;
        */
        return null;
    }


    public int consultaPago(PagoMKTDTO pago, String oficina, boolean esToken) {
        int existePago = 0;

        String lstURL = props.getProperty("ws.consultapago.url");
        String user = props.getProperty("ws.consultapago.url.usuario");
        String pass = props.getProperty("ws.consultapago.url.password");

        ReqConsultaPagoDTO dto = new ReqConsultaPagoDTO();
        dto.setAccion("CONSPAGOE");
        dto.setOficinaComercial(oficina);
        dto.setTelefono(pago.getTelefono());
        dto.setTicket(Utilerias.fillLeft(pago.getPrincipal(),12,"0")); //formateado a 12 posiciones
        dto.setBiblioteca(pago.getSecure()); //libreria
        dto.setCaja(pago.getCorreo()); //caja
        dto.setFechaPago(pago.getFechaEstatus());
        dto.setImportePago(pago.getMontoPagar());
        if ( esToken == true )
            dto.setNumTarjeta(pago.getFechaSol());
        else
            dto.setNumTarjeta(pago.getTarjeta());
        
        /*
        SOAPClienteConsulta cliente = new SOAPClienteConsulta(lstURL, user, pass);
//        cliente.setUrlConsulta(lstURL);
//        cliente.setUsername(user);
//        cliente.setPassword(pass);

        try {
        ReqConsultaPagoDTO respDTO = cliente.enviarConsultaPago(dto);
        /*
         Mensajer�a:
        00 � Pago Aplicado en PISA
        01 � Pago NO Aplicado en PISA
        99 � Error General
* 
         */
        /*
            if ( respDTO != null && respDTO.getError() != null ) {

                if (respDTO.getError().startsWith("00") ) {
                    existePago = 1;
                    log.info("pago existe: " + dto.getTelefono() + " : "+ dto.getTicket());
                }
                else if ( respDTO.getError().startsWith("01") ) {
                    existePago = 0;
                    log.info("No existe pago : " + dto.getTelefono() + " : "+ respDTO.getError() + ":" + respDTO.getDescerror() );
                }
                else {
                    existePago = -1;
                        log.info("Error val pago : " + dto.getTelefono() + " : "+ respDTO.getError() + ":" + respDTO.getDescerror() );
                }

            }
            else {
                log.info("Error o sin respuesta servicio consulta " );
                existePago = -1;
            }
        }
        catch (Exception ex) {
            Logger.getLogger(ex.toString());
        }
        */

        return existePago;
    }



    /**
     * @param pagoDAO the pagoDAO to set
     */
    public void setPagoDAO(PagoDAO pagoDAO) {
        this.pagoDAO = pagoDAO;
    }

    /**
     * @param libreriaDWHDAO the libreriaDWHDAO to set
     */
    public void setLibreriaDWHDAO(LibreriaDWHDAO libreriaDWHDAO) {
        this.libreriaDWHDAO = libreriaDWHDAO;
    }

    /**
     * @param libreriaDAO the libreriaDAO to set
     */
    public void setLibreriaDAO(LibreriaDAO libreriaDAO) {
        this.libreriaDAO = libreriaDAO;
    }

    /**
     * @param pagoDAO2 the pagoDAO2 to set
     */
    public void setPagoDAO2(PagoDAO pagoDAO2) {
        this.pagoDAO2 = pagoDAO2;
    }




}
