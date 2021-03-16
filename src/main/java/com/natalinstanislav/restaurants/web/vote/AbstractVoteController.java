package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.vote.VoteRepository;
import com.natalinstanislav.restaurants.util.TimeValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.assureIdConsistent;
import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

public abstract class AbstractVoteController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteRepository voteRepository;

    public AbstractVoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote get(int id) {
        log.info("get vote with id {}", id);
        return checkNotFoundWithId(voteRepository.get(id), id);
    }

    public Vote get(int id, int userId) {
        log.info("get vote with id {} for user with id {}", id, userId);
        return checkNotFoundWithId(voteRepository.get(id, userId), id);
    }

    public void delete(int id) {
        log.info("delete vote with id {}", id);
        checkNotFoundWithId(voteRepository.delete(id), id);
    }

    public List<Vote> getAll() {
        log.info("getAll");
        return voteRepository.getAll();
    }

    public List<Vote> getAllFromUser(int userId) {
        log.info("getAllFromUser with id {}", userId);
        return voteRepository.getAllFromUser(userId);
    }

    public List<Vote> getAllByDate(LocalDate date) {
        log.info("getAllByDate by date {}", date);
        return voteRepository.getAllByDate(date);
    }

    public List<Vote> getAllByDateForRestaurant(LocalDate date, int restaurantId) {
        log.info("getAllByDateForRestaurant by date {} from restaurant with id {}", date, restaurantId);
        return voteRepository.getAllByDateForRestaurant(date, restaurantId);
    }

    public Vote create(LocalDateTime dateTime, int restaurantId, int userId) {
        log.info("create vote for restaurant with id {} by user with id {}", restaurantId, userId);
        LocalDate date = TimeValidationUtil.checkVoteTime(dateTime);
        Vote vote = voteRepository.getByDateAndUser(date, userId);
        if (vote == null) {
            vote = new Vote(null, date);
            return voteRepository.save(vote, restaurantId, userId);
        } else {
            return update(vote, vote.getId(), restaurantId, userId);
        }
    }

    public Vote update(Vote vote, int id, int restaurantId, int userId) {
        log.info("update {} with id={}", vote, id);
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, id);
        return checkNotFoundWithId(voteRepository.save(vote, restaurantId, userId), id);
    }
}
