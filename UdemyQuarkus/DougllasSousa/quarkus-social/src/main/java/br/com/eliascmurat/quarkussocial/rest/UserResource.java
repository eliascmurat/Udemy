package br.com.eliascmurat.quarkussocial.rest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.eliascmurat.quarkussocial.domain.model.User;
import br.com.eliascmurat.quarkussocial.domain.repository.UserRepository;
import br.com.eliascmurat.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private UserRepository userRepository;

    @Inject
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> query = userRepository.findAll();
        
        return Response.ok(query.list()).build();
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());
        userRepository.persist(user);

        return Response.ok(user).build();
    }

    @DELETE
    @Path("{userId}")
    @Transactional
    public Response deleteUser(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        userRepository.delete(user);

        return Response.ok().build();
    }

    @PUT
    @Path("{userId}")
    @Transactional
    public Response updateUser(@PathParam("userId") Long userId, CreateUserRequest userRequest) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        return Response.ok().build();
    }
}
