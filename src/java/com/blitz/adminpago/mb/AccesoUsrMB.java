/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.mb;

import com.blitz.adminpago.bo.AccesoBO;
import com.blitz.adminpago.bo.ComercioBO;
import com.blitz.adminpago.bo.ModuloBO;
import com.blitz.adminpago.bo.PerfilBO;
import com.blitz.adminpago.bo.SucursalBO;
import com.blitz.adminpago.dto.ListaComercioDTO;
import com.blitz.adminpago.dto.ListaModuloDTO;
import com.blitz.adminpago.dto.ListaPerfilDTO;
import com.blitz.adminpago.dto.MenuDTO;
import com.blitz.adminpago.dto.UsuarioDTO;
import java.io.Serializable;
import java.util.Map;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 *
 * @author SMMIGUEL
 */
@ManagedBean(name = "AccesoUsrMB")
@ViewScoped
public class AccesoUsrMB implements Serializable{   

    private String usuario;
    private String clave;
    private String mensajeUsr;
    private String mensajeCve;
    private String mensaje;
    @ManagedProperty(value = "#{AccesoBO}")
    private AccesoBO accesoBO;
    @ManagedProperty(value = "#{ModuloBO}")
    private ModuloBO moduloBO;
    @ManagedProperty(value = "#{ComercioBO}")
    private ComercioBO comercioBO;
    @ManagedProperty(value = "#{SucursalBO}")
    private SucursalBO sucursalBO;
    @ManagedProperty(value = "#{PerfilBO}")
    private PerfilBO perfilBO;
    /*
    private TiendaBO tiendaBO;   
    
    */
    
    Log log = LogFactory.getLog(this.getClass());

    /** Creates a new instance of AccesoMB */
    public AccesoUsrMB() {
    }

    public String validaUsuario() {
        String respuesta = null;
        
        if(usuario == null || usuario.equals("")){
            log.info("Falta nombre de usuario");
            mensajeUsr = "Falta nombre de usuario";  
            return respuesta;
        }
        if(clave == null || clave.equals("")){
            log.info("Falta la clave");
            mensajeCve = "Falta la clave";
            return respuesta;
        }
        
        log.info(usuario);
        log.info(clave);
        UsuarioDTO usuarioValido = accesoBO.validarUsuario(usuario.toUpperCase(), clave.toUpperCase());
        log.info(usuarioValido);
        mensaje = "Usuario no valido";
        if (usuarioValido != null) {

            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();

            respuesta = "usrvalido";

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.setAttribute("usuario", usuarioValido);
            

            //Buscamos los modulos que puede ver de acuerdo a su perfil
            MenuDTO menu = (MenuDTO) elContext.getELResolver().getValue(elContext,null,"MenuDTO");
            if(menu == null) menu = new MenuDTO();

            int idPerfil = 0;
            if (usuarioValido.getIdPerfil() != null )
                idPerfil = Integer.parseInt(usuarioValido.getIdPerfil());
            /* 18/05/2011, se elimina el valor del subperfil para mostrar el men� de opciones
            int idSubPerfil = 0;
            if (usuarioValido.getIdSubPerfil() != null )
                idSubPerfil = Integer.parseInt(usuarioValido.getIdSubPerfil());
            */
            
            log.info(moduloBO);
            log.info(idPerfil);
            log.info(usuarioValido);
            
            menu.setMenu(moduloBO.obtenerModPerfil(idPerfil,usuarioValido.getUniversal() ));
            
            log.info("Menu Size: " + menu.getMenu().size() );
            
            mensaje = null;

            //Llenamos los valores de comercios y sucursales
            ListaComercioDTO listaCom = (ListaComercioDTO) elContext.getELResolver().getValue(elContext,null,"ListaComercioDTO");
            if(listaCom == null) listaCom = new ListaComercioDTO();
            if ( listaCom == null  || listaCom.getListaComercio() == null)
            {
                Map lista = comercioBO.obtenerComercios();
                if ( lista != null ){
                    listaCom.setListaComercio(lista);                   
                }
                Map listaXcve = comercioBO.obtenerComerciosXcve();
                if(listaXcve!=null)
                    listaCom.setListaComercioXid(listaXcve);
                Map listaXcveNom = comercioBO.obtenerComerciosXNomCve();
                if(listaXcveNom!=null)
                    listaCom.setListaComercioXNom(listaXcveNom);

            }

            ListaPerfilDTO listaPerfil = (ListaPerfilDTO) elContext.getELResolver().getValue(elContext,null,"ListaPerfilDTO");
            if(listaPerfil == null) listaPerfil = new ListaPerfilDTO();
            if ( listaPerfil == null  || listaPerfil.getListaPerfil() == null)
            {
                Map lista = perfilBO.obtenerPerfiles();
                if ( lista != null )
                    listaPerfil.setListaPerfil(lista);
            }

            ListaModuloDTO listaModulo = (ListaModuloDTO) elContext.getELResolver().getValue(elContext,null,"ListaModuloDTO");
            if(listaModulo == null) listaModulo = new ListaModuloDTO();
            if ( listaModulo == null  || listaModulo.getListaModulos() == null)
            {
                Map lista = moduloBO.obtenerModulo();
                if ( lista != null )
                    listaModulo.setListaModulos(lista);
            }

            BancoMB banco = (BancoMB) elContext.getELResolver().getValue(elContext,null,"BancoMB");
            Map bancos = banco.obtenerListaBancos();
            
            if ( bancos != null )
            {
                BinesMB bines = (BinesMB) elContext.getELResolver().getValue(elContext,null,"BinesMB");
                bines.setBancos(bancos);
            }
            
        }
            
        else{
            mensaje = "Usuario no valido para administrador";
            log.info("usuario no valido");
            return null;
        }
        
        log.info("-------> " + respuesta);
        return respuesta;       
    }

    public String cancelaEntradas() {
        usuario = null;
        clave = null;
        mensaje = null;
        mensajeCve = null;
        mensajeUsr = null;
        return null;
    }
    
    public String terminarAplicacion() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("usuario");
        return "salir";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getMensajeUsr() {
        return mensajeUsr;
    }

    public void setMensajeUsr(String mensajeUsr) {
        this.mensajeUsr = mensajeUsr;
    }

    public String getMensajeCve() {
        return mensajeCve;
    }

    public void setMensajeCve(String mensajeCve) {
        this.mensajeCve = mensajeCve;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public AccesoBO getAccesoBO() {
        return accesoBO;
    }

    public void setAccesoBO(AccesoBO accesoBO) {
        this.accesoBO = accesoBO;
    }

    public ModuloBO getModuloBO() {
        return moduloBO;
    }

    public void setModuloBO(ModuloBO moduloBO) {
        this.moduloBO = moduloBO;
    }

    public ComercioBO getComercioBO() {
        return comercioBO;
    }

    public void setComercioBO(ComercioBO comercioBO) {
        this.comercioBO = comercioBO;
    }

    public SucursalBO getSucursalBO() {
        return sucursalBO;
    }

    public void setSucursalBO(SucursalBO sucursalBO) {
        this.sucursalBO = sucursalBO;
    }

    public PerfilBO getPerfilBO() {
        return perfilBO;
    }

    public void setPerfilBO(PerfilBO perfilBO) {
        this.perfilBO = perfilBO;
    }    
    
}
