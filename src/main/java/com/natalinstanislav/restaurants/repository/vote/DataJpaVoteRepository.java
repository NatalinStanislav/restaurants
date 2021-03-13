package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.restaurant.JpaRestaurantRepository;
import com.natalinstanislav.restaurants.repository.user.JpaUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Sort SORT_DATETIME = Sort.by(Sort.Direction.ASC, "date", "id");

    private final JpaVoteRepository voteRepository;
    private final JpaRestaurantRepository restaurantRepository;
    private final JpaUserRepository userRepository;

    public DataJpaVoteRepository(JpaVoteRepository voteRepository, JpaRestaurantRepository restaurantRepository, JpaUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Vote save(Vote vote, int restaurantId, int userId) {
        vote.setRestaurant(restaurantRepository.findById(restaurantId).orElse(null));
        vote.setUser(userRepository.findById(userId).orElse(null));
        return voteRepository.save(vote);
    }

    @Override
    public boolean delete(int id) {
        return voteRepository.delete(id) != 0;
    }

    @Override
    public Vote get(int id) {
        return voteRepository.findById(id).orElse(null);
    }

    @Override
    public Vote getByDateAndUser(LocalDate date, int userId) {
        return voteRepository.getByDateAndUser(date, userId);
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll(SORT_DATETIME);
    }

    @Override
    public List<Vote> getAllFromUser(int userId) {
        return voteRepository.getAllFromUser(userId);
    }

    @Override
    public List<Vote> getAllByDate(LocalDate date) {
        return voteRepository.getAllByDate(date);
    }

    @Override
    public List<Vote> getAllByDateForRestaurant(LocalDate date, int restaurantId) {
        return voteRepository.getAllByDateForRestaurant(date, restaurantId);
    }
}
