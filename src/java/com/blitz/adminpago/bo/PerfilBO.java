/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.bo;

import com.blitz.adminpago.dao.ModuloDAO;
import com.blitz.adminpago.dao.PerfilDAO;
import com.blitz.adminpago.dto.PerfilDTO;
import com.blitz.adminpago.dto.PerfilModuloDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 *
 * @author pgrande
 */
@Service
public class PerfilBO implements Serializable{

    private Logger log = Logger.getLogger("com.blitz.negocio.PedidoBO");
    @Autowired
    private PerfilDAO perfilDAO;
    @Autowired
    private ModuloDAO moduloDAO;

    public List obtenerPerfiles(PerfilDTO pobPerfil) {
        List perfiles = perfilDAO.obtenerPerfiles(pobPerfil);

        if (perfiles != null) {
            Iterator it = perfiles.iterator();
            while (it.hasNext()) {
                PerfilDTO dto = (PerfilDTO) it.next();
                List modulos = perfilDAO.obtenerPerfilModulo(dto.getIdPerfil());
                dto.setPerfilModulo(modulos);
                if (modulos != null && modulos.size() > 0) {
                    String lstNombres = "";
                    Iterator it2 = modulos.iterator();
                    while (it2.hasNext()) {
                        PerfilModuloDTO dto2 = (PerfilModuloDTO) it2.next();
                        lstNombres += dto2.getNombreModulo() + " ";
                    }
                    dto.setNomModulos(lstNombres);
                }
            }

        }

        return perfiles;

    }

    public Map obtenerSubPerfiles(String pstIdPerfil, String pstIdTienda) {
        Map subPerfiles = new LinkedHashMap<String, String>();

        PerfilDTO perfildto = new PerfilDTO();
        perfildto.setIdPerfil(pstIdPerfil);
        perfildto.setTienda(pstIdTienda);
        List subPer = obtenerPerfiles(perfildto);

        if (subPer != null && subPer.size() > 0) {
            Iterator it = subPer.iterator();
            while (it.hasNext()) {
                PerfilDTO dto = (PerfilDTO) it.next();
                subPerfiles.put(dto.getNombre(), dto.getIdSubPerfil());
            }
        }
        return subPerfiles;
    }

    public PerfilDTO obtenerPerfil(String pstIdPerfil) {
        PerfilDTO perfil = perfilDAO.obtenerPerfil(pstIdPerfil);

        if (perfil != null) {
            List modulos = perfilDAO.obtenerPerfilModulo(pstIdPerfil);
            perfil.setPerfilModulo(modulos);
            if (modulos != null && modulos.size() > 0) {
                String lstNombres = "";
                Iterator it2 = modulos.iterator();
                while (it2.hasNext()) {
                    PerfilModuloDTO dto2 = (PerfilModuloDTO) it2.next();
                    lstNombres += dto2.getNombreModulo() + " ";
                }
                perfil.setNomModulos(lstNombres);

                //Actualizamos el maximo orden
                //perfil.setUltOrden(((PerfilModuloDTO)modulos.get(modulos.size()-1)).getOrden() + 1 );

            }

            //List modulosDisp = obtenerModDispPerfil(pstIdPerfil, pstIdSubPerfil );
            //perfil.setPerfilModDisponibles(modulosDisp);

        }

        return perfil;

    }

    public List obtenerModDispPerfil(String pnuIdPerfil, String pnuIdSubPerfil) {
        return moduloDAO.obtenerModDispPerfil(pnuIdPerfil, pnuIdSubPerfil);
    }

    public HashMap obtenerModDisp() {
        return moduloDAO.obtenerMod();
    }

    public int eliminarModPerfil(String pstIdPerMod) {
        return perfilDAO.eliminarModPerfil(pstIdPerMod);
    }

    public int eliminarPerfil(String pstIdPerMod) {
        return perfilDAO.eliminarPerfil(pstIdPerMod);
    }

    /**
     * 
     * @param pstIdPerMod
     * @return
     */
    public int eliminarModulosPerfil(String pstIdPerMod) {
        int lnuResultado = 0;
        try {
            lnuResultado = perfilDAO.eliminarModulosPerfil(pstIdPerMod);
        } catch (DataAccessException dae) {
            log.error("Error al eliminar los m�dulos del perfil. " + dae.toString());
            lnuResultado = 0;
        }
        return lnuResultado;
    }

    public int actualizarPerfil(PerfilDTO perfil) {
        int lnuResultado = 0;
        try {
            lnuResultado = perfilDAO.actualizarPerfil(perfil);
        } catch (DataAccessException dae) {
            log.error("Error al actualizar el perfil. " + dae.toString());
            lnuResultado = 0;
        }
        return lnuResultado;
    }

