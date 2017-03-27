package com.enviosya.service.clients;

import com.enviosya.clientes.dto.ClienteDto;
import com.enviosya.clientes.excepcion.DatoIncorrectoException;
import com.enviosya.clientes.excepcion.EntidadNoExisteException;
import com.enviosya.clientes.excepcion.EntidadYaExisteException;
import com.enviosya.clientes.sessionbean.ClienteBean;
import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cliente")
public class ClienteResource {
    
    @EJB
    private ClienteBean clienteBean;

    @Context
    private UriInfo context;

    private final Gson gson = new Gson();
    
    public ClienteResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() throws EntidadNoExisteException {
        Response response;
        try {
            List<ClienteDto> lista = clienteBean.listar();
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
    public Response obtenerCliente(@QueryParam("id") String id) {
        Response response;
        try {
            ClienteDto envio = clienteBean.buscar(id);
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response agregar(String body) throws EntidadYaExisteException, DatoIncorrectoException {
        Response response;
        try {
            ClienteDto cliente = gson.fromJson(body, ClienteDto.class);
            ClienteDto creado = clienteBean.agregar(cliente);
            response = Response
                    .status(Response.Status.CREATED)
                    .entity(gson.toJson(creado))
                    .build();
        } catch (EntidadYaExisteException ex) {
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
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificar(String body) throws EntidadNoExisteException {
        Response response;
        try {
            ClienteDto cliente = gson.fromJson(body, ClienteDto.class);
            ClienteDto modificado = clienteBean.modificar(cliente);
            if (modificado == null) {
                response = Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Error de ingreso.")
                        .build();
            } else {
                response = Response
                        .status(Response.Status.CREATED)
                        .entity(gson.toJson(modificado))
                        .build();
            }
        } catch (EntidadNoExisteException ex) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        }
        return response;
    }
}
