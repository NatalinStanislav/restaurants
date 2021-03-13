package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.repository.restaurant.RestaurantRepository;
import com.natalinstanislav.restaurants.repository.vote.VoteRepository;
import com.natalinstanislav.restaurants.to.RestaurantTo;
import com.natalinstanislav.restaurants.util.RestaurantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;
import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

public abstract class AbstractRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final RestaurantRepository restaurantRepository;

    protected final VoteRepository voteRepository;

    public AbstractRestaurantController(RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
    }

    public Restaurant get(int id) {
        log.info("get restaurant with id {}", id);
        return checkNotFoundWithId(restaurantRepository.get(id), id);
    }

    public Restaurant getWithMenu(int id, LocalDate date) {
        log.info("getWithMenu restaurant with id {} with menu from date {}", id, date);
        return restaurantRepository.getWithMenu(id, date);
    }

    public RestaurantTo getWithMenuAndRating(int id, LocalDate date) {
        log.info("getWithMenuAndRating restaurant with id {} with menu from date {}", id, date);
        Restaurant restaurant = restaurantRepository.getWithMenu(id, date);
        int rating = voteRepository.getAllByDateForRestaurant(date, id).size();
        return RestaurantUtil.createTo(restaurant, rating);
    }

    public void delete(int id) {
        log.info("delete restaurant with id {}", id);
        checkNotFoundWithId(restaurantRepository.delete(id), id);
    }

    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.getAll();
    }

    public List<Restaurant> getAllWithMenu(LocalDate date) {
        log.info("getAllWithMenu from date {}", date);
        return restaurantRepository.getAllWithMenu(date);
    }

    public List<RestaurantTo> getAllWithMenuAndRating(LocalDate date) {
        log.info("getAllWithMenuAndRating from date {}", date);
        List<Restaurant> restaurants = restaurantRepository.getAllWithMenu(date);
        Map<Restaurant, Integer> restaurantRatingMap = RestaurantUtil.getRestaurantRatingMap(voteRepository.getAllByDate(date));
        return RestaurantUtil.getTos(restaurants, restaurantRatingMap);
    }

    public ResponseEntity<Restaurant> create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/restaurants" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    public void update(Restaurant restaurant, int id) {
        log.info("update {} with id={}", restaurant, id);
        Assert.notNull(restaurant, "restaurant must not be null");
        assureIdConsistent(restaurant, id);
        checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.getId());
    }
}
