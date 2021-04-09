package com.natalinstanislav.restaurants.web.restaurant;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.service.RestaurantService;
import com.natalinstanislav.restaurants.service.VoteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestaurantRestController extends AbstractRestaurantController{

    public ProfileRestaurantRestController(RestaurantService restaurantService, VoteService voteService) {
        super(restaurantService, voteService);
    }

    @GetMapping("/withMenu")
    public List<Restaurant> getAllWithMenu(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getAllWithMenu(date);
    }
}
