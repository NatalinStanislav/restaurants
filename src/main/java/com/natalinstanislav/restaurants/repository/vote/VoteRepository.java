package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {
    Vote save(Vote vote, int restaurantId, int userId);

    boolean delete(int id);

    Vote get(int id);

    Vote getByDateAndUser(LocalDate date, int userId);

    List<Vote> getAll();

    List<Vote> getAllFromUser(int userId);

    List<Vote> getAllByDate(LocalDate date);

    List<Vote> getAllByDateForRestaurant(LocalDate date, int restaurantId);
}
