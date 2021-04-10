package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.service.VoteService;
import com.natalinstanislav.restaurants.web.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.natalinstanislav.restaurants.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = "/profile/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteRestController extends AbstractVoteController {

    public ProfileVoteRestController(VoteService voteService) {
        super(voteService);
    }

    @GetMapping("/today")
    public Vote getToday() {
        int userId = SecurityUtil.authUserId();
        log.info("get today's vote for user with id {}", userId);
        return voteService.getToday(userId);
    }

    @DeleteMapping("/today")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteToday() {
        int userId = SecurityUtil.authUserId();
        log.info("delete today's vote for user with id {}", userId);
        voteService.deleteToday(userId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@RequestParam int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("create vote for restaurant with id {} by user with id {}", restaurantId, userId);
        Vote vote = voteService.create(restaurantId, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile/votes" + "/{id}")
                .buildAndExpand(vote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Vote update(@RequestBody @Valid Vote vote, @PathVariable int id, @RequestParam int restaurantId) {
        log.info("update {} with id={}", vote, id);
        assureIdConsistent(vote, id);
        return voteService.update(vote, id, restaurantId, SecurityUtil.authUserId());
    }
}
