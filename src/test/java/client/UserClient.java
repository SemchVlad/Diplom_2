package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class UserClient extends RestApiClient{
    final static private String PATH = "/api/auth/";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(PATH + "register");
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given().spec(getSpec()).header("Authorization", accessToken).delete(PATH + "user")
                .then().statusCode(202).and().body("success", Matchers.equalTo(true));
    }

    @Step("Логин пользователя")
    public Response loginUser(User user) {
        return given().spec(getSpec())
                .body(user)
                .post(PATH + "login");
    }

    @Step("Изменение данных о пользователя без авторизации")
    public  Response changeUserData(User user) {
        return given().spec(getSpec())
                .body(user)
                .patch(PATH + "user");
    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response changeUserData(User user, String accessToken) {
        return given().spec(getSpec())
                .header("Authorization", accessToken)
                .body(user)
                .patch(PATH + "user");
    }

    @Step("Получение данных пользователя")
    public Response getUserData(String accessToken) {
        return given().spec(getSpec())
                .header("Authorization", accessToken)
                .get(PATH + "user");
    }
}