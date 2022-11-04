package br.com.eliascmurat.quarkussocial.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.eliascmurat.quarkussocial.rest.dto.CreateUserRequest;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @POST
    public Response createUser(CreateUserRequest createUserRequest) {
        return Response.ok(createUserRequest).build();
    }

    @GET
    public Response listAllUsers() {
        return Response.ok().build();
    }
}
