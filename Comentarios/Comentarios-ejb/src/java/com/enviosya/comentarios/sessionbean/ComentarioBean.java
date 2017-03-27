package com.enviosya.comentarios.sessionbean;

import com.enviosya.comentarios.dominio.Comentario;
import com.enviosya.comentarios.dto.ComentarioDto;
import com.enviosya.comentarios.excepcion.DatoIncorrectoException;
import com.enviosya.comentarios.excepcion.EntidadNoExisteException;
import com.enviosya.comentarios.excepcion.EntidadYaExisteException;
import com.enviosya.comentarios.excepcion.ErrorUrlException;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ComentarioBean {

    static Logger log = Logger.getLogger(ComentarioBean.class.getName());

    private static final String URI_ENVIO = "http://localhost:8080/Envios-war/envio";

    Gson gson = new Gson();

    @PersistenceContext
    private EntityManager em;

    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory cf;

    @Resource(lookup = "jms/Queue")
    private Queue queue;

    private int cantidadPalabras = 20;

    private ArrayList<String> listaNegra = new ArrayList<String>() {
        {
            add("mierda");
            add("sorete");
            add("feo");
        }
    };

    @PostConstruct
    private void init() {
    }

    public String getUrl(String urlRecibida, String id) throws MalformedURLException, IOException, ErrorUrlException {
        String urlRecibidaParametros = urlRecibida;
        if (!id.equals("")) {
            urlRecibidaParametros += "/obtener?id=" + id;
        }
        try {
            URL url = new URL(urlRecibidaParametros);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int code = conn.getResponseCode();

            if (conn.getResponseCode() != 200) {
                System.out.println(conn.getResponseCode());
                System.out.println(conn.getResponseMessage());
                throw new ErrorUrlException("Error: HTTP error code: "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output = "";
            String reader;
            while ((reader = br.readLine()) != null) {
                output += reader;
            }
            conn.disconnect();
            return output;

        } catch (MalformedURLException e) {
            log.debug("Módulo Comentarios - URL - Error: URL inválida: " + urlRecibidaParametros);
            throw new ErrorUrlException("URL inválida: " + urlRecibidaParametros);
        }
    }

    public ComentarioDto buscar(String id) throws EntidadNoExisteException, DatoIncorrectoException {
        Long idLong = Long.parseLong(id);
        if (id != null) {
            Comentario cadete = em.find(Comentario.class, idLong);
            if (cadete == null) {
                log.debug("Módulo Comentarios - Buscar Comentario - Error: No existe comentario con ID " + id);
                throw new EntidadNoExisteException("No existe comentario con ID " + id);
            } else {
                return convertirADto(cadete);
            }
        }
        log.debug("Módulo Comentarios - Buscar Comentario - Error: ID de cadete incorrecto.");
        throw new DatoIncorrectoException("Error en el ID del comentario.");
    }

    public ComentarioDto agregar(ComentarioDto comentarioDto)
            throws EntidadYaExisteException, DatoIncorrectoException, javax.ejb.EJBException,
            IOException, EntidadNoExisteException, ErrorUrlException {
        Comentario comentario = null;
        ComentarioDto dto = comentarioDto;
        dto = clienteValido(dto);
        if (!existe(dto)) {
            dto = actualizarEstado(dto, "P");
            if (validarCantidadPalabras(dto)) {
                dto = validarListaNegra(dto);
                dto = calificarSemantica(dto);
                dto = notificarComentario(dto);
                comentario = convertirAEntity(dto);
                em.persist(comentario);
                return convertirADto(comentario);
            }
        }
        return convertirADto(comentario);
    }

    public ComentarioDto clienteValido(ComentarioDto dto)
            throws DatoIncorrectoException, IOException, MalformedURLException, ErrorUrlException {
        String idEnvio = dto.getEnvio().toString();
        String envio = getUrl(URI_ENVIO, idEnvio);

        if (envio != null) {
            Map<String, Object> map = new HashMap<>();
            map = (Map<String, Object>) gson.fromJson(envio, map.getClass());
            Object emisor = map.get("clienteEmisor");
            String emisorString = emisor.toString();
            double emisorDouble = Double.parseDouble(emisorString);
            double idClienteComentario = dto.getAutor();

            if (emisorDouble == idClienteComentario) {
                Object cadete = map.get("cadete");
                if (cadete != null) {
                    String cadeteString = cadete.toString();
                    dto.setCadete(cadeteString);
                } else {
                    log.debug("Módulo Comentarios - Validar Cliente - "
                            + "Error: No puede calificar el servicio. El envío no cuenta con un cadete asignado aún.");
                    throw new DatoIncorrectoException(
                            "No puede calificar el servicio. El envío no cuenta con un cadete asignado aún.");
                }
                return dto;
            } else {
                log.debug("Módulo Comentarios - Validar Cliente - Error: El cliente realizando "
                        + "el comentario no coincide con el del servicio que desea calificar.");
                throw new DatoIncorrectoException(
                        "El cliente realizando el comentario no coincide con el del servicio que desea calificar.");
            }
        } else {
            throw new DatoIncorrectoException("Datos incorrectos.");
        }
    }

    public boolean existe(ComentarioDto dto) throws DatoIncorrectoException, EntidadYaExisteException {
        try {
            if (dto.getEnvio() == null) {
                log.debug("Módulo Comentarios - Existe Comentario - Error: Debe ingresar el servicio a calificar.");
                throw new DatoIncorrectoException("Debe ingresar el servicio a calificar.");
            }
            List<ComentarioDto> lista
                    = convertirADto(em.createQuery(
                            "select c "
                            + "from Comentario c "
                            + "where c.envio = :envio")
                            .setParameter("envio", dto.getEnvio())
                            .getResultList());
            if (lista.size() > 0) {
                log.debug("Módulo Comentarios - Existe Comentario - Error: Ya existe un comentario para ese servicio.");
                throw new EntidadYaExisteException("Ya existe un comentario para ese servicio.");
            } else {
                return false;
            }
        } catch (javax.ejb.EJBException e) {
            log.debug("Módulo Comentarios - Existe Comentario - Error: Datos incorrectos.");
            throw new DatoIncorrectoException("Error en los datos provistos.");
        } catch (java.lang.NullPointerException e) {
            log.debug("Módulo Comentarios - Existe Comentario - Error: Datos incorrectos.");
            throw new DatoIncorrectoException("Error en los datos provistos.");
        }
    }

    public boolean validarCantidadPalabras(ComentarioDto dto) throws DatoIncorrectoException {
        String comentario = dto.getComentario();
        if (comentario != null) {
            int contar = contarPalabras(comentario);
            if (contar <= cantidadPalabras) {
                return true;
            } else {
                log.debug("Módulo Comentarios - Validar Cantidad Palabras - "
                        + "Error: Comentario demasiado largo. La máxima cantidad de palabras permitida es "
                        + cantidadPalabras);
                throw new DatoIncorrectoException(
                        "Comentario demasiado largo. La máxima cantidad de palabras permitida es " + cantidadPalabras);
            }
        } else {
            log.debug("Módulo Comentarios - Validar Cantidad Palabras - Error: Debe ingresar un comentario.");
            throw new DatoIncorrectoException("Debe ingresar un comentario.");
        }
    }

    private int contarPalabras(String texto) {
        int ivar;
        int contador = 0;
        int res = 0;
        char ch[] = new char[texto.length()];
        for (ivar = 0; ivar < texto.length(); ivar++) {
            ch[ivar] = texto.charAt(ivar);
            if (((ivar > 0) && (ch[ivar] != ' ') && (ch[ivar - 1] == ' ')) || ((ch[0] != ' ') && (ivar == 0))) {
                contador++;
            }
        }
        return contador;
    }

    private ComentarioDto validarListaNegra(ComentarioDto dto) {
        String comentario = dto.getComentario();
        for (int i = 0; i < listaNegra.size(); i++) {
            String palabra = listaNegra.get(i);
            if (comentario.toLowerCase().contains(palabra.toLowerCase())) {
                return actualizarEstado(dto, "R");
            }
        }
        return dto;
    }

    private ComentarioDto actualizarEstado(ComentarioDto dto, String estado) {
        dto.setEstado(estado);
        return dto;
    }

    private ComentarioDto calificarSemantica(ComentarioDto comentarioDto) {
        ComentarioDto dto = comentarioDto;
        String sentimiento = determinarValor(dto.getComentario());
        if (sentimiento != null) {
            if ("P".equals(dto.getEstado())) {
                dto = actualizarEstado(dto, "A");
            }
            dto.setSentimiento(sentimiento);
        }
        return dto;
    }

    private String determinarValor(String comentario) {
        //simula lo que retornaria el sistema externo (Natlang) a través de una llamada REST
        if (contarPalabras(comentario) <= 5) {
            return "Negativo";
        } else if (contarPalabras(comentario) > 5 && contarPalabras(comentario) < 10) {
            return "Neutro";
        } else if (contarPalabras(comentario) > 10) {
            return "Positivo";
        } else {
            return null;
        }
    }

    public List<ComentarioDto> listar() throws EntidadNoExisteException {
        List<ComentarioDto> lista = convertirADto(em.createQuery(
                "SELECT c FROM Comentario c").getResultList());
        if (lista.isEmpty()) {
            log.debug("Módulo Comentarios - Listar Comentarios - Error: No se han registrado comentarios.");
            throw new EntidadNoExisteException("No se han registrado comentarios.");
        } else {
            return lista;
        }
    }

    public List<ComentarioDto> listarComentariosCadete(String id) throws EntidadNoExisteException {
        String idCadete = id;
        idCadete += ".0";
        String estado = "A";
        List<ComentarioDto> lista = convertirADto(em.createQuery(
                "SELECT c FROM Comentario c "
                + "where c.cadete= :cadete"
                + " and c.estado= :estado "
                + "order by c.id desc")
                .setParameter("cadete", idCadete)
                .setParameter("estado", estado)
                .getResultList());
        if (lista.isEmpty()) {
            log.debug("Módulo Comentarios - Listar Comentarios - "
                    + "Error: No se han registrado comentarios para el cadete solicitado.");
            throw new EntidadNoExisteException("No se han registrado comentarios para ese cadete.");
        } else {
            return lista;
        }
    }

    private ComentarioDto notificarComentario(ComentarioDto dto) {
        //POST a Notificaciones
        return dto;
    }

    private ComentarioDto convertirADto(Comentario entity) {
        ComentarioDto dto = new ComentarioDto();
        dto.setId(entity.getId());
        dto.setAutor(entity.getAutor());
        dto.setEnvio(entity.getEnvio());
        dto.setRating(entity.getRating());
        dto.setComentario(entity.getComentario());
        dto.setEstado(entity.getEstado());
        dto.setSentimiento(entity.getSentimiento());
        dto.setCadete(entity.getCadete());
        return dto;
    }

    private List<ComentarioDto> convertirADto(List<Comentario> envios) {
        List<ComentarioDto> dtos = new ArrayList<>();
        for (Comentario envio : envios) {
            dtos.add(convertirADto(envio));
        }
        return dtos;
    }

    private Comentario convertirAEntity(ComentarioDto dto) {
        Comentario entity = new Comentario();
        entity.setAutor(dto.getAutor());
        entity.setEnvio(dto.getEnvio());
        entity.setRating(dto.getRating());
        entity.setComentario(dto.getComentario());
        entity.setEstado(dto.getEstado());
        entity.setSentimiento(dto.getSentimiento());
        entity.setCadete(dto.getCadete());
        return entity;
    }
}
