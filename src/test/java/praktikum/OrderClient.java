package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderClient extends BaseClient {
    private static final String ORDERS_PATH = "/api/orders";
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return getSpec()
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Получение ингредиентов")
    public Response getIngredients() {
        return getSpec()
                .when()
                .get(INGREDIENTS_PATH);
    }
}