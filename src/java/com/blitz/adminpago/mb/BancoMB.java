/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.mb;
import com.blitz.adminpago.bo.BancoBO;
import com.blitz.adminpago.dto.BancoDTO;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Metodos para la administracion del catalogo de bancos
 * @author PGRANDE
 */
public class BancoMB {
    
    @ManagedProperty(value = "#{BancoBO}")
    private BancoBO bancoBO;
    private BancoDTO bancoDTO;
    private boolean mostrarErrorBanco;
    private boolean mostrarBanco;
    private String errorBanco;
    private List bancos;
    private DataScrollerList dataScrollerList;


    private String parNombreBanco;


    public String muestraBancos() {

        try
        {

            mostrarBanco = false;
            mostrarErrorBanco = false;
            errorBanco = null;

            bancoDTO = new BancoDTO();

            bancos = bancoBO.obtenerBancos(parNombreBanco);

            if (bancos == null) {
                mostrarErrorBanco = true;
                mostrarBanco = false;
                errorBanco = "HUBO UN ERROR AL CONSULTAR LA INFORMACION.";

            } else if (bancos.size() > 0) {
                mostrarErrorBanco = false;
                mostrarBanco = true;
                errorBanco = null;
                setDataScrollerList(new DataScrollerList(bancos));
            } else {
                mostrarErrorBanco = true;
                mostrarBanco = false;
                errorBanco = "NO EXISTEN BANCOS CON LOS DATOS DE BUSQUEDA SELECCIONADOS";
            }

        } catch (Exception e) {
            return null;
        }

        return null;

    }

    public void borrarCampos()
    {
        mostrarErrorBanco = false;
        mostrarBanco = false;
        errorBanco = null;
        bancos = null;
        parNombreBanco = null;

    }


    public String buscaBanco(ActionEvent event) {

        UIParameter idBan = (UIParameter) event.getComponent().findComponent("idBanco");
        String lstBan = idBan.getValue().toString();

        if (lstBan != null && lstBan.length() > 0) {

            bancoDTO = bancoBO.obtenerBanco(lstBan);

            if (bancoDTO != null && bancoDTO.getNombreBanco() != null )
                return "detalleBanco";


        }

        return null;
    }



    public String altaBanco() {
        bancoDTO = new BancoDTO();
        borrarCampos();
        return "altaBanco";
    }


    public String registrarBanco()
    {

        if ( bancoDTO == null || bancoDTO.getNombreBanco() == null || bancoDTO.getNombreBanco().length() == 0)
        {
            mostrarErrorBanco = true;
            errorBanco = "EL NOMBRE DEL BANCO NO PUEDE ESTAR VACIO";
            return null;

        }

        bancoDTO.setIdBanco(0);
        int ret = bancoBO.add(bancoDTO);
        if (ret > 0 )
        {
            mostrarErrorBanco = true;
            errorBanco = "BANCO REGISTRADO";
            //Actualizamos la lista de los bancos
            bancos = bancoBO.obtenerBancos(null);
            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();
            BinesMB bines = (BinesMB) elContext.getELResolver().getValue(elContext,null,"BinesMB");
            bines.setBancos(this.obtenerListaBancos());
        }
        else
        {
            mostrarErrorBanco = true;
            errorBanco = "ERROR AL REGISTRAR EL BANCO";
        }

        return null;
    }



    public String actualizaBanco()
    {
        if ( bancoDTO != null && bancoDTO.getNombreBanco() != null )
        {

            int ret = bancoBO.actualizarBanco(bancoDTO);
            if (ret > 0 )
            {
                mostrarErrorBanco = true;
                errorBanco = "BANCO ACTUALIZADO";
                //Actualizamos la lista de los bancos
                bancos = bancoBO.obtenerBancos(null);
                FacesContext context = FacesContext.getCurrentInstance();
                ELContext elContext = context.getELContext();
                BinesMB bines = (BinesMB) elContext.getELResolver().getValue(elContext,null,"BinesMB");
                bines.setBancos(this.obtenerListaBancos());

            }
            else
            {
                mostrarErrorBanco = true;
                errorBanco = "ERROR AL ACTUALIZAR EL BANCO";
            }

        }
        return null;
    }


    public String regresar() {
        bancoDTO = new BancoDTO();
        mostrarErrorBanco = false;
        errorBanco = null;
        return "consultaBanco";
    }
    


    public Map obtenerListaBancos()
    {
        return bancoBO.obtenerBancosH();
    }



    /**
     * @param bancoBO the bancoBO to set
     */
    public void setBancoBO(BancoBO bancoBO) {
        this.bancoBO = bancoBO;
    }

    /**
     * @return the mostrarErrorBanco
     */
    public boolean isMostrarErrorBanco() {
        return mostrarErrorBanco;
    }

    /**
     * @param mostrarErrorBanco the mostrarErrorBanco to set
     */
    public void setMostrarErrorBanco(boolean mostrarErrorBanco) {
        this.mostrarErrorBanco = mostrarErrorBanco;
    }

    /**
     * @return the mostrarBanco
     */
    public boolean isMostrarBanco() {
        return mostrarBanco;
    }

    /**
     * @param mostrarBanco the mostrarBanco to set
     */
    public void setMostrarBanco(boolean mostrarBanco) {
        this.mostrarBanco = mostrarBanco;
    }

    /**
     * @return the errorBanco
     */
    public String getErrorBanco() {
        return errorBanco;
    }

    /**
     * @param errorBanco the errorBanco to set
     */
    public void setErrorBanco(String errorBanco) {
        this.errorBanco = errorBanco;
    }

    /**
     * @return the bancos
     */
    public List getBancos() {
        return bancos;
    }

    /**
     * @param bancos the bancos to set
     */
    public void setBancos(List bancos) {
        this.bancos = bancos;
    }

    /**
     * @return the dataScrollerList
     */
    public DataScrollerList getDataScrollerList() {
        return dataScrollerList;
    }

    /**
     * @param dataScrollerList the dataScrollerList to set
     */
    public void setDataScrollerList(DataScrollerList dataScrollerList) {
        this.dataScrollerList = dataScrollerList;
    }

    /**
     * @return the parNombreBanco
     */
    public String getParNombreBanco() {
        return parNombreBanco;
    }

    /**
     * @param parNombreBanco the parNombreBanco to set
     */
    public void setParNombreBanco(String parNombreBanco) {
        this.parNombreBanco = parNombreBanco;
    }

    /**
     * @return the bancoDTO
     */
    public BancoDTO getBancoDTO() {
        if ( bancoDTO == null )
            bancoDTO = new BancoDTO();
        return bancoDTO;
    }

    /**
     * @param bancoDTO the bancoDTO to set
     */
    public void setBancoDTO(BancoDTO bancoDTO) {
        this.bancoDTO = bancoDTO;
    }

}
