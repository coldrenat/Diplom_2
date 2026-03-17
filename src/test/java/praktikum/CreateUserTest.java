package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) { userClient.deleteUser(accessToken); }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя")
    public void createUniqueUserReturnsSuccess() {
        Response response = userClient.createUser(user);
        response.then().statusCode(200).body("success", equalTo(true)).body("accessToken", notNullValue());
        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Повторная регистрация должна вернуть ошибку")
    public void createDuplicateUserReturnsError() {
        Response first = userClient.createUser(user);
        accessToken = first.jsonPath().getString("accessToken");
        Response response = userClient.createUser(user);
        response.then().statusCode(403).body("success", equalTo(false)).body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Без обязательного поля email должна быть ошибка")
    public void createUserWithoutEmailReturnsError() {
        user.setEmail(null);
        Response response = userClient.createUser(user);
        response.then().statusCode(403).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Без обязательного поля password должна быть ошибка")
    public void createUserWithoutPasswordReturnsError() {
        user.setPassword(null);
        Response response = userClient.createUser(user);
        response.then().statusCode(403).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Без обязательного поля name должна быть ошибка")
    public void createUserWithoutNameReturnsError() {
        user.setName(null);
        Response response = userClient.createUser(user);
        response.then().statusCode(403).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }
}
