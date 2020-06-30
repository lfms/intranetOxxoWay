/*
 * COMPONENTE Utilerias
 * RESPONSABLE GPF
 * OBJETIVO utilerias
 * VERSION 1.0
 * FECHA 25/04/2007
 */
package com.blitz.adminpago.util;

import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;


public class Utilerias {
    private Logger log = Logger.getLogger("com.blitz.adminpagoline.util.Utilerias");
    private MailSender mailSender;
    private SimpleMailMessage templateMessage;
    
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
    
    /** Creates a new instance of Utilerias */
    public Utilerias() {
    }
    
    /**
     * @param int longitud
     * @return String
     */
    public static String generarCadena(int longitud) {
        Random random = new Random();
        String[] letra ={"0","1","2","3","4","5","6","7","8","9",
        "A","B","C","D","E","F","G","H","I","J",
        "K","L","M","N","O","P","Q","R","S","T",
        "U","V","W","X","Y","Z"};
        String cadena = "";
        int x;
        
        for (int i = 0; i < longitud; i++) {
            // x = (int)(random.nextDouble() * 36.0);
            x = random.nextInt(36);
            cadena = cadena + letra[x];
        }
        
        return cadena;
    }
    
    /**
     * @param cadena
     * @return String
     */
    public static String encriptarCadena(String cadena) {
        try{
            Cifrado c = new Cifrado();
            return c.encriptar(cadena);
        }catch(Exception e){
            return null;
        }
    }
    
    /**
     * @param cadena
     * @return String
     */
    public static String desencriptarCadena(String cadena) {
        try{
            Cifrado c = new Cifrado();
            return c.desencriptar(cadena);
        }catch(Exception e){
            return null;
        }
    }
    
    /**
     * METODO enviarCorreo
     * OBJETIVO Enviar Correo
     * @param para
     * @param texto
     * @return String
     */
    /*
    public void enviarCorreo(String para, String asunto, String texto) throws MailNotSendException {
        // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(para);
        msg.setSubject(asunto);
        msg.setText(texto);
        try{
            this.mailSender.send(msg);
        } catch(MailException ex) {
            // simply log it and go on...
            log.error("enviarCorreo: error al enviar correo");
            throw new MailNotSendException(); 
        } catch(Exception e){
            log.error(e);
            throw new MailNotSendException();
        }
    }*/
    
    /**
     * METODO enviarCorreo
     * OBJETIVO Enviar Correo
     * @param para
     * @param texto
     * @return String
     */
    /*
    public void enviarCorreo(String[] para,String[]ccp, String asunto, String texto) throws MailNotSendException {
        // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(para);
        msg.setCc(ccp);
        msg.setSubject(asunto);
        msg.setText(texto);
        try{
            this.mailSender.send(msg);
        } catch(MailException ex) {
            // simply log it and go on...
            log.warn(ex.getMessage());
            System.out.println(ex.toString());
            throw new MailNotSendException(); 
        }
    }
*/
    /**
     * METODO obtenerMesesAntes
     * OBJETIVO obtiene una lista de meses con formato AAAAMM
     * @param mesActual
     * @param cuantos
     * @return List
     */
    public static List obtenerMesesAntes(String mesActual, int cuantos) {
        Calendar cal = Calendar.getInstance();
        String month = mesActual.substring(4);
        String year = mesActual.substring(0,4);
        ArrayList listMeses = new ArrayList();
        cal.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
        listMeses.add(mesActual);
        for(int i=1; i<cuantos; i++) {
            cal.add(Calendar.MONTH,-1);
            if( cal.get(Calendar.MONTH)<9)
                mesActual = cal.get(Calendar.YEAR)+"0"+(cal.get(Calendar.MONTH)+1);
            else
                mesActual = cal.get(Calendar.YEAR)+""+(cal.get(Calendar.MONTH)+1);
            listMeses.add(mesActual);
        }
        
        return listMeses;
    }
    
