package com.blitz.adminpago.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/**
 *
 * @author Luis
 */
@Configuration
public class DataSourceConfig {
    
    @Value("${jndi.name.pagovt}")
    String jndiPagoVT;
    
    @Bean(name = "pagosvtDS")
    public DataSource getDataSourcePagos(){        
        final JndiDataSourceLookup dsLookup =  new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        DataSource dataSource = dsLookup.getDataSource(jndiPagoVT);  
        
        return dataSource;
    }
   
}