    public void moverModPerfil(String pstIdPerfil, String pstIdPerMod, String pstOrden, int opcion, String pstIdSubPerfil) {
        //opcion: 1=arriba ; 0=abajo

        if (opcion == 1) {
            //Buscamos el orden anterior
            String pstIdPerModAnt = perfilDAO.obtenerPerModAntA(pstIdPerfil, pstOrden, pstIdSubPerfil);
            if (pstIdPerModAnt != null && pstIdPerModAnt.indexOf(":") > 0) {
                // ID_PER_MOD:ORDEN
                String lstId = pstIdPerModAnt.substring(0, pstIdPerModAnt.indexOf(":"));
                String lstOrden = pstIdPerModAnt.substring(pstIdPerModAnt.indexOf(":") + 1);

                //Actualizamos el orden enviado al anterior
                perfilDAO.actualizarOrdenPerMod(lstId, pstOrden);

                //Actualizamos el seleccionado con el orden anterior
                perfilDAO.actualizarOrdenPerMod(pstIdPerMod, lstOrden);

            }
        } else //abajo
        {
            //Buscamos el orden siguiente
            String pstIdPerModAnt = perfilDAO.obtenerPerModPosA(pstIdPerfil, pstOrden, pstIdSubPerfil);
            if (pstIdPerModAnt != null && pstIdPerModAnt.indexOf(":") > 0) {
                // ID_PER_MOD:ORDEN
                String lstId = pstIdPerModAnt.substring(0, pstIdPerModAnt.indexOf(":"));
                String lstOrden = pstIdPerModAnt.substring(pstIdPerModAnt.indexOf(":") + 1);

                //Actualizamos el orden enviado al siguiente
                perfilDAO.actualizarOrdenPerMod(lstId, pstOrden);

                //Actualizamos el seleccionado con el orden siguiente
                perfilDAO.actualizarOrdenPerMod(pstIdPerMod, lstOrden);

            }

        }

    }

    public int agregarModuloPerfil(String pstIdPerfil, String pstIdModulo) {
        int lnuResultado = 0;
        try {
            lnuResultado = perfilDAO.agregarModuloPerfil(pstIdPerfil, pstIdModulo);
        } catch (DataAccessException dae) {
            log.error("Error al agregar m�dulos al perfil. " + dae.toString());
            lnuResultado = 0;
        }
        return lnuResultado;
    }

    /**
     * Ibnserta nuevo perfil
     * @param pobPerfil
     * @return
     */
    public double insertarPerfil(PerfilDTO pobPerfil) {
        int lnuRet = 0;

        if (pobPerfil != null) {

            lnuRet = perfilDAO.insertarPerfil(pobPerfil);

            if (lnuRet > 0 && pobPerfil.getPerfilModDisponibles() != null) {
                //Registramos los modulos asociados
                Iterator it = pobPerfil.getPerfilModDisponibles().iterator();
                while (it.hasNext()) {
                    PerfilModuloDTO pobModulo = (PerfilModuloDTO) it.next();
                    this.agregarModuloPerfil(String.valueOf(lnuRet), Integer.toString(pobModulo.getIdModulo()));
                }
            }

        }
        return lnuRet;
    }

    public double eliminarSubPerfil(String pstIdPerfil) {
        double lnuRet = 0;

        if (pstIdPerfil != null && pstIdPerfil.length() > 0) {

            lnuRet = perfilDAO.eliminarSubPerfilMod(pstIdPerfil);

            if (lnuRet > 0) {
                lnuRet = perfilDAO.eliminarPerfil(pstIdPerfil);
            }

        }
        return lnuRet;
    }

    /**
     * Obtiene lista de perfiles 
     * @return
     */
    public HashMap obtenerPerfiles() {
        return perfilDAO.obtenerPerfiles();
    }

    /**
     * @return the perfilDAO
     */
    public PerfilDAO getPerfilDAO() {
        return perfilDAO;
    }

    /**
     * @param perfilDAO the perfilDAO to set
     */
    public void setPerfilDAO(PerfilDAO perfilDAO) {
        this.perfilDAO = perfilDAO;
    }

    /**
     * @return the moduloDAO
     */
    public ModuloDAO getModuloDAO() {
        return moduloDAO;
    }

    /**
     * @param moduloDAO the moduloDAO to set
     */
    public void setModuloDAO(ModuloDAO moduloDAO) {
        this.moduloDAO = moduloDAO;
    }
}
