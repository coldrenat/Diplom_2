package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;

public class OrderClient extends BaseClient {
    private static final String ORDERS_PATH = "/api/orders";
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(List<String> ingredients, String accessToken) {
        return getSpec().header("Authorization", accessToken).body("{\"ingredients\": " + ingredients.toString().replace("[", "[\"").replace("]", "\"]").replace(", ", "\",\"") + "}").when().post(ORDERS_PATH);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(List<String> ingredients) {
        return getSpec().body("{\"ingredients\": " + ingredients.toString().replace("[", "[\"").replace("]", "\"]").replace(", ", "\",\"") + "}").when().post(ORDERS_PATH);
    }

    @Step("Создание заказа с телом")
    public Response createOrderWithBody(String body, String accessToken) {
        return getSpec().header("Authorization", accessToken).body(body).when().post(ORDERS_PATH);
    }

    @Step("Получение ингредиентов")
    public Response getIngredients() {
        return getSpec().when().get(INGREDIENTS_PATH);
    }
}
