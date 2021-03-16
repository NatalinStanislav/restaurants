package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.vote.VoteRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping(value = "/admin/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteRestController extends AbstractVoteController {

    public AdminVoteRestController(VoteRepository voteRepository) {
        super(voteRepository);
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        return super.get(id);
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
    public ResponseEntity<Vote> createWithLocation(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                       @RequestParam int restaurantId, @RequestParam int userId) {
        Vote vote = super.create(dateTime, restaurantId, userId);
        if (vote != null) {
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/admin/votes" + "/{id}")
                    .buildAndExpand(vote.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(vote);
        } else {
            update(vote, vote.getId(), restaurantId, userId);
            return null;
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Vote update(@RequestBody @Valid Vote vote, @PathVariable int id, @RequestParam int restaurantId, @RequestParam int userId) {
        return super.update(vote, id, restaurantId, userId);
    }
}
