package qa.guru.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class DeleteUserWithSpec {
    public static RequestSpecification requestSpecificationDelete = with()
            .basePath("/api/users/")
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification responseSpecificationDelete = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .build();
}
