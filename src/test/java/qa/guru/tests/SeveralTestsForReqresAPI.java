package qa.guru.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.guru.models.lombok.CreateUserBodyLombokModel;
import qa.guru.models.lombok.CreateUserResponseLombokModel;
import qa.guru.models.lombok.UpdateUserBodyLombokModel;
import qa.guru.models.lombok.UpdateUserResponseLombokModel;
import qa.guru.models.pojo.CreateUserBodyPOJOModel;
import qa.guru.models.pojo.CreateUserResponsePOJOModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SeveralTestsForReqresAPI extends BaseTest {
    int userId;

    @Test
    @DisplayName("Создания нового юзера методом /api/users с POJO моделью и проверка параметров ответа созданного юзера")
    void creatingUserWithPOJO() {
        CreateUserBodyPOJOModel createUserBodyPOJOModel = new CreateUserBodyPOJOModel();
        createUserBodyPOJOModel.setName(dataForTheTest.userName);
        createUserBodyPOJOModel.setJob(dataForTheTest.userJob);

        CreateUserResponsePOJOModel createUserResponsePOJOModel = given().
                contentType(JSON)
                .body(createUserBodyPOJOModel)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().as(CreateUserResponsePOJOModel.class);

        assertEquals(createUserResponsePOJOModel.getName(), dataForTheTest.userName);
        assertEquals(createUserResponsePOJOModel.getJob(), dataForTheTest.userJob);
        assertNotNull(createUserResponsePOJOModel.getId());
        assertThat(createUserResponsePOJOModel.getCreatedAt()).isGreaterThan(dataForTheTest.timeBeforeStartTest);
    }

    @Test
    @DisplayName("Создаёт юзера, затем обновляет информацию по созданному юзеру методом /api/users/{id юзера} с Lombok моделью")
    void updatingUserInfoWithLombok() {
        CreateUserBodyLombokModel createUserBodyLombokModel = new CreateUserBodyLombokModel();
        createUserBodyLombokModel.setName(dataForTheTest.userName);
        createUserBodyLombokModel.setJob(dataForTheTest.userJob);

        UpdateUserBodyLombokModel updateUserBodyLombokModel = new UpdateUserBodyLombokModel();
        updateUserBodyLombokModel.setUpdateName(dataForTheTest.userNameToUpdate);
        updateUserBodyLombokModel.setUpdateJob(dataForTheTest.userJobToUpdate);

        CreateUserResponseLombokModel createUserResponseLombokModel = given().
                contentType(JSON)
                .body(createUserBodyLombokModel)
                .when()
                .post("/api/users")
                .then()
                .extract().as(CreateUserResponseLombokModel.class);

        UpdateUserResponseLombokModel updateUserResponseLombokModel = given().
                contentType(JSON)
                .body(updateUserBodyLombokModel)
                .when()
                .put("/api/users/" + createUserResponseLombokModel.getId())
                .then()
                .statusCode(200)
                .extract().as(UpdateUserResponseLombokModel.class);

        assertThat(updateUserResponseLombokModel.getUpdateName()).isEqualTo(dataForTheTest.userNameToUpdate);
        assertThat(updateUserResponseLombokModel.getUpdateJob()).isEqualTo(dataForTheTest.userJobToUpdate);
        assertThat(updateUserResponseLombokModel.getUpdatedAt()).isGreaterThan(dataForTheTest.timeBeforeStartTest);
    }


    /*@Test
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
    }*/
}
