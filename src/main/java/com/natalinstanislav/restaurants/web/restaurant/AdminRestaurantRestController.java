package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.repository.restaurant.RestaurantRepository;
import com.natalinstanislav.restaurants.util.ValidationUtil;
import com.natalinstanislav.restaurants.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.List;

@Controller
public class AdminRestaurantRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantRepository repository;

    public Restaurant get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get restaurant {} by user {}", id, userId);
        return ValidationUtil.checkNotFoundWithId(repository.get(id), id);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete restaurant {} by user {}", id, userId);
        repository.delete(id);
    }

    public List<Restaurant> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll by user {}", userId);
        return repository.getAll();
    }

    public Restaurant create(Restaurant restaurant) {
        int userId = SecurityUtil.authUserId();
        ValidationUtil.checkNew(restaurant);
        log.info("create {} by user {}", restaurant, userId);
        Assert.notNull(restaurant, "meal must not be null");
        return repository.save(restaurant);
    }

    public void update(Restaurant restaurant, int id) {
        int userId = SecurityUtil.authUserId();
        ValidationUtil.assureIdConsistent(restaurant, id);
        log.info("update {} by user {}", restaurant, userId);
        Assert.notNull(restaurant, "meal must not be null");
        ValidationUtil.checkNotFoundWithId(repository.save(restaurant), id);
    }
}