package com.natalinstanislav.restaurants.repository.user;

import com.natalinstanislav.restaurants.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

import static com.natalinstanislav.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaUserRepository implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final JpaUserRepository userRepository;

    public DataJpaUserRepository(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User get(int id) {
        return checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
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
        Assert.notNull(user, "user must not be null");
        return userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(userRepository.delete(id) != 0, id);
    }

    @Override
    public User getWithVotes(int id) {
        return userRepository.getWithVotes(id);
    }
}
