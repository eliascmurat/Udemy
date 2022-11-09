package br.com.eliascmurat.quarkussocial.rest;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.eliascmurat.quarkussocial.domain.model.Follower;
import br.com.eliascmurat.quarkussocial.domain.model.User;
import br.com.eliascmurat.quarkussocial.domain.repository.FollowerRepository;
import br.com.eliascmurat.quarkussocial.domain.repository.UserRepository;
import br.com.eliascmurat.quarkussocial.rest.dto.FollowerRequest;
import br.com.eliascmurat.quarkussocial.rest.dto.FollowerResponse;
import br.com.eliascmurat.quarkussocial.rest.dto.FollowersPerUserResponse;
import br.com.eliascmurat.quarkussocial.rest.dto.ResponseError;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {
    private FollowerRepository followerRepository;
    private UserRepository userRepository;
    private Validator validator;

    @Inject
    public FollowerResource(
        FollowerRepository followerRepository, 
        UserRepository userRepository,
        Validator validator
    ) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {
        List<Follower> result = followerRepository.findByUser(userId);

        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        FollowersPerUserResponse fpur = new FollowersPerUserResponse();
        fpur.setFollowersCount(result.size());
        fpur.setContent(FollowerResponse.fromEntity(result));

        return Response.ok(fpur).build();
    }

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest followerRequest) {
        Set<ConstraintViolation<FollowerRequest>> violations = validator.validate(followerRequest);

        if (!violations.isEmpty()) {
            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }
        
        if (userId.equals(followerRequest.getFollowerId())) {
            return Response.status(Status.CONFLICT).entity("You can't follow yourself").build();
        }

        User user = userRepository.findById(userId);
        User follower = userRepository.findById(followerRequest.getFollowerId());

        if (user == null || follower == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        boolean follows = followerRepository.follows(follower, user);

        if (!follows) {
            Follower entity = new Follower();
            entity.setUser(user);
            entity.setFollowerUser(follower);
    
            followerRepository.persist(entity);        
        }

        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
        if (userId.equals(followerId)) {
            return Response.status(Status.CONFLICT).entity("You can't unfollow yourself").build();
        }

        User user = userRepository.findById(userId);
        User follower = userRepository.findById(followerId);

        if (user == null || follower == null) {
            return Response.status(Status.NOT_FOUND).build();
        } 

        boolean follows = followerRepository.follows(follower, user);

        if (follows) followerRepository.deleteByFollowerAndUser(followerId, userId);

        return Response.status(Status.NO_CONTENT).build();
    }
}