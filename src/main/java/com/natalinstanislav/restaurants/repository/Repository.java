package com.natalinstanislav.restaurants.repository;

import java.util.List;

public interface Repository<T> {
    T save(T t);

    boolean delete(int id);

    T get(int id);

    List<T> getAll();
}
