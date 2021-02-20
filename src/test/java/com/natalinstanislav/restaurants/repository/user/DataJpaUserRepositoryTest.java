package com.natalinstanislav.restaurants.repository.user;

import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.JpaUtil;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.natalinstanislav.restaurants.UserTestData.*;
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
        User user = repository.get(100005);
        assertMatch(user, admin);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> repository.get(NOT_FOUND));
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                repository.save(new User(null, "Duplicate", "user0@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    void getByEmail() {
        User user = repository.getByEmail("admin@gmail.com");
        assertMatch(user, admin);    }

    @Test
    void getAll() {
        List<User> all = repository.getAll();
        assertMatch(all, admin, user0, user1, user2, user3, user4);
    }

    @Test
    void save() {
        User newUser = getNew();
        User created = repository.save(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(repository.get(newId), newUser);
    }

    @Test
    void delete() throws Exception {
        repository.delete(ADMIN_ID);
        assertThrows(NotFoundException.class, () -> repository.get(ADMIN_ID));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> repository.delete(NOT_FOUND));
    }

    @Test
//    @Transactional
    void getWithVotes() throws Exception {
        User admin0 = repository.get(ADMIN_ID);
//        System.out.println(admin0.getVotes());
        User admin = repository.getWithVotes(ADMIN_ID);
        System.out.println(admin.getVotes());
    }
}