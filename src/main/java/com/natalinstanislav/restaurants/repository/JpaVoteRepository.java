package com.natalinstanislav.restaurants.repository;

import com.natalinstanislav.restaurants.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface JpaVoteRepository extends JpaRepository<Vote, Integer> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.voteDate=?1 AND v.user.id =?2")
    Vote getToday(LocalDate today, int userId);

    @Query("SELECT v FROM Vote v WHERE v.voteDate=?1 AND v.user.id =?2")
    Vote getByDateAndUser(LocalDate date, int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1")
    List<Vote> getAllFromUser(int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant WHERE v.voteDate=?1")
    List<Vote> getAllByDate(LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.voteDate=?1 AND v.restaurant.id=?2")
    List<Vote> getAllByDateForRestaurant(LocalDate date, int restaurantId);
}
