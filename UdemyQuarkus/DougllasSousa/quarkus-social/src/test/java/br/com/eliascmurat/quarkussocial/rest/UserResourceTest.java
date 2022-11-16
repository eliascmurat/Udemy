package br.com.eliascmurat.quarkussocial.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import br.com.eliascmurat.quarkussocial.rest.dto.CreateUserRequest;
import br.com.eliascmurat.quarkussocial.rest.dto.ResponseError;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {
    @TestHTTPResource("/users")
    URL apiURL;

    // @Test
    // @DisplayName("Should list all users")
    // @Order(3)
    // void listAllUsersTest() {
    //     given()
    //         .contentType(ContentType.JSON)
    //     .when()
    //         .get(apiURL)
    //     .then()
    //         .statusCode(200)
    //         .body("size()", Matchers.is(1));
    // }

    @Test
    @DisplayName("Should create a user successfully")
    @Order(1)
    void createUserTest() {
        CreateUserRequest user = new CreateUserRequest();
        user.setName("Fulano");
        user.setAge(19);

        Response response = 
            given()
                .contentType(ContentType.JSON)
                .body(user)
            .when()
                .post(apiURL)
            .then()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    @Order(2)
    void createUserValidationErrorTest() {
        CreateUserRequest user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);

        Response response = 
            given()
                .contentType(ContentType.JSON)
                .body(user)
            .when()
                .post(apiURL)
            .then()
                .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("error"));
        assertNotNull(errors.get(1).get("error"));
    }
}
