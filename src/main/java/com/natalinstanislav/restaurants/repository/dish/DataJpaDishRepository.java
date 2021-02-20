package com.natalinstanislav.restaurants.repository.dish;

import com.natalinstanislav.restaurants.model.Dish;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaDishRepository implements DishRepository {
    private static final Sort SORT_RESTAURANT_DATE = Sort.by(Sort.Direction.ASC, "date", "restaurant", "id");

    private final JpaDishRepository dishRepository;

    public DataJpaDishRepository(JpaDishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public Dish save(Dish dish) {
        Assert.notNull(dish, "dish must not be null");
        return dishRepository.save(dish);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(dishRepository.delete(id) != 0, id);
    }

    @Override
    public Dish get(int id) {
        return checkNotFoundWithId(dishRepository.findById(id).orElse(null), id);
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
