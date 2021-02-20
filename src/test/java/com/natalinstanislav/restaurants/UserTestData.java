package com.natalinstanislav.restaurants;

import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static com.natalinstanislav.restaurants.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int NOT_FOUND = 123456;
    public static final int USER0_ID = START_SEQ;
    public static final int USER1_ID = START_SEQ + 1;
    public static final int USER2_ID = START_SEQ + 2;
    public static final int USER3_ID = START_SEQ + 3;
    public static final int USER4_ID = START_SEQ + 4;
    public static final int ADMIN_ID = START_SEQ + 5;

    public static final User user0 = new User(USER0_ID, "User0", "user0@yandex.ru", "password0", Role.USER);
    public static final User user1 = new User(USER1_ID, "User1", "user1@yandex.ru", "password1", Role.USER);
    public static final User user2 = new User(USER2_ID, "User2", "user2@yandex.ru", "password2", Role.USER);
    public static final User user3 = new User(USER3_ID, "User3", "user3@yandex.ru", "password3", Role.USER);
    public static final User user4 = new User(USER4_ID, "User4", "user4@yandex.ru", "password4", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);


    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("registered", "roles", "votes").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles", "votes").isEqualTo(expected);
    }

}
