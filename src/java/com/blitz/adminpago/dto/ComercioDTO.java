/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author pgrande
 */
public class ComercioDTO implements Serializable{

    private String idComercio;
    private String cveComercio;
    private String cveComercioOrigen;
    private String nombre;
    private String diasHabiles;
    private String horaIni;
    private String horaFin;
    private String estatus;
    private String correoConcil;
    private Date dHoraIni;
    private Date dHoraFin;
    private boolean horarioValido;
    private String timeout;
    private String timeoutrein;
    private String pisaoffline;
    private String pisabatch;

    public String getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(String idComercio) {
        this.idComercio = idComercio;
    }

    public String getCveComercio() {
        return cveComercio;
    }

    public void setCveComercio(String cveComercio) {
        this.cveComercio = cveComercio;
    }

    public String getCveComercioOrigen() {
        return cveComercioOrigen;
    }

    public void setCveComercioOrigen(String cveComercioOrigen) {
        this.cveComercioOrigen = cveComercioOrigen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDiasHabiles() {
        return diasHabiles;
    }

    public void setDiasHabiles(String diasHabiles) {
        this.diasHabiles = diasHabiles;
    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getCorreoConcil() {
        return correoConcil;
    }

    public void setCorreoConcil(String correoConcil) {
        this.correoConcil = correoConcil;
    }

    public Date getdHoraIni() {
        return dHoraIni;
    }

    public void setdHoraIni(Date dHoraIni) {
        this.dHoraIni = dHoraIni;
    }

    public Date getdHoraFin() {
        return dHoraFin;
    }

    public void setdHoraFin(Date dHoraFin) {
        this.dHoraFin = dHoraFin;
    }

    public boolean isHorarioValido() {
        return horarioValido;
    }

    public void setHorarioValido(boolean horarioValido) {
        this.horarioValido = horarioValido;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getTimeoutrein() {
        return timeoutrein;
    }

    public void setTimeoutrein(String timeoutrein) {
        this.timeoutrein = timeoutrein;
    }

    public String getPisaoffline() {
        return pisaoffline;
    }

    public void setPisaoffline(String pisaoffline) {
        this.pisaoffline = pisaoffline;
    }

    public String getPisabatch() {
        return pisabatch;
    }

    public void setPisabatch(String pisabatch) {
        this.pisabatch = pisabatch;
    }

}
