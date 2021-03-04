package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.vote.VoteRepository;
import com.natalinstanislav.restaurants.util.TimeValidationUtil;
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
import java.time.LocalDateTime;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/admin/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteRepository voteRepository;

    public AdminVoteRestController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        log.info("get vote with id {}", id);
        return checkNotFoundWithId(voteRepository.get(id), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete vote with id {}", id);
        checkNotFoundWithId(voteRepository.delete(id), id);
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll");
        return voteRepository.getAll();
    }

    @GetMapping("/fromUser")
    public List<Vote> getAllFromUser(@RequestParam int userId) {
        log.info("getAllFromUser with id {}", userId);
        return voteRepository.getAllFromUser(userId);
    }

    @GetMapping("/byDate")
    public List<Vote> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate by date {}", date);
        return voteRepository.getAllByDate(date);
    }

    @GetMapping("/byDateForRestaurant")
    public List<Vote> getAllByDateForRestaurant(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam int restaurantId) {
        log.info("getAllByDateForRestaurant by date {} from restaurant with id {}", date, restaurantId);
        return voteRepository.getAllByDateForRestaurant(date, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> create(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                       @RequestParam int restaurantId, @RequestParam int userId) {
        log.info("create vote for restaurant with id {} by user with id {}", restaurantId, userId);
        LocalDate date = TimeValidationUtil.checkVoteTime(dateTime);
        Vote vote = voteRepository.getByDateAndUser(date, userId);
        if (vote == null) {
            vote = new Vote(null, date);
            Vote created = voteRepository.save(vote, restaurantId, userId);
            System.out.println(created);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/admin/votes" + "/{id}")
                    .buildAndExpand(created.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(created);
        } else {
            update(vote, vote.getId(), restaurantId, userId);
            return null;
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Vote vote, @PathVariable int id, @RequestParam int restaurantId, @RequestParam int userId) {
        log.info("update {} with id={}", vote, id);
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, id);
        checkNotFoundWithId(voteRepository.save(vote, restaurantId, userId), id);
    }
}
