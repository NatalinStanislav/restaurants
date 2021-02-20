package com.natalinstanislav.restaurants.repository.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant meal);

    void delete(int id);

    Restaurant get(int id);

    List<Restaurant> getAll();

}
