package tests;

import client.IngredientsClient;
import client.OrdersClient;
import data.FactoryTestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Ingredients;
import model.IngredientsResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class RestCreateOrderTest extends BaseTest {
    IngredientsClient ingredientsClient = new IngredientsClient();
    OrdersClient ordersClient = new OrdersClient();

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("POST запрос api/orders успешно выполняется и возвращает статус 200")
    public void createOrderSuccessWithAuth() {
        // подготовка данных
        IngredientsResponse ingredientsResponse = ingredientsClient.getIngredients().as(IngredientsResponse.class);
        Ingredients ingredients = FactoryTestData.createNewTestIngredients(ingredientsResponse);

        Response response = ordersClient.createOrder(ingredients,userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200).and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("POST запрос api/orders успешно выполняется и возвращает статус 200")
    public void createOrderSuccessWithOutAuth() {
        // подготовка данных
        IngredientsResponse ingredientsResponse = ingredientsClient.getIngredients().as(IngredientsResponse.class);
        Ingredients ingredients = FactoryTestData.createNewTestIngredients(ingredientsResponse);

        Response response = ordersClient.createOrder(ingredients);
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

        Response response = ordersClient.createOrder(ingredients);
        //проверка
        response.then().statusCode(400).and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов.")
    @Description("В ответ на POST запрос api/orders с невалидным хэшем возвращает статус 500")
    public void createOrderSuccessWithNotValidIngredient() {
        // подготовка данных
        Ingredients ingredients = FactoryTestData.createNewTestIngredientsNotValidHash();

        Response response = ordersClient.createOrder(ingredients);
        //проверка
        response.then().statusCode(500);
    }
}
