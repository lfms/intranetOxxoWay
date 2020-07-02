/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;

/**
 *
 * @author pgrande
 */
public class PagoDTO implements Serializable{

    private String idPago;
    private String telefono;
    private String montoPagar;
    private String fechaSol;
    private String fechaRespPisa;
    private String fechaRespTro;
    private String fechaContable;
    private String fechaCaptura;
    private String hora;
    private String dia;
    private String codigoResp;
    private String tipoResp;
    private String adquiriente; //Comercio
    private String tiendaTerm;  //Sucursal
    private String unidad;
    private String secuenciaPisa;
    private String saldoVencido;
    private String minReanudacion;
    private String montoPagado;
    private String transaccion;
    private String auditNumber;
    private String tipoIngreso;
    private String moneda;
    private String estatus;
    private String autorizacion;
    private long idConciliacion;
    private String estrategia;
    private String fechaPosteo;
    private String fechaUltCambio;
    private long idConcilTro;
    private String fechaConcilTro;
    private String tipoProceso;
    private String secPisaArchivo;
    private String archivoRespTro;
    private String fechaConcilManual;
    private String estatusConcilPisa;
    private String estatusConcilTro;
    private String libreria;
    private String respTimeout;
    private String autorCobranza; //VoBo de MARCIA para aplicarse en los FL
    //Datos para los no autorizados
    private String formaPago;
    private String numeroTC;
    private String caja;
    private double importePagado;
    private long idConcilBco;
    private String tipoTC;
    private String secure;
    private String correo;
    private String avs;
    private String claseServicio;
    private String tipotel;



    /**
     * @return the idPago
     */
    public String getIdPago() {
        return idPago;
    }

    /**
     * @param idPago the idPago to set
     */
    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the montoPagar
     */
    public String getMontoPagar() {
        return montoPagar;
    }

    /**
     * @param montoPagar the montoPagar to set
     */
    public void setMontoPagar(String montoPagar) {
        this.montoPagar = montoPagar;
    }

    /**
     * @return the fechaSol
     */
    public String getFechaSol() {
        return fechaSol;
    }

    /**
     * @param fechaSol the fechaSol to set
     */
    public void setFechaSol(String fechaSol) {
        this.fechaSol = fechaSol;
    }

    /**
     * @return the fechaRespPisa
     */
    public String getFechaRespPisa() {
        return fechaRespPisa;
    }

    /**
     * @param fechaRespPisa the fechaRespPisa to set
     */
    public void setFechaRespPisa(String fechaRespPisa) {
        this.fechaRespPisa = fechaRespPisa;
    }

    /**
     * @return the fechaRespTro
     */
    public String getFechaRespTro() {
        return fechaRespTro;
    }

    /**
     * @param fechaRespTro the fechaRespTro to set
     */
    public void setFechaRespTro(String fechaRespTro) {
        this.fechaRespTro = fechaRespTro;
    }

    /**
     * @return the fechaContable
     */
    public String getFechaContable() {
        return fechaContable;
    }

    /**
     * @param fechaContable the fechaContable to set
     */
    public void setFechaContable(String fechaContable) {
        this.fechaContable = fechaContable;
    }

