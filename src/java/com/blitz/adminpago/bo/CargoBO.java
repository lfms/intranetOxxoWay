/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.bo;

import com.blitz.adminpago.util.Utilerias;
import com.blitz.adminpago.util.Rijndael_Algorithm;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author PGRANDE
 */
public class CargoBO {

    public static Log log = LogFactory.getLog(CargoBO.class);

    /*
    public List<com.blitz.pagoenlinea.ws.CargoDTO> obtenerCargos(String fechaConfI, String fechaConfF, String tarjeta, String telOrigen) {

        List<com.blitz.pagoenlinea.ws.CargoDTO> cargos = null;

        try { // Call Web Service Operation
            //152
            //URL lobEndPoint = new URL("http://200.57.141.152/conectorPagoTelmex/WSPropiedadBOService?wsdl");
            //URL lobEndPoint = new URL("http://172.21.33.71/conectorPagoTelmex/WSPropiedadBOService?wsdl");
            URL lobEndPoint = new URL("http://10.248.201.171:8002/conectorPagoTelmex/WSRegMonTMXService?wsdl");
            QName lobQName = new QName("http://ws.pagoenlinea.blitz.com/", "WSRegMonTMXService");
            com.blitz.pagoenlinea.ws.WSRegMonTMXService service = new com.blitz.pagoenlinea.ws.WSRegMonTMXService(lobEndPoint, lobQName);
            com.blitz.pagoenlinea.ws.WSRegMonTMX port = service.getWSRegMonTMXPort();
            cargos = port.obtenerCargos(fechaConfI, fechaConfF, tarjeta, telOrigen);
            log.info("Cargos:" + cargos.size());

            if (cargos != null ) {
                try {
                    Iterator it = cargos.iterator();
                    com.blitz.pagoenlinea.ws.CargoDTO dto = (com.blitz.pagoenlinea.ws.CargoDTO)it.next();
                    if ( dto.getTarjeta() != null && dto.getTarjeta().length() > 15) {
                        String tarDes = Rijndael_Algorithm.Desencriptar(dto.getTarjeta());
                        String mascara = Utilerias.enmascararTarjeta(tarDes);
                        if ( mascara != null )
                            dto.setTarjeta(mascara);
                        else
                            dto.setTarjeta(tarDes);
                    }

                } catch(Exception e3) {
                    ;
                }

            }


        } catch (Exception ex) {
            log.error("Cargos:" + ex.toString());
        }
        return cargos;

    }*/


}
