package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.RestaurantTestData.PIZZA_HUT_ID;
import static com.natalinstanislav.restaurants.UserTestData.USER3_ID;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DataJpaVoteRepositoryTest {

    @Autowired
    protected VoteRepository repository;

    @Test
    void save() {
        Vote newVote = getNew();
        Vote created = repository.save(newVote, PIZZA_HUT_ID, USER3_ID);
        Integer newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.get(newId), newVote);
    }

    @Test
    void delete() {
        Assertions.assertThat(repository.get(VOTE_USER0_30_OF_JANUARY_ID)).isNotNull();
        repository.delete(VOTE_USER0_30_OF_JANUARY_ID);
        Assertions.assertThat(repository.get(VOTE_USER0_30_OF_JANUARY_ID)).isNull();
    }

    @Test
    void deletedNotFound() throws Exception {
        assertFalse(repository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Vote vote = repository.get(VOTE_ADMIN_30_OF_JANUARY_ID);
        VOTE_MATCHER.assertMatch(vote, VoteAdminJanuary30);
    }

    @Test
    void getNotFound() throws Exception {
        Assertions.assertThat(repository.get(NOT_FOUND)).isNull();
    }

    @Test
    void getByDateAndUser() {
        Vote vote = repository.getByDateAndUser(LOCAL_DATE_30_OF_JANUARY, USER3_ID);
        VOTE_MATCHER.assertMatch(vote, VoteUser3January30);
    }

    @Test
    void getByDateAndUserNotFound() {
        Vote vote = repository.getByDateAndUser(LOCAL_DATE_30_OF_JANUARY, NOT_FOUND);
        Assertions.assertThat(vote).isNull();
    }

    @Test
    void getAll() {
        List<Vote> all = repository.getAll();
        VOTE_MATCHER.assertMatch(all, ALL_VOTES);
    }

    @Test
    void getAllFromUser() {
        List<Vote> allFromUser3 = repository.getAllFromUser(USER3_ID);
        VOTE_MATCHER.assertMatch(allFromUser3, ALL_VOTES_FROM_USER3);
    }

    @Test
    void getAllByDate() {
        List<Vote> allFrom30OfJanuary = repository.getAllByDate(LocalDate.of(2020, 1, 30));
        VOTE_MATCHER.assertMatch(allFrom30OfJanuary, ALL_VOTES_FROM_30_OF_JANUARY);
    }

    @Test
    void getAllByDateForRestaurant() {
        List<Vote> allFrom30OfJanuaryForPizzaHut = repository.getAllByDateForRestaurant(LocalDate.of(2020, 1, 30), PIZZA_HUT_ID);
        VOTE_MATCHER.assertMatch(allFrom30OfJanuaryForPizzaHut, ALL_VOTES_FROM_30_OF_JANUARY_FOR_PIZZA_HUT);
    }
}