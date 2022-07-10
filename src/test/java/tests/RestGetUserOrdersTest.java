package tests;

import client.IngredientsClient;
import client.OrdersClient;
import data.FactoryTestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Ingredients;
import model.IngredientsResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;

public class RestGetUserOrdersTest extends BaseTest {
    IngredientsClient ingredientsClient = new IngredientsClient();
    OrdersClient ordersClient = new OrdersClient();

    @Before
    @Override
    public void setUp() throws IOException {
        super.setUp();

        //создание первого заказа
        IngredientsResponse ingredientsResponse = ingredientsClient.getIngredients().as(IngredientsResponse.class);
        Ingredients ingredients = FactoryTestData.createNewTestIngredients(ingredientsResponse);
        ordersClient.createOrder(ingredients, userResponse.getAccessToken());
        //создание второго заказа
        IngredientsResponse ingredientsResponse1 = ingredientsClient.getIngredients().as(IngredientsResponse.class);
        Ingredients ingredients2 = FactoryTestData.createNewTestIngredients(ingredientsResponse1);
        ordersClient.createOrder(ingredients2, userResponse.getAccessToken()).getBody();
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя с авторизацией")
    @Description("GET запрос /api/orders успешно выполняется и возвращается статус 200")
    public void getOrderSuccessWithAuth() {
        Response response = ordersClient.getOrders(userResponse.getAccessToken());
        //проверка
        response.then().statusCode(200)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов не авторизированным пользователем")
    @Description("GET запрос /api/orders выполняется и возвращает статус 401")
    public void getOrderSuccessWithOutAuth() {
        Response response = ordersClient.getOrders();
        //проверка
        response.then().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }
}
