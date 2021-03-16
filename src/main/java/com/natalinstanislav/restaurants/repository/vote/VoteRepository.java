package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends Repository<Vote> {
    Vote save(Vote vote, int restaurantId, int userId);

    Vote getByDateAndUser(LocalDate date, int userId);

    List<Vote> getAllFromUser(int userId);

    List<Vote> getAllByDate(LocalDate date);

    List<Vote> getAllByDateForRestaurant(LocalDate date, int restaurantId);

    Vote get(int id, int userId);

    boolean delete(int id, int userId);

    default Vote save(Vote vote) {
        throw new UnsupportedOperationException();
    }
}
