package com.natalinstanislav.restaurants.web.user;

import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.user.UserRepository;
import com.natalinstanislav.restaurants.to.UserTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.natalinstanislav.restaurants.web.SecurityUtil.authUserId;

@RestController
@RequestMapping("/profile")
public class ProfileRestController extends AbstractUserController {

    public ProfileRestController(UserRepository userRepository) {
        super(userRepository);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return super.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        User created = super.create(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo) {
        super.update(userTo, authUserId());
    }

    @GetMapping("/withVotes")
    public User getWithVotes() {
        return super.getWithVotes(authUserId());
    }
}