    public static String obtenerNombreMes(int mes){
        String nombre=null;
        
        switch (mes){
            case 1: nombre = "ENERO";
            break;
            case 2: nombre = "FEBRERO";
            break;
            case 3: nombre = "MARZO";
            break;
            case 4: nombre = "ABRIL";
            break;
            case 5: nombre = "MAYO";
            break;
            case 6: nombre = "JUNIO";
            break;
            case 7: nombre = "JULIO";
            break;
            case 8: nombre = "AGOSTO";
            break;
            case 9: nombre = "SEPTIEMBRE";
            break;
            case 10: nombre = "OCTUBRE";
            break;
            case 11: nombre = "NOVIEMBRE";
            break;
            case 12: nombre = "DICIEMBRE";
            break;
            default: break;
        }
        return nombre;
    }
    
    public static String obtenerNombreMesEspaniol(String mes){
        String nombre=null;
        
            if(mes.equalsIgnoreCase("January")){
                nombre="Enero";
            }else if(mes.equalsIgnoreCase("February")){
                nombre="Febrero";
            }else if(mes.equalsIgnoreCase("March")){
                nombre="Marzo";
            }else if(mes.equalsIgnoreCase("April")){
                nombre="Abril";
            }else if(mes.equalsIgnoreCase("May")){
                nombre="Mayo";
            }else if(mes.equalsIgnoreCase("June")){
                nombre="Junio";
            }else if(mes.equalsIgnoreCase("July")){
                nombre="Julio";
            }else if(mes.equalsIgnoreCase("August")){
                nombre="Agosto";
            }else if(mes.equalsIgnoreCase("September")){
                nombre="Septiembre";
            }else if(mes.equalsIgnoreCase("October")){
                nombre="Octubre";
            }else if(mes.equalsIgnoreCase("November")){
                nombre="Noviembre";
            }else if(mes.equalsIgnoreCase("December")){
                nombre="Diciembre";
            }else if(mes.equalsIgnoreCase("enero")){
                nombre="Enero";
            }else if(mes.equalsIgnoreCase("febrero")){
                nombre="Febrero";
            }else if(mes.equalsIgnoreCase("marzo")){
                nombre="Marzo";
            }else if(mes.equalsIgnoreCase("abril")){
                nombre="Abril";
            }else if(mes.equalsIgnoreCase("mayo")){
                nombre="Mayo";
            }else if(mes.equalsIgnoreCase("junio")){
                nombre="Junio";
            }else if(mes.equalsIgnoreCase("julio")){
                nombre="Julio";
            }else if(mes.equalsIgnoreCase("agosto")){
                nombre="Agosto";
            }else if(mes.equalsIgnoreCase("septiembre")){
                nombre="Septiembre";
            }else if(mes.equalsIgnoreCase("octubre")){
                nombre="Octubre";
            }else if(mes.equalsIgnoreCase("noviembre")){
                nombre="Noviembre";
            }else if(mes.equalsIgnoreCase("diciembre")){
                nombre="Diciembre";
            }else{
                return mes;
            }
        
        return nombre;
    }
    
    public static int obtenerUltimoDia(int ipMes, int ipAnio) {
        int iDia = 31;
        switch (ipMes) {
            case 2:
                iDia = 28;
                if ( (ipAnio%4) == 0 )
                    iDia++;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                iDia = 30;
                break;
        }
        
        return iDia;
    }
    
    
    
    
    
    public static String getMeshexadecimal(String mes)
    {
        String hex="";
        
        if(Integer.parseInt(mes)==10)
            hex = "A";
        else if(Integer.parseInt(mes)==11)
            hex = "B";
        else if(Integer.parseInt(mes)==12)
            hex = "C";
        else hex = "" + Integer.parseInt(mes);					
        
        return hex;
    }
    
    public static String obtenerHexAMes(String mes1){
        String month = "";
            if(mes1.equals("1")){
                month="01";
            }else if(mes1.equals("2")){
                month="02";
            }else if(mes1.equals("3")){
                month="03";
            }else if(mes1.equals("4")){
                month="04";
            }else if(mes1.equals("5")){
                month="05";
            }else if(mes1.equals("6")){
                month="06";
            }else if(mes1.equals("7")){
                month="07";
            }else if(mes1.equals("8")){
                month="08";
            }else if(mes1.equals("9")){
                month="09";
            }else if(mes1.equals("A")){
                month="10";
            }else if(mes1.equals("B")){
                month="11";
            }else if(mes1.equals("C")){
                month="12";
            }
        return month;
    }
    
