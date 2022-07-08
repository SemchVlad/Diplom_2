import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class RestCreateOrderTest {

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
    @DisplayName("Создание заказа с авторизацией")
    @Description("POST запрос api/orders успешно выполняется и возвращает статус 200")
    public void createOrderSuccessWithAuth() {
        // подготовка данных
        ArrayList<String> listIngredients = new ArrayList<>();
        IngredientsResponse ingredientsResponse = StellarBurgersRestApiUtils.getIngredients().as(IngredientsResponse.class);
        int indexIng1 = new Random().nextInt(ingredientsResponse.getData().size());
        int indexIng2 = new Random().nextInt(ingredientsResponse.getData().size());
        listIngredients.add(ingredientsResponse.getData().get(indexIng1).get_id());
        listIngredients.add(ingredientsResponse.getData().get(indexIng2).get_id());
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(listIngredients);
        Response response = StellarBurgersRestApiUtils.createOrder(ingredients,userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("POST запрос api/orders успешно выполняется и возвращает статус 200")
    public void createOrderSuccessWithOutAuth() {
        // подготовка данных
        ArrayList<String> listIngredients = new ArrayList<>();
        IngredientsResponse ingredientsResponse = StellarBurgersRestApiUtils.getIngredients().as(IngredientsResponse.class);
        listIngredients.add(ingredientsResponse.getData().get(new Random().nextInt(ingredientsResponse.getData().size())).get_id());
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(listIngredients);
        Response response = StellarBurgersRestApiUtils.createOrder(ingredients);
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("POST запрос api/orders успешно выполняется и возвращает статус 200")
    public void createOrderSuccessWithThreeIngredients() {
        // подготовка данных
        ArrayList<String> listIngredients = new ArrayList<>();
        IngredientsResponse ingredientsResponse = StellarBurgersRestApiUtils.getIngredients().as(IngredientsResponse.class);
        int indexIng1 = new Random().nextInt(ingredientsResponse.getData().size());
        int indexIng2 = new Random().nextInt(ingredientsResponse.getData().size());
        int indexIng3 = new Random().nextInt(ingredientsResponse.getData().size());
        listIngredients.add(ingredientsResponse.getData().get(new Random().nextInt(indexIng1)).get_id());
        listIngredients.add(ingredientsResponse.getData().get(new Random().nextInt(indexIng2)).get_id());
        listIngredients.add(ingredientsResponse.getData().get(new Random().nextInt(indexIng3)).get_id());
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(listIngredients);
        Response response = StellarBurgersRestApiUtils.createOrder(ingredients);
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("POST запрос api/orders выполняется и возвращается статус 400")
    public void createOrderSuccessWithOutIngredients() {
        // подготовка данных
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(null);
        Response response = StellarBurgersRestApiUtils.createOrder(ingredients);
        //проверка
        response.then().statusCode(400).and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов.")
    @Description("В ответ на POST запрос api/orders с невалидным хэшем возвращает статус 500")
    public void createOrderSuccessWithNotValidIngredient() {
        // подготовка данных
        ArrayList<String> listIngredients = new ArrayList<>();
        listIngredients.add("");
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(listIngredients);
        Response response = StellarBurgersRestApiUtils.createOrder(ingredients);
        //проверка
        response.then().statusCode(500);
    }
}
