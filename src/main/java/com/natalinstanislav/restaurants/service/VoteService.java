package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.JpaRestaurantRepository;
import com.natalinstanislav.restaurants.repository.JpaUserRepository;
import com.natalinstanislav.restaurants.repository.JpaVoteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public Vote save(Vote vote, int restaurantId, int userId) {
        vote.setRestaurant(restaurantRepository.findById(restaurantId).orElse(null));
        vote.setUser(userRepository.findById(userId).orElse(null));
        return voteRepository.save(vote);
    }

    public boolean delete(int id) {
        return voteRepository.delete(id) != 0;
    }

    public Vote get(int id) {
        return voteRepository.findById(id).orElse(null);
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

    public Vote get(int id, int userId) {
        return voteRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null);
    }

    public boolean delete(int id, int userId) {
        return voteRepository.delete(id, userId) != 0;
    }
}
