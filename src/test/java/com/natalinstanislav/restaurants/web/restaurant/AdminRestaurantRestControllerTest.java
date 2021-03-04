package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.repository.restaurant.RestaurantRepository;
import com.natalinstanislav.restaurants.to.RestaurantTo;
import com.natalinstanislav.restaurants.util.RestaurantUtil;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminRestaurantRestControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + PIZZA_HUT_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(PizzaHut));
    }

    @Test
    void getWithMenu() throws Exception {
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);

        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + PIZZA_HUT_ID + "/withMenu?date=" + LOCALDATE_30_OF_JANUARY))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_MENU_MATCHER.contentJson(PizzaHut));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/restaurants/" + PIZZA_HUT_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertNull(restaurantRepository.get(PIZZA_HUT_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/restaurants/"))
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

        perform(MockMvcRequestBuilders.get("/admin/restaurants/withMenu?date=" + ISO_30_OF_JANUARY))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_MENU_MATCHER.contentJson(PizzaHut, SushiRoll, KebabHouse));
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post("/admin/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());
        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.getId();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(newId), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put("/admin/restaurants/" + PIZZA_HUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(PIZZA_HUT_ID), updated);
    }

    @Test
    void getWithMenuAndRating() throws Exception {
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);
        RestaurantTo pizzaHutTo = RestaurantUtil.createTo(PizzaHut, 3);

        perform(MockMvcRequestBuilders.get("/admin/restaurants/" + PIZZA_HUT_ID + "/withMenuAndRating?date=" + LOCALDATE_30_OF_JANUARY))
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
        RestaurantTo pizzaHutTo = RestaurantUtil.createTo(PizzaHut, 3);
        RestaurantTo sushiRollTo = RestaurantUtil.createTo(SushiRoll, 1);
        RestaurantTo kebabHouseTo = RestaurantUtil.createTo(KebabHouse, 2);

        perform(MockMvcRequestBuilders.get("/admin/restaurants/withMenuAndRating?date=" + LOCALDATE_30_OF_JANUARY))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(pizzaHutTo, sushiRollTo, kebabHouseTo));
    }
}