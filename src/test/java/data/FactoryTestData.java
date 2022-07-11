package data;

import model.Ingredients;
import model.IngredientsResponse;
import model.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class FactoryTestData {
    /**
     * Создание тестового пользователя с генерируемым email
     * @return
     */
    public static User createNewTestUser(){
        return new User("ninja_l" + UUID.randomUUID() +"@yandex.ru", TestConsts.USER_PASS_VALID, TestConsts.USER_NAME);
    }

    /**
     * Создание тестового набора ингредиентов в количестве двух штук ингредиентов
     * Если передан ingredientsResponse=null, тогда будет возвращен пустой список (ArrayList).
     * @return
     */
    public static Ingredients createNewTestIngredients(IngredientsResponse ingredientsResponse){
        ArrayList<String> listIngredients = new ArrayList<>();
        if ( ingredientsResponse != null) {
            int indexIng1 = new Random().nextInt(ingredientsResponse.getData().size());
            int indexIng2 = new Random().nextInt(ingredientsResponse.getData().size());
            listIngredients.add(ingredientsResponse.getData().get(indexIng1).get_id());
            listIngredients.add(ingredientsResponse.getData().get(indexIng2).get_id());
        }
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(listIngredients);
        return ingredients;
    }

    public static Ingredients createNewTestIngredientsNotValidHash(){
        ArrayList<String> listIngredients = new ArrayList<>();
        listIngredients.add("");
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(listIngredients);
        return ingredients;
    }
}
