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
import java.util.List;

@RestController
@RequestMapping(value = "/profile/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteRestController extends AbstractVoteController {

    public ProfileVoteRestController(VoteService voteService) {
        super(voteService);
    }

    @Override
    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        return super.get(id, SecurityUtil.authUserId());
    }

    @GetMapping("/today")
    public Vote getToday() {
        return super.getToday();
    }

    @Override
    @GetMapping
    public List<Vote> getAll() {
        return super.getAllFromUser(SecurityUtil.authUserId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@RequestParam int restaurantId) {
        Vote vote = super.create(restaurantId, SecurityUtil.authUserId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile/votes" + "/{id}")
                .buildAndExpand(vote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Vote update(@RequestBody @Valid Vote vote, @PathVariable int id, @RequestParam int restaurantId) {
        return super.update(vote, id, restaurantId, SecurityUtil.authUserId());
    }
}
