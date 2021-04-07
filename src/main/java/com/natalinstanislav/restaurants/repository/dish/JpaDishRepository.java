package com.natalinstanislav.restaurants.repository.dish;

import com.natalinstanislav.restaurants.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface JpaDishRepository extends JpaRepository<Dish, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT d FROM Dish d WHERE d.dishDate=?1 ORDER BY d.id")
    List<Dish> getAllByDate(LocalDate date);

    @Query("SELECT d FROM Dish d WHERE d.dishDate=?2 AND d.restaurant.id = ?1 ORDER BY d.id")
    List<Dish> getAllFromRestaurantByDate(int restaurantId, LocalDate date);
}
