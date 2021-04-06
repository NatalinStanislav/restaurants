package com.natalinstanislav.restaurants.repository.dish;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.repository.restaurant.JpaRestaurantRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaDishRepository implements DishRepository {
    private static final Sort SORT_RESTAURANT_DATE = Sort.by(Sort.Direction.ASC, "dishDate", "restaurant", "id");

    private final JpaDishRepository dishRepository;
    private final JpaRestaurantRepository restaurantRepository;

    public DataJpaDishRepository(JpaDishRepository dishRepository, JpaRestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Dish save(Dish dish, int restaurantId) {
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return dishRepository.save(dish);
    }

    @Override
    public boolean delete(int id) {
        return dishRepository.delete(id) != 0;
    }

    @Override
    public Dish get(int id) {
        return dishRepository.findById(id).orElse(null);
    }

    @Override
    public List<Dish> getAll() {
        return dishRepository.findAll(SORT_RESTAURANT_DATE);
    }

    @Override
    public List<Dish> getAllFromRestaurantByDate(int restaurantId, LocalDate date) {
        return dishRepository.getAllFromRestaurantByDate(restaurantId, date);
    }

    @Override
    public List<Dish> getAllByDate(LocalDate date) {
        return dishRepository.getAllByDate(date);
    }
}
