package praktikum;

import java.util.UUID;

public class UserGenerator {
    public static User getRandomUser() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        return new User("test-" + unique + "@yandex.ru", "password123", "User-" + unique);
    }
}
