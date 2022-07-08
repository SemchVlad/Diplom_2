import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StellarBurgersRestApiUtils {

    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return given()
                .header("Content-type", ContentType.JSON)
                .body(user)
                .post("/api/auth/register");
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String accessToken) {
        given().headers("Authorization", accessToken, "Content-Type",
                        ContentType.JSON).delete("/api/auth/user")
                .then().statusCode(202).and().body("success", equalTo(true));
    }

    @Step("Логин пользователя")
    public static Response loginUser(User user) {
        return given().headers("Content-Type", ContentType.JSON)
                .body(user)
                .post("/api/auth/login");
    }

    @Step("Изменение данных о пользователя без авторизации")
    public static Response changeUserData(User user) {
        return given().headers("Content-Type", ContentType.JSON)
                .body(user)
                .patch("/api/auth/user");
    }

    @Step("Изменение данных пользователя с авторизацией")
    public static Response changeUserData(User user, String accessToken) {
        return given().headers("Authorization", accessToken,
                        "Content-Type", ContentType.JSON)
                .body(user)
                .patch("/api/auth/user");
    }

    @Step("Получение данных пользователя")
    public static Response getUserData(String accessToken) {
        return given().headers("Authorization", accessToken,
                        "Content-Type", ContentType.JSON)
                .get("/api/auth/user");
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrder(Ingredients ingredients) {
        return given().headers("Content-Type", ContentType.JSON)
                .body(ingredients)
                .post("/api/orders");
    }

    @Step("Создание заказа с авторизацией")
    public static Response createOrder(Ingredients ingredients, String accessToken) {
        return given().headers("Authorization", accessToken,
                        "Content-Type", ContentType.JSON)
                .body(ingredients)
                .post("/api/orders");
    }

    @Step("Получение информации об ингредиентах")
    public static Response getIngredients() {
        return given().headers("Content-Type", ContentType.JSON, "Accept",
                        ContentType.JSON).auth().none()
                .post("/api/ingredients");
    }
}
