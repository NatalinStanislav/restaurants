package com.natalinstanislav.restaurants.repository.user;

import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.Repository;

public interface UserRepository extends Repository<User> {

    User getByEmail(String email);

    User getWithVotes(int id);
}
