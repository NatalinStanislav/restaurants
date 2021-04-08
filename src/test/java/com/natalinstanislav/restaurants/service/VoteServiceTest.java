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
import static com.natalinstanislav.restaurants.UserTestData.ADMIN_ID;
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
        VOTE_MATCHER.assertMatch(voteService.get(newId), newVote);
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
            VOTE_MATCHER.assertMatch(voteService.get(VOTE_USER0_TODAY_ID), getUpdated());
        }
    }

    @Test
    void delete() {
        voteService.delete(VOTE_USER0_30_OF_JANUARY_ID);
        assertThrows(NotFoundException.class, () -> voteService.get(VOTE_USER0_30_OF_JANUARY_ID));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> voteService.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Vote vote = voteService.get(VOTE_ADMIN_30_OF_JANUARY_ID);
        VOTE_MATCHER.assertMatch(vote, VoteAdminJanuary30);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> voteService.get(NOT_FOUND));
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
    void getAll() {
        List<Vote> all = voteService.getAll();
        VOTE_MATCHER.assertMatch(all, ALL_VOTES);
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

    @Test
    void getFromUser() {
        Vote vote = voteService.get(VOTE_ADMIN_30_OF_JANUARY_ID, ADMIN_ID);
        VOTE_MATCHER.assertMatch(vote, VoteAdminJanuary30);
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> voteService.get(VOTE_ADMIN_30_OF_JANUARY_ID, USER3_ID));
    }

    @Test
    void deleteFromUser() {
        voteService.delete(VOTE_USER0_30_OF_JANUARY_ID, USER0_ID);
        assertThrows(NotFoundException.class, () -> voteService.get(VOTE_USER0_30_OF_JANUARY_ID));
    }

    @Test
    void deleteFromUserNotOwn() {
        assertThrows(NotFoundException.class, () -> voteService.delete(VOTE_USER0_30_OF_JANUARY_ID, USER3_ID));
    }
}