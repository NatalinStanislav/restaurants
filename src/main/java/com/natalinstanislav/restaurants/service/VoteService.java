package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.JpaRestaurantRepository;
import com.natalinstanislav.restaurants.repository.JpaUserRepository;
import com.natalinstanislav.restaurants.repository.JpaVoteRepository;
import com.natalinstanislav.restaurants.util.TimeValidationUtil;
import com.natalinstanislav.restaurants.util.exception.VoteDuplicateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFound;
import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private final JpaVoteRepository voteRepository;
    private final JpaRestaurantRepository restaurantRepository;
    private final JpaUserRepository userRepository;

    public VoteService(JpaVoteRepository voteRepository, JpaRestaurantRepository restaurantRepository, JpaUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public void deleteToday(int userId) {
        LocalDate today = LocalDate.now();
        checkNotFound(voteRepository.deleteToday(today, userId) != 0, "date = " + today + " and userId = " + userId);
    }

    public Vote getToday(int userId) {
        LocalDate today = LocalDate.now();
        return checkNotFound(voteRepository.getToday(today, userId), "date = " + today + " and userId = " + userId);
    }

    public Vote getByDateAndUser(LocalDate date, int userId) {
        return voteRepository.getByDateAndUser(date, userId);
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

    @Transactional
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

    @Transactional
    public Vote update(Vote vote, int id, int restaurantId, int userId) {
        Assert.notNull(vote, "vote must not be null");
        TimeValidationUtil.checkVoteTime();
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setUser(userRepository.getOne(userId));
        return checkNotFoundWithId(voteRepository.save(vote), id);
    }
}
