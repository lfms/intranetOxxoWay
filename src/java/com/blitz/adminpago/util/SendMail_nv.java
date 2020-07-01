package com.blitz.adminpago.util;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendMail_nv {

    //private static final String host    = new String("200.57.157.191");
    private static final String host = new String("localhost");
    //private static final String host = new String("tmxmail123.ad.intranet.telmex.com");
    //private static final String host = new String("TMXMAILCL03.ad.intranet.telmex.com");

    private static final String dominio = new String("http://148.223.168.85/");
    private static boolean debug = false;
    private String remitente;
    private String destinatarios;
    private String asunto;
    private String texto;
    private String archAdjunto;

    /**
     * Habilita el debug de la clase
     */
    public void setDebug(boolean value) {
        debug = value;
    }

    /**
     *  Constructor de la clase SendMail, con cinco parametros.
     */
    public SendMail_nv(String remitente,
            String destinatarios,
            String asunto,
            String texto,
            String archAdjunto) {

        this.remitente = remitente;
        this.destinatarios = destinatarios;
        this.asunto = asunto;
        this.texto = texto;
        this.archAdjunto = archAdjunto;
    }

    /**
     *  Constructor de la clase SendMail con 4 parametros.
     */
    public SendMail_nv(String remitente,
            String destinatarios,
            String asunto,
            String texto) {

        this.remitente = remitente;
        this.destinatarios = destinatarios;
        this.asunto = asunto;
        this.texto = texto;
        this.archAdjunto = "";
    }

    /** Este es el m�todo utilizado por el servicio HTTP. Controla los
     *   m�todos POST, GET y HEAD del protocolo HTTP
     *   Cuando no se puede establecer conexi�n con el servidor, se
     *   produce una excepci�n
     */
    public void enviaMail() {
        // Crea algunas propiedades y consigue la session default
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);

        try {
            // crea un mensaje
            MimeMessage msg = new MimeMessage(session);
            //msg.setHeader("Content-Language","sp");
            msg.setFrom(new InternetAddress(remitente));

            StringTokenizer destinatario = new StringTokenizer(destinatarios, ",");
            InternetAddress[] address = new InternetAddress[destinatario.countTokens()];
            for (int i = 0; i < address.length; i++) {
                address[i] = new InternetAddress(destinatario.nextToken());
            }
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(asunto);
            // crea y llena la primer parte del mensaje
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(texto);
            // crea la segunda parte del mensaje
            MimeBodyPart mbp2 = new MimeBodyPart();
            // Adjunta el archivo al mensaje
            if (!(archAdjunto.equals(""))) {
                FileDataSource fds = new FileDataSource(archAdjunto);
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(fds.getName());
            }
            // crea el Multipart y sus partes de este
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            if (!(archAdjunto.equals(""))) {
                mp.addBodyPart(mbp2);
            }
            // Adiciona el Multipart al mensaje
            msg.setContent(mp);
            // Pone la fecha: header
            msg.setSentDate(new Date());
            // Envia el mensaje
            Transport.send(msg);
        } catch (MessagingException mex) {
            System.out.println(mex.getMessage());
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }

    /** Este es el m�todo utilizado por el servicio HTTP. Controla los
     *   m�todos POST, GET y HEAD del protocolo HTTP
     *   Cuando no se puede establecer conexi�n con el servidor, se
     *   produce una excepci�n
     *   Este metodo envia el mail en formato HTML ...
     */
    public void enviaMailHTML() {
        // Crea algunas propiedades y consigue la session default
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);

        try {
            // crea un mensaje
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(remitente));

            StringTokenizer destinatario = new StringTokenizer(destinatarios, ",");
            InternetAddress[] address = new InternetAddress[destinatario.countTokens()];
            for (int i = 0; i < address.length; i++) {
                address[i] = new InternetAddress(destinatario.nextToken());
            }

            msg.setRecipients(Message.RecipientType.TO, address); //Anade recipientes ...
            //msg.setSubject(asunto);  // Anade asunto (subject)
            msg.setSubject(MimeUtility.encodeText(asunto, "ISO_8859-1", null));

            // crea y llena la primer parte del mensaje
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(texto);

            // Sobreescribe el tipo del header ...
            mbp1.addHeader("Content-Type", "text/html");

            // crea la segunda parte del mensaje
            MimeBodyPart mbp2 = new MimeBodyPart();

            // Attacha el archivo al mensaje
            if (!(archAdjunto.equals(""))) {
                FileDataSource fds = new FileDataSource(archAdjunto);
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(fds.getName());
            }

            // crea el Multipart y sus partes de este
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            if (!(archAdjunto.equals(""))) {
                mp.addBodyPart(mbp2);
            }

            // Adiciona el Multipart al mensaje
            msg.setContent(mp);

            // Pone la fecha: header
            msg.setSentDate(new Date());

            // Envia el mensaje
            Transport.send(msg);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            //status = "Error inesperado. ";
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        String sHTML = "<HTML><font face=\"verdana, helvetica, sans-serif\" size=\"2\"><b>";
        sHTML += "Estimado Cliente:</b>";
        sHTML += "<br><br>A continuaci&oacute;n encontrar&aacute; su clave de usuario y contrase&ntilde;a de ";
        sHTML += "acceso al servicio <b>Telencuesta</b>.<br><br>";
        sHTML += "<b>Clave de Usuario:&nbsp;</b>&Uacute;ltimos 7 d&iacute;gitos de su n&uacute;mero 800 (indicar";
        sHTML += " el n&uacute;mero) <br>";
        sHTML += "<b>Contrase&ntilde;a:&nbsp;</b>VOTOS71<br><br>";
        sHTML += "Le recordamos que al momento de accesar por primera vez al sistema, por seguridad<br>";
        sHTML += "deber&aacute; sustituir &eacute;sta Contrase&ntilde;a por una nueva que desee utilizar.<br>";
        sHTML += "<br>Para cualquier aclaraci&oacute;n, por favor comun&iacute;quese con su Ejecutivo de";
        sHTML += " Cuenta Telmex.<br><br>";
        sHTML += "<b>Telencuesta<br>TELMEX</b>	</HTML>";

        SendMail_nv sm = new SendMail_nv("smmiguel@ad.intranet.telmex.com", "smmiguel@ad.intranet.telmex.com", "prueba", sHTML);
        //sm.setDebug(true);
        sm.enviaMailHTML();
    }
}

