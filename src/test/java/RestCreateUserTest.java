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

public class RestCreateUserTest {
    private String accessToken;

    @Before
    public void setUp() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("src/test/resources/env.properties"));
        RestAssured.baseURI = props.getProperty("baseURI");
    }

    @After
    public void tearDown() {
        if(accessToken != null) {
            StellarBurgersRestApiUtils.deleteUser(accessToken);
        }
        accessToken = null;
    }

    @Test
    @DisplayName("создать уникального пользователя")
    @Description("POST запрос api/auth/login успешно выполняется со заполненными полями и возвращается статус 200")
    public void createUserSuccess() {
        //создание пользователя
        User user = new User("ninja_l" + UUID.randomUUID() +"@yandex.ru", "1234", "Naruto");
        Response response = StellarBurgersRestApiUtils.createUser(user);
        response.then().statusCode(200).and().body("success", equalTo(true));

        //запишем токен для удаления пользователя
        accessToken = response.as(UserResponse.class).getAccessToken();
    }

    @Test
    @DisplayName("создать пользователя, который уже зарегистрирован")
    @Description("POST запрос /api/v1/courier/login выполняется с ошибкой \"User already exists\" и возвращается статус 403")
    public void createUserExists() {
        //создание пользователя
        String email = "ninja_l" + UUID.randomUUID() +"@yandex.ru";
        User user1 = new User(email, "1234", "Naruto");
        StellarBurgersRestApiUtils.createUser(user1);

        User user2 = new User(email, "1234", "Naruto");
        Response response = StellarBurgersRestApiUtils.createUser(user2);
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
    public void createUserWithoutEmail() {
        //создание пользователя
        User user = new User("ninja_l" + UUID.randomUUID() +"@yandex.ru", "1234");
        Response response = StellarBurgersRestApiUtils.createUser(user);
        response.then().statusCode(403).and().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"));
    }
}
