package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.repository.JpaDishRepository;
import com.natalinstanislav.restaurants.repository.JpaRestaurantRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {
    private static final Sort SORT_RESTAURANT_DATE = Sort.by(Sort.Direction.ASC, "dishDate", "restaurant", "id");

    private final JpaDishRepository dishRepository;
    private final JpaRestaurantRepository restaurantRepository;

    public DishService(JpaDishRepository dishRepository, JpaRestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return dishRepository.save(dish);
    }

    public Dish update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return checkNotFoundWithId(dishRepository.save(dish), dish.getId());
    }

    public void delete(int id) {
        checkNotFoundWithId(dishRepository.delete(id)!=0, id);
    }

    public Dish get(int id) {
        return checkNotFoundWithId(dishRepository.findById(id).orElse(null), id);
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
