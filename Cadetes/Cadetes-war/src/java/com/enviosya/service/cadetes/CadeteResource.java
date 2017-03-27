package com.enviosya.service.cadetes;

import com.enviosya.cadete.dto.CadeteDto;
import com.enviosya.cadete.excepcion.DatoIncorrectoException;
import com.enviosya.cadete.excepcion.EntidadNoExisteException;
import com.enviosya.cadete.excepcion.EntidadYaExisteException;
import com.enviosya.cadete.sessionbean.CadeteBean;
import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cadete")
public class CadeteResource {

    @EJB
    private CadeteBean cadeteBean;

    @Context
    private UriInfo context;

    private final Gson gson = new Gson();

    public CadeteResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() throws EntidadNoExisteException {
        Response response;
        try {
            List<CadeteDto> lista = cadeteBean.listar();
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
    public Response obtenerCadete(@QueryParam("id") String id) {
        Response response;
        try {
            CadeteDto envio = cadeteBean.buscar(id);
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

    @GET
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarCadetesCercanos(@QueryParam("ubicacion") String ubicacion) {
        Response response;
        try {
            List<CadeteDto> envio = cadeteBean.buscarCadetesCercanos(ubicacion);
            response = Response
                    .status(Response.Status.OK)
                    .entity(gson.toJson(envio))
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response agregar(String body) throws EntidadYaExisteException, DatoIncorrectoException {
        Response response;
        try {
            CadeteDto cadete = gson.fromJson(body, CadeteDto.class);
            CadeteDto creado = cadeteBean.agregar(cadete);
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
            CadeteDto cadete = gson.fromJson(body, CadeteDto.class);
            CadeteDto modificado = cadeteBean.modificar(cadete);
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
