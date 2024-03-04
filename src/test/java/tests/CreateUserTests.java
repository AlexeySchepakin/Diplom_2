package tests;

import io.qameta.allure.Step;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.UUID;

public class CreateUserTests extends BaseTest {

    @Test
    public void testCreateUniqueUser() {
        String uniqueEmail = "testuser_" + UUID.randomUUID() + "@example.com";
        registerUserUnique(uniqueEmail, "password", "Test User");
    }

    @Test
    public void testCreateExistingUser() {
        String existingEmail = "testuser@example.com";
        registerUserExisting(existingEmail, "password", "Test User");
    }

    @Test
    public void testCreateUserWithMissingField() {
        registerUserWithMissingField();
    }

    @Step("Register a user with a unique email")
    private void registerUserUnique(String email, String password, String name) {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}")
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email));
    }

    @Step("Try to register a user with an existing email")
    private void registerUserExisting(String email, String password, String name) {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}")
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Step("Try to register a user with a missing field")
    private void registerUserWithMissingField() {
        given()
                .contentType("application/json")
                .body("{\"password\": \"password\", \"name\": \"Test User\"}")
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
