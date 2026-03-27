package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        Response response = userClient.createUser(user);
        accessToken = response.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) { userClient.deleteUser(accessToken); }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Вход с корректными email и паролем")
    public void loginWithValidCredentialsReturnsSuccess() {
        Response response = userClient.loginUser(user);
        response.then().statusCode(200).body("success", equalTo(true)).body("accessToken", notNullValue()).body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Вход с некорректным email должен вернуть ошибку")
    public void loginWithWrongEmailReturnsError() {
        User wrongUser = new User("wrong@yandex.ru", user.getPassword(), user.getName());
        Response response = userClient.loginUser(wrongUser);
        response.then().statusCode(401).body("success", equalTo(false)).body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Вход с некорректным паролем должен вернуть ошибку")
    public void loginWithWrongPasswordReturnsError() {
        User wrongUser = new User(user.getEmail(), "wrongpassword", user.getName());
        Response response = userClient.loginUser(wrongUser);
        response.then().statusCode(401).body("success", equalTo(false)).body("message", equalTo("email or password are incorrect"));
    }
}
