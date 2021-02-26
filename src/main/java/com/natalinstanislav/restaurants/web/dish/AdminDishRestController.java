package com.natalinstanislav.restaurants.web.dish;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.repository.dish.DishRepository;
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

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/admin/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected DishRepository repository;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@RequestBody Dish dish) {
        log.info("create {}", dish);
        Assert.notNull(dish, "dish must not be null");
        checkNew(dish);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/dishes" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete dish with id {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("get dish with id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    @GetMapping
    public List<Dish> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping("/fromRestaurantByDate")
    public List<Dish> getAllFromRestaurantByDate(@RequestParam int restaurantId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllFromRestaurantByDate from restaurantID {} by date {}", restaurantId, date);
        return repository.getAllFromRestaurantByDate(restaurantId, date);
    }

    @GetMapping("/byDate")
    public List<Dish> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate by date {}", date);
        return repository.getAllByDate(date);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int id) {
        log.info("update dish {} with id={}", dish, id);
        Assert.notNull(dish, "dish must not be null");
        assureIdConsistent(dish, id);
        checkNotFoundWithId(repository.save(dish), dish.getId());
    }
}
