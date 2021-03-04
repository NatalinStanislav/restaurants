package com.natalinstanislav.restaurants.web.user;

import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected UserRepository userRepository;

    public AdminRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info("get user with id {}", id);
        return checkNotFoundWithId(userRepository.get(id), id);
    }

    @GetMapping("/byEmail")
    public User getByMail(@RequestParam String email) {
        return userRepository.getByEmail(email);
    }

    @GetMapping("/{id}/withVotes")
    public User getWithVotes(@PathVariable int id) {
        log.info("getWithVotes user with id {}", id);
        return checkNotFoundWithId(userRepository.getWithVotes(id), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete user with id {}", id);
        checkNotFoundWithId(userRepository.delete(id), id);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("getAll");
        return userRepository.getAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user, @PathVariable int id) {
        log.info("update {} with id={}", user, id);
        Assert.notNull(user, "user must not be null");
        assureIdConsistent(user, id);
        checkNotFoundWithId(userRepository.save(user), id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User user) {
        log.info("create {}", user);
        Assert.notNull(user, "user must not be null");
        checkNew(user);
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/users" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}