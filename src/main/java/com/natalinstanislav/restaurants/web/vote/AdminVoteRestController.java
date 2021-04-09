package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.service.VoteService;
import com.natalinstanislav.restaurants.web.SecurityUtil;
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


@RestController
@RequestMapping(value = "/admin/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteRestController extends AbstractVoteController {

    public AdminVoteRestController(VoteService voteService) {
        super(voteService);
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping("/today")
    public Vote getToday() {
        return super.getToday();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @GetMapping
    public List<Vote> getAll() {
        return super.getAll();
    }

    @GetMapping("/fromUser")
    public List<Vote> getAllFromUser(@RequestParam int userId) {
        return super.getAllFromUser(userId);
    }

    @GetMapping("/byDate")
    public List<Vote> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getAllByDate(date);
    }

    @GetMapping("/byDateForRestaurant")
    public List<Vote> getAllByDateForRestaurant(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam int restaurantId) {
        return super.getAllByDateForRestaurant(date, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@RequestParam int restaurantId) {
        Vote vote = super.create(restaurantId, SecurityUtil.authUserId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/votes" + "/{id}")
                .buildAndExpand(vote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Vote update(@RequestBody @Valid Vote vote, @PathVariable int id, @RequestParam int restaurantId) {
        return super.update(vote, id, restaurantId, SecurityUtil.authUserId());
    }
}
