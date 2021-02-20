package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.util.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.DishTestData.NOT_FOUND;
import static com.natalinstanislav.restaurants.UserTestData.USER3_ID;
import static com.natalinstanislav.restaurants.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Vote created = repository.save(newVote);
        Integer newId = created.getId();
        newVote.setId(newId);
        assertMatch(created, newVote);
        assertMatch(repository.get(newId), newVote);
    }

    @Test
    void delete() {
        repository.delete(VOTE_USER0_30_OF_JANUARY_ID);
        assertThrows(NotFoundException.class, () -> repository.get(VOTE_USER0_30_OF_JANUARY_ID));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> repository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Vote vote = repository.get(VOTE_ADMIN_30_OF_JANUARY_ID);
        assertMatch(vote, VoteAdminJanuary30);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> repository.get(NOT_FOUND));
    }

    @Test
    void getAll() {
        List<Vote> all = repository.getAll();
        assertMatch(all, ALL_VOTES);
    }

    @Test
    void getAllFromUser() {
        List<Vote> allFromUser3 = repository.getAllFromUser(USER3_ID);
        assertMatch(allFromUser3, ALL_VOTES_FROM_USER3);
    }

    @Test
    void getAllByDate() {
        List<Vote> allFrom30OfJanuary = repository.getAllByDate(LocalDate.of(2020, 1, 30));
        assertMatch(allFrom30OfJanuary, ALL_VOTES_FROM_30_OF_JANUARY);
    }
}