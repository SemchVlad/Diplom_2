import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class RestChangeUserDataTest {
    private UserResponse userResponse;
    private User user;

    @Before
    public void setUp() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("src/test/resources/env.properties"));
        RestAssured.baseURI = props.getProperty("baseURI");

        user = new User("ninja_l" + UUID.randomUUID() +"@yandex.ru", "1234", "Naruto");
        Response response = StellarBurgersRestApiUtils.createUser(user);
        userResponse = response.as(UserResponse.class);
    }

    @After
    public void tearDown() {
        if(userResponse.getAccessToken() != null) {
            StellarBurgersRestApiUtils.deleteUser(userResponse.getAccessToken());
        }
        userResponse = null;
        user = null;
    }

    @Test
    @DisplayName("Изменение данных пользователя все поля")
    @Description("PATCH запрос api/auth/user успешно выполняется и возвращает статус 200")
    public void userChangeDataSuccess() {
        user.setName("Perch");
        user.setEmail("fisher" + UUID.randomUUID() +"@yandex.ru");
        user.setPassword("12345");
        Response response = StellarBurgersRestApiUtils.changeUserData(user, userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true))
                .and().body("user.name", equalTo(user.getName()))
                .and().body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    @Description("PATCH запрос api/auth/user успешно выполняется и возвращает статус 200")
    public void userChangePasswordSuccess() {
        user.setPassword("123456");
        Response response = StellarBurgersRestApiUtils.changeUserData(user, userResponse.getAccessToken());
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
        user.setEmail("fisher" + UUID.randomUUID() +"@yandex.ru");
        Response response = StellarBurgersRestApiUtils.changeUserData(user, userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true))
                .and().body("user.name", equalTo(user.getName()))
                .and().body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    @Description("PATCH запрос api/auth/user успешно выполняется и возвращает статус 200")
    public void userChangeNameSuccess() {
        user.setName("Bear");
        Response response = StellarBurgersRestApiUtils.changeUserData(user, userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true))
                .and().body("user.name", equalTo(user.getName()))
                .and().body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("PATCH запрос api/auth/user выполняется с ошибкой и возвращает статус 401")
    public void userChangeDataSuccessWithA() {
        user.setName("Perch");
        user.setEmail("fisher" + UUID.randomUUID() +"@yandex.ru");
        user.setPassword("5555555");
        Response response = StellarBurgersRestApiUtils.changeUserData(user);
        //проверка
        response.then().statusCode(401).and()
                .body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }


}
