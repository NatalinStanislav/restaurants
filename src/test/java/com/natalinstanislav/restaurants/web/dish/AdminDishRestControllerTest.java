package com.natalinstanislav.restaurants.web.dish;

import com.natalinstanislav.restaurants.DishTestData;
import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.repository.dish.DishRepository;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import com.natalinstanislav.restaurants.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.natalinstanislav.restaurants.DishTestData.*;
import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static com.natalinstanislav.restaurants.TestUtil.readFromJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishRestControllerTest extends AbstractControllerTest {

    @Autowired
    private DishRepository dishRepository;

    @Test
    void create() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post("/admin/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());
        Dish created = readFromJson(action, Dish.class);
        int newId = created.getId();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.get(newId), newDish);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/dishes/" + MEXICAN_PIZZA_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertNull(dishRepository.get(MEXICAN_PIZZA_ID));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/dishes/" + MEXICAN_PIZZA_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(MexicanPizza));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/dishes/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(ALL_DISHES));
    }

    @Test
    void getAllFromRestaurantByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/dishes/fromRestaurantByDate?restaurantId=" + PIZZA_HUT_ID +
                "&date=" + ISO_30_OF_JANUARY))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(ALL_DISHES_FROM_30_OF_JANUARY_FROM_PIZZA_HUT));
    }

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/dishes/byDate?date=" + ISO_30_OF_JANUARY))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(ALL_DISHES_FROM_30_OF_JANUARY));
    }

    @Test
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put("/admin/dishes/" + MEXICAN_PIZZA_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishRepository.get(MEXICAN_PIZZA_ID), updated);
    }
}