package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.vote.VoteRepository;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import com.natalinstanislav.restaurants.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static com.natalinstanislav.restaurants.RestaurantTestData.SUSHI_ROLL_ID;
import static com.natalinstanislav.restaurants.TestUtil.readFromJson;
import static com.natalinstanislav.restaurants.TestUtil.userHttpBasic;
import static com.natalinstanislav.restaurants.UserTestData.USER3_ID;
import static com.natalinstanislav.restaurants.UserTestData.admin;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminVoteRestControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/" + VOTE_USER3_30_OF_JANUARY_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VoteUser3January30));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/votes/" + VOTE_USER3_30_OF_JANUARY_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertNull(voteRepository.get(VOTE_USER3_30_OF_JANUARY_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(ALL_VOTES));
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

    @Test
    void create() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post("/admin/votes?dateTime=" + ISO_29_OF_JANUARY_TIME +
                "&restaurantId=" + PIZZA_HUT_ID + "&userId=" + USER3_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        int newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.get(newId), newVote);
    }

    @Test
    void update() throws Exception {
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put("/admin/votes/" + VOTE_USER3_30_OF_JANUARY_ID + "?restaurantId=" + SUSHI_ROLL_ID + "&userId=" + USER3_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(voteRepository.get(VOTE_USER3_30_OF_JANUARY_ID), updated);
    }
}