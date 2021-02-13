package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.model.Vote;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.UserTestData.USER3_ID;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static org.junit.Assert.*;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@RunWith(SpringRunner.class)
public class DataJpaVoteRepositoryTest {

    @Autowired
    protected VoteRepository repository;

    @Test
    public void save() {
        Vote newVote = getNew();
        Vote created = repository.save(newVote);
        Integer newId = created.getId();
        newVote.setId(newId);
        assertMatch(created, newVote);
        assertMatch(repository.get(newId), newVote);
    }

    @Test
    public void delete() {
        Assertions.assertThat(repository.get(VOTE_USER0_30_OF_JANUARY_ID)).isNotNull();
        repository.delete(VOTE_USER0_30_OF_JANUARY_ID);
        assertMatch(null, repository.get(VOTE_USER0_30_OF_JANUARY_ID));
    }

    @Test
    public void get() {
        Vote vote = repository.get(VOTE_ADMIN_30_OF_JANUARY_ID);
        assertMatch(vote, VoteAdminJanuary30);
    }

    @Test
    public void getAll() {
        List<Vote> all = repository.getAll();
        assertMatch(all, ALL_VOTES);
    }

    @Test
    public void getAllFromUser() {
        List<Vote> allFromUser3 = repository.getAllFromUser(USER3_ID);
        assertMatch(allFromUser3, ALL_VOTES_FROM_USER3);
    }

    @Test
    public void getAllByDate() {
        List<Vote> allFrom30OfJanuary = repository.getAllByDate(LocalDate.of(2020, 1, 30));
        assertMatch(allFrom30OfJanuary, ALL_VOTES_FROM_30_OF_JANUARY);
    }
}