    /**
     * @return the hora
     */
    public String getHora() {
        return hora;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * @return the dia
     */
    public String getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(String dia) {
        this.dia = dia;
    }

    /**
     * @return the codigoResp
     */
    public String getCodigoResp() {
        return codigoResp;
    }

    /**
     * @param codigoResp the codigoResp to set
     */
    public void setCodigoResp(String codigoResp) {
        this.codigoResp = codigoResp;
    }

    /**
     * @return the tipoResp
     */
    public String getTipoResp() {
        return tipoResp;
    }

    /**
     * @param tipoResp the tipoResp to set
     */
    public void setTipoResp(String tipoResp) {
        this.tipoResp = tipoResp;
    }

    /**
     * @return the adquiriente
     */
    public String getAdquiriente() {
        return adquiriente;
    }

    /**
     * @param adquiriente the adquiriente to set
     */
    public void setAdquiriente(String adquiriente) {
        this.adquiriente = adquiriente;
    }

    /**
     * @return the tiendaTerm
     */
    public String getTiendaTerm() {
        return tiendaTerm;
    }

    /**
     * @param tiendaTerm the tiendaTerm to set
     */
    public void setTiendaTerm(String tiendaTerm) {
        this.tiendaTerm = tiendaTerm;
    }

    /**
     * @return the unidad
     */
    public String getUnidad() {
        return unidad;
    }

    /**
     * @param unidad the unidad to set
     */
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    /**
     * @return the secuenciaPisa
     */
    public String getSecuenciaPisa() {
        return secuenciaPisa;
    }

    /**
     * @param secuenciaPisa the secuenciaPisa to set
     */
    public void setSecuenciaPisa(String secuenciaPisa) {
        this.secuenciaPisa = secuenciaPisa;
    }

    /**
     * @return the saldoVencido
     */
    public String getSaldoVencido() {
        return saldoVencido;
    }

    /**
     * @param saldoVencido the saldoVencido to set
     */
    public void setSaldoVencido(String saldoVencido) {
        this.saldoVencido = saldoVencido;
    }

    /**
     * @return the minReanudacion
     */
    public String getMinReanudacion() {
        return minReanudacion;
    }

    /**
     * @param minReanudacion the minReanudacion to set
     */
    public void setMinReanudacion(String minReanudacion) {
        this.minReanudacion = minReanudacion;
    }

    /**
     * @return the montoPagado
     */
    public String getMontoPagado() {
        return montoPagado;
    }

    /**
     * @param montoPagado the montoPagado to set
     */
    public void setMontoPagado(String montoPagado) {
        this.montoPagado = montoPagado;
    }

    /**
     * @return the transaccion
     */
    public String getTransaccion() {
        return transaccion;
    }

    /**
     * @param transaccion the transaccion to set
     */
    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    /**
     * @return the fechaCaptura
     */
    public String getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * @param fechaCaptura the fechaCaptura to set
     */
    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    /**
     * @return the auditNumber
     */
    public String getAuditNumber() {
        return auditNumber;
    }

    /**
     * @param auditNumber the auditNumber to set
     */
    public void setAuditNumber(String auditNumber) {
        this.auditNumber = auditNumber;
    }

    /**
     * @return the tipoIngreso
     */
    public String getTipoIngreso() {
        return tipoIngreso;
    }

    /**
     * @param tipoIngreso the tipoIngreso to set
     */
    public void setTipoIngreso(String tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    /**
     * @return the moneda
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the estatus
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * @param estatus the estatus to set
     */
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    /**
     * @return the autorizacion
     */
    public String getAutorizacion() {
        return autorizacion;
    }

    /**
     * @param autorizacion the autorizacion to set
     */
    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    /**
     * @return the idConciliacion
     */
    public long getIdConciliacion() {
        return idConciliacion;
    }

    /**
     * @param idConciliacion the idConciliacion to set
     */
    public void setIdConciliacion(long idConciliacion) {
        this.idConciliacion = idConciliacion;
    }

    /**
     * @return the estrategia
     */
    public String getEstrategia() {
        return estrategia;
    }

    /**
     * @param estrategia the estrategia to set
     */
    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * @return the fechaPosteo
     */
    public String getFechaPosteo() {
        return fechaPosteo;
    }

    /**
     * @param fechaPosteo the fechaPosteo to set
     */
    public void setFechaPosteo(String fechaPosteo) {
        this.fechaPosteo = fechaPosteo;
    }

    /**
     * @return the fechaUltCambio
     */
    public String getFechaUltCambio() {
        return fechaUltCambio;
    }

    /**
     * @param fechaUltCambio the fechaUltCambio to set
     */
    public void setFechaUltCambio(String fechaUltCambio) {
        this.fechaUltCambio = fechaUltCambio;
    }

    /**
     * @return the idConcilTro
     */
    public long getIdConcilTro() {
        return idConcilTro;
    }

    /**
     * @param idConcilTro the idConcilTro to set
     */
    public void setIdConcilTro(long idConcilTro) {
        this.idConcilTro = idConcilTro;
    }

    /**
     * @return the fechaConcilTro
     */
    public String getFechaConcilTro() {
        return fechaConcilTro;
    }

    /**
     * @param fechaConcilTro the fechaConcilTro to set
     */
    public void setFechaConcilTro(String fechaConcilTro) {
        this.fechaConcilTro = fechaConcilTro;
    }

    /**
     * @return the tipoProceso
     */
    public String getTipoProceso() {
        return tipoProceso;
    }

    /**
     * @param tipoProceso the tipoProceso to set
     */
    public void setTipoProceso(String tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    /**
     * @return the secPisaArchivo
     */
    public String getSecPisaArchivo() {
        return secPisaArchivo;
    }

    /**
     * @param secPisaArchivo the secPisaArchivo to set
     */
    public void setSecPisaArchivo(String secPisaArchivo) {
        this.secPisaArchivo = secPisaArchivo;
    }

    /**
     * @return the archivoRespTro
     */
    public String getArchivoRespTro() {
        return archivoRespTro;
    }

    /**
     * @param archivoRespTro the archivoRespTro to set
     */
    public void setArchivoRespTro(String archivoRespTro) {
        this.archivoRespTro = archivoRespTro;
    }

    /**
     * @return the fechaConcilManual
     */
    public String getFechaConcilManual() {
        return fechaConcilManual;
    }

    /**
     * @param fechaConcilManual the fechaConcilManual to set
     */
    public void setFechaConcilManual(String fechaConcilManual) {
        this.fechaConcilManual = fechaConcilManual;
    }

    /**
     * @return the estatusConcilPisa
     */
    public String getEstatusConcilPisa() {
        return estatusConcilPisa;
    }

    /**
     * @param estatusConcilPisa the estatusConcilPisa to set
     */
    public void setEstatusConcilPisa(String estatusConcilPisa) {
        this.estatusConcilPisa = estatusConcilPisa;
    }

    /**
     * @return the estatusConcilTro
     */
    public String getEstatusConcilTro() {
        return estatusConcilTro;
    }

    /**
     * @param estatusConcilTro the estatusConcilTro to set
     */
    public void setEstatusConcilTro(String estatusConcilTro) {
        this.estatusConcilTro = estatusConcilTro;
    }

    /**
     * @return the respTimeout
     */
    public String getRespTimeout() {
        return respTimeout;
    }

    /**
     * @param respTimeout the respTimeout to set
     */
    public void setRespTimeout(String respTimeout) {
        this.respTimeout = respTimeout;
    }

    /**
     * @return the autorCobranza
     */
    public String getAutorCobranza() {
        return autorCobranza;
    }

    /**
     * @param autorCobranza the autorCobranza to set
     */
    public void setAutorCobranza(String autorCobranza) {
        this.autorCobranza = autorCobranza;
    }

    /**
     * @return the libreria
     */
    public String getLibreria() {
        return libreria;
    }

    /**
     * @param libreria the libreria to set
     */
    public void setLibreria(String libreria) {
        this.libreria = libreria;
    }

    /**
     * @return the formaPago
     */
    public String getFormaPago() {
        return formaPago;
    }

    /**
     * @param formaPago the formaPago to set
     */
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    /**
     * @return the numeroTC
     */
    public String getNumeroTC() {
        return numeroTC;
    }

    /**
     * @param numeroTC the numeroTC to set
     */
    public void setNumeroTC(String numeroTC) {
        this.numeroTC = numeroTC;
    }

    /**
     * @return the caja
     */
    public String getCaja() {
        return caja;
    }

    /**
     * @param caja the caja to set
     */
    public void setCaja(String caja) {
        this.caja = caja;
    }

    /**
     * @return the importePagado
     */
    public double getImportePagado() {
        return importePagado;
    }

    /**
     * @param importePagado the importePagado to set
     */
    public void setImportePagado(double importePagado) {
        this.importePagado = importePagado;
    }

    /**
     * @return the idConcilBco
     */
    public long getIdConcilBco() {
        return idConcilBco;
    }

    /**
     * @param idConcilBco the idConcilBco to set
     */
    public void setIdConcilBco(long idConcilBco) {
        this.idConcilBco = idConcilBco;
    }

    /**
     * @return the tipoTC
     */
    public String getTipoTC() {
        return tipoTC;
    }

    /**
     * @param tipoTC the tipoTC to set
     */
    public void setTipoTC(String tipoTC) {
        this.tipoTC = tipoTC;
    }

    /**
     * @return the secure
     */
    public String getSecure() {
        return secure;
    }

    /**
     * @param secure the secure to set
     */
    public void setSecure(String secure) {
        this.secure = secure;
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the avs
     */
    public String getAvs() {
        return avs;
    }

    /**
     * @param avs the avs to set
     */
    public void setAvs(String avs) {
        this.avs = avs;
    }

    /**
     * @return the claseServicio
     */
    public String getClaseServicio() {
        return claseServicio;
    }

    /**
     * @param claseServicio the claseServicio to set
     */
    public void setClaseServicio(String claseServicio) {
        this.claseServicio = claseServicio;
    }

    /**
     * @return the tipotel
     */
    public String getTipotel() {
        return tipotel;
    }

    /**
     * @param tipotel the tipotel to set
     */
    public void setTipotel(String tipotel) {
        this.tipotel = tipotel;
    }


}
