package br.com.eliascmurat.quarkussocial.rest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import br.com.eliascmurat.quarkussocial.domain.model.User;
import br.com.eliascmurat.quarkussocial.domain.repository.UserRepository;
import br.com.eliascmurat.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FollowerResourceTest {
    @Inject
    UserRepository userRepository;

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
    }

    @Order(5)
    @Test
    @DisplayName("Should return 404 on list user followers and userId doens't exist")
    void userNotFoundWhenListingFollowersTest() {
        given()
            .contentType(ContentType.JSON)
            .pathParam("userId", 0)
        .when()
            .get()
        .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Order(6)
    @Test
    @DisplayName("Should list a user's followers")
    void listFollowersTest() {
        Response response = 
            given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
            .when()
                .get()
            .then()
                .extract().response();

        assertEquals(Status.OK.getStatusCode(), response.statusCode());
    }

    @Order(1)
    @Test
    @DisplayName("Should return 409 when followerId is equal to userId")
    void sameUserAsFollowerTest() {
        FollowerRequest fr = new FollowerRequest();
        fr.setFollowerId(userId);

        given()
            .contentType(ContentType.JSON)
            .body(fr)
            .pathParam("userId", userId)
        .when()
            .put()
        .then()
            .statusCode(Status.CONFLICT.getStatusCode());
    }

    @Order(2)
    @Test
    @DisplayName("Should return 404 on follow a user when userId doens't exist")
    void userNotFoundWhenTryingToFollowTest() {
        FollowerRequest fr = new FollowerRequest();
        fr.setFollowerId(userId);

        given()
            .contentType(ContentType.JSON)
            .body(fr)
            .pathParam("userId", 0)
        .when()
            .put()
        .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Order(3)
    @Test
    @DisplayName("Should return 404 on unfollow a user when userId doens't exist")
    void userNotFoundWhenUnfollowingAUSerTest() {
        given()
            .pathParam("userId", 0)
            .queryParam("followerId", followerId)
        .when()
            .delete()
        .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Order(4)
    @Test
    @DisplayName("Should follow a user")
    void followUserTest() {
        FollowerRequest fr = new FollowerRequest();
        fr.setFollowerId(followerId);

        given()
            .contentType(ContentType.JSON)
            .body(fr)
            .pathParam("userId", userId)
        .when()
            .put()
        .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Order(7)
    @Test
    @DisplayName("Should unfollow a user")
    void unfollowingAUSerTest() {
        given()
            .pathParam("userId", userId)
            .queryParam("followerId", followerId)
        .when()
            .delete()
        .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }
}
