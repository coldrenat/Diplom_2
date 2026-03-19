package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private String accessToken;
    private List<String> ingredientIds;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.getRandomUser();
        Response response = userClient.createUser(user);
        accessToken = response.jsonPath().getString("accessToken");
        Response ingredientsResponse = orderClient.getIngredients();
        ingredientIds = ingredientsResponse.jsonPath().getList("data._id");
    }

    @After
    public void tearDown() {
        if (accessToken != null) { userClient.deleteUser(accessToken); }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Заказ с токеном и валидными ингредиентами должен быть успешным")
    public void createOrderWithAuthAndIngredientsReturnsSuccess() {
        Order order = new Order(ingredientIds.subList(0, 2));
        Response response = orderClient.createOrderWithAuth(order, accessToken);
        response.then().statusCode(200).body("success", equalTo(true)).body("order", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Заказ без токена авторизации")
    public void createOrderWithoutAuthReturnsSuccess() {
        Order order = new Order(ingredientIds.subList(0, 2));
        Response response = orderClient.createOrderWithoutAuth(order);
        response.then().statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Заказ без ингредиентов должен вернуть ошибку")
    public void createOrderWithoutIngredientsReturnsError() {
        Order order = new Order(Collections.emptyList());
        Response response = orderClient.createOrderWithAuth(order, accessToken);
        response.then().statusCode(400).body("success", equalTo(false)).body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Заказ с невалидным хешем должен вернуть ошибку 500")
    public void createOrderWithInvalidHashReturnsError() {
        Order order = new Order(List.of("invalidhash123"));
        Response response = orderClient.createOrderWithAuth(order, accessToken);
        response.then().statusCode(500);
    }
}