/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.config;

import com.sun.faces.config.FacesInitializer;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 *
 * @author Charolastros
 */
public class MainWebAppInitializer extends FacesInitializer implements WebApplicationInitializer{

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        super.onStartup(classes, servletContext); 
    }
    
    

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.register(new Class[]{SpringCoreConfig.class, DataSourceConfig.class});
        sc.addListener(new ContextLoaderListener(root));
    }
    
}
