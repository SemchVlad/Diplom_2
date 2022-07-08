import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class RestGetUserOrdersTest {
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

        //создание первого заказа
        ArrayList<String> listIngredients = new ArrayList<>();
        IngredientsResponse ingredientsResponse = StellarBurgersRestApiUtils.getIngredients().as(IngredientsResponse.class);
        int indexIng1 = new Random().nextInt(ingredientsResponse.getData().size());
        int indexIng2 = new Random().nextInt(ingredientsResponse.getData().size());
        listIngredients.add(ingredientsResponse.getData().get(indexIng1).get_id());
        listIngredients.add(ingredientsResponse.getData().get(indexIng2).get_id());
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(listIngredients);
        StellarBurgersRestApiUtils.createOrder(ingredients, userResponse.getAccessToken());
        //создание второго заказа
        ArrayList<String> listIngredients1 = new ArrayList<>();
        IngredientsResponse ingredientsResponse1 = StellarBurgersRestApiUtils.getIngredients().as(IngredientsResponse.class);
        int indexIng3 = new Random().nextInt(ingredientsResponse.getData().size());
        int indexIng4 = new Random().nextInt(ingredientsResponse.getData().size());
        listIngredients.add(ingredientsResponse.getData().get(indexIng3).get_id());
        listIngredients.add(ingredientsResponse.getData().get(indexIng4).get_id());
        Ingredients ingredients2 = new Ingredients();
        ingredients2.setIngredients(listIngredients);
        StellarBurgersRestApiUtils.createOrder(ingredients2, userResponse.getAccessToken()).getBody();
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
    @DisplayName("Получение заказов конкретного пользователя с авторизацией")
    @Description("GET запрос /api/orders успешно выполняется и возвращается статус 200")
    public void getOrderSuccessWithAuth() {
        Response response = StellarBurgersRestApiUtils.getOrders(userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов не авторизированным пользователем")
    @Description("GET запрос /api/orders выполняется и возвращает статус 401")
    public void getOrderSuccessWithOutAuth() {
        Response response = StellarBurgersRestApiUtils.getOrders();
        //проверка
        response.then().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }
}
