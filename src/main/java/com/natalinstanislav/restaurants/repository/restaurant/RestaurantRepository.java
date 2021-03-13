package com.natalinstanislav.restaurants.repository.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository extends Repository<Restaurant> {

    Restaurant getWithMenu(int id, LocalDate date);

    List<Restaurant> getAllWithMenu(LocalDate date);
}
