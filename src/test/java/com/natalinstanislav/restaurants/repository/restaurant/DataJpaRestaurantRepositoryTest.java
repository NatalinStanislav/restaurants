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

import static com.natalinstanislav.restaurants.DishTestData.ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.DishTestData.ALL_DISHES_FROM_KEBAB_HOUSE_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.DishTestData.ALL_DISHES_FROM_SUSHI_ROLL_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.DishTestData.LOCALDATE_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.DishTestData.RANDOM_DISHES;

import static com.natalinstanislav.restaurants.RestaurantTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.get(newId), newRestaurant);
    }

    @Test
    void delete() {
        Assertions.assertThat(repository.get(PIZZA_HUT_ID)).isNotNull();
        repository.delete(PIZZA_HUT_ID);
        Assertions.assertThat(repository.get(PIZZA_HUT_ID)).isNull();
    }

    @Test
    void deletedNotFound() throws Exception {
        assertFalse(repository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Restaurant restaurant = repository.get(PIZZA_HUT_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, PizzaHut);
    }

    @Test
    void getWithMenu() {
        Restaurant restaurant = repository.getWithMenu(PIZZA_HUT_ID, LOCALDATE_30_OF_JANUARY);
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);
        RESTAURANT_WITH_MENU_MATCHER.assertMatch(restaurant, PizzaHut);
    }

    @Test
    void getNotFound() throws Exception {
        Assertions.assertThat(repository.get(NOT_FOUND)).isNull();
    }

    @Test
    void getAll() {
        List<Restaurant> all = repository.getAll();
        RESTAURANT_MATCHER.assertMatch(all, KebabHouse, PizzaHut, SushiRoll);
    }

    @Test
    void getAllWithMenu() {
        List<Restaurant> allWithMenu = repository.getAllWithMenu(LOCALDATE_30_OF_JANUARY);
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);
        KebabHouse.setDishes(ALL_DISHES_FROM_KEBAB_HOUSE_30_OF_JANUARY);
        SushiRoll.setDishes(ALL_DISHES_FROM_SUSHI_ROLL_30_OF_JANUARY);
        RESTAURANT_WITH_MENU_MATCHER.assertMatch(allWithMenu, PizzaHut, SushiRoll, KebabHouse);
    }

}