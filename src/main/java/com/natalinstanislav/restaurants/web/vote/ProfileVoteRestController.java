package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.repository.vote.VoteRepository;
import com.natalinstanislav.restaurants.util.TimeValidationUtil;
import com.natalinstanislav.restaurants.web.SecurityUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/profile/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteRestController extends AbstractVoteController {

    public ProfileVoteRestController(VoteRepository voteRepository) {
        super(voteRepository);
    }

    @Override
    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        return super.get(id, SecurityUtil.authUserId());
    }

    @Override
    @GetMapping
    public List<Vote> getAll() {
        return super.getAllFromUser(SecurityUtil.authUserId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                                   @RequestParam int restaurantId) {
        TimeValidationUtil.checkVoteDate(dateTime);
        Vote vote = super.create(dateTime, restaurantId, SecurityUtil.authUserId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile/votes" + "/{id}")
                .buildAndExpand(vote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }
}
