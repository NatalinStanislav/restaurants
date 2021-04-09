package com.natalinstanislav.restaurants.service;

import com.natalinstanislav.restaurants.AuthorizedUser;
import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.JpaUserRepository;
import com.natalinstanislav.restaurants.to.UserTo;
import com.natalinstanislav.restaurants.util.UserUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.natalinstanislav.restaurants.util.UserUtil.prepareToSave;
import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFound;
import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(JpaUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User get(int id) {
        return checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(userRepository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return userRepository.findAll(SORT_NAME_EMAIL);
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return userRepository.save(prepareToSave(user, passwordEncoder));
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        userRepository.save(prepareToSave(user, passwordEncoder));
    }

    @Transactional
    public void update(UserTo userTo, int id) {
        User user = get(id);
        userRepository.save(prepareToSave((UserUtil.updateFromTo(user, userTo)), passwordEncoder));
    }

    public void delete(int id) {
        checkNotFoundWithId(userRepository.delete(id) != 0, id);
    }

    public User getWithVotes(int id) {
        return checkNotFoundWithId(userRepository.getWithVotes(id), id);
    }

    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
