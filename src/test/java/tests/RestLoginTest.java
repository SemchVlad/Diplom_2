package tests;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class RestLoginTest extends BaseTest {
    UserClient userClient = new UserClient();

    @Test
    @DisplayName("логин под существующим пользователем")
    @Description("POST запрос api/auth/login успешно выполняется и возвращает статус 200")
    public void userLoginSuccess() {
        Response response = userClient.loginUser(user);
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("логин с неверным логином и паролем")
    @Description("POST запрос api/auth/login выполняется с ошибкой и возвращается статус 403")
    public void userLoginFailed() {
        //создание пользователя
        String email = "ninja_l" + UUID.randomUUID() +"@yandex.ru";
        User user_fail = new User(email, "1234_e", "Naruto");

        Response response = userClient.loginUser(user_fail);
        //проверка
        response.then().statusCode(401).and()
                .body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

}
