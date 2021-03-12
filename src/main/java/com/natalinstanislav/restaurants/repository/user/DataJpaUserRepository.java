package com.natalinstanislav.restaurants.repository.user;

import com.natalinstanislav.restaurants.AuthorizedUser;
import com.natalinstanislav.restaurants.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.natalinstanislav.restaurants.util.UserUtil.prepareToSave;

@Repository("userRepository")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)

public class DataJpaUserRepository implements UserRepository, UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataJpaUserRepository(JpaUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User get(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    @Cacheable("users")
    public List<User> getAll() {
        return userRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User save(User user) {
        return userRepository.save(prepareToSave(user, passwordEncoder));
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public boolean delete(int id) {
        return userRepository.delete(id) != 0;
    }

    @Override
    public User getWithVotes(int id) {
        return userRepository.getWithVotes(id);
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
