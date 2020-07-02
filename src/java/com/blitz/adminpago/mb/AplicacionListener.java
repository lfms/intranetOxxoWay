/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.mb;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author pgrande
 */
public class AplicacionListener implements PhaseListener  {
    
    Log log = LogFactory.getLog(this.getClass());

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext fc = event.getFacesContext();
        HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
        String vista = req.getRequestURI();
        
        if(vista.indexOf("generaReporteCaja.jspx") >= 0)
        {
            ELContext elContext = fc.getELContext();
            ReportesMB reporte = (ReportesMB)elContext.getELResolver().getValue(elContext, null, "ReportesMB");
            log.info("Generando el reporte -->");
            reporte.generarReporteCaja(fc);
        }
/*
        if(vista.indexOf("generaReporte.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            ReportesMB reporte = (ReportesMB)elContext.getELResolver().getValue(elContext, null, "ReportesMB");
            reporte.generarReporte(fc);
        }
        if(vista.indexOf("generaReporteTel.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            ReportesMB reporte = (ReportesMB)elContext.getELResolver().getValue(elContext, null, "ReportesMB");
            reporte.generarReporteTel(fc);
        }
        if(vista.indexOf("generaReporteCargo.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            ReportesMB reporte = (ReportesMB)elContext.getELResolver().getValue(elContext, null, "ReportesMB");
            reporte.generarReporteCargos(fc);
        }
        if(vista.indexOf("generaReportePagosSinConcil.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosSinConciliarMB reporte = (RepPagosSinConciliarMB)elContext.getELResolver().getValue(elContext, null, "RepPagosSinConciliarMB");
            reporte.generarReportePagosSinConcil(fc);
        }
        if(vista.indexOf("generaReporteConcilia.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            ConciliacionMB reporte = (ConciliacionMB)elContext.getELResolver().getValue(elContext, null, "ConciliacionMB");
            reporte.generarReporteConcilia(fc);
        }
        if(vista.indexOf("genReporConcilTroArchivo.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepCobranzaGralMB reporte = (RepCobranzaGralMB)elContext.getELResolver().getValue(elContext, null, "RepCobranzaGralMB");
            reporte.generarReporteArchivos(fc);
        }
        if(vista.indexOf("genReporEstadoMKT.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepEstadoMKTMB reporte = (RepEstadoMKTMB)elContext.getELResolver().getValue(elContext, null, "RepEstadoMKTMB");
            reporte.generarReportePagos(fc);
        }
        if(vista.indexOf("genReporConcilTroPagos.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepCobranzaGralMB reporte = (RepCobranzaGralMB)elContext.getELResolver().getValue(elContext, null, "RepCobranzaGralMB");
            reporte.generarReportePagos(fc);
        }
        if(vista.indexOf("genReporConcilTroPagosTxt.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepCobranzaGralMB reporte = (RepCobranzaGralMB)elContext.getELResolver().getValue(elContext, null, "RepCobranzaGralMB");
            reporte.generarReportePagosTxt(fc);
        }
        if(vista.indexOf("genReporConcilPisaArchivo.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepViaCobroMB reporte = (RepViaCobroMB)elContext.getELResolver().getValue(elContext, null, "RepViaCobroMB");
            reporte.generarReporteArchivos(fc);
        }
        if(vista.indexOf("genReporConcilPisaPagos.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepViaCobroMB reporte = (RepViaCobroMB)elContext.getELResolver().getValue(elContext, null, "RepViaCobroMB");
            reporte.generarReportePagos(fc);
        }
        if(vista.indexOf("genReporConcilPisaPagosTxt.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepViaCobroMB reporte = (RepViaCobroMB)elContext.getELResolver().getValue(elContext, null, "RepViaCobroMB");
            reporte.generarReportePagosTxt(fc);
        }
        if(vista.indexOf("genReporPagosSinConcil.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosSinConciliarMB reporte = (RepPagosSinConciliarMB)elContext.getELResolver().getValue(elContext, null, "RepPagosSinConciliarMB");
            reporte.generarReportePagosSinConcil(fc);
        }
        if(vista.indexOf("genReporPagosSinConcilTxt.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosSinConciliarMB reporte = (RepPagosSinConciliarMB)elContext.getELResolver().getValue(elContext, null, "RepPagosSinConciliarMB");
            reporte.generarReportePagosSinConcilTxt(fc);
        }
        if(vista.indexOf("genReporPagosSinConcilCancel.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosSinConciliarMB reporte = (RepPagosSinConciliarMB)elContext.getELResolver().getValue(elContext, null, "RepPagosSinConciliarMB");
            reporte.generarReportePagosSinConcil5Dias(fc);
        }
        if(vista.indexOf("genReporPagosSinLib.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosSinLibMB reporte = (RepPagosSinLibMB)elContext.getELResolver().getValue(elContext, null, "RepPagosSinLibMB");
            reporte.generarReportePagosSinLib(fc);
        }
        if(vista.indexOf("genReporPagosSinLibTxt.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosSinLibMB reporte = (RepPagosSinLibMB)elContext.getELResolver().getValue(elContext, null, "RepPagosSinLibMB");
            reporte.generarReportePagosSinLibTxt(fc);
        }
        if(vista.indexOf("genReporPagosProbDup.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosProbDupMB reporte = (RepPagosProbDupMB)elContext.getELResolver().getValue(elContext, null, "RepPagosProbDupMB");
            reporte.generarReportePagosProbDup(fc);
        }
        if(vista.indexOf("genReporPagosProbDupTxt.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosProbDupMB reporte = (RepPagosProbDupMB)elContext.getELResolver().getValue(elContext, null, "RepPagosProbDupMB");
            reporte.generarReportePagosProbDupTxt(fc);
        }


        if(vista.indexOf("genReporteSucursal.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            SucursalMB reporte = (SucursalMB)elContext.getELResolver().getValue(elContext, null, "SucursalMB");
            reporte.generarReporteSuc(fc);
        }
        if(vista.indexOf("genReporPagosLibCom.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosLibComMB reporte = (RepPagosLibComMB)elContext.getELResolver().getValue(elContext, null, "RepPagosLibComMB");
            reporte.generarReportePagosSinLib(fc);
        }
        if(vista.indexOf("genReporPagosLibComTxt.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepPagosLibComMB reporte = (RepPagosLibComMB)elContext.getELResolver().getValue(elContext, null, "RepPagosLibComMB");
            reporte.generarReportePagosSinLibTxt(fc);
        }
        if(vista.indexOf("genRepHistorialPagos.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepHistorialPagosMB reporte = (RepHistorialPagosMB)elContext.getELResolver().getValue(elContext, null, "RepHistorialPagosMB");
            reporte.generarRepHistorialPagos(fc);
        }
        if(vista.indexOf("genRepHistorialPagosTXT.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepHistorialPagosMB reporte = (RepHistorialPagosMB)elContext.getELResolver().getValue(elContext, null, "RepHistorialPagosMB");
            reporte.generarRepHistorialPagosTxt(fc);
        }
        if(vista.indexOf("genReporIncidencias.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepAntiFraudeMB reporte = (RepAntiFraudeMB)elContext.getELResolver().getValue(elContext, null, "RepAntiFraudeMB");
            reporte.generarReporteIncidencias(fc);
        }
        if(vista.indexOf("genRepAnalisisFraude.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            RepAntiFraudeMB reporte = (RepAntiFraudeMB)elContext.getELResolver().getValue(elContext, null, "RepAntiFraudeMB");
            reporte.generarRepAnalisisFraude(fc);
        }
        if(vista.indexOf("generaReporteCaja.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            ReportesMB reporte = (ReportesMB)elContext.getELResolver().getValue(elContext, null, "ReportesMB");
            reporte.generarReporteCaja(fc);
        }
        if(vista.indexOf("generaReporteBinesF.jsp") >= 0)
        {
            ELContext elContext = fc.getELContext();
            BinesMB reporte = (BinesMB)elContext.getELResolver().getValue(elContext, null, "BinesMB");
            reporte.generarReporteBinesF(fc);
        }
*/
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        ;
        //throw new UnsupportedOperationException("Not supported yet.");
    }


}
