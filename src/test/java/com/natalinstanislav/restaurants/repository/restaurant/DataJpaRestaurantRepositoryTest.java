package com.natalinstanislav.restaurants.repository.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.natalinstanislav.restaurants.RestaurantTestData.*;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DataJpaRestaurantRepositoryTest {

    @Autowired
    protected RestaurantRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    void save() {
        Restaurant newRestaurant = getNew();
        Restaurant created = repository.save(newRestaurant);
        Integer newId = created.getId();
        newRestaurant.setId(newId);
        assertMatch(created, newRestaurant);
        assertMatch(repository.get(newId), newRestaurant);
    }

    @Test
    void delete() {
        Assertions.assertThat(repository.get(PIZZA_HUT_ID)).isNotNull();
        repository.delete(PIZZA_HUT_ID);
        assertMatch(null, repository.get(PIZZA_HUT_ID));
    }

        @Test
    void get() {
        Restaurant restaurant = repository.get(PIZZA_HUT_ID);
        assertMatch(restaurant, PizzaHut);
    }

    @Test
    void getAll() {
        List<Restaurant> all = repository.getAll();
        assertMatch(all, KebabHouse, PizzaHut, SushiRoll);
    }
}