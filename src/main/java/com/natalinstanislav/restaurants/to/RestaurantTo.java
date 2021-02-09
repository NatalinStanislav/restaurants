package com.natalinstanislav.restaurants.to;

import com.natalinstanislav.restaurants.model.Dish;

import java.util.List;

public class RestaurantTo {

    private Integer id;

    private String name;

    private List<Dish> dishes;

    private Integer rating;

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
}
