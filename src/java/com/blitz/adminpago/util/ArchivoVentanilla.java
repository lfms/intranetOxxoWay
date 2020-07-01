/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.util;

import com.blitz.adminpago.dto.PagoMKTDTO;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;
import java.util.List;

/**
 *
 * @author PGRANDE
 */
public class ArchivoVentanilla {

    public static String DETAIL_C1 = "1";
    public static String DETAIL_C8 = "0";
    public static String DETAIL_C9 = "01";
    public static String DETAIL_C15 = " ";
    public static String DETAIL_C16 = "2";
    public static String DETAIL_C20 = "80";

    public static String HEADER_C1 = "0";
    public static String HEADER_C4 = "0011";
    public static String HEADER_C5 = "TELEFONOS DE MEXICO";
    public static String HEADER_C6 = "2";
    public static String HEADER_C8 = "80";

    public static String TRAILER_C1 = "9";
    public static String TRAILER_C6 = "80";



    public static boolean soloCaracteresNumericos(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);

        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!Character.isDigit(ch)){
                return false;
            }
        }
        return true;
    }



    public static String formateaHead(String fechaPago, String numOficina) {
        byte dataByte[] = new byte[512];

        String bufferOut = HEADER_C1 + llenarEspacios(" ", 1) +
                llenarEspacios(fechaPago, 8) + HEADER_C4 +
                llenarEspacios(HEADER_C5,38) + HEADER_C6 +
                //llenarEspacios(" ", 65) +HEADER_C8 + "\n";
                llenarEspacios(" ", 65) + numOficina + "\n";

        return bufferOut.toString();
    }


    public static String formateaDetail(PagoMKTDTO pago, String numOficina) {
        byte dataByte[] = new byte[512];

        String fecha = pago.getFechaEstatus();
        if (pago.getFechaEstatus().indexOf("/") > 0 ) {
            fecha = pago.getFechaEstatus().replaceAll("/", "");
        }

        String importe = importeFormato(Double.parseDouble(pago.getMontoPagar()));

        String bufferOut = DETAIL_C1 + llenarEspacios(" ", 3) +
                llenarEspacios(" ", 10) +  llenarEspacios(" ", 7) +
                llenarEspacios(" ", 2) +  obtieneMontoFixLong(13, importe) +
                llenarEspacios(pago.getTelefono(), 10) +
                DETAIL_C8 + DETAIL_C9 +
                llenarEspacios(" ", 8) + llenarEspacios(pago.getFechaEstatus(), 8) +
                llenarEspacios(fecha, 8) +
                llenarEspacios(" ", 17) + llenarEspacios(" ", 13) +
                DETAIL_C15 + DETAIL_C16 +
                llenarEspacios(" ", 4) + llenarEspacios(" ", 2) +
                //llenarEspacios(" ", 7) + DETAIL_C20 + "\n"; // el detal 20 es el numero de oficina
                llenarEspacios(" ", 7) + numOficina + "\n"; // el detal 20 es el numero de oficina

        return bufferOut.toString();
    }

    public static String formateaTrail(String totPagos, double totImporte, String numOficina) {
        byte dataByte[] = new byte[512];

        String importe = importeFormato(totImporte);


        String bufferOut = TRAILER_C1 + llenarEspacios(" ", 1) +
                obtieneTotFixLong(9,totPagos) + obtieneMontoFixLong(17, importe) +
                //llenarEspacios(" ", 90) + TRAILER_C6 + "\n";
                llenarEspacios(" ", 90) + numOficina + "\n";

        return bufferOut.toString();
    }


    private static String llenarEspacios(String dato, int max) {
        String tmp = dato;
	for(int i = 0; i < max; i++) {
            if(tmp.length() < max) {
                tmp = tmp + " ";
            } else {
                return tmp;
            }
	}

	return tmp;

    }


    public static String obtieneMontoFixLong(int longitud, String pstMonto)
    {
        String lstMonto = "";

        if ( pstMonto != null && pstMonto.length() > 0 )
        {
            //el 306.09 lo transforma a 306.08 con el int
            //int lnuMonto = (int)(Double.parseDouble(pstMonto)*100);
            long lnuMonto = Math.round(Double.parseDouble(pstMonto)*100);

            String lstMontoNew = String.valueOf(lnuMonto);
            for(int i=0; i < (longitud-lstMontoNew.length()); i++ )
                lstMonto += "0";
            lstMonto += String.valueOf(lnuMonto);
        }
        else {
            lstMonto="";
            for (int i=0; i<longitud; i++)
                lstMonto += "0";
        }

        return lstMonto;

    }

    public static String obtieneTotFixLong(int longitud, String pstMonto)
    {
        String lstMonto = "";

        if ( pstMonto != null && pstMonto.length() > 0 )
        {
            //el 306.09 lo transforma a 306.08 con el int
            //int lnuMonto = (int)(Double.parseDouble(pstMonto)*100);
            long lnuMonto = Math.round(Double.parseDouble(pstMonto));

            String lstMontoNew = String.valueOf(lnuMonto);
            for(int i=0; i < (longitud-lstMontoNew.length()); i++ )
                lstMonto += "0";
            lstMonto += String.valueOf(lnuMonto);
        }
        else {
            lstMonto="";
            for (int i=0; i<longitud; i++)
                lstMonto += "0";
        }

        return lstMonto;

    }


    public static String importeFormato(double importe) {

        DecimalFormat format = new DecimalFormat("#.##");

        return format.format(importe);

    }


}
