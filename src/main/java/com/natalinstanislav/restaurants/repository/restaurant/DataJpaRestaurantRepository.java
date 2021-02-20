package com.natalinstanislav.restaurants.repository.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final JpaRestaurantRepository restaurantRepository;

    public DataJpaRestaurantRepository(JpaRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return restaurantRepository.save(restaurant);
    }

    @Override
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    @Override
    public Restaurant get(int id) {
        return checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
    }

    @Override
    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_NAME);
    }
}
