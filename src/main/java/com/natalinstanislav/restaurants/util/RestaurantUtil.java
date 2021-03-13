package com.natalinstanislav.restaurants.util;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.model.Vote;
import com.natalinstanislav.restaurants.to.RestaurantTo;

import java.util.*;

public class RestaurantUtil {

    private RestaurantUtil() {
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants, Map<Restaurant, Integer> restaurantRatingMap) {
        List<RestaurantTo> list = new ArrayList<>(restaurants.size());
        for (Restaurant r : restaurants) {
            list.add(new RestaurantTo(r.getId(), r.getName(), r.getDishes(), restaurantRatingMap.get(r)));
        }
        return list;
    }

    public static RestaurantTo createTo(Restaurant restaurant, int rating) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getDishes(), rating);
    }

    public static Map<Restaurant, Integer> getRestaurantRatingMap(List<Vote> votes) {
        Map<Restaurant, Integer> restaurantRatingMap = new HashMap<>();
        for (Vote vote : votes) {
            if (restaurantRatingMap.containsKey(vote.getRestaurant())) {
                restaurantRatingMap.put(vote.getRestaurant(), restaurantRatingMap.get(vote.getRestaurant()) + 1);
            } else {
                restaurantRatingMap.put(vote.getRestaurant(), 1);
            }
        }
        return restaurantRatingMap;
    }
}
