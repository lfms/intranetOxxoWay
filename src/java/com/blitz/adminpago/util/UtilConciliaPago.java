/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.util;

import com.blitz.adminpago.dto.Concilia3roDTO;
import com.blitz.adminpago.dto.PagoConcilPISADTO;
import com.blitz.adminpago.dto.ConciliacionDTO;
import com.blitz.adminpago.dto.ConsultaDTO;
import com.blitz.adminpago.dto.PagoConcilBancoDTO;
import com.blitz.adminpago.dto.PagoConciliacionDTO;
import com.blitz.adminpago.dto.PagoDTO;
import com.blitz.adminpago.dto.PagoMKTDTO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 *
 * @author PGRANDE
 */
public class UtilConciliaPago {

    private Logger log = Logger.getLogger("com.blitz.adminpagoline.util.UtilConciliaPago");


    public List verificaArchivo(File pobArchivo,Concilia3roDTO pobConciliacion, String pagosPermitidos)
    {
        String lstLinea;
        int lineaActual=0;
        double lnuMontoTotal = 0;
        Calendar cal = Calendar.getInstance();
        List lobPagos = new ArrayList();
        String pagos = "";

        HashMap lobPagosEL = new HashMap(); //pagos en linea
        HashMap lobPagosFL = new HashMap(); //pagos fuera de linea
        HashMap lobPagosEE = new HashMap(); //pagos con error
        //HashMap lobPagosCC = new HashMap(); //pagos para abanderar como cancelados todavia no se usa
        HashMap lobPagosPT = new HashMap(); //relacion de telefonos reportados en el archivo con llave suc|tel|monto
        HashMap lobPagosPN = new HashMap(); //relacion de telefonos reportados en el archivo con llave suc|tel|monto duplicada o sea negados

        try {
            BufferedReader lobLector = new BufferedReader(new FileReader(pobArchivo));
            while ((lstLinea = lobLector.readLine()) != null) {
                lineaActual++;

                if (lstLinea.contains("#")) { // Es el encabezado o las cifras de control

                    StringTokenizer stLinea = new StringTokenizer(lstLinea,"|");
                    if ( lineaActual == 1 )//Encabezado
                    {
                        //#2011/08/25|BSN
                        String lstFecha = stLinea.nextToken();
                        if ( lstFecha != null )
                        {
                            int lnuValor = Integer.parseInt(lstFecha.substring(1,5));
                            cal.set(Calendar.YEAR, lnuValor);

                            lnuValor = Integer.parseInt(lstFecha.substring(6,8)) - 1;
                            cal.set(Calendar.MONTH, lnuValor);

                            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(lstFecha.substring(9,11)));
                            pobConciliacion.setFecha(cal.getTime());

                            //establecemos un mes como rango maximo para buscar el pago para conciliar
                            cal.add(Calendar.DAY_OF_MONTH, -7);
                            pobConciliacion.setFechaMin(cal.getTime());

                            pobConciliacion.setComercio(stLinea.nextToken().trim());
                        }
                     }
                     else //Cifras de control
                     {
                        //#7|1355.00
                        try{
                            pobConciliacion.setTotalRegistros(Integer.parseInt(stLinea.nextToken().substring(1)));
                            pobConciliacion.setMontoTotal(Double.parseDouble(stLinea.nextToken()));
                        }
                        catch(Exception e2)
                        {
                            log.error("error en cifras de control->" + e2.toString());
                        }

                     }

                }
                else { //Son los pagos
                    //5555123434|BSN|10010000|2011/05/17|17:16:40|011970001629|145|1|700.00|E
                    StringTokenizer stLinea = new StringTokenizer(lstLinea,"|");
                    PagoConciliacionDTO pagoDTO = new PagoConciliacionDTO();
                    String telefono = stLinea.nextToken();
                    pagoDTO.setTelefono(telefono);
                    pagoDTO.setComercio(stLinea.nextToken());
                    pagoDTO.setRegError(false);

                    //La sucursal en el caso de sanborns son los ultimos 4 caracteres :S
                    String lstSuc = stLinea.nextToken();
                    if ( pagoDTO.getComercio() != null && pagoDTO.getComercio().trim().equals("BSN"))
                        lstSuc = lstSuc.substring(lstSuc.length() - 4);
                    
                    pagoDTO.setSucursal(lstSuc);
                    pagoDTO.setFecha(stLinea.nextToken());
                    pagoDTO.setHora(stLinea.nextToken());

                    //A partir del 23Ene a la TX se adiciono fecha del lado de tmx, BSN la envia a 12 posiciones
                    //los completamos con la fecha del pago reportado
                    String transaccion = stLinea.nextToken();
                    if ( pagoDTO.getComercio() != null && pagoDTO.getComercio().trim().equals("BSN")
                            && !transaccion.equals("000000000000")) {
                        pagoDTO.setTransaccion(Utilerias.obtenerFechaConcil(pagoDTO.getFecha()) + transaccion);
                    }
                    else
                        pagoDTO.setTransaccion(transaccion);

                    pagoDTO.setAutorizacion(stLinea.nextToken());
                    pagoDTO.setFormaPago(stLinea.nextToken());
                    pagoDTO.setMonto(stLinea.nextToken().trim());

                    String tipoProceso = stLinea.nextToken();
                    pagoDTO.setTipoProceso(tipoProceso);

                    //Si tiene otro campo, lo consideramos la tarjeta de credito
                    try
                    {
                    //5555123434|BSN|10010000|2011/05/17|17:16:40|011970001629|145|1|700.00|E|1234567890123456
                        String lstNumTC = stLinea.nextToken();
                        if ( lstNumTC != null && lstNumTC.length() > 0)
                            pagoDTO.setNumeroTC(lstNumTC);


                    }
                    catch(Exception e){;}

                    //if ( pagoDTO.getTipoProceso()!= null && !pagoDTO.getTipoProceso().equals("C") )
                    lnuMontoTotal += Double.parseDouble(pagoDTO.getMonto());

                    //Validamos telefono solo numeros ANTAD metio ruido con eso
                    if ( telefono == null || telefono.length() != 10 || this.soloCaracteresNumericos(telefono) == false)
                        pagoDTO.setRegError(true);


                    //Generamos la llave para todos los pagos del archivo
                    String llavePT = pagoDTO.getSucursal() + "|" + pagoDTO.getTelefono() + "|" + pagoDTO.getMonto();
                    if (lobPagosPT.containsKey(llavePT) == true) { //ya existe el valor de la llave compuesta
                        lobPagosPN.put(llavePT, pagoDTO); //pagos que seran reportados como negados
                        log.info("Pago reportado dos veces o mas en el mismo archivo:" + llavePT + ":" +  pagoDTO.getTransaccion() );
                    }
                    else {
                        lobPagosPT.put(llavePT, pagoDTO);
                    }



                    if ( pagoDTO.getTipoProceso()!= null && pagoDTO.getTipoProceso().equals("E") ) {

                        if (lobPagosEL.containsKey(pagoDTO.getTransaccion()) == true) { //ya existe el valor de transaccion
                            lobPagosEE.put(pagoDTO.getTransaccion(), pagoDTO);
                            log.info("Transaccion duplicada:" + pagoDTO.getTransaccion() );
                        }
                        else {
                            lobPagosEL.put(pagoDTO.getTransaccion(), pagoDTO);

                            /*
                             //Modificaciones para validacion de un solo pago
                            //Registramos los telefonos en una lista
                            if ( pagosPermitidos != null && pagosPermitidos.indexOf(pagoDTO.getComercio()) != -1) {
                                if (lobPagosPT.containsKey("E|"+pagoDTO.getTelefono()) == true)  //ya existe el valor de telefono
                                    log.info("Telefono duplicado E:" + pagoDTO.getTelefono() + "|" + pagoDTO.getTransaccion() ); //reemplaza el ultimo valor encontrado
                                lobPagosPT.put("E|"+pagoDTO.getTelefono(), pagoDTO);
                            }
                            //Modificaciones para validacion de un solo pago
                            */

                        }
                    }
                    else if(pagoDTO.getTipoProceso() != null && pagoDTO.getTipoProceso().equals("F"))
                    {

                            //lobPagosFL.put(pagoDTO.getTransaccion(), pagoDTO); ya no puede ser llave porque viene en ceros

                        //validamos el telefono y cs
                        //OJO Es necesario solo validar los nuevos, los que traigan transaccion o folio de autorizacion no
                        String codigoCONS = "00";
                        if ( pagoDTO.getAutorizacion()!= null && pagoDTO.getAutorizacion().equals("000000000000"))
                            codigoCONS = this.codigoConsulta(telefono);
                        //String codigoCONS = "00";
                        pagoDTO.setEstatusSaldo(codigoCONS);

                        if (codigoCONS != null && ( codigoCONS.equals("00") || codigoCONS.equals("15") ) ) // si es valido
                            pagoDTO.setRegError(false); //es ok
                        else
                        {
                            pagoDTO.setRegError(true);
                            log.info("No pasa consulta->" + pagoDTO.getTelefono());
                        }

                        lobPagosFL.put(pagoDTO.getComercio()+"|"+pagoDTO.getTelefono()+"|"+pagoDTO.getFecha()+"|"+pagoDTO.getHora()+"|"+pagoDTO.getMonto(), pagoDTO);

                        /*
                        //Modificaciones para validacion de un solo pago
                        //Registramos los telefonos en una lista
                        if ( pagosPermitidos != null && pagosPermitidos.indexOf(pagoDTO.getComercio()) != -1) {
                            if (lobPagosPT.containsKey("F|"+pagoDTO.getTelefono()) == true)  //ya existe el valor de telefono
                                log.info("Telefono duplicado F:" + pagoDTO.getTelefono() + "|" + pagoDTO.getTransaccion() ); //reemplaza el ultimo valor encontrado
                            lobPagosPT.put("F|"+pagoDTO.getTelefono(), pagoDTO);
                        }
                        //Modificaciones para validacion de un solo pago
                        */
                        
                    }

                        //else if ( pagoDTO.getTipoProceso()!= null && pagoDTO.getTipoProceso().equals("C") )
                        //    lobPagosCC.put(pagoDTO.getTransaccion(), pagoDTO); //No se considera en las cifras de control
                    else
                        lobPagosEE.put(pagoDTO.getTransaccion(), pagoDTO);

                }

            }

            /*
            if ( lobPagosFL != null)
            {
                Iterator it2 = lobPagosFL.keySet().iterator();
                while( it2.hasNext() )
                {
                    String llave = (String)it2.next();
                    PagoConciliacionDTO pago = (PagoConciliacionDTO)lobPagosFL.get(llave);
                    log.info(pago.getTelefono() + "|" + pago.getMonto());
                    //System.out.println(pago.getTelefono() );

                }

            }
             *
             */
            log.info("Archivo->" + pobConciliacion.getArchivo());
            log.info("TotalReg->" + pobConciliacion.getMontoTotal() + ";" + pobConciliacion.getTotalRegistros());
            log.info("MontoTotalS->" + lnuMontoTotal);
            log.info("TotalEL->" + lobPagosEL.size() + "; TotalFL->" + lobPagosFL.size() + ";TotalEE->" + lobPagosEE.size());
            log.info("TotalPT->" + lobPagosPT.size());
            log.info("TotalPN->" + lobPagosPN.size());
            //Verificamos las cifras de control
            if ( lobPagosEE.size() > 0  
                    || 
                    ( pobConciliacion.getTotalRegistros() != lobPagosEL.size() + lobPagosFL.size() )
                    ||
                    (   Math.abs(pobConciliacion.getMontoTotal() - lnuMontoTotal ) > 0.01 )
                )
            {

                pobConciliacion.setEstado("ARCHIVO_NOK");
                lobPagos = null;
            }
            else {
                lobPagos.add(lobPagosEL);
                lobPagos.add(lobPagosFL);
                lobPagos.add(lobPagosEE);
                //lobPagos.add(lobPagosCC); //los que se deberian de cancelar sin uso
                lobPagos.add(lobPagosPN);
                pobConciliacion.setEstado("VALIDADO");
            }


        } catch (FileNotFoundException ex) {
            log.error("Error al cargar archivo de conciliacion " + pobArchivo.getName() + ": " + ex.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (IOException ioe) {
            log.error("Error al leer el archivo de conciliaci�n " + pobArchivo.getName() + ": " + ioe.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (Exception e) {
            log.error("Error en el archivo " + pobArchivo.getName() + " en la l�nea " + lineaActual + " : " + e.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        }

        return lobPagos;
    }





    public List verificaArchivoPorIdPago(File pobArchivo,Concilia3roDTO pobConciliacion, String pagosPermitidos)
    {
        String lstLinea;
        int lineaActual=0;
        double lnuMontoTotal = 0;
        Calendar cal = Calendar.getInstance();
        List lobPagos = new ArrayList();
        String pagos = "";

        HashMap lobPagosEL = new HashMap(); //pagos en linea
        HashMap lobPagosFL = new HashMap(); //pagos fuera de linea
        HashMap lobPagosEE = new HashMap(); //pagos con error
        HashMap lobPagosCC = new HashMap(); //pagos para abanderar como no autorizados

        try {
            BufferedReader lobLector = new BufferedReader(new FileReader(pobArchivo));
            while ((lstLinea = lobLector.readLine()) != null) {
                lineaActual++;

                if (lstLinea.contains("#")) { // Es el encabezado o las cifras de control

                    StringTokenizer stLinea = new StringTokenizer(lstLinea,"|");
                    if ( lineaActual == 1 )//Encabezado
                    {
                        //#2011/08/25|BSN
                        String lstFecha = stLinea.nextToken();
                        if ( lstFecha != null )
                        {
                            int lnuValor = Integer.parseInt(lstFecha.substring(1,5));
                            cal.set(Calendar.YEAR, lnuValor);

                            lnuValor = Integer.parseInt(lstFecha.substring(6,8)) - 1;
                            cal.set(Calendar.MONTH, lnuValor);

                            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(lstFecha.substring(9,11)));
                            pobConciliacion.setFecha(cal.getTime());

                            //establecemos un mes como rango maximo para buscar el pago para conciliar
                            cal.add(Calendar.DAY_OF_MONTH, -7);
                            pobConciliacion.setFechaMin(cal.getTime());

                            pobConciliacion.setComercio(stLinea.nextToken().trim());
                        }
                     }
                     else //Cifras de control
                     {
                        //#7|1355.00
                        try{
                            pobConciliacion.setTotalRegistros(Integer.parseInt(stLinea.nextToken().substring(1)));
                            pobConciliacion.setMontoTotal(Double.parseDouble(stLinea.nextToken()));
                        }
                        catch(Exception e2)
                        {
                            log.error("error en cifras de control->" + e2.toString());
                        }

                     }

                }
                else { //Son los pagos
                    //5555123434|BSN|10010000|2011/05/17|17:16:40|011970001629|145|1|700.00|E
                    StringTokenizer stLinea = new StringTokenizer(lstLinea,"|");
                    PagoConciliacionDTO pagoDTO = new PagoConciliacionDTO();
                    String telefono = stLinea.nextToken();
                    pagoDTO.setTelefono(telefono);
                    pagoDTO.setComercio(stLinea.nextToken());
                    pagoDTO.setRegError(false);

                    //La sucursal en el caso de sanborns son los ultimos 4 caracteres :S
                    String lstSuc = stLinea.nextToken();
                    if ( pagoDTO.getComercio() != null && pagoDTO.getComercio().trim().equals("BSN"))
                        lstSuc = lstSuc.substring(lstSuc.length() - 4);

                    pagoDTO.setSucursal(lstSuc);
                    pagoDTO.setFecha(stLinea.nextToken());
                    pagoDTO.setHora(stLinea.nextToken());

                    //A partir del 23Ene a la TX se adiciono fecha del lado de tmx, BSN la envia a 12 posiciones
                    //los completamos con la fecha del pago reportado
                    String transaccion = stLinea.nextToken();
                    if ( pagoDTO.getComercio() != null && pagoDTO.getComercio().trim().equals("BSN")
                            && !transaccion.equals("000000000000")) {
                        pagoDTO.setTransaccion(Utilerias.obtenerFechaConcil(pagoDTO.getFecha()) + transaccion);
                    }
                    else
                        pagoDTO.setTransaccion(transaccion);

                    BigDecimal idPago = new BigDecimal(0);
                    String autorizacion = stLinea.nextToken();
                    pagoDTO.setAutorizacion(autorizacion); //IdPago
                    if ( autorizacion != null && autorizacion.length()>0) {
                        try {
                            idPago = new BigDecimal(autorizacion);
                        }catch(Exception ebig) {
                            idPago = new BigDecimal(0);;
                        }
                    }

                    pagoDTO.setFormaPago(stLinea.nextToken());
                    pagoDTO.setMonto(stLinea.nextToken().trim());

                    String tipoProceso = stLinea.nextToken();
                    pagoDTO.setTipoProceso(tipoProceso);

                    //Si tiene otro campo, lo consideramos la tarjeta de credito
                    try
                    {
                    //5555123434|BSN|10010000|2011/05/17|17:16:40|011970001629|145|1|700.00|E|1234567890123456
                        String lstNumTC = stLinea.nextToken();
                        if ( lstNumTC != null && lstNumTC.length() > 0)
                            pagoDTO.setNumeroTC(lstNumTC);


                    }
                    catch(Exception e){;}

                    lnuMontoTotal += Double.parseDouble(pagoDTO.getMonto());

                    //Validamos telefono solo numeros ANTAD metio ruido con eso
                    if ( telefono == null || telefono.length() != 10 || this.soloCaracteresNumericos(telefono) == false)
                        pagoDTO.setRegError(true);



                    if ( pagoDTO.getAutorizacion() != null && idPago.doubleValue() > 0) { //tiene un idPago valido

                        if (lobPagosEL.containsKey(pagoDTO.getAutorizacion()) == true) { //ya existe el valor de idPago
                            lobPagosEE.put(pagoDTO.getAutorizacion(), pagoDTO);
                            log.info("IdPago idPago duplicado:" + pagoDTO.getAutorizacion() );
                        }
                        else {
                            lobPagosEL.put(pagoDTO.getAutorizacion(), pagoDTO);

                            /*
                             //Modificaciones para validacion de un solo pago
                            //Registramos los telefonos en una lista
                            if ( pagosPermitidos != null && pagosPermitidos.indexOf(pagoDTO.getComercio()) != -1) {
                                if (lobPagosPT.containsKey("E|"+pagoDTO.getTelefono()) == true)  //ya existe el valor de telefono
                                    log.info("Telefono duplicado E:" + pagoDTO.getTelefono() + "|" + pagoDTO.getTransaccion() ); //reemplaza el ultimo valor encontrado
                                lobPagosPT.put("E|"+pagoDTO.getTelefono(), pagoDTO);
                            }
                            //Modificaciones para validacion de un solo pago
                            */

                        }
                    }
                    else if ( pagoDTO.getTransaccion() != null && pagoDTO.getTransaccion().length() > 0) {
                    

                        //validamos el telefono y cs
                        //OJO Es necesario solo validar los nuevos, los que traigan transaccion o folio de autorizacion no
                        String codigoCONS = "00";
                        if ( pagoDTO.getAutorizacion()!= null && pagoDTO.getAutorizacion().equals("000000000000"))
                            codigoCONS = this.codigoConsulta(telefono);
                        //String codigoCONS = "00";
                        pagoDTO.setEstatusSaldo(codigoCONS);

                        if (codigoCONS != null && ( codigoCONS.equals("00") || codigoCONS.equals("15") ) ) // si es valido
                            pagoDTO.setRegError(false); //es ok
                        else
                        {
                            pagoDTO.setRegError(true);
                            log.info("No pasa consulta->" + pagoDTO.getTelefono());
                        }

                        String llaveCompuesta = pagoDTO.getSucursal()+"|"+pagoDTO.getTelefono()+"|"+pagoDTO.getMonto();
                        if ((lobPagosFL.containsKey(llaveCompuesta) == true ) || pagoDTO.isRegError() == true ) { 
                            //ya existe un pago con las mismas condiciones en el arreglo o no paso la consulta del telefono
                            lobPagosEE.put(llaveCompuesta, pagoDTO);
                            log.info("IdPago idPago duplicado:" + pagoDTO.getAutorizacion() );
                        }
                        else 
                            lobPagosFL.put(llaveCompuesta, pagoDTO);

                    }
                    else
                        lobPagosEE.put(pagoDTO.getTelefono()+"|"+pagoDTO.getMonto(), pagoDTO);

                }

            }

            /*
            if ( lobPagosFL != null)
            {
                Iterator it2 = lobPagosFL.keySet().iterator();
                while( it2.hasNext() )
                {
                    String llave = (String)it2.next();
                    PagoConciliacionDTO pago = (PagoConciliacionDTO)lobPagosFL.get(llave);
                    log.info(pago.getTelefono() + "|" + pago.getMonto());
                    //System.out.println(pago.getTelefono() );

                }

            }
             *
             */
            log.info("Archivo->" + pobConciliacion.getArchivo());
            log.info("TotalReg->" + pobConciliacion.getMontoTotal() + ";" + pobConciliacion.getTotalRegistros());
            log.info("MontoTotalS->" + lnuMontoTotal);
            log.info("TotalEL->" + lobPagosEL.size() + "; TotalFL->" + lobPagosFL.size() + ";TotalEE->" + lobPagosEE.size());
            //Verificamos las cifras de control
//            if ( lobPagosEE.size() > 0
//                    ||

              if ( ( pobConciliacion.getTotalRegistros() != lobPagosEL.size() + lobPagosFL.size() + lobPagosEE.size())
                    ||
                    (   Math.abs(pobConciliacion.getMontoTotal() - lnuMontoTotal ) > 0.01 )
                )
            {

                pobConciliacion.setEstado("ARCHIVO_NOK");
                lobPagos = null;
            }
            else {
                lobPagos.add(lobPagosEL);
                lobPagos.add(lobPagosFL);
                lobPagos.add(lobPagosEE);
                //lobPagos.add(lobPagosCC); //los que se deberian de cancelar sin uso
                pobConciliacion.setEstado("VALIDADO");
            }


        } catch (FileNotFoundException ex) {
            log.error("Error al cargar archivo de conciliacion " + pobArchivo.getName() + ": " + ex.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (IOException ioe) {
            log.error("Error al leer el archivo de conciliaci�n " + pobArchivo.getName() + ": " + ioe.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (Exception e) {
            log.error("Error en el archivo " + pobArchivo.getName() + " en la l�nea " + lineaActual + " : " + e.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        }

        return lobPagos;
    }


    public int compararFechas(String fechaSol, Calendar fecha) {
        int rango = 0;
        Calendar fechaSolicitud = Calendar.getInstance();
        fechaSolicitud.set(Integer.parseInt(fechaSol.substring(0, 4)),
                Integer.parseInt(fechaSol.substring(5, 7)),
                Integer.parseInt(fechaSol.substring(8, 10)),
                Integer.parseInt(fechaSol.substring(11, 13)),
                Integer.parseInt(fechaSol.substring(14, 16)),
                Integer.parseInt(fechaSol.substring(17)));
        long milisegundosFechaSolicitud = fechaSolicitud.getTimeInMillis();
        long milisegundosFechaConcilia = fecha.getTimeInMillis();
        long diferencia = milisegundosFechaConcilia - milisegundosFechaSolicitud;
        long horas = diferencia / (3600000);
        if (horas < 24) {
            rango = 0;
        } else if (horas < 48) {
            rango = 1;
        } else if (horas < 72) {
            rango = 2;
        }
        return rango;
    }

    public String obtenerEstatusConciliacion(PagoDTO pobPagoBD)
    {
        String lstEstatConcil = null;
        Calendar lobHoy = Calendar.getInstance();
        Calendar lobFechaSol = null;
        Utilerias util = new Utilerias();


        if ( pobPagoBD != null )
        {
            //Verificamos la diferencia entre las fechas de hoy y la de la solicitud
            //2011/03/17 11:00:46
            // rango == 0 -->  <24 hr
            // rango == 1 -->  <48 hr
            // rango == 2 -->  <72 hr
            int rango = compararFechas(pobPagoBD.getFechaSol().trim(), lobHoy);

            //Si el pago no esta atendido y posteado
            if ( pobPagoBD.getEstatus() != null && !pobPagoBD.getEstatus().equals("AP") )
            {
                //Verificamos la fecha y lo cambiamos al estatus a1 o a2


                if ( pobPagoBD.getCodigoResp() != null && !pobPagoBD.getCodigoResp().equals("00") )
                    lstEstatConcil = "EE"; //Error No se pudo enviar el telefono
                else
                    lstEstatConcil = "RR"; //Recibido y no enviado a PISA
            }

            else if(pobPagoBD.getEstatus() != null && pobPagoBD.getEstatus().equals("AP"))
            {
                switch ( rango )
                {
                    case 0: lstEstatConcil = "A1"; //Atendido y Pendiente Postear 24 horas
                        break;
                    case 1: lstEstatConcil = "A2"; //Atendido y Pendiente Postear 48 horas
                        break;
                    default:
                    case 2: lstEstatConcil = "A3"; //Atendido y Pendiente Postear 72 horas
                        break;

                }


            }
            else if(pobPagoBD.getEstatus() != null &&  (
                    pobPagoBD.getEstatus().equals("CO") ||
                    pobPagoBD.getEstatus().equals("CO24") ||
                    pobPagoBD.getEstatus().equals("CO48") ||
                    pobPagoBD.getEstatus().equals("COMA") ))
                lstEstatConcil = "AP"; //Atendido y Posteado

            else if(pobPagoBD.getEstatus() != null && pobPagoBD.getEstatus().equals("CA") )
                lstEstatConcil = "EE"; //Error No se pudo enviar el telefono

            else
                lstEstatConcil = "XX"; //Estatus desconocido

        }

        return lstEstatConcil;
    }


    public void generaArchivoRespuesta(String pstNombreArchivo, String pstComercio, List pobPagos)
    {
        String strContenido = null;
        double lnuMontoTotalEst[] = {0,0,0};
        String estatus[] = {"TT","RA","RNA"};
        int lnuTotalEst[] = {0,0,0};


        //Tomamos la fecha del nombre del archivo: TRO_BCN20110818.txt
        String lstFechaArch = null;
        try
        {
            lstFechaArch = pstNombreArchivo.substring(pstNombreArchivo.indexOf("20"), pstNombreArchivo.indexOf("."));
        }
        catch(Exception e)
        {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Calendar lobHoy = Calendar.getInstance();
                lstFechaArch = sdf.format(lobHoy.getTime());
        }

        try
        {
            FileOutputStream fos = new FileOutputStream(pstNombreArchivo, false);

            if (fos != null)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Calendar lobHoy = Calendar.getInstance();
                //Encabezado
                strContenido = "#" + lstFechaArch + "|" + pstComercio + "\r";
                fos.write(strContenido.getBytes());

                if ( pobPagos != null )
                {

                    for (int i=0; i<pobPagos.size(); i++)
                    {

                        //PagosEL, PagosFL

                        HashMap pobPagosX = (HashMap)pobPagos.get(i);
                        Iterator it = (pobPagosX).keySet().iterator();

                        while (it.hasNext())
                        {
                            lnuTotalEst[0]++;

                            String lobPagoTrans = (String)it.next();
                            PagoConciliacionDTO pagoDTO = (PagoConciliacionDTO)pobPagosX.get(lobPagoTrans);

                            StringBuffer lstP = new StringBuffer();
                            lstP.append(pagoDTO.getTelefono()); lstP.append("|");
                            lstP.append(pagoDTO.getComercio()); lstP.append("|");
                            lstP.append(pagoDTO.getSucursal()); lstP.append("|");
                            lstP.append(pagoDTO.getFecha()); lstP.append("|");
                            lstP.append(pagoDTO.getHora()); lstP.append("|");
                            lstP.append(pagoDTO.getTransaccion()); lstP.append("|");
                            lstP.append(pagoDTO.getAutorizacion()); lstP.append("|");
                            lstP.append(pagoDTO.getFormaPago()); lstP.append("|");
                            lstP.append(this.formateaCantidad(pagoDTO.getMonto(),11)); lstP.append("|");
                            lstP.append(pagoDTO.getTipoProceso()); lstP.append("|");
                            lstP.append(pagoDTO.getEstatus());lstP.append("|");
                            //tarjeta de credito
                            if ( pagoDTO.getNumeroTC() == null )
                                pagoDTO.setNumeroTC("");
                            lstP.append(pagoDTO.getNumeroTC());lstP.append("|");

                            if ( pagoDTO.isRegError() )
                                lstP.append("NO CONCILIADO");
                            else
                                lstP.append("CONCILIADO");
                            lstP.append("\r");

                            fos.write(lstP.toString().getBytes());

                            lnuMontoTotalEst[0] += Double.parseDouble(pagoDTO.getMonto());

                            for(int j=1; j<estatus.length; j++)
                            {
                                if (pagoDTO.getEstatus()!=null &&
                                        pagoDTO.getEstatus().trim().startsWith(estatus[j]) )
                                {
                                    lnuMontoTotalEst[j] += Double.parseDouble(pagoDTO.getMonto());
                                    lnuTotalEst[j] ++;
                                }

                            }


                        }

                    }

                    //Cifras de control por estatus
                    for(int i=0; i<estatus.length; i++)
                    {
                        strContenido = "#" + estatus[i] + "|"
                                + this.formateaCantidad(Integer.toString(lnuTotalEst[i]),5) + "|"
                                + this.formateaCantidad(Double.toString(lnuMontoTotalEst[i]),11) + "\r";
                        fos.write(strContenido.getBytes());

                    }


                }


            }


            fos.close();

        }
        catch(IOException ioe)
        {
            log.error("error en generaArchivoRespuesta IOException : " + ioe);
        }


    }


    public void generaArchRespPostConcilPISA(String pstNombreArchivo, String pstComercio, List pobPagos)
    {
        String strContenido = null;
        double lnuMontoTotalEst[] = {0,0,0};
        String estatus[] = {"TT","RA","RNA"};
        int lnuTotalEst[] = {0,0,0};


        //Tomamos la fecha del nombre del archivo: TRO_BCN20110818.txt
        String lstFechaArch = null;
        try
        {
            lstFechaArch = pstNombreArchivo.substring(pstNombreArchivo.indexOf("20"), pstNombreArchivo.indexOf("."));
        }
        catch(Exception e)
        {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Calendar lobHoy = Calendar.getInstance();
                lstFechaArch = sdf.format(lobHoy.getTime());
        }

        try
        {
            FileOutputStream fos = new FileOutputStream(pstNombreArchivo, false);

            if (fos != null)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Calendar lobHoy = Calendar.getInstance();
                //Encabezado
                strContenido = "#" + lstFechaArch + "|" + pstComercio + "\r";
                fos.write(strContenido.getBytes());

                if ( pobPagos != null )
                {

                    Iterator it = pobPagos.iterator();

                    while (it.hasNext())
                    {
                        lnuTotalEst[0]++;

                        PagoDTO pagoDTO = (PagoDTO)it.next();

                        StringBuffer lstP = new StringBuffer();
                        lstP.append(pagoDTO.getTelefono()); lstP.append("|");
                        lstP.append(pagoDTO.getAdquiriente().trim()); lstP.append("|");
                        lstP.append(pagoDTO.getTiendaTerm()); lstP.append("|");

                        //Separamos la fecha en YYYY/MM/DD y en hora HH:mm:ss
                        String lstFechaSol = pagoDTO.getFechaSol();
                        if ( lstFechaSol != null && lstFechaSol.length() >= 17 )
                        {
                            lstP.append(lstFechaSol.substring(0, 10)); lstP.append("|");
                            lstP.append(lstFechaSol.substring(11)); lstP.append("|");

                        }
                        else
                        {
                            lstP.append(pagoDTO.getFechaSol()); lstP.append("|");
                            lstP.append(pagoDTO.getHora()); lstP.append("|");
                        }


                        lstP.append(pagoDTO.getTransaccion()); lstP.append("|");
                        lstP.append(pagoDTO.getAutorizacion()); lstP.append("|");

                        if ( pagoDTO.getTipoIngreso() != null)
                            lstP.append(pagoDTO.getTipoIngreso().trim());
                        lstP.append("|");

                        lstP.append(this.formateaCantidad(pagoDTO.getMontoPagado(),11)); lstP.append("|");
                        lstP.append(pagoDTO.getTipoProceso()); lstP.append("|");
                        lstP.append(pagoDTO.getEstatusConcilTro());
                        if (pagoDTO.getEstatusConcilTro()!=null && pagoDTO.getEstatusConcilTro().trim().equals("RNA") )
                            lstP.append(":" + pagoDTO.getEstatus());
                        lstP.append("\r");

                        fos.write(lstP.toString().getBytes());

                        lnuMontoTotalEst[0] += Double.parseDouble(pagoDTO.getMontoPagado());

                        for(int j=1; j<estatus.length; j++)
                        {
                            if (pagoDTO.getEstatusConcilTro()!=null &&
                                pagoDTO.getEstatusConcilTro().trim().startsWith(estatus[j]) )
                            {
                                lnuMontoTotalEst[j] += Double.parseDouble(pagoDTO.getMontoPagado());
                                lnuTotalEst[j] ++;
                            }

                        }


                    }


                    //Cifras de control por estatus
                    for(int i=0; i<estatus.length; i++)
                    {
                        strContenido = "#" + estatus[i] + "|"
                                + this.formateaCantidad(Integer.toString(lnuTotalEst[i]),5) + "|"
                                + this.formateaCantidad(Double.toString(lnuMontoTotalEst[i]),11) + "\r";
                        fos.write(strContenido.getBytes());

                    }


                }


            }


            fos.close();

        }
        catch(IOException ioe)
        {
            log.error("error en generaArchRespPostPISA IOException : " + ioe);
        }


    }

    public void enviaCorreo(String pstCorreo, String pstArchivo, int opcion)
    {
        String sHTML = null;
        String lstAsunto = null;
        ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();


        String lstNomCorto = pstArchivo;
            if ( pstArchivo != null && pstArchivo.indexOf("/") != -1 )
                lstNomCorto = pstArchivo.substring(pstArchivo.lastIndexOf("/"));

        switch (opcion)
        {
            case 1:
                    sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Estimado Usuario:</b>" +
                        "<br><br>Se env&iacute;a Archivo resultado de la conciliaci&oacute;n : " +
                        pstArchivo;
                    sHTML += "<br><br>" + "</HTML>";
                    lstAsunto = props.getProperty("reporte.asunto");
                break;
            case 2:
                    sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Estimado Usuario:</b>" +
                        "<br><br>Se le informa que el archivo " + lstNomCorto +
                        " no fu&eacute; posible conciliarlo. " +
                        "Favor de enviarlo nuevamente. Gracias. " ;
                    sHTML += "<br><br>" + "</HTML>";
                    lstAsunto = props.getProperty("reporte.asunto.error");
                break;
            case 3:
                    sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Estimado Usuario de PISA:</b>" +
                        "<br><br>Se le informa que el archivo " + lstNomCorto +
                        " no fu&eacute; posible conciliarlo. " +
                        "Favor de enviarlo nuevamente. Gracias. " ;
                    sHTML += "<br><br>" + "</HTML>";
                    lstAsunto = props.getProperty("reporte.asunto.error.pisa");
                break;
            case 4:
                    sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Estimado Usuario de PISA:</b>" +
                        "<br><br>Se le informa que en el archivo " + lstNomCorto +
                        " se encontraron registros con error. " +
                        "Favor de enviarlo nuevamente. Gracias. " ;
                    sHTML += "<br><br>" + "</HTML>";
                    lstAsunto = props.getProperty("reporte.asunto.error.pisa");
                break;
            case 5:
                    sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Estimado Usuario de Cobranza:</b>" +
                        "<br><br>Se le informa que los pagos siguientes no han sido reportados " +
                        " en los archivos de conciliaci�n de terceros: " ;
                    sHTML += "<br><br>" + pstArchivo + "</HTML>";
                    lstAsunto = props.getProperty("reporte.asunto.pagossinconcil");
                break;
            case 6:
                    sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Estimado Usuario de PISA:</b>" +
                        "<br><br>Se le informa que el(los) archivo(s) de la(s) librer�a(s) " + pstArchivo +
                        " no fueron recibidos. " +
                        "Favor de enviarlo(s) nuevamente. Gracias. " ;
                    sHTML += "<br><br>" + "</HTML>";
                    lstAsunto = props.getProperty("reporte.asunto.error2.pisa");
                break;
            case 7:
                    sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Estimado Usuario de eGlobal:</b>" +
                        "<br><br>Se le informa que el(los) archivo(s) siguiente(s) no fueron recibidos:<br><br>"
                        + pstArchivo + "<br><br>" +
                        "Favor de enviarlo(s) nuevamente. Gracias. " ;
                    sHTML += "<br><br>" + "</HTML>";
                    lstAsunto = props.getProperty("reporte.asunto.error.eglobal");
                break;
            case 8:
            case 9:
                    sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Estimado Usuario de Cobranza:</b>" +
                        "<br><br>Se le informa que los pagos siguientes no han sido reportados " +
                        " en los archivos de conciliaci�n de terceros: " ;
                    sHTML += "<br><br>" + pstArchivo + "</HTML>";
                    lstAsunto = props.getProperty("reporte.asunto.pagossinconcilMonto");
                break;

        }
        if(opcion == 7)
            this.enviaCorreo(props.getProperty("reporte.correo.error.eglobal"), pstArchivo, sHTML, lstAsunto);
        else if(opcion == 6)
            this.enviaCorreo(props.getProperty("reporte.correo.error.pisa"), "", sHTML, lstAsunto);
        if(opcion == 8)
            this.enviaCorreo(props.getProperty("correo.aviso.cobranza"), "", sHTML, lstAsunto);
        else if(opcion == 9)
            this.enviaCorreo(props.getProperty("correo.aviso.cobranza.terceros"), pstArchivo, sHTML, lstAsunto);
        else if(opcion != 5)
            this.enviaCorreo(pstCorreo, pstArchivo, sHTML, lstAsunto);
        else
        {
            this.enviaCorreo(props.getProperty("correo.aviso.cobranza"), "", sHTML, lstAsunto);

        }
    }



    public void enviaCorreo(String pstCorreo, String pstArchivo, String pstHTML, String pstAsunto)
    {

        ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
        String lstFrom = props.getProperty("reporte.from");

        if ( pstCorreo == null || pstCorreo.length() == 0)
            pstCorreo = props.getProperty("correo.aviso.default");

        String lstTo = pstCorreo;
        if ( props.getProperty("reporte.cc") != null &&  props.getProperty("reporte.cc").length() > 0)
            lstTo += "," + props.getProperty("reporte.cc");

        try
        {
            log.info("Enviado correo:" + pstAsunto + "|" + pstCorreo);
            SendMail_nv sendMail2 = new SendMail_nv(lstFrom, lstTo, pstAsunto , pstHTML, pstArchivo);
            sendMail2.enviaMailHTML();
        }
        catch(Exception e)
        {
            log.info("Error al enviar el correo a " + lstTo + ":" + e.toString());
            log.info("Se intento enviar el archivo:" + pstArchivo);
        }

    }

    public String obtenerFechaHoy()
    {
        Calendar lobHoy = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return formatter.format(Calendar.getInstance().getTime());
    }

    public String obtenerFechaHoyFmt(String pstFormato)
    {
        Calendar lobHoy = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(pstFormato);

        return formatter.format(Calendar.getInstance().getTime());
    }



    public Date obtenerFechaString(String pstFecha) //mmdd
    {
        Calendar lobHoy = Calendar.getInstance();
        try
        {

            int lnuValor = Integer.parseInt(pstFecha.substring(0,2)) - 1;
            lobHoy.set(Calendar.MONTH, lnuValor);

            lnuValor = Integer.parseInt(pstFecha.substring(2,4));
            lobHoy.set(Calendar.DAY_OF_MONTH, lnuValor);
            
        }
        catch(Exception e){
            log.error("error en obtenerFechaString Exception : " + e.toString());
        }

        return lobHoy.getTime();
        
    }



    public List verificaArchivoPISA(File pobArchivo,ConciliacionDTO pobConciliacion)
    {
        String lstLinea;
        int lineaActual=0;
        int lnuTotal = 0;
        double lnuImporteTot = 0;
        long totalLineas = 0;

        Calendar cal = Calendar.getInstance();
        List lobPagos = new ArrayList();
        int lnuError = 0;

        HashMap lobPagosCON = new HashMap(); //pagos OK
        List lobPagosERR = new ArrayList(); //pagos con error

        try {
            BufferedReader lobLector = new BufferedReader(new FileReader(pobArchivo));
            while ((lstLinea = lobLector.readLine()) != null && lnuError == 0) {
                lineaActual++;


                if (lstLinea.contains("PAGOONLINE") && lstLinea.contains("END") ) {
                    //PAGOONLINE00002END
                    try {
                        totalLineas = Long.parseLong(lstLinea.substring(10, 15));
                    } catch (Exception e) {
                        log.error("Error en el archivo " + pobArchivo.getName() + " en la l�nea " + lineaActual + " : " + e.toString());
                    }
                } else if (lstLinea.contains("PAGOONLINE")) {
                    //PAGOONLINE08062011164450MEX00002
                    try {

                        pobConciliacion.setProceso(lstLinea.substring(0, 10));
                        //Ya no aplica, es la libreria el corte
                        //pobConciliacion.setViaCobro(lstLinea.substring(24, 27));
                        Calendar fechaArchivo = Calendar.getInstance();
                        /*
                        * Se cambia la fecha en caso de que se deba registrar la fecha dada en el archivo.
                        *
                        fechaArchivo.set(Integer.parseInt(lstLinea.substring(10, 12)),
                        Integer.parseInt(lstLinea.substring(12, 14)),
                        Integer.parseInt(lstLinea.substring(14, 18)),
                        Integer.parseInt(lstLinea.substring(18, 20)),
                        Integer.parseInt(lstLinea.substring(20, 22)),
                        Integer.parseInt(lstLinea.substring(22, 24)));
                        */
                        //pobConciliacion.setFecha(fechaArchivo.getTime());
                    } catch (Exception e) {
                        log.error("Error en el archivo " + pobArchivo.getName() + " en la l�nea " + lineaActual + " : " + e.toString());
                    }
                }
                else if (lstLinea!= null && lstLinea.length() > 10) // eliminamos el ultimo return enviado
                {
                    //4422444041|012345678912|123456789012345|20110101|23:59:59|20110101|23:59:59|0000030.00|PVI0411|PVI
                    //Segun el ejemplo enviado la hora viene sin puntos:
                    //5555123459|000123313166117|000000001|20111129|111823|20111129|183030|0000046500|BSN111129|BSN
                    try {

                        StringTokenizer st = new StringTokenizer(lstLinea,"|");

                        PagoConcilPISADTO pagoDTO = new PagoConcilPISADTO();
                        pagoDTO.setNoBD(0);
                        pagoDTO.setTelefono(st.nextToken());

                        //viene con ceros a la izq: 000123313166117
                        //Le quitamos los ceros a la izq porque se registra la transaccion tal y como la envia el tercero
                        //pisa lo completa con ceros a la izquierda a 15 posiciones
                        //pagoDTO.setTransaccion(this.obtenTransaccion(st.nextToken()));
                        //le cortamos el folio a 12 posiciones para que empate con lo enviado por el tercero BSN
                        /*
                        String lstTransaccion = st.nextToken();
                        if ( lstTransaccion != null && lstTransaccion.length() == 15 )
                            pagoDTO.setTransaccion(lstTransaccion.substring(3));
                        else
                            pagoDTO.setTransaccion(lstTransaccion);

                         *
                         */
                        //Ya envio el idPago en lugar de tranaccion 3ro para asegurar unicidad
                        String lstIdPago = st.nextToken();
                        if (lstIdPago != null )
                        {
                            Long idPago = Long.parseLong(lstIdPago);
                            pagoDTO.setIdPago(idPago.toString());
                        }
                        pagoDTO.setSecuencia(st.nextToken()); // 000000001

                        pagoDTO.setFechaOperacion(this.obtenerFechaFormato(st.nextToken())); //20111129
                        pagoDTO.setHoraOperacion(this.obtenerHoraFormato(st.nextToken())); //111823
                        //Formateamos la fecha para registrarla
                        pagoDTO.setFechaPosteo(this.obtenerFechaFormato(st.nextToken())); //20111129
                        pagoDTO.setHoraPosteo(this.obtenerHoraFormato(st.nextToken())); //183030
                        //viene sin punto decimal $465--> 0000046500
                        pagoDTO.setImportePagado(this.obtenImporteConDecimal(st.nextToken()));
                        pagoDTO.setCaja(st.nextToken()); //BSN111129
                        pagoDTO.setOficina(st.nextToken()); //BSN

                        lobPagosCON.put(pagoDTO.getIdPago(), pagoDTO);
                        lnuTotal++;

                        lnuImporteTot += Double.parseDouble(pagoDTO.getImportePagado());

                    }
                    catch(Exception e)
                    {
                        log.error("Error en el archivo de conciliacionPISA, linea " + lineaActual);
                        pobConciliacion.setProceso("PAGOS_NOK");
                        lobPagosERR.add(lstLinea);
                    }
                }

            }

            pobConciliacion.setEstado("EN PROCESO");
            pobConciliacion.setImporte(Double.toString(lnuImporteTot));
            //Verificamos el numero de pagos enviados con los reportados
            if ( lnuTotal != totalLineas ) //No corresponde
                pobConciliacion.setEstado("EN PROCESO NCT");
            lobPagos.add(lobPagosCON);
            lobPagos.add(lobPagosERR);
            pobConciliacion.setTotalRegistros(lnuTotal);

        } catch (FileNotFoundException ex) {
            log.error("Error al cargar archivo de conciliacionPISA " + pobArchivo.getName() + ": " + ex.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (IOException ioe) {
            log.error("Error al leer el archivo de conciliacionPISA " + pobArchivo.getName() + ": " + ioe.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (Exception e) {
            log.error("Error en el archivo " + pobArchivo.getName() + " en la l�nea " + lineaActual + " : " + e.toString());
            lobPagos = null;
        }

        return lobPagos;
    }


    public void enviaCorreoRespPISA(String pstHTML)
    {

        ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
        String lstFrom = props.getProperty("reporte.from");
        String lstTo = props.getProperty("reporte.correo.resp.pisa");
        String lstAsunto = props.getProperty("reporte.asunto.resp.pisa");

        if ( props.getProperty("reporte.correo.resp.pisa.cc") != null )
            lstTo += "," + props.getProperty("reporte.correo.resp.pisa.cc");

        try
        {
            SendMail_nv sendMail2 = new SendMail_nv(lstFrom, lstTo, lstAsunto , pstHTML, "");
            sendMail2.enviaMailHTML();
        }
        catch(Exception e)
        {
            log.info("Error al enviar el correo de respuesta a pisa: " + lstTo + ":" + e.toString());
        }

    }

    public void enviaCorreoEGlobal(String pstHTML, int santander)
    {

        ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
        String lstFrom = props.getProperty("reporte.from");
        String lstTo = props.getProperty("reporte.correo.resp.egloblal");
        String lstAsunto = props.getProperty("reporte.asunto.resp.eglobal");
        if ( santander == 1)
            lstAsunto = props.getProperty("reporte.asunto.resp.santander");
        
        try
        {
            SendMail_nv sendMail2 = new SendMail_nv(lstFrom, lstTo, lstAsunto , pstHTML, "");
            sendMail2.enviaMailHTML();
        }
        catch(Exception e)
        {
            log.info("Error al enviar el correo de respuesta a eGlobal: " + lstTo + ":" + e.toString());
        }

    }



    public String generaArchivoRespPISA(List pobPagosNoExisten, List pobPagosNoConciliados, List pobPagosPreviamenteConciliados,
            int pnuTotPagosConciliados , double pnuImpPagosConciliados)
    {
        String lstHTML = null;
        Iterator it = null;

        try
        {

            lstHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Resultados de la conciliaci&oacute;n del d&iacute;a: " + this.obtenerFechaHoyFmt("yyyy/MM/dd") + "<br>" ;


            //Pagos posteados
            lstHTML += "<br><br>Pagos aplicados y postedos: <br><br> " +
                        "<table border=\"0\">" +
                        "<tr>" +
                            "<td>Total de pagos:</td>" +
                            "<td>" + pnuTotPagosConciliados + "</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>Importe Total de pagos:</td>" +
                            "<td>" + pnuImpPagosConciliados + "</td>" +
                        "</tr>" +
                        "</table>" ;


            if ( pobPagosNoConciliados != null && pobPagosNoConciliados.size() > 0 )
            {
                //Pagos sin conciliar
                lstHTML += "<br><br>Pagos sin conciliar: <br><br> " +
                        "<table border=\"0\">" +
                        "<tr>" +
                            "<td>OFICINA</td>" +
                            "<td>FECHA</td>" +
                            "<td>TELEFONO</td>" +
                            "<td>IMPORTE</td>" +
                            "<td>SECUENCIA</td>" +
                        "</tr>" ;

                it = pobPagosNoConciliados.iterator();
                while (it.hasNext())
                {
                    PagoConcilPISADTO lobPagoPISA = (PagoConcilPISADTO)it.next();
                    lstHTML += "<tr>" +
                                "<td>" + lobPagoPISA.getOficina() + "</td>" +
                                "<td>" + lobPagoPISA.getFechaOperacion() + "</td>" +
                                "<td>" + lobPagoPISA.getTelefono() + "</td>" +
                                "<td>" + lobPagoPISA.getImportePagado() + "</td>" +
                                "<td>" + lobPagoPISA.getSecuencia() + "</td>" +
                               "</tr>" ;
                }
                lstHTML += "</table>" ;
            }

            if ( pobPagosNoExisten != null && pobPagosNoExisten.size() > 0 )
            {
                //Pagos no localizados
                lstHTML += "<br><br>Pagos no localizados: <br><br> " +
                            "<table border=\"0\">" +
                            "<tr>" +
                                "<td>OFICINA</td>" +
                                "<td>FECHA</td>" +
                                "<td>TELEFONO</td>" +
                                "<td>IMPORTE</td>" +
                                "<td>SECUENCIA</td>" +
                            "</tr>" ;

                it = pobPagosNoExisten.iterator();
                while (it.hasNext())
                {
                    PagoConcilPISADTO lobPagoPISA = (PagoConcilPISADTO)it.next();
                    lstHTML += "<tr>" +
                                "<td>" + lobPagoPISA.getOficina() + "</td>" +
                                "<td>" + lobPagoPISA.getFechaOperacion() + "</td>" +
                                "<td>" + lobPagoPISA.getTelefono() + "</td>" +
                                "<td>" + lobPagoPISA.getImportePagado() + "</td>" +
                                "<td>" + lobPagoPISA.getSecuencia() + "</td>" +
                               "</tr>" ;
                }
                lstHTML += "</table>" ;
            }

            if ( pobPagosPreviamenteConciliados != null && pobPagosPreviamenteConciliados.size() > 0 )
            {
                //Pagos previamente conciliacion
                lstHTML += "<br><br>Pagos previamente conciliados: <br><br> " +
                            "<table border=\"0\">" +
                            "<tr>" +
                                "<td>OFICINA</td>" +
                                "<td>FECHA</td>" +
                                "<td>TELEFONO</td>" +
                                "<td>IMPORTE</td>" +
                                "<td>SECUENCIA</td>" +
                            "</tr>" ;

                it = pobPagosPreviamenteConciliados.iterator();
                while (it.hasNext())
                {
                    PagoConcilPISADTO lobPagoPISA = (PagoConcilPISADTO)it.next();
                    lstHTML += "<tr>" +
                                "<td>" + lobPagoPISA.getOficina() + "</td>" +
                                "<td>" + lobPagoPISA.getFechaOperacion() + "</td>" +
                                "<td>" + lobPagoPISA.getTelefono() + "</td>" +
                                "<td>" + lobPagoPISA.getImportePagado() + "</td>" +
                                "<td>" + lobPagoPISA.getSecuencia() + "</td>" +
                               "</tr>" ;
                }
                lstHTML += "</table>" ;
            }
            
            lstHTML += "<br>" ;


        }
        catch(Exception e)
        {
            log.error("error en generaArchivoErrorPISA IOException : " + e);
        }

        return lstHTML;

    }


    public String generaArchivoRespEGlobal(List pobPagosEncolados, long pnuTotPagosConciliados , double pnuImpPagosConciliados)
    {
        String lstHTML = null;
        Iterator it = null;
        try
        {
            lstHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Resultados de la conciliaci&oacute;n eGlobal del d&iacute;a: " + this.obtenerFechaHoyFmt("yyyy/MM/dd") + "<br>" ;
            
            //Pagos posteados
            lstHTML += "<br><br>Pagos conciliados: <br><br> " +
                        "<table border=\"0\">" +
                        "<tr>" +
                            "<td>Total de pagos:</td>" +
                            "<td>" + pnuTotPagosConciliados + "</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>Importe Total de pagos:</td>" +
                            "<td>" + pnuImpPagosConciliados + "</td>" +
                        "</tr>" +
                        "</table>" ;


            if ( pobPagosEncolados != null && pobPagosEncolados.size() > 0 )
            {
                //Pagos sin conciliar
                lstHTML += "<br><br>Los pagos que se muestran sin referencia fueron encolados por conciliaci�n. Los pagos con la leyenda NO EXISTE favor de verificar si est� aplicado.<br><br> " +
                        "<table border=\"0\">" +
                        "<tr>" +
                            "<td>TELEFONO</td>" +
                            "<td>REFERENCIA</td>" +
                            "<td>TRANSACCION</td>" +
                            "<td>IMPORTE</td>" +
                        "</tr>" ;

                it = pobPagosEncolados.iterator();
                while (it.hasNext())
                {
                    PagoMKTDTO lobPagoBCO = (PagoMKTDTO)it.next();
                    lstHTML += "<tr>" +
                                "<td>" + lobPagoBCO.getTelefono() + "</td>" +
                                "<td>" + lobPagoBCO.getReferencia() + "</td>" +
                                "<td>" + lobPagoBCO.getIdPago() + "</td>" +
                                "<td>" + lobPagoBCO.getMontoPagar() + "</td>" +
                               "</tr>" ;
                }
                lstHTML += "</table>" ;
            }

            lstHTML += "<br>" ;

        }
        catch(Exception e)
        {
            log.error("error en generaArchivoErrorBanco IOException : " + e);
        }
        return lstHTML;
    }

    public String generaArchivoRespSantander(List pobPagosEncolados, long pnuTotPagosConciliados , double pnuImpPagosConciliados, long pnuTotPagosRechazados)
    {
        String lstHTML = null;
        Iterator it = null;
        try
        {
            lstHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>" +
                        "Resultados de la conciliaci&oacute;n Santander del d&iacute;a: " + this.obtenerFechaHoyFmt("yyyy/MM/dd") + "<br>" ;


            //Pagos aceptados
            lstHTML += "<br><br>Pagos conciliados: <br><br> " +
                        "<table border=\"0\">" +
                        "<tr>" +
                            "<td>Total de pagos:</td>" +
                            "<td>" + pnuTotPagosConciliados + "</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>Importe Total de pagos:</td>" +
                            "<td>" + pnuImpPagosConciliados + "</td>" +
                        "</tr>" +
                        "</table>" ;
            
            if ( pnuTotPagosRechazados > 0) {
                //Pagos rechazados
                lstHTML += "<br><br>Pagos rechazados: <br><br> " +
                            "<table border=\"0\">" +
                            "<tr>" +
                                "<td>Total de rechazos:</td>" +
                                "<td>" + pnuTotPagosRechazados + "</td>" +
                            "</tr>" +
                            "</table>" ;

            }

            if ( pobPagosEncolados != null && pobPagosEncolados.size() > 0 )
            {
                //Pagos sin conciliar
                lstHTML += "<br><br>Los pagos listados fueron encolados por conciliaci�n. <br><br> " +
                        "<table border=\"0\">" +
                        "<tr>" +
                            "<td>TELEFONO</td>" +
                            "<td>REFERENCIA</td>" +
                            "<td>TRANSACCION</td>" +
                            "<td>IMPORTE</td>" +
                        "</tr>" ;

                it = pobPagosEncolados.iterator();
                while (it.hasNext())
                {
                    PagoMKTDTO lobPagoBCO = (PagoMKTDTO)it.next();
                    lstHTML += "<tr>" +
                                "<td>" + lobPagoBCO.getTelefono() + "</td>" +
                                "<td>" + lobPagoBCO.getReferencia() + "</td>" +
                                "<td>" + lobPagoBCO.getIdPago() + "</td>" +
                                "<td>" + lobPagoBCO.getMontoPagar() + "</td>" +
                               "</tr>" ;
                }
                lstHTML += "</table>" ;
            }

            lstHTML += "<br>" ;

        }
        catch(Exception e)
        {
            log.error("error en generaArchivoErrorBanco IOException : " + e);
        }
        return lstHTML;
    }





    public String formateaCantidad(String pstValor, int pnuLongitud)
    {
        String lstValor = null;

        if ( pstValor != null )
        {
            lstValor = "";
            for (int i=pstValor.length(); i<pnuLongitud; i++)
                lstValor += "0";
            lstValor += pstValor;

        }
        return lstValor;
    }

    public String obtenImporteConDecimal(String pstImporte)
    {
        String lstImporte = null;

        try
        {
            if (pstImporte != null )
            {
                double impPISA = Double.parseDouble(pstImporte) / 100;
                lstImporte = Double.toString(impPISA);
            }
        }
        catch(Exception e)
        {
            lstImporte = null;
        }
        return lstImporte;
    }

    public String obtenTransaccionXpos(String pstTransaccion, int pnuPosiciones)
    {
        String lstTransaccion = "";

        if ( pstTransaccion != null )
        {
            for ( int i = pstTransaccion.length(); i<pnuPosiciones; i++)
                lstTransaccion += "0";

            lstTransaccion += pstTransaccion;
        }
        
        return lstTransaccion;
    }

    public String obtenSecuencia(String pstSecuencia)
    {
        String lstSecuencia = null;

        try
        {
            if (pstSecuencia != null )
            {
                long sec = Long.parseLong(pstSecuencia) ;
                lstSecuencia = Long.toString(sec);
            }
        }
        catch(Exception e)
        {
            lstSecuencia = null;
        }
        return lstSecuencia;
    }

    public String obtenerHoraFormato(String pstHora) //HHMMSS
    {
        String lstHora = null;

        if ( pstHora != null && pstHora.length() > 5 )
        {
            lstHora = pstHora.substring(0,2) + ":" +
                    pstHora.substring(2,4) + ":" +
                    pstHora.substring(4) ;

        }
        else
            lstHora = pstHora;

        return lstHora;
        
    }

    public String obtenerFechaFormato(String pstFecha) //yyyymmdd 20110818
    {
        String lstFecha = null;

        if ( pstFecha != null && pstFecha.length() > 5 )
        {
            lstFecha = pstFecha.substring(0,4) + "/" +
                    pstFecha.substring(4,6) + "/" +
                    pstFecha.substring(6) ;

        }
        else
            lstFecha = pstFecha;

        return lstFecha;

    }

    public void enviaCorreoPagoSinAplicar(String pstHTML)
    {

        ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
        String lstFrom = props.getProperty("reporte.from");
        String lstTo = props.getProperty("correo.aviso.cobranza");
        String lstAsunto = props.getProperty("reporte.asunto.sinaplicar");

        try
        {
            SendMail_nv sendMail2 = new SendMail_nv(lstFrom, lstTo, lstAsunto , pstHTML, "");
            sendMail2.enviaMailHTML();
        }
        catch(Exception e)
        {
            log.info("Error al enviar el correo de respuesta a pisa: " + lstTo + ":" + e.toString());
        }

    }

    public void enviaCorreoAntiFraude(String pstHTML)
    {

        ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
        String lstFrom = props.getProperty("reporte.from");
        String lstTo = "brcebrer@blitzsoftware.com.mx, pgrande@blitzsoftware.com.mx, GCRUZM@telmex.com";
        String lstAsunto = props.getProperty("Resultado an�lisis");

        try
        {
            SendMail_nv sendMail2 = new SendMail_nv(lstFrom, lstTo, lstAsunto , pstHTML, "");
            sendMail2.enviaMailHTML();
        }
        catch(Exception e)
        {
            log.info("Error al enviar el correo de respuesta a pisa: " + lstTo + ":" + e.toString());
        }

    }



    public List verificaArchivoBanco(File pobArchivo,Concilia3roDTO pobConciliacion)
    {
        String lstLinea;
        int lineaActual=0;
        int lnuTotal = 0;
        double lnuImporteTot = 0;
        long totalLineas = 0;

        Calendar cal = Calendar.getInstance();
        List lobPagos = new ArrayList();
        int lnuError = 0;

        HashMap lobPagosCON = new HashMap(); //pagos OK
        List lobPagosERR = new ArrayList(); //pagos con error

        try {
            BufferedReader lobLector = new BufferedReader(new FileReader(pobArchivo));
            while ((lstLinea = lobLector.readLine()) != null && lnuError == 0) {
                lineaActual++;

                if (lstLinea!= null && lstLinea.length() > 10) // eliminamos el ultimo return enviado
                {
                    try {

                        StringTokenizer st = new StringTokenizer(lstLinea,"|");
                        //FECHA|HORA|MOV5=VENTA|AFILIACION|AUTORIZACION|TRANSACCION|NUMEROTC|TIPOTC|MONTO
                        //30/11/2011|18:54:15|5|1234567|016657|2921041|5180040000000001|5|4849.00

                        PagoConcilBancoDTO pagoDTO = new PagoConcilBancoDTO();
                        pagoDTO.setComercio("MKT");
                        pagoDTO.setSucursal("0001"); //la misma sucursal
                        pagoDTO.setTipoProceso("E");//Todos en linea
                        //pagoDTO.setFormaPago("2");//TC
                        pagoDTO.setFormaPago("5");//TC
                        pagoDTO.setFecha(st.nextToken());
                        pagoDTO.setHora(st.nextToken());
                        pagoDTO.setMovimiento(st.nextToken());
                        pagoDTO.setAfiliacion(st.nextToken());
                        pagoDTO.setAutorizacion(st.nextToken());
                        String transaccion = st.nextToken(); //este es el id del pago en mt_pago y es transaccion en 02_pago
                        Long transac = Long.valueOf(transaccion);
                        pagoDTO.setTransaccion(transac.toString());
                        pagoDTO.setNumeroTC(st.nextToken());
                        pagoDTO.setTipoTC(st.nextToken());
                        pagoDTO.setMonto(st.nextToken());

                        //La llave es la transaccion
                        //Filtramos solo las ventas y filiacion = 8457517
                        if ( pagoDTO.getMovimiento()!=null && pagoDTO.getMovimiento().equals("5")
                                && pagoDTO.getAfiliacion()!=null && pagoDTO.getAfiliacion().equals("8457517") )
                        {
                            lobPagosCON.put(pagoDTO.getTransaccion().trim(), pagoDTO);
                            lnuTotal++;
                            lnuImporteTot += Double.parseDouble(pagoDTO.getMonto());
                        }

                    }
                    catch(Exception e)
                    {
                        log.error("Error en el archivo de conciliacionBanco, linea " + lineaActual);
                        pobConciliacion.setEstado("ARCHIVO_NOK");
                        lobPagosERR.add(lstLinea);
                    }
                }

            }

            pobConciliacion.setEstado("EN PROCESO");
            pobConciliacion.setMontoTotal(lnuImporteTot);
            lobPagos.add(lobPagosCON);
            lobPagos.add(lobPagosERR);
            pobConciliacion.setTotalRegistros(lnuTotal);

        } catch (FileNotFoundException ex) {
            log.error("Error al cargar archivo de conciliacionBanco " + pobArchivo.getName() + ": " + ex.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (IOException ioe) {
            log.error("Error al leer el archivo de conciliacionBanco " + pobArchivo.getName() + ": " + ioe.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (Exception e) {
            log.error("Error en el archivo " + pobArchivo.getName() + " en la l�nea " + lineaActual + " : " + e.toString());
            lobPagos = null;
        }

        return lobPagos;
    }


    public List verificaArchivoSantander(File pobArchivo,Concilia3roDTO pobConciliacion)
    {
        String lstLinea;
        int lineaActual=0;
        int lnuTotal = 0;
        double lnuImporteTot = 0;
        long totalLineas = 0;

        Calendar cal = Calendar.getInstance();
        List lobPagos = new ArrayList();
        int lnuError = 0;

        HashMap lobPagosCON = new HashMap(); //pagos OK
        HashMap lobPagosRECH = new HashMap(); //pagos rechazados
        List lobPagosERR = new ArrayList(); //pagos con error

        try {
            BufferedReader lobLector = new BufferedReader(new FileReader(pobArchivo));
            while ((lstLinea = lobLector.readLine()) != null && lnuError == 0) {
                lineaActual++;

                if (lstLinea!= null && lstLinea.length() > 10) // eliminamos el ultimo return enviado
                {
                    try {

                        //StringTokenizer st = new StringTokenizer(lstLinea,"|");

                        String st[] = lstLinea.split("\\,");
                        /*
458411693,APP TELMEX,80124867,USUARIO SISTEMAAPP TELMEX ,E-COMMERCE,Contado,520416******6590,,,MOTO,7123378,TELMEX,389,MXN,25/08/2017,16:04:55,MasterCard,D,BANAMEX,denied,05,VENTA,,
458424674,APP TELMEX,80125901,USUARIO SISTEMAAPP TELMEX ,E-COMMERCE,Contado,528851******2094,,059528,MOTO,7123378,TELMEX,187,MXN,25/08/2017,16:35:53,MasterCard,C,BANAMEX,approved,00,VENTA,1,28/08/17
                         */

                        /*
699620005,APP TELMEX,25413,USUARIO SISTEMAAPP TELMEX E-COMMERCE,Contado,415231FMCJW08395,,,MOTO,7123378,TELMEX,10,MXN,27/05/2019,23:56:30,Visa,D,BBVA BANCOMER,denied,57,VENTA,*
                         */

                        PagoConcilBancoDTO pagoDTO = new PagoConcilBancoDTO();
                        pagoDTO.setComercio("APT"); //tenemos que validar que realmente sea de APT desde 2enero2018 se recibiran de MKT tambien
                        pagoDTO.setSucursal("0001"); //la misma sucursal
                        pagoDTO.setTipoProceso("E");//Todos en linea
                        //pagoDTO.setFormaPago("2");//TC
                        pagoDTO.setFormaPago("5");//TC

                        pagoDTO.setFoliocpagos(st[0]);
                        String referencia = st[2]; //este es el id del pago en mt_pago y es transaccion en 02_pago
                        Long ref = Long.valueOf(referencia);
                        pagoDTO.setTransaccion(ref.toString());
                        pagoDTO.setNumeroTC(st[6]); //4 ultimos digitos
                        pagoDTO.setAutorizacion(st[8]);//autorizacion
                        pagoDTO.setMonto(st[12]);
                        pagoDTO.setFecha(st[14]); // la fecha viene en formato dd/mm/yyyy
                        pagoDTO.setHora(st[15]);
                        pagoDTO.setTipoTC(st[16]);//marca visa, mastercard, amex

                        String tipoTarj = st[17];
                        if ( tipoTarj != null && tipoTarj.length() > 0) { //tipo tarjeta
                            if ( tipoTarj.startsWith("C")) //Credito
                                pagoDTO.setDebitoCredito("04");
                            else if ( tipoTarj.startsWith("D")) //Debito
                                pagoDTO.setDebitoCredito("28");
                            else
                                pagoDTO.setDebitoCredito(tipoTarj);
                        }
                        pagoDTO.setBanco(st[18]);//Banco

                        pagoDTO.setEstatus(st[19]);//approved o denied

                        pagoDTO.setPagoSimilar(st[20]); //cd_response codigo error en caso de negacion
                        pagoDTO.setMovimiento(st[21]); //tipo op = VENTA

                        //Se incluye bandera de domiciliacion y guardartoken
/*
 * negado:
694605769,APP TELMEX,108610525,USUARIO SISTEMAAPP TELMEX ,E-COMMERCE,Contado,41523140E5Y80466,,,MOTO,7123378,TELMEX,1774,MXN,16/05/2019,00:00:53,Visa,D,BBVA BANCOMER,denied,82,VENTA,
 * Aprobado:
694605775,APP TELMEX,108610474,USUARIO SISTEMAAPP TELMEX ,E-COMMERCE,Contado,481516Y8N5FE0494,,095196,MOTO,7123378,TELMEX,389,MXN,16/05/2019,00:00:55,Visa,D,BBVA BANCOMER,approved,00,VENTA,1
 */
                        
                        if ( st.length > 22 ) //indica si fue conciliado. bandera de santander no util para nosotros
                            ;//pagoDTO.setDomiciliarToken(st[22]); //

                        if ( st.length > 23 ) //ESta es la bandera de que se pago con una tarjeta domiciliada
                            pagoDTO.setPagoConToken(st[23]);

                        if ( st.length > 24 ) //ESta es la bandera peticion de domiciliar
                            pagoDTO.setDomiciliarToken(st[24]);
                        

                        //modifica la fecha a formato YYYY/MM/DD
                        if ( st[14] != null)
                            pagoDTO.setFecha(Utilerias.cambiaFtoFechaDDMMYYYY(st[14]));
                        

                        if ( pagoDTO.getEstatus()!=null && pagoDTO.getEstatus().startsWith("approved"))
                        {
                            lobPagosCON.put(pagoDTO.getTransaccion().trim(), pagoDTO);
                            lnuTotal++;
                            lnuImporteTot += Double.parseDouble(pagoDTO.getMonto());
                        }
                        else //son rechazos
                        {
                            lobPagosRECH.put(pagoDTO.getTransaccion().trim(), pagoDTO);
//                            lnuTotal++;
//                            lnuImporteTot += Double.parseDouble(pagoDTO.getMonto());
                        }

                    }
                    catch(Exception e)
                    {
                        log.error("Error en el archivo de conciliacionBanco, linea " + lineaActual);
                        pobConciliacion.setEstado("ARCHIVO_NOK");
                        lobPagosERR.add(lstLinea);
                    }
                }

            }

            pobConciliacion.setEstado("EN PROCESO");
            pobConciliacion.setMontoTotal(lnuImporteTot);
            lobPagos.add(lobPagosCON);
            lobPagos.add(lobPagosRECH);
            lobPagos.add(lobPagosERR);
            pobConciliacion.setTotalRegistros(lnuTotal);

        } catch (FileNotFoundException ex) {
            log.error("Error al cargar archivo de conciliacionBanco " + pobArchivo.getName() + ": " + ex.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (IOException ioe) {
            log.error("Error al leer el archivo de conciliacionBanco " + pobArchivo.getName() + ": " + ioe.toString());
            pobConciliacion.setEstado("ARCHIVO_NOK");
            lobPagos = null;
        } catch (Exception e) {
            log.error("Error en el archivo " + pobArchivo.getName() + " en la l�nea " + lineaActual + " : " + e.toString());
            lobPagos = null;
        }

        return lobPagos;
    }

    public static boolean soloCaracteresNumericos(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);

        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!Character.isDigit(ch)){
                return false;
            }
        }
        return true;
    }

    public String codigoConsulta(String telefono)
    {
        String codigo = null;
        UtilPago util = new UtilPago();

        ConsultaDTO consulta = new ConsultaDTO();
        consulta.setTelefono(telefono);
        consulta.setAdquiriente("X_X");
        consulta.setTiendaTerminal("01");
        consulta.setTransaccion("01");
        //String lstPeticion = util.generalXMLConsulta(consulta);
        String lstPeticion = "";
        String lstRespuesta = util.enviaPeticionConsulta(lstPeticion);

        /*String lstRespuesta = "<root><accion>ConsultaRespuesta</accion><telefono>555" +
            "3696758</telefono><saldo>575.00</saldo><saldovencido>0.00</saldovencido><minimor" +
            "eanudacion>0.00</minimoreanudacion><error>00</error></root>" ;
         *
         */

        if ( lstRespuesta != null && lstRespuesta.indexOf("<error>") != -1 )
            codigo = lstRespuesta.substring(lstRespuesta.indexOf("<error>")+7, lstRespuesta.indexOf("</error>"));

        return codigo;
    }

/*
    public static void main(String[] args) {

        UtilConciliaPago util = new UtilConciliaPago();
        File archivo = new File("C:\\Blitz\\MisProyectos\\12190_PagoR\\ArchivosConcilAtrasadas\\BTF20170323.txt");
        List lobPagosFL = util.verificaArchivo(archivo, new Concilia3roDTO());

    if ( lobPagosFL != null)
            {
                Iterator it2 = lobPagosFL.iterator();
                while( it2.hasNext() )
                {
                    PagoConciliacionDTO pago = (PagoConciliacionDTO)it2.next();
                    System.out.println(pago.getTelefono() );

                }

            }

    }
 * 
 */


}
