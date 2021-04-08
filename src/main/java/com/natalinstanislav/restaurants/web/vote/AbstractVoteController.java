package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.assureIdConsistent;

public abstract class AbstractVoteController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteService voteService;

    public AbstractVoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    public Vote get(int id) {
        log.info("get vote with id {}", id);
        return voteService.get(id);
    }

    public Vote get(int id, int userId) {
        log.info("get vote with id {} for user with id {}", id, userId);
        return voteService.get(id, userId);
    }

    public void delete(int id) {
        log.info("delete vote with id {}", id);
        voteService.delete(id);
    }

    public void delete(int id, int userId) {
        log.info("delete vote with id {} and userId {}", id, userId);
        voteService.delete(id, userId);
    }

    public List<Vote> getAll() {
        log.info("getAll");
        return voteService.getAll();
    }

    public List<Vote> getAllFromUser(int userId) {
        log.info("getAllFromUser with id {}", userId);
        return voteService.getAllFromUser(userId);
    }

    public List<Vote> getAllByDate(LocalDate date) {
        log.info("getAllByDate by date {}", date);
        return voteService.getAllByDate(date);
    }

    public List<Vote> getAllByDateForRestaurant(LocalDate date, int restaurantId) {
        log.info("getAllByDateForRestaurant by date {} from restaurant with id {}", date, restaurantId);
        return voteService.getAllByDateForRestaurant(date, restaurantId);
    }

    public Vote create(int restaurantId, int userId) {
        log.info("create vote for restaurant with id {} by user with id {}", restaurantId, userId);
        return voteService.create(restaurantId, userId);
    }

    public Vote update(Vote vote, int id, int restaurantId, int userId) {
        log.info("update {} with id={}", vote, id);
        assureIdConsistent(vote, id);
        return voteService.update(vote, id, restaurantId, userId);
    }
}
