package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository{
    private static final Sort SORT_DATETIME = Sort.by(Sort.Direction.ASC, "date", "id");

    private final JpaVoteRepository voteRepository;

    public DataJpaVoteRepository(JpaVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public Vote save(Vote vote) {
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
}
