package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.natalinstanislav.restaurants.DishTestData.*;
import static com.natalinstanislav.restaurants.DishTestData.getNew;
import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DishServiceTest {

    @Autowired
    protected DishService service;

    @Test
    void create() {
        Dish newDish = getNew();
        Dish created = service.create(newDish, PIZZA_HUT_ID);
        Integer newId = created.getId();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId), newDish);
    }

    @Test
    void createDuplicate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(MexicanPizzaClone, PIZZA_HUT_ID));
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        service.update(updated, PIZZA_HUT_ID);
        DISH_MATCHER.assertMatch(service.get(MEXICAN_PIZZA_ID), getUpdated());
    }

    @Test
    void delete() {
        service.delete(FISH_SALAD_ID);
        assertThrows(NotFoundException.class, () -> service.get(FISH_SALAD_ID));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Dish dish = service.get(MEXICAN_PIZZA_ID);
        DISH_MATCHER.assertMatch(dish, MexicanPizza);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getAll() {
        List<Dish> all = service.getAll();
        DISH_MATCHER.assertMatch(all, ALL_DISHES);
    }

    @Test
    void getAllFromRestaurantByDate() {
        List<Dish> allFrom30_OfJanuaryFromPizzaHut = service.getAllFromRestaurantByDate(100006, LOCALDATE_30_OF_JANUARY);
        DISH_MATCHER.assertMatch(allFrom30_OfJanuaryFromPizzaHut, ALL_DISHES_FROM_30_OF_JANUARY_FROM_PIZZA_HUT);
    }

    @Test
    void getAllByDate() {
        List<Dish> allFrom30_OfJanuary = service.getAllByDate(LOCALDATE_30_OF_JANUARY);
        DISH_MATCHER.assertMatch(allFrom30_OfJanuary, ALL_DISHES_FROM_30_OF_JANUARY);
    }
}