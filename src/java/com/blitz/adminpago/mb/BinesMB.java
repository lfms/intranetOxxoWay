/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.mb;

import com.blitz.adminpago.bo.BancoBO;
import com.blitz.adminpago.bo.BinesBO;
import com.blitz.adminpago.dto.BinesDTO;
import com.blitz.adminpago.util.GeneralValidator;
import com.blitz.adminpago.util.Utilerias;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;
import jxl.*;
import jxl.write.*;


/**
 * Metodos para administracion del catalogo de BINES
 * @author PGRANDE
 */
@ManagedBean(name = "BinesMB")
@ViewScoped
public class BinesMB {

    @ManagedProperty(value = "#{BinesBO}")
    private BinesBO binesBO;
    @ManagedProperty(value = "#{BancoBO}")
    private BancoBO bancoBO;
    private BinesDTO binDTO;
    private boolean mostrarErrorBines;
    private boolean mostrarBines;
    private String errorBines;
    private List bines;
    private DataScrollerList dataScrollerList;

    private Map bancos;
    private List bancosLista;


    private String parPrefijo;
    private String parNombreProd;
    private String parEmisor;
    private String fechaI;
    private String fechaF;
    

    /**
     *
     * @return
     */

    public String muestraBines() {

        try
        {

            mostrarBines = false;
            mostrarErrorBines = false;
            errorBines = null;
            binDTO = new BinesDTO();
            if (parPrefijo != null && parPrefijo.length() > 0) {
                binDTO.setPrefijo(parPrefijo);
            }
            if (parNombreProd != null && parNombreProd.length() > 0) {
                binDTO.setNombreProducto(parNombreProd);
            }
            if (parEmisor != null && parEmisor.length() > 0) {
                binDTO.setEmisor(parEmisor);
                binDTO.setIdBanco(parEmisor);
            }

            bines = binesBO.obtenerBines(binDTO);
            if (bines == null) {
                mostrarErrorBines = true;
                mostrarBines = false;
                errorBines = "HUBO UN ERROR AL CONSULTAR LA INFORMACION.";

            } else if (bines.size() > 0) {
                mostrarErrorBines = false;
                mostrarBines = true;
                errorBines = null;
                setDataScrollerList(new DataScrollerList(bines));
            } else {
                mostrarErrorBines = true;
                mostrarBines = false;
                errorBines = "NO EXISTEN BINES CON LOS DATOS DE BUSQUEDA SELECCIONADOS";
            }

        } catch (Exception e) {
            return null;
        }

        return null;

    }

    public void borrarCampos()
    {
        mostrarErrorBines = false;
        mostrarBines = false;
        errorBines = null;
        parPrefijo = null;
        parNombreProd = null;
        parEmisor = null;
        bines = null;
        binDTO = null;

    }


    public String buscaBin(ActionEvent event) {

        //UsuarioDTO usr = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

        UIParameter idBin = (UIParameter) event.getComponent().findComponent("prefijoBin");
        String lstBin = idBin.getValue().toString();

        if (lstBin != null && lstBin.length() > 0) {

            BinesDTO bin = binesBO.obtenerBin(lstBin);

            if (bin != null && bin.getEmisor() != null && bin.getEmisor().length() > 0) {
                //Buscamos el perfil de sesion
                FacesContext context = FacesContext.getCurrentInstance();
                ELContext elContext = context.getELContext();
                binDTO = new BinesDTO();

                binDTO.setPrefijo(lstBin);
                binDTO.setEmisor(bin.getEmisor());
                binDTO.setComentarios(bin.getComentarios());
                binDTO.setFecha(bin.getFecha());
                binDTO.setNombreProducto(bin.getNombreProducto());
                binDTO.setResponsable(bin.getResponsable());
                binDTO.setTipoTarjeta(bin.getTipoTarjeta());
                binDTO.setPrioridad(bin.getPrioridad());
                binDTO.setIdBanco(bin.getIdBanco());

                return "detalleBin";

            }

        }

        return null;
    }


    public String actualizaBin()
    {
        if ( binDTO != null && binDTO.getPrefijo() != null )
        {
            binDTO.setFecha(Utilerias.fechaHoy());
            //Obtenemos el nombre del emisor para dejar la tabla igual
            binDTO.setEmisor(bancoBO.obtenerNombreBanco(binDTO.getIdBanco()));

            int ret = binesBO.actualizarBin(binDTO);
            if (ret > 0 )
            {
                mostrarErrorBines = true;
                errorBines = "BIN ACTUALIZADO";
            }
            else
            {
                mostrarErrorBines = true;
                errorBines = "ERROR AL ACTUALIZAR EL BIN";
            }

        }
        return null;
    }


