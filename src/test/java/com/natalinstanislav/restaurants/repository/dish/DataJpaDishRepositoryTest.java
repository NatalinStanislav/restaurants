package com.natalinstanislav.restaurants.repository.dish;

import com.natalinstanislav.restaurants.model.Dish;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.DishTestData.*;
import static com.natalinstanislav.restaurants.DishTestData.getNew;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DataJpaDishRepositoryTest {

    @Autowired
    protected DishRepository repository;

    @Test
    void save() {
        Dish newDish = getNew();
        Dish created = repository.save(newDish);
        Integer newId = created.getId();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.get(newId), newDish);
    }

    @Test
    void delete() {
        Assertions.assertThat(repository.get(FISH_SALAD_ID)).isNotNull();
        repository.delete(FISH_SALAD_ID);
        Assertions.assertThat(repository.get(FISH_SALAD_ID)).isNull();
    }

    @Test
    void deletedNotFound() throws Exception {
        assertFalse(repository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Dish dish = repository.get(MEXICAN_PIZZA_ID);
        DISH_MATCHER.assertMatch(dish, MexicanPizza);
    }

    @Test
    void getNotFound() throws Exception {
        Assertions.assertThat(repository.get(NOT_FOUND)).isNull();
    }

    @Test
    void getAll() {
        List<Dish> all = repository.getAll();
        DISH_MATCHER.assertMatch(all, ALL_DISHES);
    }

    @Test
    void getAllFromRestaurantByDate() {
        List<Dish> allFrom30_OfJanuaryFromPizzaHut = repository.getAllFromRestaurantByDate(100006, LOCALDATE_30_OF_JANUARY);
        DISH_MATCHER.assertMatch(allFrom30_OfJanuaryFromPizzaHut, ALL_DISHES_FROM_30_OF_JANUARY_FROM_PIZZA_HUT);
    }

    @Test
    void getAllByDate() {
        List<Dish> allFrom30_OfJanuary = repository.getAllByDate(LOCALDATE_30_OF_JANUARY);
        DISH_MATCHER.assertMatch(allFrom30_OfJanuary, ALL_DISHES_FROM_30_OF_JANUARY);
    }
}