package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.VoteTestData;
import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.service.VoteService;
import com.natalinstanislav.restaurants.util.exception.ErrorType;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static com.natalinstanislav.restaurants.TestUtil.readFromJson;
import static com.natalinstanislav.restaurants.TestUtil.userHttpBasic;
import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminVoteRestControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteService voteService;

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
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/" + VOTE_USER3_30_OF_JANUARY_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/" + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/" + VOTE_USER3_30_OF_JANUARY_ID)
                .with(userHttpBasic(user3)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getToday() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/votes/today")
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/votes/" + VOTE_USER3_30_OF_JANUARY_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.get(VOTE_USER3_30_OF_JANUARY_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/votes/" + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity());
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
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNewNow();
        ResultActions action = perform(MockMvcRequestBuilders.post("/admin/votes?restaurantId=" + PIZZA_HUT_ID)
                .with(userHttpBasic(admin))
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
        perform(MockMvcRequestBuilders.post("/admin/votes?restaurantId=" + null)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }
}