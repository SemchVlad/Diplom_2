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

public class RestLoginTest {
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
    @DisplayName("логин под существующим пользователем")
    @Description("POST запрос api/auth/login успешно выполняется и возвращает статус 200")
    public void userLoginSuccess() {
        Response response = StellarBurgersRestApiUtils.loginUser(user);
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

        Response response = StellarBurgersRestApiUtils.loginUser(user_fail);
        //проверка
        response.then().statusCode(401).and()
                .body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

}
