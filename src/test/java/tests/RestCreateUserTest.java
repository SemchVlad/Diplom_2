package tests;

import client.UserClient;
import data.FactoryTestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.User;
import model.UserResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class RestCreateUserTest extends BaseTest {
    private String accessToken;
    UserClient userClient = new UserClient();

    @After
    @Override
    public void tearDown() {
        if(accessToken != null && !accessToken.isEmpty()) {
            userClient.deleteUser(accessToken);
        }
        accessToken = null;
    }

    @Test
    @DisplayName("создать уникального пользователя")
    @Description("POST запрос api/auth/login успешно выполняется со заполненными полями и возвращается статус 200")
    public void createUserSuccess() throws IOException {
        //создание пользователя
        User user = FactoryTestData.createNewTestUser();
        Response response = userClient.createUser(user);
        response.then().statusCode(200).and().body("success", equalTo(true));

        //запишем токен для удаления пользователя
        accessToken = response.as(UserResponse.class).getAccessToken();
    }

    @Test
    @DisplayName("создать пользователя, который уже зарегистрирован")
    @Description("POST запрос /api/v1/courier/login выполняется с ошибкой \"tests.User already exists\" и возвращается статус 403")
    public void createUserExists() throws IOException {
        //создание пользователя
        User user = FactoryTestData.createNewTestUser();

        userClient.createUser(user);
        //повторное создание того же пользователя
        Response response = userClient.createUser(user);
        //запишем токен для удаления пользователя
        accessToken = response.as(UserResponse.class).getAccessToken();
        //проверка
        response.then().statusCode(403).and()
                .body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей")
    @Description("POST запрос /api/v1/courier/login вернет ошибку \"Email, password and name are required fields\"" +
            " и статус 403")
    public void createUserWithoutEmail() throws IOException {
        //создание пользователя без mail
        User user = new User("ninja_l" + UUID.randomUUID() +"@yandex.ru", "1234");
        Response response = userClient.createUser(user);
        accessToken = response.as(UserResponse.class).getAccessToken();
        response.then().statusCode(403).and().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"));
    }
}
