package qa.guru.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.guru.models.pojo.CreateUserBodyModel;
import qa.guru.models.pojo.CreateUserResponseModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SeveralTestsForReqresAPI extends BaseTest {
    int userId;

    @Test
    @DisplayName("Создания нового юзера методом /api/users с POJO моделью и проверка параметров ответа созданного юзера")
    void creatingUserWithPOJO() {
        CreateUserBodyModel createUserBodyModel = new CreateUserBodyModel();
        createUserBodyModel.setName(dataForTheTest.userName);
        createUserBodyModel.setJob(dataForTheTest.userJob);

        CreateUserResponseModel createUserResponseModel = given().
                contentType(JSON)
                .body(createUserBodyModel)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().as(CreateUserResponseModel.class);

        assertEquals(createUserResponseModel.getName(), dataForTheTest.userName);
        assertEquals(createUserResponseModel.getJob(), dataForTheTest.userJob);
        assertEquals(createUserResponseModel.getId(), notNullValue());
        assertEquals(createUserResponseModel.getCreatedAt(), greaterThan(dataForTheTest.timeBeforeStartTest));
    }

    @Test
    @DisplayName("Создаёт юзера, затем обновляет информацию по созданному юзеру методом /api/users/{id юзера}")
    void updatingUserInfoWithLombok() {
        userId = Integer.parseInt(given().
                contentType(JSON)
                .body(dataForTheTest.jsonBodyToCreate.toString())
                .when()
                .post("/api/users")
                .then()
                .extract()
                .path("id"));

        given().
                contentType(JSON)
                .body(dataForTheTest.jsonBodyToUpdate.toString())
                .when()
                .put("/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("name", equalTo(dataForTheTest.userNameToUpdate)
                        , "job", equalTo(dataForTheTest.userJobToUpdate)
                        , "updatedAt", greaterThan(dataForTheTest.timeBeforeStartTest));
    }

    @Test
    @DisplayName("Создаём юзера, затем удаленяем информацию по юзеру /api/users/{id юзера}")
    void deletingUser() {
        userId = Integer.parseInt(given().
                contentType(JSON)
                .body(dataForTheTest.jsonBodyToCreate.toString())
                .when()
                .post("/api/users")
                .then()
                .extract()
                .path("id"));

        given().
                contentType(JSON)
                .when()
                .delete("/api/users/" + userId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Получает информацию по одном пользователю /api/users/{id юзера}")
    void gettingUser() {
        given().
                contentType(JSON)
                .when()
                .get("/api/users/" + dataForTheTest.randomUserId)
                .then()
                .statusCode(200)
                .body("data.id", equalTo(dataForTheTest.randomUserId)
                        , "data.email", containsString("@reqres.in")
                        , "data.first_name", notNullValue()
                        , "data.avatar", containsString(dataForTheTest.randomUserId + "-image.jpg"));
    }

    @Test
    @DisplayName("Неудачная попытка логирования в методе api/login")
    void loggingUser() {
        given().
                contentType(JSON)
                .body(dataForTheTest.jsonBodyUnsuccessfullLogin.toString())
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}
