package com.enviosya.cadete.sessionbean;

import com.enviosya.cadete.dominio.Cadete;
import com.enviosya.cadete.dto.CadeteDto;
import com.enviosya.cadete.dominio.Vehiculo;
import com.enviosya.cadete.dto.VehiculoDto;
import com.enviosya.cadete.excepcion.DatoIncorrectoException;
import com.enviosya.cadete.excepcion.EntidadNoExisteException;
import com.enviosya.cadete.excepcion.EntidadYaExisteException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

@Stateless
@LocalBean
public class CadeteBean {
    static Logger log = Logger.getLogger(CadeteBean.class.getName());
    
    Gson gson = new Gson();
    
    @PersistenceContext
    private EntityManager em;

    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory cf;

    @Resource(lookup = "jms/Queue")
    private Queue queue;

    @PostConstruct
    private void init() {
    }

    public CadeteDto agregar(CadeteDto dto) throws EntidadYaExisteException,
            DatoIncorrectoException, javax.ejb.EJBException {
        Cadete cadete = null;
        if (!existe(dto)) {
            cadete = convertirAEntity(dto);
            em.persist(cadete);
        }
        return convertirADto(cadete);
    }

    public List<CadeteDto> listar() throws EntidadNoExisteException {
        List<CadeteDto> lista = convertirADto(em.createQuery(
                "SELECT c FROM Cadete c").getResultList());
        if (lista.isEmpty()) {
            log.debug("Módulo Cadetes - Listar Cadetes - Error: No se han registrado cadetes.");
            throw new EntidadNoExisteException("No se han registrado cadetes.");
        } else {
            return lista;
        }
    }

    public Cadete buscarEntity(Long id) {
        if (id != null) {
            return em.find(Cadete.class, id);
        }
        return null;
    }

    public CadeteDto buscar(String id) throws EntidadNoExisteException, DatoIncorrectoException {    
        if (!id.equals("")) {
            Long idLong = Long.parseLong(id);
            Cadete cadete = em.find(Cadete.class, idLong);
            if (cadete == null) {
                log.debug("Módulo Cadetes - Buscar Cadete - Error: No existe un cadete con ID " + id);
                throw new EntidadNoExisteException("No existe un cadete con el ID provisto.");
            } else {
                return convertirADto(cadete);
            }
        }
        log.debug("Módulo Cadetes - Buscar Cadete - Error: ID de cadete incorrecto.");
        throw new DatoIncorrectoException("Error en el ID del cadete.");
    }

    public List<CadeteDto> buscarCadetesCercanos(String ubicacion) throws EntidadNoExisteException {
        List<CadeteDto> lista
                = convertirADto(em.createQuery(
                        "SELECT c FROM Cadete c")
                        .setMaxResults(4)
                        .getResultList());
        if (lista.isEmpty()) {
            log.debug("Módulo Cadetes - Buscar Cadetes Cercanos - Error: No hay cadetes disponibles.");
            throw new EntidadNoExisteException("No hay cadetes disponibles.");
        } else {
            return lista;
        }
    }

    public CadeteDto modificar(CadeteDto dto) throws EntidadNoExisteException {
        Cadete cadete = null;
        cadete = em.find(Cadete.class, dto.getId());
        if (cadete != null) {
            cadete.setCedula(dto.getCedula());
            cadete.setNombre(dto.getNombre());
            cadete.setApellido(dto.getApellido());
            cadete.setEmail(dto.getEmail());
            em.merge(cadete);
            return convertirADto(cadete);
        } else {
            log.debug("Módulo Cadetes - Modificar Cadete - Error: El cadete a modificar no existe.");
            throw new EntidadNoExisteException("El cadete a modificar no existe.");
        }
    }

    public boolean eliminar(Long id) {
        Cadete cadete = em.find(Cadete.class, id);
        if (cadete != null) {
            List<VehiculoDto> listaVehiculos = listarVehiculosCadete(id);
            for (VehiculoDto vehiculo : listaVehiculos) {
                vehiculo.setCadete(null);
            }
            em.remove(cadete);
            return true;
        } else {
            return false;
        }
    }

    public boolean existe(CadeteDto dto) throws DatoIncorrectoException, EntidadYaExisteException {
        try {
            if (dto.getCedula() == null || dto.getEmail() == null) {
                log.debug("Módulo Cadetes - Existe Cadete - Error: Datos provistos incorrectos.");
                throw new DatoIncorrectoException("Datos incorrectos.");
            }
            List<CadeteDto> lista
                    = convertirADto(em.createQuery(
                            "select c "
                            + "from Cadete c "
                            + "where c.cedula = :cedula or c.email = :email")
                            .setParameter("cedula", dto.getCedula())
                            .setParameter("email", dto.getEmail()).getResultList());
            if (lista.size() > 0) {
                log.debug("Módulo Cadetes - Existe Cadete - Error: "
                        + "Ya existe un usuario con esa cedula o email registrado en el sistema.");
                throw new EntidadYaExisteException(
                        "Ya existe un usuario con esa cedula o email registrado en el sistema.");
            } else {
                return false;
            }
        } catch (javax.ejb.EJBException e) {
            log.debug("Módulo Cadetes - Existe Cadete - Error: Datos provistos incorrectos.");
            throw new DatoIncorrectoException("Error en los datos provistos.");
        } catch (java.lang.NullPointerException e) {
            log.debug("Módulo Cadetes - Existe Cadete - Error: Datos provistos incorrectos.");
            throw new DatoIncorrectoException("Error en los datos provistos.");
        }
    }

    private CadeteDto convertirADto(Cadete entity) {
        CadeteDto dto = new CadeteDto();
        dto.setId(entity.getId());
        dto.setCedula(entity.getCedula());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    private List<CadeteDto> convertirADto(List<Cadete> cadetes) {
        List<CadeteDto> dtos = new ArrayList<>();
        for (Cadete cadete : cadetes) {
            dtos.add(convertirADto(cadete));
        }
        return dtos;
    }

    private Cadete convertirAEntity(CadeteDto dto) {
        Cadete entity = new Cadete();
        entity.setCedula(dto.getCedula());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    public boolean asignarVehiculo(Long idCadete, Long idVehiculo) {
        Cadete cadete = em.find(Cadete.class, idCadete);
        Vehiculo vehiculo = em.find(Vehiculo.class, idVehiculo);
        if (cadete != null && vehiculo != null) {
            vehiculo.setCadete(cadete);
            return true;
        }
        return false;
    }

    public List<VehiculoDto> listarVehiculosCadete(Long idCadete) {
        Cadete cadete = em.find(Cadete.class, idCadete);
        return convertirADto(em.createQuery(
                "SELECT v FROM Vehiculo v WHERE v.cadete=:cadete")
                .setParameter("cadete", cadete)
                .getResultList());
    }
}
