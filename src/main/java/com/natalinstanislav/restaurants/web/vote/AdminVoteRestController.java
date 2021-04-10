package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.service.VoteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "/admin/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteRestController extends AbstractVoteController {

    public AdminVoteRestController(VoteService voteService) {
        super(voteService);
    }

    @GetMapping("/fromUser")
    public List<Vote> getAllFromUser(@RequestParam int userId) {
        log.info("getAllFromUser with id {}", userId);
        return voteService.getAllFromUser(userId);
    }

    @GetMapping("/byDate")
    public List<Vote> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate by date {}", date);
        return voteService.getAllByDate(date);
    }

    @GetMapping("/byDateForRestaurant")
    public List<Vote> getAllByDateForRestaurant(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam int restaurantId) {
        log.info("getAllByDateForRestaurant by date {} from restaurant with id {}", date, restaurantId);
        return voteService.getAllByDateForRestaurant(date, restaurantId);
    }
}
