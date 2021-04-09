package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.service.RestaurantService;
import com.natalinstanislav.restaurants.service.VoteService;
import com.natalinstanislav.restaurants.to.RestaurantTo;
import com.natalinstanislav.restaurants.util.RestaurantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;

public abstract class AbstractRestaurantController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantService restaurantService;

    private final VoteService voteService;

    public AbstractRestaurantController(RestaurantService restaurantService, VoteService voteService) {
        this.restaurantService = restaurantService;
        this.voteService = voteService;
    }

    public Restaurant get(int id) {
        log.info("get restaurant with id {}", id);
        return restaurantService.get(id);
    }

    public Restaurant getWithMenu(int id, LocalDate date) {
        log.info("getWithMenu restaurant with id {} with menu from date {}", id, date);
        return restaurantService.getWithMenu(id, date);
    }

    public RestaurantTo getWithMenuAndRating(int id, LocalDate date) {
        log.info("getWithMenuAndRating restaurant with id {} with menu from date {}", id, date);
        Restaurant restaurant = restaurantService.getWithMenu(id, date);
        long rating = voteService.getAllByDateForRestaurant(date, id).size();
        return RestaurantUtil.createTo(restaurant, rating);
    }

    public void delete(int id) {
        log.info("delete restaurant with id {}", id);
        restaurantService.delete(id);
    }

    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

    public List<Restaurant> getAllWithMenu(LocalDate date) {
        log.info("getAllWithMenu from date {}", date);
        return restaurantService.getAllWithMenu(date);
    }

    public List<RestaurantTo> getAllWithMenuAndRating(LocalDate date) {
        log.info("getAllWithMenuAndRating from date {}", date);
        List<Restaurant> restaurants = restaurantService.getAllWithMenu(date);
        Map<Restaurant, Long> restaurantRatingMap = RestaurantUtil.getRestaurantRatingMap(voteService.getAllByDate(date));
        return RestaurantUtil.getTos(restaurants, restaurantRatingMap);
    }

    public ResponseEntity<Restaurant> create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/restaurants" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    public void update(Restaurant restaurant, int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }
}
