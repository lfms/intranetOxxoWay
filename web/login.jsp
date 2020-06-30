<?xml version="1.0" encoding="UTF-8"?>

<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>

    <f:view>
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                <link href="../css/estilos.css" rel="stylesheet" type="text/css"/>
                <link href="../css/estructura.css" rel="stylesheet" type="text/css"/>
                <title>Administrador Pago Line</title>
            </head>
            <body>
 <h:form id="form1">

<table width="766" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td height="66" colspan="2" valign="middle">
        <table width="766" height="64" border="0" align="left" cellpadding="0" cellspacing="0" >
            <tr>
                <td height="28">
                    <a href="http://www.telmex.com/mx/" target="blank"><img src="../images/_logo_tmx2.gif" width="177" height="40" border="0" /></a>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
     <br />
     <br />
     
<table width="766" border="0" align="center" cellpadding="0" cellspacing="0" background="images/fondo_login.gif">

<tr>
    <td height="215" colspan="2" align="left">
    <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" >
        <tr>
            <td width="43%" height="40" align="right" class="txt_general" style="padding-right:12px">Usuario :</td>
            <td width="65%" height="30">
                <h:inputText id="usuario" styleClass="frm_input" size="30" immediate="true" value="#{AccesoUsrMB.usuario}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div align="left">
                    <h:outputText styleClass="txt_error" value="#{AccesoUsrMB.mensajeUsr}"></h:outputText>
                 </div>
            </td>
       </tr>
       <tr>
            <td height="40" align="right" class="txt_general" style="padding-right:12px">Contraseña :</td>
            <td height="30">
                <h:inputSecret id="clave" size="30" styleClass="frm_input" value="#{AccesoUsrMB.clave}">
                </h:inputSecret>
            </td>
       </tr>
       <tr>
        <td colspan="2">
            <div align="left">
            <h:outputText styleClass="txt_error" value="#{AccesoUsrMB.mensajeCve}" ></h:outputText>
            </div>
        </td>
       </tr>

        <tr>
            <td colspan="2">
            <div align="left">
                <h:outputText styleClass="txt_error" value="#{AccesoUsrMB.mensaje}" ></h:outputText>
            </div>
        </td>
        </tr>

        <tr>
            <td colspan="2">
            <div align="right">
                <h:commandButton styleClass="frm_bot_gris" action="#{AccesoUsrMB.cancelaEntradas}" value="Limpiar">
                </h:commandButton>
                <h:commandButton styleClass="frm_bot_naranja" action="#{AccesoUsrMB.validaUsuario}" value="Aceptar">
                </h:commandButton>
            </div>
        </td>
        </tr>


    </table>

    </td>
</tr>
</table>

</h:form>
            </body>
        </html>
    </f:view>
</jsp:root>
