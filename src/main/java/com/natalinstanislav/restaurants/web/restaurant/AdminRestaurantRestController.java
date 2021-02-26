package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.repository.restaurant.RestaurantRepository;
import com.natalinstanislav.restaurants.repository.vote.VoteRepository;
import com.natalinstanislav.restaurants.to.RestaurantTo;
import com.natalinstanislav.restaurants.util.RestaurantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/admin/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant with id {}", id);
        return checkNotFoundWithId(restaurantRepository.get(id), id);
    }

    @GetMapping("/{id}/withMenu")
    public Restaurant getWithMenu(@PathVariable int id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getWithMenu restaurant with id {} with menu from date {}", id, date);
        return restaurantRepository.getWithMenu(id, date);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id {}", id);
        checkNotFoundWithId(restaurantRepository.delete(id), id);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.getAll();
    }

    @GetMapping("/withMenu")
    public List<Restaurant> getAllWithMenu(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllWithMenu from date {}", date);
        return restaurantRepository.getAllWithMenu(date);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/restaurants" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        Assert.notNull(restaurant, "restaurant must not be null");
        assureIdConsistent(restaurant, id);
        checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.getId());
    }

    @GetMapping("/{id}/withMenuAndRating")
    public RestaurantTo getWithMenuAndRating(@PathVariable int id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getWithMenuAndRating restaurant with id {} with menu from date {}", id, date);
        Restaurant restaurant = restaurantRepository.getWithMenu(id, date);
        int rating = voteRepository.get(666).getId(); //bullshit!!!!!!!!!
        return RestaurantUtil.createTo(restaurant, rating);
    }

    @GetMapping("/withMenuAndRating")
    public List<RestaurantTo> getAllWithMenuAndRating(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllWithMenuAndRating from date {}", date);
        List<Restaurant> restaurants = restaurantRepository.getAllWithMenu(date);
        Map<Restaurant,Integer> restaurantRatingMap = null;
//        restaurantRatingMap = voteRepository.getMap(date);
        return RestaurantUtil.getTos(restaurants, restaurantRatingMap);
    }


}