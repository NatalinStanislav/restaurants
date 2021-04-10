package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.service.RestaurantService;
import com.natalinstanislav.restaurants.service.VoteService;
import com.natalinstanislav.restaurants.to.RestaurantTo;
import com.natalinstanislav.restaurants.util.RestaurantUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.natalinstanislav.restaurants.util.ValidationUtil.assureIdConsistent;
import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = "/admin/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController extends AbstractRestaurantController {
    private final VoteService voteService;

    public AdminRestaurantRestController(RestaurantService restaurantService, VoteService voteService) {
        super(restaurantService);
        this.voteService = voteService;
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant with id {}", id);
        return restaurantService.get(id);
    }

    @GetMapping("/{id}/withMenu")
    public Restaurant getWithMenu(@PathVariable int id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getWithMenu restaurant with id {} with menu from date {}", id, date);
        return restaurantService.getWithMenu(id, date);
    }

    @GetMapping("/{id}/withMenuAndRating")
    public RestaurantTo getWithMenuAndRating(@PathVariable int id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getWithMenuAndRating restaurant with id {} with menu from date {}", id, date);
        Restaurant restaurant = restaurantService.getWithMenu(id, date);
        long rating = voteService.getAllByDateForRestaurant(date, id).size();
        return RestaurantUtil.createTo(restaurant, rating);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id {}", id);
        restaurantService.delete(id);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

    @GetMapping("/withMenu")
    public List<Restaurant> getAllWithMenu(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getAllWithMenu(date);
    }

    @GetMapping("/withMenuAndRating")
    public List<RestaurantTo> getAllWithMenuAndRating(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllWithMenuAndRating from date {}", date);
        List<Restaurant> restaurants = restaurantService.getAllWithMenu(date);
        Map<Restaurant, Long> restaurantRatingMap = RestaurantUtil.getRestaurantRatingMap(voteService.getAllByDate(date));
        return RestaurantUtil.getTos(restaurants, restaurantRatingMap);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody @Valid Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/restaurants" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }
}