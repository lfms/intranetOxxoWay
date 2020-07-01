/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.dto;

public class DatosCuerpoDTO {

	public String texto;

	public DatosCuerpoDTO(String texto) {
		this.texto=texto;
	}

	public DatosCuerpoDTO() {
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
}