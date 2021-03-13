package com.natalinstanislav.restaurants.repository.user;

import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User> {

    User getByEmail(String email);

    User getWithVotes(int id);
}
