package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserClient extends BaseClient {
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_PATH = "/api/auth/user";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return getSpec().body(user).when().post(REGISTER_PATH);
    }

    @Step("Логин пользователя")
    public Response loginUser(User user) {
        return getSpec().body(user).when().post(LOGIN_PATH);
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        if (accessToken != null) {
            getSpec().header("Authorization", accessToken).when().delete(USER_PATH);
        }
    }
}
