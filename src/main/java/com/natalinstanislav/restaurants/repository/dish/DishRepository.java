package com.natalinstanislav.restaurants.repository.dish;

import com.natalinstanislav.restaurants.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository {

    Dish save(Dish dish);

    void delete(int id);

    Dish get(int id);

    List<Dish> getAll();

    List<Dish> getAllFromRestaurantByDate(int restaurantId, LocalDate date);

    List<Dish> getAllByDate(LocalDate date);
}
