package com.natalinstanislav.restaurants.repository.user;

import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.JpaUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.ALL_VOTES_FROM_USER3;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DataJpaUserRepositoryTest {

    @Autowired
    protected UserRepository repository;

    @Autowired
    protected JpaUtil jpaUtil;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
        jpaUtil.clear2ndLevelHibernateCache();
    }

    @Test
    void get() {
        User user = repository.get(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getNotFound() throws Exception {
        Assertions.assertThat(repository.get(NOT_FOUND)).isNull();
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                repository.save(new User(null, "Duplicate", "user0@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    void getByEmail() {
        User user = repository.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);    }

    @Test
    void getAll() {
        List<User> all = repository.getAll();
        USER_MATCHER.assertMatch(all, admin, user0, user1, user2, user3, user4);
    }

    @Test
    void save() {
        User newUser = getNew();
        User created = repository.save(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(repository.get(newId), newUser);
    }

    @Test
    void delete() throws Exception {
        Assertions.assertThat(repository.get(ADMIN_ID)).isNotNull();
        repository.delete(ADMIN_ID);
        Assertions.assertThat(repository.get(ADMIN_ID)).isNull();
    }

    @Test
    void deletedNotFound() throws Exception {
        assertFalse(repository.delete(NOT_FOUND));    }

    @Test
    void getWithVotes() throws Exception {
        User user3WithVotes = repository.getWithVotes(USER3_ID);
        user3.setVotes(ALL_VOTES_FROM_USER3);
        USER_WITH_VOTES_MATCHER.assertMatch(user3WithVotes, user3);
    }
}