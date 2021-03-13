package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.to.RestaurantTo;
import com.natalinstanislav.restaurants.util.RestaurantUtil;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.natalinstanislav.restaurants.DishTestData.*;
import static com.natalinstanislav.restaurants.DishTestData.ISO_30_OF_JANUARY;
import static com.natalinstanislav.restaurants.RestaurantTestData.*;
import static com.natalinstanislav.restaurants.TestUtil.userHttpBasic;
import static com.natalinstanislav.restaurants.UserTestData.user0;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestaurantRestControllerTest extends AbstractControllerTest {

    @Test
    void getAllWithMenu() throws Exception {
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);
        SushiRoll.setDishes(ALL_DISHES_FROM_SUSHI_ROLL_30_OF_JANUARY);
        KebabHouse.setDishes(ALL_DISHES_FROM_KEBAB_HOUSE_30_OF_JANUARY);

        perform(MockMvcRequestBuilders.get("/restaurants/withMenu?date=" + ISO_30_OF_JANUARY)
                .with(userHttpBasic(user0)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_MENU_MATCHER.contentJson(PizzaHut, SushiRoll, KebabHouse));
    }

    @Test
    void getAllWithMenuAndRating() throws Exception {
        PizzaHut.setDishes(ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY);
        SushiRoll.setDishes(ALL_DISHES_FROM_SUSHI_ROLL_30_OF_JANUARY);
        KebabHouse.setDishes(ALL_DISHES_FROM_KEBAB_HOUSE_30_OF_JANUARY);
        RestaurantTo pizzaHutTo = RestaurantUtil.createTo(PizzaHut, 3);
        RestaurantTo sushiRollTo = RestaurantUtil.createTo(SushiRoll, 1);
        RestaurantTo kebabHouseTo = RestaurantUtil.createTo(KebabHouse, 2);

        perform(MockMvcRequestBuilders.get("/restaurants/withMenuAndRating?date=" + LOCALDATE_30_OF_JANUARY)
                .with(userHttpBasic(user0)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(pizzaHutTo, sushiRollTo, kebabHouseTo));
    }
}