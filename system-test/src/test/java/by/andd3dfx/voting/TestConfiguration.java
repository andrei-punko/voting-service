package by.andd3dfx.voting;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;

public class TestConfiguration {

    public static final String BASE_URL = "http://localhost:8090";

    static {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())
                .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JACKSON_2));

        RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;
    }

    public static void resetVotingResults() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/votings")
                .then()
                .statusCode(204);
    }
}
