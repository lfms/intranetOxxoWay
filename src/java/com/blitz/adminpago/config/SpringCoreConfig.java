/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.config;

import com.blitz.adminpago.bo.AccesoBO;
import com.blitz.adminpago.bo.BancoBO;
import com.blitz.adminpago.bo.BinesBO;
import com.blitz.adminpago.bo.ComercioBO;
import com.blitz.adminpago.bo.ModuloBO;
import com.blitz.adminpago.bo.PagoBO;
import com.blitz.adminpago.bo.PerfilBO;
import com.blitz.adminpago.bo.SucursalBO;
import com.blitz.adminpago.dao.AccesoDAO;
import com.blitz.adminpago.dao.BancoDAO;
import com.blitz.adminpago.dao.BinesDAO;
import com.blitz.adminpago.dao.ComercioDAO;
import com.blitz.adminpago.dao.ModuloDAO;
import com.blitz.adminpago.dao.PagoDAO;
import com.blitz.adminpago.dao.PerfilDAO;
import com.blitz.adminpago.dao.SucursalDAO;
import com.blitz.adminpago.dto.ListaComercioDTO;
import com.blitz.adminpago.dto.ListaSucursalDTO;
import com.blitz.adminpago.dto.MenuDTO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(value = "/WEB-INF/resource/qa/app.properties")
public class SpringCoreConfig {
    
    @Bean 
    public AccesoDAO getAccesoDAO(){
        return new AccesoDAO();
    }  
    
    @Bean
    public BancoDAO getBancoDAO(){
        return new BancoDAO();
    }
    
    @Bean 
    public BinesDAO getBinesDAO(){
        return new BinesDAO();
    }
    
    @Bean
    public ComercioDAO getComercioDAO(){
        return new ComercioDAO();
    }
    
    @Bean
    public ModuloDAO getModuloDAO(){
        return new ModuloDAO();
    }
    
    @Bean
    public PerfilDAO getPerfilDAO(){
        return new PerfilDAO();
    }  
    
    @Bean
    public SucursalDAO getSucursalDAO(){
        return new SucursalDAO();
    }
    
    @Bean
    public PagoDAO getPagoDAO(){
        return new PagoDAO();
    }
    
    //BO
    
    @Bean(name = "AccesoBO")
    public AccesoBO getAccesoBO(){
        return new AccesoBO();
    }
    
    @Bean(name = "ModuloBO")
    public ModuloBO getModuloBO(){
        return new ModuloBO();
    }

    @Bean(name = "ComercioBO")
    public ComercioBO getComercioBO(){
        return new ComercioBO();
    }
    
    @Bean(name = "PerfilBO")
    public PerfilBO getPerfilBO(){
        return new PerfilBO();
    }
    
    @Bean(name = "BancoBO")
    public BancoBO getBancoBO(){
        return new BancoBO();
    }
    
    @Bean(name = "BinesBO")
    public BinesBO getBinesBO(){
        return new BinesBO();
    }
    
    @Bean(name = "SucursalBO")
    public SucursalBO getSucursalBO(){
        return new SucursalBO();
    }
    
    @Bean(name ="PagoBO")
    public PagoBO getPagoBO(){
        PagoBO pagoBO = new PagoBO();
        pagoBO.setComercioBD1(".BSN.MKT.APT.TWA.");
        return pagoBO;
    }
    
    //DTOs
    @Bean(name = "MenuDTO")
    public MenuDTO getMenuDTO(){
        return new MenuDTO();
    }
    
    @Bean(name = "ListaComercioDTO")
    public ListaComercioDTO getListaComercioDTO(){
        return new ListaComercioDTO();
    }
    
    @Bean(name = "estatusPagoMap")
    public Map getEstatusPagoMap(){
        Map<String, String> map = new HashMap<>();       
        map.put("AP - ATENDIDO Y POSTEADO", "AP");
        map.put("AA - RECIBIDO Y ENVIADO A PISA", "AA");
        map.put("AS - ENVIADO A PISA SIN SECUENCIA", "AS");
        map.put("RR - RECIBIDO Y NO ENVIADO A PISA", "RR");
        map.put("EN - ERROR NO EXISTE FOLIO", "EN");
        map.put("EE - ERROR NO SE PUDO ENVIAR PAGO", "EE");
        map.put("CA - CANCELADO", "CA");
        return map;
    }
    
    @Bean(name = "csMap")
    public Map getCsMap(){
        Map<String, String> map = new HashMap<>();       
        map.put("Hogar", "'10','19','56','1P','1T','1L','9L','1I','ML','PI','1C','1G'");
        map.put("Negocio", "'20','24','2I','2L','55','80','30','2M','2G','50'");        
        return map;
    }

    @Bean(name = "libreriaMap")
    public Map getLibreriaMap(){
        Map<String, String> map = new HashMap<>();       
        map.put("HMO", "HMO");
        map.put("GDL", "GDL");        
        map.put("CHI", "CHI");        
        map.put("QRO", "QRO");        
        map.put("PBA", "PBA");        
        map.put("MDA", "MDA");        
        map.put("CVA", "CVA");        
        map.put("MEX", "MEX");        
        map.put("MTY", "MTY");        
        map.put("CDJ", "CDJ");        
        map.put("TIJ", "TIJ");        
        map.put("VACIA", "VACIA");        
        return map;
    }
    
    @Bean(name = "ListaSucursalDTO")
    public ListaSucursalDTO getListaSucursalDTO(){
        return new ListaSucursalDTO();
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
