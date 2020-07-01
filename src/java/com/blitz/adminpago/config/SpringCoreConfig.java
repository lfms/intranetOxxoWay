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
import com.blitz.adminpago.bo.PerfilBO;
import com.blitz.adminpago.dao.AccesoDAO;
import com.blitz.adminpago.dao.BancoDAO;
import com.blitz.adminpago.dao.BinesDAO;
import com.blitz.adminpago.dao.ComercioDAO;
import com.blitz.adminpago.dao.ModuloDAO;
import com.blitz.adminpago.dao.PerfilDAO;
import com.blitz.adminpago.dao.SucursalDAO;
import com.blitz.adminpago.dto.ListaComercioDTO;
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
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
