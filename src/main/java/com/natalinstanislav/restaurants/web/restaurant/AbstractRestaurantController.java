package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final RestaurantService restaurantService;

    public AbstractRestaurantController(RestaurantService restaurantService/*, VoteService voteService*/) {
        this.restaurantService = restaurantService;
    }

    public List<Restaurant> getAllWithMenu(LocalDate date) {
        log.info("getAllWithMenu from date {}", date);
        return restaurantService.getAllWithMenu(date);
    }
}
