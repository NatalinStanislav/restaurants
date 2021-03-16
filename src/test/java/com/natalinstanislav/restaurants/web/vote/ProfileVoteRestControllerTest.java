package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.VoteTestData;
import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.vote.VoteRepository;
import com.natalinstanislav.restaurants.util.exception.ErrorType;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static com.natalinstanislav.restaurants.RestaurantTestData.SUSHI_ROLL_ID;
import static com.natalinstanislav.restaurants.TestUtil.readFromJson;
import static com.natalinstanislav.restaurants.TestUtil.userHttpBasic;
import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileVoteRestControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

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
        Vote newVote = getNewNow();
        String isoDateTimeNow = LocalDateTime.now().toString();
        String isoDateTimeNowForTest = isoDateTimeNow.substring(0, 11) + "10:00:00.000000000";

        ResultActions action = perform(MockMvcRequestBuilders.post("/profile/votes?dateTime=" + isoDateTimeNowForTest +
                "&restaurantId=" + PIZZA_HUT_ID)
                .with(userHttpBasic(user3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        int newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.get(newId), newVote);
    }

    @Test
    void createInvalidTime() throws Exception {
        String isoDateTimeNow = LocalDateTime.now().toString();
        String isoDateTimeNowForTest = isoDateTimeNow.substring(0, 11) + "12:00:00.000000000";

        perform(MockMvcRequestBuilders.post("/profile/votes?dateTime=" + isoDateTimeNowForTest +
                "&restaurantId=" + PIZZA_HUT_ID)
                .with(userHttpBasic(user3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void createInvalidDate() throws Exception {
        perform(MockMvcRequestBuilders.post("/profile/votes?dateTime=" + ISO_29_OF_JANUARY_TIME +
                "&restaurantId=" + PIZZA_HUT_ID)
                .with(userHttpBasic(user3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post("/profile/votes?dateTime=" + null +
                "&restaurantId=" + null)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user3)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void doubleVotePerDay() throws Exception {
        String isoDateTimeNow = LocalDateTime.now().toString();
        String isoDateTimeNowForTest = isoDateTimeNow.substring(0, 11) + "10:00:00.000000000";

        ResultActions action = perform(MockMvcRequestBuilders.post("/profile/votes?dateTime=" + isoDateTimeNowForTest +
                "&restaurantId=" + PIZZA_HUT_ID)
                .with(userHttpBasic(user3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Vote firstVote = readFromJson(action, Vote.class);
        isoDateTimeNowForTest = isoDateTimeNow.substring(0, 11) + "10:30:00.000000000";

        action = perform(MockMvcRequestBuilders.post("/profile/votes?dateTime=" + isoDateTimeNowForTest +
                "&restaurantId=" + SUSHI_ROLL_ID)
                .with(userHttpBasic(user3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Vote secondVote = readFromJson(action, Vote.class);

        Assertions.assertEquals(firstVote.getId(), secondVote.getId());
        Assertions.assertEquals(firstVote.getRestaurant().getName(), "Pizza Hut");
        Assertions.assertEquals(secondVote.getRestaurant().getName(), "Sushi Roll");
        VOTE_MATCHER.assertMatch(secondVote, voteRepository.get(firstVote.getId()));
    }
}