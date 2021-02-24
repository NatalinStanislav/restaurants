package com.natalinstanislav.restaurants.repository.dish;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.DishTestData.*;
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
        assertMatch(created, newDish);
        assertMatch(repository.get(newId), newDish);
    }

    @Test
    void delete() {
        Assertions.assertThat(repository.get(FISH_SALAD_ID)).isNotNull();
        repository.delete(FISH_SALAD_ID);
        assertMatch(null, repository.get(FISH_SALAD_ID));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertFalse(repository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Dish dish = repository.get(MEXICAN_PIZZA_ID);
        assertMatch(dish, MexicanPizza);
    }

    @Test
    void getNotFound() throws Exception {
        assertMatch(null, repository.get(NOT_FOUND));
    }

    @Test
    void getAll() {
        List<Dish> all = repository.getAll();
        assertMatch(all, ALL_DISHES);
    }

    @Test
    void getAllFromRestaurantByDate() {
        List<Dish> allFrom30_OfJanuaryFromPizzaHut = repository.getAllFromRestaurantByDate(100006, LocalDate.of(2020, 1, 30));
        assertMatch(allFrom30_OfJanuaryFromPizzaHut, ALL_DISHES_FROM_30_OF_JANUARY_FROM_PIZZA_HUT);
    }

    @Test
    void getAllByDate() {
        List<Dish> allFrom30_OfJanuary = repository.getAllByDate(LocalDate.of(2020, 1, 30));
        assertMatch(allFrom30_OfJanuary, ALL_DISHES_FROM_30_OF_JANUARY);
    }
}