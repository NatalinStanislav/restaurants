package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.service.RestaurantService;
import com.natalinstanislav.restaurants.to.RestaurantTo;
import com.natalinstanislav.restaurants.util.RestaurantUtil;
import com.natalinstanislav.restaurants.util.exception.ErrorType;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import com.natalinstanislav.restaurants.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.natalinstanislav.restaurants.DishTestData.ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.DishTestData.ALL_DISHES_FROM_KEBAB_HOUSE_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.DishTestData.ALL_DISHES_FROM_SUSHI_ROLL_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.DishTestData.LOCALDATE_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.DishTestData.ISO_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.RestaurantTestData.*;
import static com.natalinstanislav.restaurants.TestUtil.readFromJson;
import static com.natalinstanislav.restaurants.TestUtil.userHttpBasic;
import static com.natalinstanislav.restaurants.UserTestData.admin;
import static com.natalinstanislav.restaurants.UserTestData.user0;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminRestaurantRestControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + PIZZA_HUT_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(PizzaHut));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + PIZZA_HUT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + PIZZA_HUT_ID)
                .with(userHttpBasic(user0)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getWithMenu() throws Exception {
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);

        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + PIZZA_HUT_ID + "/withMenu?date=" + LOCALDATE_30_OF_JANUARY)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_MENU_MATCHER.contentJson(PizzaHut));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/restaurants/" + PIZZA_HUT_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantService.get(PIZZA_HUT_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/restaurants/" + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/restaurants/")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(ALL_RESTAURANTS));
    }

    @Test
    void getAllWithMenu() throws Exception {
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);
        SushiRoll.setDishes(ALL_DISHES_FROM_SUSHI_ROLL_30_OF_JANUARY);
        KebabHouse.setDishes(ALL_DISHES_FROM_KEBAB_HOUSE_30_OF_JANUARY);

        perform(MockMvcRequestBuilders.get("/admin/restaurants/withMenu?date=" + ISO_30_OF_JANUARY)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_MENU_MATCHER.contentJson(PizzaHut, SushiRoll, KebabHouse));
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post("/admin/restaurants")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());
        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.getId();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put("/admin/restaurants/" + PIZZA_HUT_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(PIZZA_HUT_ID), updated);
    }

    @Test
    void getWithMenuAndRating() throws Exception {
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);
        RestaurantTo pizzaHutTo = RestaurantUtil.createTo(PizzaHut, (long) 3);

        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + PIZZA_HUT_ID + "/withMenuAndRating?date=" + LOCALDATE_30_OF_JANUARY)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(pizzaHutTo));
    }

    @Test
    void getAllWithMenuAndRating() throws Exception {
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);
        SushiRoll.setDishes(ALL_DISHES_FROM_SUSHI_ROLL_30_OF_JANUARY);
        KebabHouse.setDishes(ALL_DISHES_FROM_KEBAB_HOUSE_30_OF_JANUARY);
        RestaurantTo pizzaHutTo = RestaurantUtil.createTo(PizzaHut, (long) 3);
        RestaurantTo sushiRollTo = RestaurantUtil.createTo(SushiRoll, (long) 1);
        RestaurantTo kebabHouseTo = RestaurantUtil.createTo(KebabHouse, (long) 2);

        perform(MockMvcRequestBuilders.get("/admin/restaurants/withMenuAndRating?date=" + LOCALDATE_30_OF_JANUARY)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(pizzaHutTo, sushiRollTo, kebabHouseTo));
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, "");
        perform(MockMvcRequestBuilders.post("/admin/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, "");
        perform(MockMvcRequestBuilders.put("/admin/restaurants/" + PIZZA_HUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }
}