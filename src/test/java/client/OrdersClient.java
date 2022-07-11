package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Ingredients;

import static io.restassured.RestAssured.given;

public class OrdersClient extends RestApiClient {
    final static private String PATH = "/api/orders";

    @Step("Создание заказа без авторизации")
    public Response createOrder(Ingredients ingredients) {
        return given().spec(getSpec())
                .body(ingredients)
                .post(PATH);
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrder(Ingredients ingredients, String accessToken) {
        return given().spec(getSpec())
                .headers("Authorization", accessToken)
                .body(ingredients)
                .post(PATH);
    }

    @Step("Получение информации о заказах")
    public Response getOrders() {
        return given().spec(getSpec())
                .get(PATH);
    }

    @Step("Получение информации о заказах")
    public Response getOrders(String accessToken) {
        return given().spec(getSpec())
                .headers("Authorization", accessToken)
                .get(PATH);
    }
}
