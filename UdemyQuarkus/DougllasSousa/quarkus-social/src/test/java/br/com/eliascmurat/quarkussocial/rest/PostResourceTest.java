package br.com.eliascmurat.quarkussocial.rest;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import br.com.eliascmurat.quarkussocial.domain.model.Follower;
import br.com.eliascmurat.quarkussocial.domain.model.User;
import br.com.eliascmurat.quarkussocial.domain.repository.FollowerRepository;
import br.com.eliascmurat.quarkussocial.domain.repository.UserRepository;
import br.com.eliascmurat.quarkussocial.rest.dto.CreatePostRequest;
import br.com.eliascmurat.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response.Status;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostResourceTest {
    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    void setup() {
        User user = new User();
        user.setName("Fulano");        
        user.setAge(19);  
        userRepository.persist(user);
        userId = user.getId();

        User follower = new User();
        user.setName("Ciclano");        
        user.setAge(19);  
        userRepository.persist(follower);
        followerId = follower.getId();

        Follower entity = new Follower();
        entity.setUser(user);
        entity.setFollowerUser(follower);
        followerRepository.persist(entity);
    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    void listPostFollowerHeaderNotSendTest() {
        given()
            .pathParam("userId", userId)
        .when()
            .get()
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("Should return 404 when userId doens't exist")
    void listPostUserNotFoundTest() {
        given()
            .pathParam("userId", 0)
            .header("followerId", userId)
        .when()
            .get()
        .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    @DisplayName("Should return 403 when user isn't a follower")
    void listPostNotAFollowerTest() {
        given()
            .pathParam("userId", userId)
            .header("followerId", userId)
        .when()
            .get()
        .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @DisplayName("Should list post")
    void listPostTest() {
        given()
            .pathParam("userId", userId)
            .header("followerId", followerId)
        .when()
            .get()
        .then()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @DisplayName("Should create a post for a user")
    void createPost() {
        CreatePostRequest post = new CreatePostRequest();
        post.setText("Lorem ipsum");
        
        given()
            .contentType(ContentType.JSON)
            .body(post)
            .pathParam("userId", userId)
        .when()
            .post()
        .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for an inextent user")
    void postForAnInexistentUserTest() {
        CreatePostRequest post = new CreatePostRequest();
        post.setText("Lorem ipsum");
        
        given()
            .contentType(ContentType.JSON)
            .body(post)
            .pathParam("userId", 0)
        .when()
            .post()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    void createPostValidationErrorTest() {
        CreatePostRequest post = new CreatePostRequest();
        post.setText(null);
        
        Response response = 
            given()
                .contentType(ContentType.JSON)
                .body(post)
                .pathParam("userId", userId)
            .when()
                .post()
            .then()
                .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("error"));
    }
}
