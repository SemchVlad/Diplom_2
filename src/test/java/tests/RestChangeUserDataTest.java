package tests;

import client.UserClient;
import data.TestConsts;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class RestChangeUserDataTest extends BaseTest {
    UserClient userClient = new UserClient();

    @Test
    @DisplayName("Изменение данных пользователя все поля")
    @Description("PATCH запрос api/auth/user успешно выполняется и возвращает статус 200")
    public void userChangeDataSuccess() {
        user.setName(TestConsts.USER_NAME);
        String email = "fisher" + UUID.randomUUID() +"@yandex.ru";
        user.setEmail(email);
        user.setPassword(TestConsts.USER_PASS_VALID);
        Response response = userClient.changeUserData(user, userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true))
                .and().body("user.name", equalTo(TestConsts.USER_NAME))
                .and().body("user.email", equalTo(email));
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    @Description("PATCH запрос api/auth/user успешно выполняется и возвращает статус 200")
    public void userChangePasswordSuccess() {
        user.setPassword(TestConsts.USER_PASS_VALID);
        Response response = userClient.changeUserData(user, userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true))
                .and().body("user.name", equalTo(user.getName()))
                .and().body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Изменение email пользователя")
    @Description("PATCH запрос api/auth/user успешно выполняется и возвращает статус 200")
    public void userChangeEmailSuccess() {
        String email = "fisher" + UUID.randomUUID() +"@yandex.ru";
        user.setEmail(email);
        Response response = userClient.changeUserData(user, userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true))
                .and().body("user.name", equalTo(TestConsts.USER_NAME))
                .and().body("user.email", equalTo(email));
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    @Description("PATCH запрос api/auth/user успешно выполняется и возвращает статус 200")
    public void userChangeNameSuccess() {
        user.setName(TestConsts.USER_NAME2);
        Response response = userClient.changeUserData(user, userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true))
                .and().body("user.name", equalTo(TestConsts.USER_NAME2))
                .and().body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("PATCH запрос api/auth/user выполняется с ошибкой и возвращает статус 401")
    public void userChangeDataSuccessWithA() {
        user.setName(TestConsts.USER_NAME2);
        String email = "fisher" + UUID.randomUUID() +"@yandex.ru";
        user.setEmail(email);
        user.setPassword(TestConsts.USER_PASS_VALID2);
        Response response = userClient.changeUserData(user);
        //проверка
        response.then().statusCode(401).and()
                .body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }


}