    public static double obtieneVariacionPorcentual(double a, double b){
        double variacion=0;
        variacion=((a-b)/b)*100;
        if(Double.isInfinite(variacion) || Double.isNaN(variacion))variacion=100;
        return variacion;
    }
    
    public static String daFormatoDinero(String cantidad){
        String lcant="0";
        DecimalFormat myFormatter = new DecimalFormat("###,##0.00");
        double ld=Double.parseDouble(cantidad);
        lcant=myFormatter.format(ld);
        return lcant;
    }
    
    public static String daFormatoNumero(String cantidad){
        String lcant="0";
        if ( cantidad != null )
        {
            DecimalFormat myFormatter = new DecimalFormat("###,##0.00");
            double ld=Double.parseDouble(cantidad);
            lcant=myFormatter.format(ld);
        }
        return lcant;
    }
    
   
    public static String fillLeft(String valor, int tot, String car) {
        String resultado = "";
        for (int i = valor.length(); i < tot; i++)
            resultado += car;
        return resultado + valor;
    }
    
    public static boolean isNumeric(String cadena){
	try {
		Long.parseLong(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
    }
    
    public static boolean validaCorreo(String cadena){
        Matcher mat=null;
        Pattern pat=null;

        pat=Pattern.compile("(\\s|>|^)(?!(:|www\\.|\\.))[A-Za-z0-9_.-]+@([A-Za-z0-9_-]+\\.)+[A-Za-z]{2,4}(\\s|\\W|_|<|$)");
        mat=pat.matcher(cadena);
        
        if(mat.find()){
            return true;
        }
        
        return false;
    }

    public static String[] partir(String linea,int num,int lon) {
        int idx;
        int ini;
        int fin;
        int max = linea.length();
        String[] resultado = new String[num];

        for (idx = 0; idx < num; idx++) {
            resultado[idx] = "";
        }
        if (max <= lon) {
            resultado[0] = linea;
            return resultado;
        }

        idx = 0;
        ini = 0;
        while (ini < max && idx < num) {
            fin = ini + lon - 1;
            if (fin > max)
                fin = max - 1;
            if ((fin + 1) < max && linea.charAt(fin+1) != ' ') {
                while (linea.charAt(fin) != ' ')
                    fin--;
            }
            resultado[idx] = linea.substring(ini,fin+1);
            ini = fin + 1;
            idx++;
        }

        return resultado;
    }


    public static String rellenar(String linea,String car,int lon,boolean derecha) {
        StringBuffer resultado = new StringBuffer(linea);
        for (int i = linea.length(); i < lon; i++) {
            if (derecha) {
                resultado.append(car);
            } else {
                resultado.insert(0, car);
            }
        }

        return resultado.toString();
    }

    public static String formatearNumero(double cantidad){
        String lcant="0";
        DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,##0.00");
        lcant = myFormatter.format(cantidad);
        return lcant;
    }

    public long minutosDiferencia(Date fecha1, Date fecha2) {

        Calendar calFecha1 = Calendar.getInstance();
        calFecha1.setTime(fecha1);
        Calendar calFecha2 = Calendar.getInstance();
        calFecha2.setTime(fecha2);

        long milisegundosFecha1 = calFecha1.getTimeInMillis();
        long milisegundosFecha2 = calFecha2.getTimeInMillis();
        long diferencia = milisegundosFecha1 - milisegundosFecha2;
        long minutos = diferencia / (60000);

        return minutos;
    }


    public Date obtenerFechaHoraString(String pstFecha) //YYYY/MM/DD HH24:MI:SS
    {
        Calendar lobHoy = Calendar.getInstance();
        try
        {
            int lnuValor = Integer.parseInt(pstFecha.substring(0,4));
            lobHoy.set(Calendar.YEAR, lnuValor);

            lnuValor = Integer.parseInt(pstFecha.substring(5,7)) - 1;
            lobHoy.set(Calendar.MONTH, lnuValor);

            lnuValor = Integer.parseInt(pstFecha.substring(8,10));
            lobHoy.set(Calendar.DAY_OF_MONTH, lnuValor);

            lnuValor = Integer.parseInt(pstFecha.substring(11,13));
            lobHoy.set(Calendar.HOUR_OF_DAY, lnuValor);

            lnuValor = Integer.parseInt(pstFecha.substring(14,16));
            lobHoy.set(Calendar.MINUTE, lnuValor);

            lnuValor = Integer.parseInt(pstFecha.substring(17));
            lobHoy.set(Calendar.SECOND, lnuValor);

        }
        catch(Exception e){
            log.error("error en obtenerFechaString Exception : " + e.toString());
        }

        return lobHoy.getTime();

    }

    public String traduceLibreria(String pstLibreria)
    {
        String lstLib = null;

        if ( pstLibreria != null && pstLibreria.length() > 0)
        {
            if ( pstLibreria.startsWith("CDJUA") )
                lstLib = "CDJ";
            else if(pstLibreria.startsWith("DTSH"))
                lstLib = "HMO";
            else if(pstLibreria.startsWith("CHI"))
                lstLib = "CHI";
            else if(pstLibreria.startsWith("QRO"))
                lstLib = "QRO";
            else if(pstLibreria.startsWith("MTY"))
                lstLib = "MTY";
            else if(pstLibreria.startsWith("GDL"))
                lstLib = "GDL";
            else if(pstLibreria.startsWith("PBA"))
                lstLib = "PBA";
            else if(pstLibreria.startsWith("MEX"))
                lstLib = "MEX";
            else if(pstLibreria.startsWith("CVA"))
                lstLib = "CVA";
            else if(pstLibreria.startsWith("MDA"))
                lstLib = "MDA";
            else if(pstLibreria.startsWith("TIJ"))
                lstLib = "TIJ";
            else
                lstLib = pstLibreria;

            return lstLib;

        }
        else
            return pstLibreria;


    }

    public static String fechaHoy()
    {
        Calendar hoy = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(hoy.getTime());
    }

    public static String fechaAyer(String formato)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if ( formato != null && formato.length() > 0 )
            sdf = new SimpleDateFormat(formato);

        Calendar hoy = Calendar.getInstance();
        hoy.add(Calendar.DAY_OF_MONTH, -1);
        return sdf.format(hoy.getTime());
    }

    public static String fechaHaceDias(int dias)
    {
        Calendar hoy = Calendar.getInstance();
        hoy.add(Calendar.DAY_OF_MONTH, -dias);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(hoy.getTime());
    }

    public static String fechaHaceDias(int dias, String formato)
    {
        Calendar hoy = Calendar.getInstance();
        hoy.add(Calendar.DAY_OF_MONTH, -dias);
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(hoy.getTime());
    }



    /*
     * Genera un objeto Date a partir
     * de la cadena de entrada del tipo: 2012/02/24 18:30:00
     */
    public static Date obtenerObjetoFecha(String pstFecha)
    {
        Date lobFecha = null;
            Calendar cal = Calendar.getInstance();

            try
            {
                int anio = Integer.parseInt(pstFecha.substring(0, 4));
                int mes = Integer.parseInt(pstFecha.substring(5, 7));
                int dia = Integer.parseInt(pstFecha.substring(8, 10));
                int hora = Integer.parseInt(pstFecha.substring(11, 13));
                int min = Integer.parseInt(pstFecha.substring(14, 16));
                int seg = Integer.parseInt(pstFecha.substring(17));

                cal.set(Calendar.YEAR, anio);
                cal.set(Calendar.MONTH, mes-1);
                cal.set(Calendar.DAY_OF_MONTH, dia);
                cal.set(Calendar.HOUR_OF_DAY, hora);
                cal.set(Calendar.MINUTE, min);
                cal.set(Calendar.SECOND, seg);

                lobFecha = cal.getTime();

            }
            catch(Exception e)
            {
                lobFecha = cal.getTime();
            }

        return lobFecha;
    }

    /*
     * Genera un objeto Date a partir
     * de la cadena de entrada del tipo: aammdd
     */
    public Date obtenerObjetoFechaR(String pstFecha)
    {
        Date lobFecha = null;
            Calendar cal = Calendar.getInstance();

            try
            {
                int anio = Integer.parseInt(pstFecha.substring(0, 2))+2000;
                int mes = Integer.parseInt(pstFecha.substring(2, 4));
                int dia = Integer.parseInt(pstFecha.substring(4));

                cal.set(Calendar.YEAR, anio);
                cal.set(Calendar.MONTH, mes-1);
                cal.set(Calendar.DAY_OF_MONTH, dia);

                lobFecha = cal.getTime();

            }
            catch(Exception e)
            {
                lobFecha = cal.getTime();
            }

        return lobFecha;
    }
    /*
     * Genera un objeto Date a partir
     * de la cadena de entrada del tipo: ddmmaa
     */

    public static Date obtenerObjetoFechaR2(String pstFecha)
    {
        Date lobFecha = null;
            Calendar cal = Calendar.getInstance();

            try
            {
                int dia = Integer.parseInt(pstFecha.substring(0, 2));
                int mes = Integer.parseInt(pstFecha.substring(2, 4));
                int anio = Integer.parseInt(pstFecha.substring(4))+2000;

                cal.set(Calendar.YEAR, anio);
                cal.set(Calendar.MONTH, mes-1);
                cal.set(Calendar.DAY_OF_MONTH, dia);

                lobFecha = cal.getTime();

            }
            catch(Exception e)
            {
                lobFecha = cal.getTime();
            }

        return lobFecha;
    }


    public String obtenerCadenaFecha(Date pobFecha)
    {
        String lobFecha = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            try
            {
                cal.setTime(pobFecha);
                lobFecha = sdf.format(pobFecha);

            }
            catch(Exception e)
            {
                lobFecha = null;
            }

        return lobFecha;
    }


    public int obtenerHaceCuantosDias(Date d1)
    {
        Calendar lobHoy = Calendar.getInstance();
        Date d2 = lobHoy.getTime();

        try
        {
            return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        }
        catch(Exception e)
        {
            return 0;
        }
    }



    public int daysBetween(Date d1, Date d2)
    {
        try
        {
            return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        }
        catch(Exception e)
        {
            return 0;
        }
    }


    /*
     * Genera un objeto Date a partir
     * de la cadena de entrada del tipo: aaaa/mm/dd 2011/05/17
     */
    public static String obtenerFechaConcil(String pstFecha)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

            try
            {
                int anio = Integer.parseInt(pstFecha.substring(0, 4));
                int mes = Integer.parseInt(pstFecha.substring(5, 7));
                int dia = Integer.parseInt(pstFecha.substring(8));

                cal.set(Calendar.YEAR, anio);
                cal.set(Calendar.MONTH, mes-1);
                cal.set(Calendar.DAY_OF_MONTH, dia);

                return sdf.format(cal.getTime());

            }
            catch(Exception e)
            {
                return null;
            }


    }

