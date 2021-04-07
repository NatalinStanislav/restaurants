package com.natalinstanislav.restaurants.web.dish;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/admin/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final DishService dishService;

    public AdminDishRestController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@RequestBody @Valid Dish dish, @RequestParam int restaurantId) {
        log.info("create {} for restaurant with id {}", dish, restaurantId);
        Assert.notNull(dish, "dish must not be null");
        checkNew(dish);
        Dish created = dishService.save(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/dishes" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete dish with id {}", id);
        checkNotFoundWithId(dishService.delete(id), id);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("get dish with id {}", id);
        return checkNotFoundWithId(dishService.get(id), id);
    }

    @GetMapping
    public List<Dish> getAll() {
        log.info("getAll");
        return dishService.getAll();
    }

    @GetMapping("/fromRestaurantByDate")
    public List<Dish> getAllFromRestaurantByDate(@RequestParam int restaurantId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllFromRestaurantByDate from restaurantID {} by date {}", restaurantId, date);
        return dishService.getAllFromRestaurantByDate(restaurantId, date);
    }

    @GetMapping("/byDate")
    public List<Dish> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate by date {}", date);
        return dishService.getAllByDate(date);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Dish dish, @PathVariable int id, @RequestParam int restaurantId) {
        log.info("update dish {} with id={} for restaurant with id {}", dish, id, restaurantId);
        Assert.notNull(dish, "dish must not be null");
        assureIdConsistent(dish, id);
        checkNotFoundWithId(dishService.save(dish, restaurantId), dish.getId());
    }
}
