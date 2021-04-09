package com.natalinstanislav.restaurants.util;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.to.RestaurantTo;

import java.util.*;
import java.util.stream.Collectors;

public class RestaurantUtil {

    private RestaurantUtil() {
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants, Map<Restaurant, Long> restaurantRatingMap) {
        List<RestaurantTo> list = new ArrayList<>(restaurants.size());
        for (Restaurant r : restaurants) {
            list.add(new RestaurantTo(r.getId(), r.getName(), r.getDishes(), restaurantRatingMap.get(r)));
        }
        return list;
    }

    public static RestaurantTo createTo(Restaurant restaurant, Long rating) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getDishes(), rating);
    }

    public static Map<Restaurant, Long> getRestaurantRatingMap(List<Vote> votes) {
        return votes.stream().collect(Collectors.groupingBy(Vote::getRestaurant, Collectors.counting()));
    }
}
