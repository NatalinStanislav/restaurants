package com.natalinstanislav.restaurants.repository.dish;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository extends Repository<Dish> {

    Dish save(Dish dish, int restaurantId);

    List<Dish> getAllFromRestaurantByDate(int restaurantId, LocalDate date);

    List<Dish> getAllByDate(LocalDate date);

    default Dish save(Dish dish) {
        throw new UnsupportedOperationException();
    }
}