    public String altaBines() {
        binDTO = new BinesDTO();
        borrarCampos();
        return "altaBines";
    }

    public String faltantesBines() {
        binDTO = new BinesDTO();
        borrarCampos();
        return "consBinesFaltantes";
    }

    public String registrarBin()
    {

        if ( binDTO != null && binDTO.getPrefijo() != null && binDTO.getPrefijo().length() != 6)
        {
            mostrarErrorBines = true;
            errorBines = "LA LONGITUD DEL BIN DEBE SER DE 6 POSICIONES";
            return null;

        }
        
        if ( binDTO != null && (binDTO.getIdBanco()== null || binDTO.getIdBanco().length() == 0) )
        {
            mostrarErrorBines = true;
            errorBines = "EL EMISOR NO PUEDE QUEDAR VACIO";
            return null;

        }



        if ( binDTO != null && binDTO.getPrefijo() != null && binDTO.getPrefijo().length() == 6)
        {
            binDTO.setFecha(Utilerias.fechaHoy());
            //Obtenemos el nombre del emisor para dejar la tabla igual
            binDTO.setEmisor(bancoBO.obtenerNombreBanco(binDTO.getIdBanco()));

            double ret = binesBO.registrarBin(binDTO);
            if (ret > 0 )
            {
                mostrarErrorBines = true;
                errorBines = "BIN REGISTRADO";
            }
            else
            {
                mostrarErrorBines = true;
                errorBines = "ERROR AL REGISTRAR EL BIN";
            }
        }
        else
        {
                mostrarErrorBines = true;
                errorBines = "LA LONGITUD DEL BIN DEBE SER DE 6 POSICIONES";

        }
        return null;
    }

    public String regresar() {
        binDTO = new BinesDTO();
        borrarCampos();
        return "consultaBines";
    }

    public int validaCampo() {

        String TXT_REQUERIDO = "Campo Requerido";
        int error = 0;

        if (binDTO != null )
        {
            if (binDTO.getPrefijo() == null || binDTO.getPrefijo().length() != 6 ||
                    GeneralValidator.soloCaracteresNumericos(binDTO.getPrefijo())==false )
            {
                error = 1;
            }

        }


        return error;
    }


    public String muestraBinesFaltantes() {

        mostrarBines = false;
        mostrarErrorBines = false;
        errorBines = null;

        if ( validaDato(fechaI) == false && validaDato(fechaF) == false  )
        {
            mostrarBines=false;
            mostrarErrorBines=true;
            errorBines = "PROPORCIONE UNA FECHA VALIDA";
            return null;
        }


        try {
            bines = binesBO.obtenerBinesFaltantes(fechaI, fechaF);

            if (bines == null || bines.size() == 0 ) {
                mostrarErrorBines = true;
                mostrarBines = false;
                errorBines = "NO EXISTEN BINES CON LOS DATOS DE BUSQUEDA SELECCIONADOS";

            } else {
                mostrarErrorBines = false;
                mostrarBines = true;
                errorBines = null;
                setDataScrollerList(new DataScrollerList(bines));
            }

        }
        catch(Exception e) {
            return null;
        }

        return null;
    }



    public boolean validaDato(String pstCadena)
    {
        boolean lnuValido = true;

        if ( pstCadena == null || pstCadena.length() == 0 || pstCadena.equals("*"))
            lnuValido = false;

        return lnuValido;

    }

