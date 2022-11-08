package br.com.eliascmurat.quarkussocial.rest;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
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

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followerId);

        if (follower == null) {
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
        
        List<Post> posts = query.list();
        
        return Response.ok(PostResponse.fromEntity(posts)).build();

    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{postId}")
    @Transactional
    public Response updatePost(@PathParam("userId") Long userId, @PathParam("postId") Long postId, CreatePostRequest postRequest) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Post post = postRepository.findById(postId);

        if (post == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        post.setText(postRequest.getText());

        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{postId}")
    @Transactional
    public Response deletePost(@PathParam("userId") Long userId, @PathParam("postId") Long postId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Post post = postRepository.findById(postId);

        if (post == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        postRepository.delete(post);

        return Response.status(Status.NO_CONTENT).build();
    }
}
