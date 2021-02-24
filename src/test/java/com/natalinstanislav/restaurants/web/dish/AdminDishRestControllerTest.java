package com.natalinstanislav.restaurants.web.dish;

import com.natalinstanislav.restaurants.repository.dish.DishRepository;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import com.natalinstanislav.restaurants.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.natalinstanislav.restaurants.DishTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishRestControllerTest extends AbstractControllerTest {

    @Autowired
    private DishRepository dishRepository;

    @Test
    void create() {
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/dishes/" + MEXICAN_PIZZA_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(null, dishRepository.get(MEXICAN_PIZZA_ID));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/dishes/" + MEXICAN_PIZZA_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeIgnoreProps(MexicanPizza, "restaurant")));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/dishes/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllFromRestaurantByDate() {
    }

    @Test
    void getAllByDate() {
    }

    @Test
    void update() {
    }
}