    public void generarReporteBinesF(FacesContext fc) {

        OutputStream out = null;

        HttpServletResponse resp = (HttpServletResponse) fc.getExternalContext().getResponse();


        resp.setContentType("application/vnd.ms-excel");
        resp.addHeader("Content-Disposition", "attachment;filename=BinesFaltantes.xls");

        try {

            out = resp.getOutputStream();

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("BinesFaltantes", 0);
            int fila = 0;

            List lobDatos = bines;

            if ( lobDatos == null )
                lobDatos = binesBO.obtenerBinesFaltantes(fechaI, fechaF);

            if ( lobDatos != null )
            {
                Label encabezado = new Label(0, fila, "BIN");
                sheet.addCell(encabezado);

                Iterator it = null;

                DateFormat customDateFormat = new DateFormat("dd MMM yyyy hh:mm:ss");
                WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);

                BinesDTO bin;
                it = lobDatos.iterator();
                fila++;
                while (it.hasNext()) {

                    bin = (BinesDTO) it.next();
                    Label accion = new Label(0, fila, String.valueOf(bin.getPrefijo()));
                    sheet.addCell(accion);
                    fila++;

                }
                fila++;
            }


            workbook.write();
            workbook.close();
            out.flush();



        } catch (Exception ex) {
            System.out.println(ex)
            ; //logger.equals("generarExcel: " + ex);
        }
        finally {
            if (out != null)
                try {
                out.close();
                }catch(Exception e) { ; }

        }
        fc.responseComplete();

    }


    /**
     * @return the mostrarErrorBines
     */
    public boolean isMostrarErrorBines() {
        return mostrarErrorBines;
    }

    /**
     * @param mostrarErrorBines the mostrarErrorBines to set
     */
    public void setMostrarErrorBines(boolean mostrarErrorBines) {
        this.mostrarErrorBines = mostrarErrorBines;
    }

    /**
     * @return the errorBines
     */
    public String getErrorBines() {
        return errorBines;
    }

    /**
     * @param errorBines the errorBines to set
     */
    public void setErrorBines(String errorBines) {
        this.errorBines = errorBines;
    }

    /**
     * @return the mostrarBines
     */
    public boolean isMostrarBines() {
        return mostrarBines;
    }

    /**
     * @param mostrarBines the mostrarBines to set
     */
    public void setMostrarBines(boolean mostrarBines) {
        this.mostrarBines = mostrarBines;
    }

    /**
     * @return the parPrefijo
     */
    public String getParPrefijo() {
        return parPrefijo;
    }

    /**
     * @param parPrefijo the parPrefijo to set
     */
    public void setParPrefijo(String parPrefijo) {
        this.parPrefijo = parPrefijo;
    }

    /**
     * @return the parNombreProd
     */
    public String getParNombreProd() {
        return parNombreProd;
    }

    /**
     * @param parNombreProd the parNombreProd to set
     */
    public void setParNombreProd(String parNombreProd) {
        this.parNombreProd = parNombreProd;
    }

    /**
     * @return the parEmisor
     */
    public String getParEmisor() {
        return parEmisor;
    }

    /**
     * @param parEmisor the parEmisor to set
     */
    public void setParEmisor(String parEmisor) {
        this.parEmisor = parEmisor;
    }

    /**
     * @return the binDTO
     */
    public BinesDTO getBinDTO() {
        if( this.binDTO == null )
            binDTO = new BinesDTO();
        return binDTO;
    }

    /**
     * @param binDTO the binDTO to set
     */
    public void setBinDTO(BinesDTO binDTO) {
        this.binDTO = binDTO;
    }

    /**
     * @return the bines
     */
    public List getBines() {
        return bines;
    }

    /**
     * @param bines the bines to set
     */
    public void setBines(List bines) {
        this.bines = bines;
    }

    /**
     * @param binesBO the binesBO to set
     */
    public void setBinesBO(BinesBO binesBO) {
        this.binesBO = binesBO;
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
     * @return the bancos
     */
    public Map getBancos() {

        if ( bancos == null )
        {
            bancos = bancoBO.obtenerBancosH();
        }
        return bancos;
    }

    /**
     * @param bancos the bancos to set
     */
    public void setBancos(Map bancos) {
        this.bancos = bancos;
    }

    /**
     * @param bancoBO the bancoBO to set
     */
    public void setBancoBO(BancoBO bancoBO) {
        this.bancoBO = bancoBO;
    }

    /**
     * @return the bancosLista
     */
    public List getBancosLista() {
        return bancosLista;
    }

    /**
     * @param bancosLista the bancosLista to set
     */
    public void setBancosLista(List bancosLista) {
        this.bancosLista = bancosLista;
    }

    /**
     * @return the fechaI
     */
    public String getFechaI() {
        return fechaI;
    }

    /**
     * @param fechaI the fechaI to set
     */
    public void setFechaI(String fechaI) {
        this.fechaI = fechaI;
    }

    /**
     * @return the fechaF
     */
    public String getFechaF() {
        return fechaF;
    }

    /**
     * @param fechaF the fechaF to set
     */
    public void setFechaF(String fechaF) {
        this.fechaF = fechaF;
    }


}
