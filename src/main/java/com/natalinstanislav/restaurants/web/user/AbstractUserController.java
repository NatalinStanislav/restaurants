package com.natalinstanislav.restaurants.web.user;

import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.service.UserService;
import com.natalinstanislav.restaurants.to.UserTo;
import com.natalinstanislav.restaurants.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.*;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final UserService userService;

    public AbstractUserController(UserService userService) {
        this.userService = userService;
    }

    public List<User> getAll() {
        log.info("getAll");
        return userService.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return userService.get(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        userService.delete(id);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return userService.getByEmail(email);
    }

    public User getWithVotes(int id) {
        log.info("getWithVotes {}", id);
        return userService.getWithVotes(id);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        userService.enable(id, enabled);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return userService.create(user);
    }

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        userService.update(userTo, id);
    }
}
