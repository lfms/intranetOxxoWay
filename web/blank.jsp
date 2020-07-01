<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<f:view>
    <html>
        <%
            String path = request.getContextPath();
        %>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link href="css/estilos.css" rel="stylesheet" type="text/css">
            <link href="css/estructura.css" rel="stylesheet" type="text/css">
            <!--link rel="stylesheet" href="css/print.css" type="text/css" media="print" /-->

            <link href="css/menu.css" rel="stylesheet" type="text/css">
            <script src="js/jquery.min.js" type="text/javascript"></script>
            <script type="text/javascript">
                 <!--
                var timeout         = 500;
                var closetimer		= 0;
                var ddmenuitem      = 0;

                function jsddm_open()
                {	jsddm_canceltimer();
                    jsddm_close();
                    ddmenuitem = $(this).find('ul').eq(0).css('visibility', 'visible');}

                function jsddm_close()
                {	if(ddmenuitem) ddmenuitem.css('visibility', 'hidden');}

                function jsddm_timer()
                {	closetimer = window.setTimeout(jsddm_close, timeout);}

                function jsddm_canceltimer()
                {	if(closetimer)
                    {	window.clearTimeout(closetimer);
                        closetimer = null;}}

                $(document).ready(function()
                {	$('#jsddm > li').bind('mouseover', jsddm_open);
                    $('#jsddm > li').bind('mouseout',  jsddm_timer);});

                document.onclick = jsddm_close;
                //-->            </script>


            <title>Intranet Pago en Linea</title>

            <script language="jscript" src="js/calendario.js"></script>
        </head>
        <body>
            <h:form id="form1">
                <div id="contenedor1">
                    <div id="pagina1">
                        <table width="766" border="0" align="center" cellpadding="0" cellspacing="0">
                            <tr>
                                <td height="64" colspan="3">
                                    <table width="766" height="124" border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td width="201">&nbsp;</td>
                                            <td width="396">&nbsp;</td>
                                            <td width="169">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                    <a href="http://www.telmex.com/mx/"><img src="images/_logo_tmx2.gif" width="177" height="40" border="0" /></a>                                            </td>
                                            <td>&nbsp;</td>
                                            <td><!--img src="images/logo2.gif" width="120" height="69" /--></td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <!-- Inicio Menu -->
                                        <tr>
                                            <td colspan="5">
                                                <!-- Se incluye el menu -->
                                                <%@ include file="menu.jsp" %>
                                            </td>
                                        </tr>
                                        <!-- Fin  Menu -->


                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3">&nbsp;</td>
                            </tr>
                            <tr>
                                <td width="244">&nbsp;</td>
                                <td width="302">&nbsp;</td>
                                <td width="72">&nbsp;</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </h:form>
        </body>
    </html>
</f:view>