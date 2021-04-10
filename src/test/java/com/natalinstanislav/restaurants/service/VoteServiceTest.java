package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import com.natalinstanislav.restaurants.util.exception.TimeValidationException;
import com.natalinstanislav.restaurants.util.exception.VoteDuplicateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static com.natalinstanislav.restaurants.UserTestData.USER3_ID;
import static com.natalinstanislav.restaurants.UserTestData.USER0_ID;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class VoteServiceTest {

    @Autowired
    protected VoteService voteService;

    @Test
    void create() {
        Vote newVote = getNewNow();
        Vote created = voteService.create(PIZZA_HUT_ID, USER3_ID);
        Integer newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.getToday(USER3_ID), newVote);
    }

    @Test
    void createTwiceByDay() {
        assertThrows(VoteDuplicateException.class, () -> voteService.create(PIZZA_HUT_ID, USER0_ID));
    }

    @Test
    void update() throws Exception {
        Vote updated = getUpdated();
        if (LocalTime.now().isAfter(LocalTime.of(11, 0))) {
            assertThrows(TimeValidationException.class, () -> voteService.update(updated, VOTE_USER0_TODAY_ID, PIZZA_HUT_ID, USER0_ID));
        } else {
            voteService.update(updated, VOTE_USER0_TODAY_ID, PIZZA_HUT_ID, USER0_ID);
            VOTE_MATCHER.assertMatch(voteService.getToday(USER0_ID), getUpdated());
        }
    }

    @Test
    void getByDateAndUser() {
        Vote vote = voteService.getByDateAndUser(LOCAL_DATE_30_OF_JANUARY, USER3_ID);
        VOTE_MATCHER.assertMatch(vote, VoteUser3January30);
    }

    @Test
    void getByDateAndUserNotFound() {
        Vote vote = voteService.getByDateAndUser(LOCAL_DATE_30_OF_JANUARY, NOT_FOUND);
        Assertions.assertThat(vote).isNull();
    }

    @Test
    void getToday() {
        Vote user0TodaysVote = voteService.getToday(USER0_ID);
        VOTE_MATCHER.assertMatch(user0TodaysVote, VoteUser0Today);
    }

    @Test
    void getTodayNotFound() {
        assertThrows(NotFoundException.class, () -> voteService.getToday(USER3_ID));
    }

    @Test
    void getAllFromUser() {
        List<Vote> allFromUser3 = voteService.getAllFromUser(USER3_ID);
        VOTE_MATCHER.assertMatch(allFromUser3, ALL_VOTES_FROM_USER3);
    }

    @Test
    void getAllByDate() {
        List<Vote> allFrom30OfJanuary = voteService.getAllByDate(LocalDate.of(2020, 1, 30));
        VOTE_MATCHER.assertMatch(allFrom30OfJanuary, ALL_VOTES_FROM_30_OF_JANUARY);
    }

    @Test
    void getAllByDateForRestaurant() {
        List<Vote> allFrom30OfJanuaryForPizzaHut = voteService.getAllByDateForRestaurant(LocalDate.of(2020, 1, 30), PIZZA_HUT_ID);
        VOTE_MATCHER.assertMatch(allFrom30OfJanuaryForPizzaHut, ALL_VOTES_FROM_30_OF_JANUARY_FOR_PIZZA_HUT);
    }
}