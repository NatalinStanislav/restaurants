package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.ALL_VOTES_FROM_USER3;
import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {

    @Autowired
    protected UserService userService;

    @Test
    void get() {
        User user = userService.get(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> userService.get(NOT_FOUND));
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                userService.create(new User(null, "Duplicate", "user0@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    void getByEmail() {
        User user = userService.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getAll() {
        List<User> all = userService.getAll();
        USER_MATCHER.assertMatch(all, admin, user0, user1, user2, user3, user4);
    }

    @Test
    void save() {
        User newUser = getNew();
        User created = userService.create(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void delete() throws Exception {
        userService.delete(ADMIN_ID);
        assertThrows(NotFoundException.class, () -> userService.get(ADMIN_ID));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> userService.delete(NOT_FOUND));
    }

    @Test
    void getWithVotes() throws Exception {
        User user3WithVotes = userService.getWithVotes(USER3_ID);
        user3.setVotes(ALL_VOTES_FROM_USER3);
        USER_WITH_VOTES_MATCHER.assertMatch(user3WithVotes, user3);
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        userService.update(updated);
        USER_MATCHER.assertMatch(userService.get(USER3_ID), getUpdated());
    }

    @Test
    void enable() {
        userService.enable(USER3_ID, false);
        assertFalse(userService.get(USER3_ID).isEnabled());
        userService.enable(USER3_ID, true);
        assertTrue(userService.get(USER3_ID).isEnabled());
    }
}