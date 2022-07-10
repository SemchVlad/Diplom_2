package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends RestApiClient{
    @Step("Получение информации об ингредиентах")
    public Response getIngredients() {
        return given().spec(getSpec())
                .get("/api/ingredients");
    }
}