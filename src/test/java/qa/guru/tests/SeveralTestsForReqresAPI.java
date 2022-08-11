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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static qa.guru.specs.CreateUserWithSpec.requestSpecificationCreate;
import static qa.guru.specs.CreateUserWithSpec.responseSpecificationCreate;
import static qa.guru.specs.DeleteUserWithSpec.requestSpecificationDelete;
import static qa.guru.specs.DeleteUserWithSpec.responseSpecificationDelete;


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
                .statusCode(201)
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


    @Test
    @DisplayName("Создаём юзера, затем удаленяем информацию по юзеру /api/users/{id юзера}")
    void deletingUserWithSpec() {
        CreateUserBodyLombokModel createUserBodyLombokModel = new CreateUserBodyLombokModel();
        createUserBodyLombokModel.setName(dataForTheTest.userName);
        createUserBodyLombokModel.setJob(dataForTheTest.userJob);

        CreateUserResponseLombokModel createUserResponseLombokModel = given().
                spec(requestSpecificationCreate)
                .body(createUserBodyLombokModel)
                .when()
                .post()
                .then()
                .spec(responseSpecificationCreate)
                .extract().as(CreateUserResponseLombokModel.class);

        given().
                contentType(JSON)
                .spec(requestSpecificationDelete)
                .when()
                .delete("/api/users/" + userId)
                .then()
                .spec(responseSpecificationDelete);
    }
}