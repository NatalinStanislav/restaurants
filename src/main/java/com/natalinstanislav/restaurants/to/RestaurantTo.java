package com.natalinstanislav.restaurants.to;

import com.natalinstanislav.restaurants.model.Dish;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Objects;

public class RestaurantTo {

    private final Integer id;

    private final String name;

    private final List<Dish> dishes;

    private final Integer rating;

    @ConstructorProperties({"id", "name", "dishes", "rating"})
    public RestaurantTo(Integer id, String name, List<Dish> dishes, Integer rating) {
        this.id = id;
        this.name = name;
        this. dishes = dishes;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishes=" + dishes +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTo that = (RestaurantTo) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                dishes.equals(that.dishes) &&
                rating.equals(that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishes, rating);
    }
}
