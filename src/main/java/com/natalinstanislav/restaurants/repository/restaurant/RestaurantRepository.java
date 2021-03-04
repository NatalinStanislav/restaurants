package com.natalinstanislav.restaurants.repository.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean delete(int id);

    Restaurant get(int id);

    Restaurant getWithMenu(int id, LocalDate date);

    List<Restaurant> getAll();

    List<Restaurant> getAllWithMenu(LocalDate date);
}
