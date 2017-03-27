package com.enviosya.service.envio;

import com.enviosya.envios.dto.EnvioDto;
import com.enviosya.envios.excepcion.DatoIncorrectoException;
import com.enviosya.envios.excepcion.EntidadNoExisteException;
import com.enviosya.envios.excepcion.ErrorUrlException;
import com.enviosya.envios.excepcion.JmsExcepcion;
import com.enviosya.envios.sessionbean.EnvioBean;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("envio")
public class EnvioResource {

    @EJB
    private EnvioBean envioBean;

    private final Gson gson = new Gson();

    @Context
    private UriInfo context;

    public EnvioResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() throws EntidadNoExisteException {
        Response response;
        try {
            List<EnvioDto> lista = envioBean.listar();
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
    public Response crearEnvio(String body) throws IOException, DatoIncorrectoException {
        Response response;
        EnvioDto envio;
        try {
            envio = gson.fromJson(body, EnvioDto.class);
            String creado = envioBean.crearEnvio(body);
            response = Response
                    .status(Response.Status.CREATED)
                    .entity(gson.toJson(creado))
                    .build();
        } catch (ErrorUrlException ex) {
            response = Response
                    .status(Response.Status.CONFLICT)
                    .entity(ex.getMessage()).build();
        } catch (DatoIncorrectoException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage()).build();
        }
        return response;
    }

    @POST
    @Path("confirmar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmarEnvio(String body) 
            throws IOException, EntidadNoExisteException, DatoIncorrectoException, JmsExcepcion {
        Response response;
        try {
            EnvioDto envio = envioBean.confirmarEnvio(body);
            response = Response
                    .status(Response.Status.OK)
                    .entity(gson.toJson(envio))
                    .build();
        } catch (ErrorUrlException ex) {
            response = Response
                    .status(Response.Status.CONFLICT)
                    .entity(ex.getMessage()).build();
        } catch (EntidadNoExisteException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        } catch (DatoIncorrectoException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        }
        return response;
    }

    @POST
    @Path("recepcion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response recepcionEnvio(@QueryParam("id") String id) 
            throws IOException, EntidadNoExisteException, DatoIncorrectoException, ErrorUrlException, JmsExcepcion {
        Response response;
        try {
            EnvioDto envio = envioBean.recepcionEnvio(id);
            response = Response
                    .status(Response.Status.OK)
                    .entity(gson.toJson(envio))
                    .build();
        } catch (EntidadNoExisteException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        } catch (DatoIncorrectoException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        } catch (ErrorUrlException ex) {
            response = Response
                    .status(Response.Status.CONFLICT)
                    .entity(ex.getMessage()).build();
        }
        return response;
    }

    @GET
    @Path("obtener")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerEnvio(@QueryParam("id") String id) {
        Response response;
        try {
            EnvioDto envio = envioBean.buscar(id);
            response = Response
                    .status(Response.Status.OK)
                    .entity(gson.toJson(envio))
                    .build();
        } catch (EntidadNoExisteException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        } catch (DatoIncorrectoException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        }
        return response;
    }

}
