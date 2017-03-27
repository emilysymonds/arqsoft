package com.enviosya.service.comentario;

import com.enviosya.comentarios.dto.ComentarioDto;
import com.enviosya.comentarios.excepcion.DatoIncorrectoException;
import com.enviosya.comentarios.excepcion.EntidadNoExisteException;
import com.enviosya.comentarios.excepcion.EntidadYaExisteException;
import com.enviosya.comentarios.excepcion.ErrorUrlException;
import com.enviosya.comentarios.sessionbean.ComentarioBean;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("comentario")
public class ComentarioResource {

    @EJB
    private ComentarioBean comentarioBean;

    @Context
    private UriInfo context;

    private final Gson gson = new Gson();

    public ComentarioResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() throws EntidadNoExisteException {
        Response response;
        try {
            List<ComentarioDto> lista = comentarioBean.listar();
            response = Response
                    .status(Response.Status.CREATED)
                    .entity(gson.toJson(lista))
                    .build();
        } catch (EntidadNoExisteException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        }
        return response;
    }

    @GET
    @Path("obtener")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerComentariosCadete(@QueryParam("id") String id) throws EntidadNoExisteException {
        Response response;
        try {
            List<ComentarioDto> lista = comentarioBean.listarComentariosCadete(id);
            response = Response
                    .status(Response.Status.CREATED)
                    .entity(gson.toJson(lista))
                    .build();
        } catch (EntidadNoExisteException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        }
        return response;
    }

    @POST
    @Path("crear")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearComentario(String body) throws ErrorUrlException, IOException {
        Response response;
        ComentarioDto comentario;
        try {
            comentario = gson.fromJson(body, ComentarioDto.class);
            ComentarioDto creado = comentarioBean.agregar(comentario);
            response = Response
                    .status(Response.Status.CREATED)
                    .entity(gson.toJson(creado))
                    .build();
        } catch (EntidadNoExisteException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        } catch (EntidadYaExisteException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        } catch (DatoIncorrectoException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage()).build();
        } catch (ErrorUrlException ex) {
            response = Response
                    .status(Response.Status.CONFLICT)
                    .entity(ex.getMessage()).build();
        } catch (Exception ex) {
            response = Response
                    .status(Response.Status.CONFLICT)
                    .entity(ex.getMessage()).build();
        }
        return response;
    }
}
