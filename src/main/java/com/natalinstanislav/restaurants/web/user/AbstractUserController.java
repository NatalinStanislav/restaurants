package com.natalinstanislav.restaurants.web.user;

import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.user.UserRepository;
import com.natalinstanislav.restaurants.to.UserTo;
import com.natalinstanislav.restaurants.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected UserRepository userRepository;

    public AbstractUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        log.info("getAll");
        return userRepository.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(userRepository.get(id), id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(userRepository.delete(id), id);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(userRepository.getByEmail(email), "email=" + email);
    }

    public User getWithVotes(int id) {
        log.info("getWithVotes {}", id);
        return checkNotFoundWithId(userRepository.getWithVotes(id), id);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = get(id);
        user.setEnabled(enabled);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        Assert.notNull(user, "user must not be null");
        return userRepository.save(user);
    }

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        Assert.notNull(user, "user must not be null");
        assureIdConsistent(user, id);
        checkNotFoundWithId(userRepository.save(user), id);
    }

    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        User user = get(id);
        userRepository.save(UserUtil.updateFromTo(user, userTo));
    }
}
