package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static com.natalinstanislav.restaurants.TestUtil.userHttpBasic;
import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminVoteRestControllerTest extends AbstractControllerTest {

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/fromUser?userId=" + USER3_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/fromUser?userId=" + USER3_ID)
                .with(userHttpBasic(user3)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllFromUser() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/fromUser?userId=" + USER3_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(ALL_VOTES_FROM_USER3));
    }

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/byDate?date=" + ISO_30_OF_JANUARY)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(ALL_VOTES_FROM_30_OF_JANUARY));
    }

    @Test
    void getAllByDateForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/byDateForRestaurant?date=" + ISO_30_OF_JANUARY + "&restaurantId=" + PIZZA_HUT_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(ALL_VOTES_FROM_30_OF_JANUARY_FOR_PIZZA_HUT));
    }
}