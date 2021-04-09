package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.VoteTestData;
import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.service.VoteService;
import com.natalinstanislav.restaurants.util.exception.ErrorType;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import com.natalinstanislav.restaurants.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;

import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static com.natalinstanislav.restaurants.TestUtil.readFromJson;
import static com.natalinstanislav.restaurants.TestUtil.userHttpBasic;
import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileVoteRestControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteService voteService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/" + VOTE_USER3_30_OF_JANUARY_ID)
                .with(userHttpBasic(user3)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VoteUser3January30));
    }

    @Test
    void getNotOwn() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/" + VOTE_USER3_30_OF_JANUARY_ID)
                .with(userHttpBasic(user2)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/" + VoteTestData.NOT_FOUND)
                .with(userHttpBasic(user2)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getToday() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/today")
                .with(userHttpBasic(user0)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VoteUser0Today));
    }

    @Test
    void getTodayNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/today")
                .with(userHttpBasic(user3)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile/votes/")
                .with(userHttpBasic(user3)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(ALL_VOTES_FROM_USER3));
    }

    @Test
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNewNow();
        ResultActions action = perform(MockMvcRequestBuilders.post("/profile/votes?restaurantId=" + PIZZA_HUT_ID)
                .with(userHttpBasic(user3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        int newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.get(newId), newVote);
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post("/profile/votes?restaurantId=" + null)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user3)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void doubleVotePerDay() throws Exception {
        perform(MockMvcRequestBuilders.post("/profile/votes?restaurantId=" + PIZZA_HUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user2)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void update() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        if (LocalTime.now().isAfter(LocalTime.of(11, 0))) {

            perform(MockMvcRequestBuilders.put("/profile/votes/" + VOTE_USER0_TODAY_ID + "?restaurantId=" + PIZZA_HUT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated))
                    .with(userHttpBasic(user0)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        } else {
            perform(MockMvcRequestBuilders.put("/profile/votes/" + VOTE_USER0_TODAY_ID + "?restaurantId=" + PIZZA_HUT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated))
                    .with(userHttpBasic(user0)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            VOTE_MATCHER.assertMatch(voteService.get(VOTE_USER0_TODAY_ID), updated);
        }
    }

    @Test
    void updateNotOwn() throws Exception {
        Vote updated = VoteTestData.getUpdated();

        perform(MockMvcRequestBuilders.put("/profile/votes/" + VOTE_USER1_TODAY_ID + "?restaurantId=" + PIZZA_HUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(user0)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}