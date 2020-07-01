package com.blitz.adminpago.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.telmex.tiendaenlinea.util.commons.UnknownPropertyException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionPropertiesContainer {

    private static Log log = LogFactory.getLog(ConnectionPropertiesContainer.class);
    private Properties props;
    /*

    private static ResourceBundle properties;

    private static final String CONFIGURATION_FILE = "connections.properties";

    static {

    try {

    properties = ResourceBundle.getBundle(CONFIGURATION_FILE,new Locale("es", "MX"),ConnectionPropertiesContainer.class.getClassLoader());

    } catch (Exception e) {
    log.error("El error es: " + e.getMessage(), e);
    }
    }

    public static String getProperty(String nombre)
    throws UnknownPropertyException {
    String valor = (String) properties.getObject(nombre);

    if (valor == null)
    throw new UnknownPropertyException(nombre);

    return valor;
    }
     * */
    public ConnectionPropertiesContainer() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream( "connections.properties" );
        this.props = new Properties();
        try {
            this.props.load(in);
            //log.info("properties = " + props);
        } catch (IOException e) {
            log.error("MensajesProperties: " + e);
        }
    }

    /**
     * Devuelve la propiedad correspondiente a la llave indicada
     *
     * @param   key     Llave que identifica la propiedad.
     * @return Devuelve la propiedad identificada por la llave.
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
