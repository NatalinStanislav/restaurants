package com.natalinstanislav.restaurants.repository;

import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static com.natalinstanislav.restaurants.UserTestData.*;


@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@RunWith(SpringRunner.class)
public class DataJpaUserRepositoryTest {

    @Autowired
    protected UserRepository repository;

    @Test
    public void get() {
        User user = repository.get(100005);
        assertMatch(user, admin);
    }

    @Test
    public void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                repository.save(new User(null, "Duplicate", "user0@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void getByEmail() {
        User user = repository.getByEmail("admin@gmail.com");
        assertMatch(user, admin);    }

    @Test
    public void getAll() {
        List<User> all = repository.getAll();
        assertMatch(all, admin, user0, user1, user2, user3, user4);
    }

    @Test
    public void save() {
        User newUser = getNew();
        User created = repository.save(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(repository.get(newId), newUser);
    }

    @Test
    public void delete() throws Exception {
        repository.delete(ADMIN_ID);
        assertMatch(null, repository.get(ADMIN_ID));
    }
}