package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaVoteRepository implements VoteRepository{
    private static final Sort SORT_DATETIME = Sort.by(Sort.Direction.ASC, "date", "id");

    private final JpaVoteRepository voteRepository;

    public DataJpaVoteRepository(JpaVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public Vote save(Vote vote) {
        Assert.notNull(vote, "vote must not be null");
        return voteRepository.save(vote);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(voteRepository.delete(id) != 0, id);
    }

    @Override
    public Vote get(int id) {
        return checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
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
