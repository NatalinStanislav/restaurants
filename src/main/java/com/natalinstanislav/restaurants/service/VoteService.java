package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.JpaRestaurantRepository;
import com.natalinstanislav.restaurants.repository.JpaUserRepository;
import com.natalinstanislav.restaurants.repository.JpaVoteRepository;
import com.natalinstanislav.restaurants.util.TimeValidationUtil;
import com.natalinstanislav.restaurants.util.exception.VoteDuplicateException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    private static final Sort SORT_DATETIME = Sort.by(Sort.Direction.ASC, "voteDate", "id");

    private final JpaVoteRepository voteRepository;
    private final JpaRestaurantRepository restaurantRepository;
    private final JpaUserRepository userRepository;

    public VoteService(JpaVoteRepository voteRepository, JpaRestaurantRepository restaurantRepository, JpaUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public void delete(int id) {
        checkNotFoundWithId(voteRepository.delete(id) != 0, id);
    }

    public Vote get(int id) {
        return checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(voteRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null), id);
    }

    public Vote getByDateAndUser(LocalDate date, int userId) {
        return voteRepository.getByDateAndUser(date, userId);
    }

    public List<Vote> getAll() {
        return voteRepository.findAll(SORT_DATETIME);
    }

    public List<Vote> getAllFromUser(int userId) {
        return voteRepository.getAllFromUser(userId);
    }

    public List<Vote> getAllByDate(LocalDate date) {
        return voteRepository.getAllByDate(date);
    }

    public List<Vote> getAllByDateForRestaurant(LocalDate date, int restaurantId) {
        return voteRepository.getAllByDateForRestaurant(date, restaurantId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(voteRepository.delete(id, userId) != 0, id);
    }

    public Vote create(int restaurantId, int userId) {
        LocalDate today = LocalDate.now();
        Vote vote = getByDateAndUser(today, userId);
        if (vote != null) {
            throw new VoteDuplicateException("You already voted today! If you want to change your choice, you can do it in " +
                    "special section in your personal account.");
        }
        vote = new Vote(null, today);
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setUser(userRepository.getOne(userId));
        return voteRepository.save(vote);
    }

    public Vote update(Vote vote, int id, int restaurantId, int userId) {
        Assert.notNull(vote, "vote must not be null");
        TimeValidationUtil.checkVoteTime();
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setUser(userRepository.getOne(userId));
        return checkNotFoundWithId(voteRepository.save(vote), id);
    }
}
