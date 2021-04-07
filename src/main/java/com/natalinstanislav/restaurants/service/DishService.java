package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.repository.JpaDishRepository;
import com.natalinstanislav.restaurants.repository.JpaRestaurantRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DishService {
    private static final Sort SORT_RESTAURANT_DATE = Sort.by(Sort.Direction.ASC, "dishDate", "restaurant", "id");

    private final JpaDishRepository dishRepository;
    private final JpaRestaurantRepository restaurantRepository;

    public DishService(JpaDishRepository dishRepository, JpaRestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Dish save(Dish dish, int restaurantId) {
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return dishRepository.save(dish);
    }

    public boolean delete(int id) {
        return dishRepository.delete(id) != 0;
    }

    public Dish get(int id) {
        return dishRepository.findById(id).orElse(null);
    }

    public List<Dish> getAll() {
        return dishRepository.findAll(SORT_RESTAURANT_DATE);
    }

    public List<Dish> getAllFromRestaurantByDate(int restaurantId, LocalDate date) {
        return dishRepository.getAllFromRestaurantByDate(restaurantId, date);
    }

    public List<Dish> getAllByDate(LocalDate date) {
        return dishRepository.getAllByDate(date);
    }
}
