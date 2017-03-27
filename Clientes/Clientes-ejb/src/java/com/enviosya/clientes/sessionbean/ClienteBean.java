package com.enviosya.clientes.sessionbean;

import com.enviosya.clientes.dto.ClienteDto;
import com.enviosya.clientes.dominio.Cliente;
import com.enviosya.clientes.excepcion.DatoIncorrectoException;
import com.enviosya.clientes.excepcion.EntidadNoExisteException;
import com.enviosya.clientes.excepcion.EntidadYaExisteException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

@Stateless
@LocalBean
public class ClienteBean {
    static Logger log = Logger.getLogger(ClienteBean.class.getName());
    
    Gson gson = new Gson();
    
    @PersistenceContext
    private EntityManager em;  

    @PostConstruct
    private void init() {
    }

    public ClienteDto agregar(ClienteDto dto) throws EntidadYaExisteException,
            DatoIncorrectoException, javax.ejb.EJBException {
        Cliente cliente = null;
        if (!existe(dto)) {
            cliente = convertirAEntity(dto);
            em.persist(cliente);
        }
        return convertirADto(cliente);
    }

    public List<ClienteDto> listar() throws EntidadNoExisteException {
        List<ClienteDto> lista = convertirADto(em.createQuery(
                "SELECT c FROM Cliente c").getResultList());
        if (lista.isEmpty()) {
            log.debug("Módulo Clientes - Listar Clientes - Error: No se han registrado clientes.");
            throw new EntidadNoExisteException("No se han registrado clientes.");
        } else {
            return lista;
        }
    }

    public Cliente buscarEntity(Long id) {
        if (id != null) {
            return em.find(Cliente.class, id);
        }
        return null;
    }

    public ClienteDto buscar(String id) throws EntidadNoExisteException, DatoIncorrectoException {
        if (!id.equals("")) {
            Long idLong = Long.parseLong(id);
            Cliente cliente = em.find(Cliente.class, idLong);
            if (cliente == null) {
                log.debug("Módulo Clientes - Buscar Cliente - Error: No existe un cliente con ID " + id);
                throw new EntidadNoExisteException("No existe un cliente con el ID provisto.");
            } else {
                return convertirADto(cliente);
            }
        }
        log.debug("Módulo Clientes - Buscar Cliente - Error: ID de cliente incorrecto.");
        throw new DatoIncorrectoException("Error en el ID del cliente.");
    }

    public ClienteDto modificar(ClienteDto dto) throws EntidadNoExisteException {
        Cliente cliente = null;
        cliente = em.find(Cliente.class, dto.getId());
        if (cliente != null) {
            cliente.setCedula(dto.getCedula());
            cliente.setNombre(dto.getNombre());
            cliente.setApellido(dto.getApellido());
            cliente.setEmail(dto.getEmail());
            em.merge(cliente);
            return convertirADto(cliente);
        } else {
            log.debug("Módulo Clientes - Modificar Cliente - Error: El cliente a modificar no existe.");
            throw new EntidadNoExisteException("El cliente a modificar no existe.");
        }
    }

    public boolean eliminar(Long id) throws EntidadNoExisteException {
        Cliente cliente = em.find(Cliente.class, id);
        if (cliente != null) {
            em.remove(cliente);
            return true;
        } else {
            log.debug("Módulo Clientes - Modificar Cliente - Error: El cliente a eliminar no existe.");
            throw new EntidadNoExisteException("El cliente a eliminar no existe.");
        }
    }

    public boolean existe(ClienteDto dto) throws DatoIncorrectoException, EntidadYaExisteException {
        try {
            if (dto.getCedula() == null || dto.getEmail() == null) {
                log.debug("Módulo Clientes - Existe Cliente - Error: Datos provistos incorrectos.");
                throw new DatoIncorrectoException("Datos incorrectos.");
            }
            List<ClienteDto> lista
                    = convertirADto(em.createQuery(
                            "select c "
                            + "from Cliente c "
                            + "where c.cedula = :cedula or c.email = :email")
                            .setParameter("cedula", dto.getCedula())
                            .setParameter("email", dto.getEmail()).getResultList());
            if (lista.size() > 0) {
                log.debug("Módulo Clientes - Existe Cliente - Error: "
                        + "Ya existe un usuario con esa cedula o email registrado en el sistema.");
                throw new EntidadYaExisteException(
                        "Ya existe un usuario con esa cedula o email registrado en el sistema.");
            } else {
                return false;
            }
        } catch (javax.ejb.EJBException e) {
            log.debug("Módulo Clientes - Existe Cliente - Error: Datos provistos incorrectos.");
            throw new DatoIncorrectoException("Datos incorrectos.");
        } catch (java.lang.NullPointerException e) {
            log.debug("Módulo Clientes - Existe Cliente - Error: Datos provistos incorrectos.");
            throw new DatoIncorrectoException("Datos incorrectos.");
        }
    }

    private ClienteDto convertirADto(Cliente entity) {
        ClienteDto dto = new ClienteDto();
        dto.setId(entity.getId());
        dto.setCedula(entity.getCedula());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    private List<ClienteDto> convertirADto(List<Cliente> clientes) {
        List<ClienteDto> dtos = new ArrayList<>();
        for (Cliente cliente : clientes) {
            dtos.add(convertirADto(cliente));
        }
        return dtos;
    }

    private Cliente convertirAEntity(ClienteDto dto) {
        Cliente entity = new Cliente();
        entity.setCedula(dto.getCedula());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEmail(dto.getEmail());
        return entity;
    }
}
