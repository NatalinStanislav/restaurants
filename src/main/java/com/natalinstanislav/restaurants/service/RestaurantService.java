package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.repository.JpaRestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final JpaRestaurantRepository restaurantRepository;

    public RestaurantService(JpaRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.getId());
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
    }

    public Restaurant getWithMenu(int id, LocalDate date) {
        return restaurantRepository.getWithMenu(id, date);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_NAME);
    }

    public List<Restaurant> getAllWithMenu(LocalDate date) {
        return restaurantRepository.getAllWithMenu(date);
    }
}
