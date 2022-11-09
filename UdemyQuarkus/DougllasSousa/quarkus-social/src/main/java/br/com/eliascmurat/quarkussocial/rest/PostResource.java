package br.com.eliascmurat.quarkussocial.rest;

import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.eliascmurat.quarkussocial.domain.model.Post;
import br.com.eliascmurat.quarkussocial.domain.model.User;
import br.com.eliascmurat.quarkussocial.domain.repository.FollowerRepository;
import br.com.eliascmurat.quarkussocial.domain.repository.PostRepository;
import br.com.eliascmurat.quarkussocial.domain.repository.UserRepository;
import br.com.eliascmurat.quarkussocial.rest.dto.CreatePostRequest;
import br.com.eliascmurat.quarkussocial.rest.dto.PostResponse;
import br.com.eliascmurat.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;
    private Validator validator;

    @Inject
    public PostResource(
        UserRepository userRepository, 
        PostRepository postRepository, 
        FollowerRepository followerRepository,
        Validator validator
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId) {
        if (followerId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        User user = userRepository.findById(userId);
        User follower = userRepository.findById(followerId);

        if (user == null || follower == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        if (!followerRepository.follows(follower, user)) {
            return Response.status(Status.FORBIDDEN).entity("You can't see these posts").build();
        }

        PanacheQuery<Post> query = postRepository.find(
            "user", 
            Sort.by("dateTime", Direction.Descending), 
            user
        );
        
        return Response.ok(PostResponse.fromEntity(query.list())).build();
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(postRequest);

        if (!violations.isEmpty()) {
            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("/{postId}")
    @Transactional
    public Response updatePost(@PathParam("userId") Long userId, @PathParam("postId") Long postId, CreatePostRequest postRequest) {
        User user = userRepository.findById(userId);
        Post post = postRepository.findById(postId);

        if (user == null || post == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(postRequest);

        if (!violations.isEmpty()) {
            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        post.setText(postRequest.getText());

        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{postId}")
    @Transactional
    public Response deletePost(@PathParam("userId") Long userId, @PathParam("postId") Long postId) {
        User user = userRepository.findById(userId);
        Post post = postRepository.findById(postId);
        boolean deleted = userRepository.deleteById(userId);

        return (user == null || post == null || !deleted) 
            ? Response.status(Status.NOT_FOUND).build()
            : Response.noContent().build();
    }
}
