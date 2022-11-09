package br.com.eliascmurat.quarkussocial.rest;

import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
import br.com.eliascmurat.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private UserRepository userRepository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> query = userRepository.findAll();
        return Response.ok(query.list()).build();
    }

    @GET
    @Path("/{userId}")
    public Response findById(@PathParam("userId") Long userId) {
        User query = userRepository.findById(userId);
        return query != null
                ? Response.ok(query).build()
                : Response.status(Status.NOT_FOUND).build();
    }


    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);

        if (!violations.isEmpty()) {
            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());
        userRepository.persist(user);

        return Response.status(Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{userId}")
    @Transactional
    public Response updateUser(@PathParam("userId") Long userId, CreateUserRequest userRequest) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);

        if (!violations.isEmpty()) {
            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{userId}")
    @Transactional
    public Response deleteUser(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);
        boolean deleted = userRepository.deleteById(userId);

        return (user == null || !deleted) 
            ? Response.status(Status.NOT_FOUND).build()
            : Response.noContent().build();
    }
}
