package com.natalinstanislav.restaurants.repository.vote;

import com.natalinstanislav.restaurants.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface JpaVoteRepository extends JpaRepository<Vote, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vote v WHERE v.date=?1 AND v.user.id =?2")
    Vote getByDateAndUser(LocalDate date, int userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1")
    List<Vote> getAllFromUser(int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant WHERE v.date=?1")
    List<Vote> getAllByDate(LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.date=?1 AND v.restaurant.id=?2")
    List<Vote> getAllByDateForRestaurant(LocalDate date, int restaurantId);
}
