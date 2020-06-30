/*
 * @(#) PhaseTracker.java	1.0.0 14/11/2008
 *
 * Copyright 2008 Blitz Software S.A. de C.V. Todos los derechos reservados.
 * 
 */
package com.blitz.adminpago.util;

import com.blitz.adminpago.dto.UsuarioDTO;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * Objetivo:
 * 
 * @author  aghelgue
 * @since 1.0.0
 * @version 1.0.0, 14/11/2008
 */
public class PhaseTracker implements
        javax.faces.event.PhaseListener {

    public void afterPhase(PhaseEvent event) {
        //FacesContext.getCurrentInstance().getExternalContext().log(event.getPhaseId());
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext fc = event.getFacesContext();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "must-revalidate");
        response.setHeader("Cache-Control", "max-age=0");
        response.addHeader("Expires", "0"); // some date in the past
        String view = FacesContext.getCurrentInstance().
                getViewRoot().getViewId();

        //Salimos de la aplicacion
        if ( view.indexOf("salir.jsp") != -1 )
        {
            HttpSession session =
                    (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

            session.invalidate();

        }

        UsuarioDTO usr = (UsuarioDTO) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("usuario");

        //if ((view.equals("/login.jsp")==false) && usr == null) {
        if (usr == null)
        {
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "accesoInvalido");
        }

    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