    public static String obtenerCadenaFechaXDiasHabilesAtras(int noDias)
    {
        String lobFecha = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            try
            {
                int i=noDias;


                while ( i > 0 ) {
                    //System.out.println(sdf.format(cal.getTime()));
                    if ( cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                        i--;
                    cal.add(Calendar.DATE, -1);
                }
                lobFecha = sdf.format(cal.getTime());

            }
            catch(Exception e)
            {
                lobFecha = null;
            }

        return lobFecha;
    }


    public static boolean esFechaHistorico(String fecha) {
        boolean fechaHist = false;

        //ConnectionPropertiesContainer props = new ConnectionPropertiesContainer();
        //String fechaHistorico = props.getProperty("reporte.fechahistorico") + " 00:00:00";
        String fechaHistorico = "2017-03-01 00:00:00";
        Date fechaH = obtenerObjetoFecha(fechaHistorico);
        
        Date fechaP ;
        if ( fecha != null && fecha.length() <= 10 )
            fechaP = obtenerObjetoFecha(fecha+" 00:00:00");
        else
            fechaP = obtenerObjetoFecha(fecha);
        
        if ( fechaP.before(fechaH))
            fechaHist = true;

        return fechaHist;
    }

    public static String cambiaFtoFechaDDMMYYYY(String pstFecha) {
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            try
            {
                int dia = Integer.parseInt(pstFecha.substring(0,2));
                int mes = Integer.parseInt(pstFecha.substring(3, 5));
                int anio = Integer.parseInt(pstFecha.substring(6));

                cal.set(Calendar.YEAR, anio);
                cal.set(Calendar.MONTH, mes-1);
                cal.set(Calendar.DAY_OF_MONTH, dia);

                return sdf.format(cal.getTime());

            }
            catch(Exception e)
            {
                return null;
            }
    }

    /*
     * Genera un objeto Date a partir
     * de la cadena de entrada del tipo: aaaammdd
     */

    public static Date obtenerObjetoFechaR3(String pstFecha)
    {
        Date lobFecha = null;
            Calendar cal = Calendar.getInstance();

            try
            {
                int anio = Integer.parseInt(pstFecha.substring(0, 4));
                int mes = Integer.parseInt(pstFecha.substring(4, 6));
                int dia = Integer.parseInt(pstFecha.substring(6));

                cal.set(Calendar.YEAR, anio);
                cal.set(Calendar.MONTH, mes-1);
                cal.set(Calendar.DAY_OF_MONTH, dia);

                lobFecha = cal.getTime();

            }
            catch(Exception e)
            {
                lobFecha = cal.getTime();
            }

        return lobFecha;
    }


    public static String obtenerObjetoFechaR4(String pstFecha) //cambio de formato en fecha
    {
        String fecha = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        try {
            return sdf.format( obtenerObjetoFechaR3(fecha));

        }
        catch(Exception e) {
            return null;
        }

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


    public static String formateaCantidad(String pstValor, int pnuLongitud)
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

    public static String obtenImporteConDecimal(String pstImporte)
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



    public static void main(String [] args){
        Utilerias u = new Utilerias();
        SimpleDateFormat sDateDay = new SimpleDateFormat("dd");
        SimpleDateFormat sDateMonth = new SimpleDateFormat("MMMM");
        SimpleDateFormat sDateYear = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String fecha = sDateDay.format(date)+ "/" 
                + u.obtenerNombreMesEspaniol(sDateMonth.format(date)) 
                + "/" + sDateYear.format(date);
        System.out.println(obtenerCadenaFechaXDiasHabilesAtras(5));


        System.out.println(obtenerObjetoFechaR4("20181206"));



        try {
            Double id = new Double("000000000");
            if ( id.doubleValue() <= 0 )
                System.out.println("codigoCONS = this.codigoConsulta(telefono)");
            else
                System.out.println("No hace la validacion");
        }
        catch(Exception e) {;}


//        System.out.println(u.obtenerFechaConcil("2011/05/17"));
//
//        System.out.println(Double.parseDouble("1067"));
//        System.out.println(Double.parseDouble("01067.00"));
//
//        System.out.println(u.esFechaHistorico("2017-03-01"));
//        System.out.println(u.esFechaHistorico("2017-02-28"));
//
//        System.out.println(u.cambiaFtoFechaDDMMYYYY("02/06/2017"));

    }

    public static String obtenerFechaHoy(String pstFormato)
    {
        String lstFecha = null;
            try
            {
                //String fmtoFecha = "yyyy/MM/dd HH:mm:ss";
                String fmtoFecha = pstFormato;
                SimpleDateFormat lobFecha = new SimpleDateFormat(fmtoFecha);

                Calendar cal = Calendar.getInstance();

                lstFecha = lobFecha.format(cal.getTime()).toString();

            }
            catch(Exception e)
            {
                lstFecha = null;
            }

        return lstFecha;
    }

    
}
