package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {
    Vote save(Vote vote);

    void delete(int id);

    Vote get(int id);

    List<Vote> getAll();

    List<Vote> getAllFromUser(int userId);

    List<Vote> getAllByDate(LocalDate date);
}
