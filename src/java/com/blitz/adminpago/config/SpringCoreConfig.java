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
import com.blitz.adminpago.bo.SucursalBO;
import com.blitz.adminpago.dao.AccesoDAO;
import com.blitz.adminpago.dao.DaoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author Charolastros
 */
@Configuration
@PropertySource(value = "/WEB-INF/resource/qa/app.properties")
public class SpringCoreConfig {
    
    @Bean
    public DaoTest daoTest(){
        return new DaoTest();
    }
    
    @Bean 
    public AccesoDAO getAccesoDAO(){
        return new AccesoDAO();
    }
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
    
    @Bean(name = "SucursalBO")
    public SucursalBO getSucursalBO(){
        return new SucursalBO();
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
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
