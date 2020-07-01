
<tr>
    <td align="center" colspan="3">
                    <table width="720" border="0" align="center" cellpadding="0" cellspacing="0" >
                                                    <tr> <td width="37" background="images/menu_r1_c1.jpg"></td>
                                                        <td align="center">
                                                            <div align="center">
                                                                <ul style="text-align:center;" id="jsddm">
                                                                    <c:if test="${fn:length(MenuDTO.menu[0]) > 0}">
                                                                        <li><a href="#" class="lnk_botonhed">Catálogos</a>
                                                                            <ul>
                                                                                <c:forEach var="modulo" items="${MenuDTO.menu[0]}">
                                                                                    <li>
                                                                                        <a href="<%=path%>${modulo.url}"  class="lnk_botonhed">${modulo.nombre}</a>
                                                                                    </li>
                                                                                </c:forEach>
                                                                            </ul>
                                                                        </li>
                                                                    </c:if>
                                                                    <c:if test="${fn:length(MenuDTO.menu[1]) > 0}">
                                                                        <li><a href="#" class="lnk_botonhed">Reportes</a>
                                                                            <ul>
                                                                                <c:forEach var="modulo" items="${MenuDTO.menu[1]}">
                                                                                    <li>
                                                                                        <a href="<%=path%>${modulo.url}" class="lnk_botonhed">${modulo.nombre}</a>
                                                                                    </li>
                                                                                </c:forEach>
                                                                            </ul>
                                                                        </li>
                                                                    </c:if>
                                                                    <c:if test="${fn:length(MenuDTO.menu[2]) > 0}">
                                                                        <li><a href="#" class="lnk_botonhed">Monitoreo</a>
                                                                            <ul>
                                                                                <c:forEach var="modulo" items="${MenuDTO.menu[2]}">
                                                                                    <li>
                                                                                        <a href="<%=path%>${modulo.url}" class="lnk_botonhed">${modulo.nombre}</a>
                                                                                    </li>
                                                                                </c:forEach>
                                                                            </ul>
                                                                        </li>
                                                                    </c:if>
                                                                    <li>
                                                                        <a href="<%=path%>/ManAdminPagoOnLine.pdf" target="AyudaPago" class="lnk_botonhed">Ayuda</a>
                                                                    </li>
                                                                    <li>
                                                                        <a href="<%=path%>/salir.jspx" class="lnk_botonhed">Salir</a>
                                                                    </li>
                                                                    <c:if test="${fn:length(MenuDTO.menu[0]) < 2}">
                                                                        <li class="botonhed">&nbsp;</li>
                                                                    </c:if>

                                                                </ul>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                        </tr>
