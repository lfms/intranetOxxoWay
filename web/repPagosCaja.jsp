<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
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
            <link rel="stylesheet" href="css/print.css" type="text/css" media="print" />

            <link href="css/menu.css" rel="stylesheet" type="text/css">
            <script src="js/jquery.min.js" type="text/javascript"></script>
            <script src="js/menu.js" type="text/javascript"></script>

            <title>Intranet Pago en Linea</title>
            
            <SCRIPT LANGUAGE="JavaScript">
                function fechaCarga()
                {
                    document.forms['form1']['form1:fechaI'].value = document.forms['form1'].txtFecIni.value;
                    document.forms['form1']['form1:fechaF'].value = document.forms['form1'].txtFecIni.value;
                }
                function alInicio()
                {
<%
        Calendar lobHoy = Calendar.getInstance();
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy/MM/dd");
        String fechaFin = sdt.format(lobHoy.getTime());
        //lobHoy.add(Calendar.MONTH, -1);
        String fechaIni = sdt.format(lobHoy.getTime());

%>
                    document.forms['form1'].txtFecIni.value = "<%=fechaIni%>";
                }
            </SCRIPT>

<style type="text/css">
.row1 {
	background-color: #EDEBEB;
	font-size: 10px;
}

.row2 {
	background-color: #ffffff;
	font-size: 10px;
}
</style>

        </head>
        <body onload="alInicio()">
            <!--script language="jscript" src="js/calendario.js"></script-->
            <script src="js/calendario.js"></script>

            <h:form id="form1">

                <div id="contenedor">
                    <div id="pagina">


                        <table width="766" border="0" align="center" cellpadding="0" cellspacing="0">
                            <tr>
                                <td height="64" colspan="3"><table width="766" height="124" border="0" cellpadding="0" cellspacing="0">

                                        <tr>
                                            <td width="201">&nbsp;</td>
                                            <td width="396">&nbsp;</td>
                                            <td width="169">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td><a href="http://www.telmex.com/mx/"><img src="images/_logo_tmx2.gif" width="177" height="40" border="0" /></a></td>
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


                                    </table></td>
                            </tr>

                                <tr>
                                    <td colspan="3">

                                    <div id="top" class="top">
                                    <img src="images/tit_reportePorCaja.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
                                    <img src="images/spacer.gif" alt="Esquina superior derecha" class="esquina_sup_der" />
                                    </div>

                                    </td>
                                </tr>


                            <tr align="right">
                                <td height="15" colspan="3" background="images/recuadro_fondo.gif"><table width="767" border="0" align="center" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td><!--p></p-->
                                                <table width="700" border="0" align="center" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td colpan="3" class="txt_general_bold">&nbsp;</td>
                                                    </tr>
                                                    
                                                    <tr>
                                                        <td>Comercio: </td>
                                                        <td width="35%">
                                                            <h:selectOneMenu id="idtienda" styleClass="frm_input" value="#{ReportesMB.parComercio}">
                                                                <f:selectItem itemLabel="TODOS" itemValue="*"/>
                                                                <f:selectItems value="#{ListaComercioDTO.listaComercioXNom}" />
                                                            </h:selectOneMenu>
                                                        </td>

                                                        <td>Día: </td>
                                                        <td width="45%">
                                                            <h:inputHidden id="fechaI" value="#{ReportesMB.fechaI}" ></h:inputHidden>
                                                            <input type="text"  name="txtFecIni" size="10" maxlength="10" class="frm_input" align="top" >
                                                            <a href="javascript: show_calendar('form1.txtFecIni','YYYY/MM/DD');">
                                                                <img src="images/calendario_icono.gif" width="30" height="30" border="0" align="absmiddle">
                                                            </a>

                                                        </td>

                                                    </tr>
                                                    <tr>
                                                        <td>Estatus: </td>
                                                        <td width="35%">
                                                            <h:selectOneMenu id="estatusP" styleClass="frm_input" value="#{ReportesMB.parEstatus}">
                                                                <f:selectItem itemLabel="Seleccione valor" itemValue="*"/>
                                                                <f:selectItems value="#{ReportesMB.estatusPagoMap}" />
                                                            </h:selectOneMenu>
                                                        </td>

                                                        <td>&nbsp;</td>
                                                        <td width="15%">&nbsp;
                                                        </td>

                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>



                                        <tr>
                                            <td height="5"><div align="center">
                                                                <table width="700" border="0" cellpadding="0" cellspacing="0">
                                                                    <tr>
                                                                        <td width="700" height="100">
                                                                           <h:panelGrid rendered="#{ReportesMB.mostrarPagos}">
                                                                           <h:panelGroup>

                                                                            <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
                                                                               <tr>
                                                                                    <td width="800" >
                                                                                        <h:outputText id="fil" value="#{ReportesMB.filtros}" />
                                                                                        <h:outputText styleClass="tit_rojob" value="#{ReportesMB.totalesFiltro.idPago} pagos " />&nbsp;<h:outputText styleClass="tit_rojob" value="$ #{ReportesMB.totalesFiltro.montoPagado}" />
                                                                                    </td>
                                                                               </tr>
                                                                                <tr>
                                                                                    <td>
                                                                                                <h:dataTable id="pagos"  value="#{ReportesMB.pagos}"  var="pago"  border="1" 
                                                                                                             styleClass="txt_general" rowClasses="row1, row2"
                                                                                                             first="0" rows="20">


                                                                                                    <h:column>
                                                                                                        <f:facet name="header"> <h:outputText styleClass="txt_boton_azul" value="TIENDA"/> </f:facet>
                                                                                                        <h:outputText value="#{pago.adquiriente}" />
                                                                                                    </h:column>

                                                                                                    

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="TRANSACCION"/></f:facet>
                                                                                                        <h:outputText value="#{pago.transaccion}" />
                                                                                                    </h:column>


                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="TELEFONO"/></f:facet>
                                                                                                        <h:outputText value="#{pago.telefono}" />
                                                                                                    </h:column>

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="IMPORTE APLICADO"/></f:facet>
                                                                                                            <h:outputText rendered="#{pago.estatus == 'AA' ||pago.estatus == 'AP' }" value="#{pago.montoPagado}" />
                                                                                                            <h:outputText rendered="#{pago.estatus != 'AA' && pago.estatus != 'AP' }" value="#{pago.montoPagar}" />
                                                                                                    </h:column>

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="FECHA"/></f:facet>
                                                                                                        <h:outputText value="#{pago.fechaSol}" />
                                                                                                    </h:column>

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="ESTATUS"
                                                                                                            title="RR=RECIBIDO NO ENVIADO A PISA;AA=ENVIADO A PISA;AP=ATENDIDO Y POSTEADO;AS=ENVIADO Y SIN SECUENCIA DE PISA;EE=NO SE PUDO ENVIAR PAGO;EN=NO EXISTE FOLIO" />
                                                                                                        </f:facet>
                                                                                                        <h:outputText value="#{pago.estatus}" />
                                                                                                    </h:column>

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="SECUENCIA"/></f:facet>
                                                                                                        <h:outputText value="#{pago.secuenciaPisa}" />
                                                                                                    </h:column>

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="FOLIOPISA"/></f:facet>
                                                                                                        <h:outputText value="#{pago.idPago}" />
                                                                                                    </h:column>

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="CAJA"/></f:facet>
                                                                                                        <h:outputText value="#{pago.caja}" />
                                                                                                    </h:column>

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="LIB"/></f:facet>
                                                                                                        <h:outputText value="#{pago.libreria}" />
                                                                                                    </h:column>

                                                                                                    <h:column>
                                                                                                        <f:facet name="header"><h:outputText styleClass="txt_boton_azul" value="CONCILIACION"/></f:facet>
                                                                                                        <h:outputText value="#{pago.auditNumber}" />
                                                                                                    </h:column>
                                                                                                    
                                                                                                    
                                                                                                </h:dataTable>


                                                                                                <br>
                                                                                                <div align="center">
                                                                                                    <t:dataScroller id="scroller" for="pagos" paginator="true" fastStep="2"
                                                                                                                    paginatorMaxPages="5"
                                                                                                                    style="color:#FFFFFF;"
                                                                                                                    paginatorActiveColumnStyle="color:#FFFFFF;font-size:20px;font-weight:bold;"
                                                                                                                    immediate="true">
                                                                                                        <f:facet name="first" >
                                                                                                            <t:graphicImage url="images/ic_fchaizq.gif" border="1" />
                                                                                                        </f:facet>
                                                                                                        <f:facet name="last">
                                                                                                            <t:graphicImage url="images/ic_fchader.gif" border="1" />
                                                                                                        </f:facet>
                                                                                                        <f:facet name="previous">
                                                                                                            <t:graphicImage url="images/fcha_atras.gif" border="1" />
                                                                                                        </f:facet>
                                                                                                        <f:facet name="next">
                                                                                                            <t:graphicImage url="images/fcha_adelante.gif" border="1" />
                                                                                                        </f:facet>
                                                                                                        <f:facet name="fastforward">
                                                                                                            <t:graphicImage url="images/fcha_masdelante.gif" border="1" />
                                                                                                        </f:facet>
                                                                                                        <f:facet name="fastrewind">
                                                                                                            <t:graphicImage url="images/fcha_masatras.gif" border="1" />
                                                                                                        </f:facet>
                                                                                                    </t:dataScroller>
                                                                                                </div>



                                                                                        <!--tr>
                                                                                            <td colspan="8"-->
                                                                                        <!--/td>
                                                                                    </tr-->

                                                                                    </td>

                                                                                </tr>


                                                                            </table>
                                                                            </h:panelGroup>
                                                                            </h:panelGrid>

                                                                            <h:panelGrid columns="0" rendered="#{ReportesMB.mostrarErrorPagos}">
                                                                                <h:outputText style="color: #FF0000;" value="#{ReportesMB.msgError}"/>
                                                                            </h:panelGrid>




                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                </div></td>
                                        </tr>
                                        <tr>
                                            <td >&nbsp;</td>
                                        </tr>

                                        <tr>
                                            <td><table width="648" border="0" align="center" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td width="461">
                                                            <div align="left"></div></td>
                                                        <td width="90"><div align="left"><h:commandButton styleClass="frm_bot_naranja" value="Exportar" action="reporteExcelCaja" /></div></td>
                                                        <td width="97"><h:commandButton id="buscar" styleClass="frm_bot_naranja" value="Buscar" action="#{ReportesMB.muestraPagosCaja}" onclick="return fechaCarga();this.form.submit();"/></td>
                                                        <td width="97"><h:commandButton styleClass="frm_bot_naranja" value="Limpiar" action="#{ReportesMB.borrarCampos}" /></td>
                                                    </tr>
                                                </table>
                                           </td>
                                        </tr>
                                    </table></td>
                            </tr>
                        <tr>
                            <td  align="right">
                                <div id="bottom" class="bottom">
                                <img src="images/spacer.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq" />

                                <img src="images/spacer.gif" alt="Esquina inferior iderecha" class="esquina_inf_der" /></div>

                            </td>
                        </tr>
                        </table>


                    </div>
                </div>
            </h:form>
            <div id="overDiv" style="position:absolute; left: 413px; top: 236px;"></div>
        </body>
    </html>
</f:view>
