package com.natalinstanislav.restaurants.util;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.to.RestaurantTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RestaurantUtil {
    private RestaurantUtil() {
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants, Map<Restaurant,Integer> restaurantRatingMap) {
        List<RestaurantTo> list = new ArrayList<>(restaurants.size());
        for(Restaurant r: restaurants) {
            list.add(new RestaurantTo(r.getId(), r.getName(), r.getDishes(), restaurantRatingMap.get(r)));
        }
        return list;
    }

    public static RestaurantTo createTo(Restaurant restaurant, int rating) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getDishes(), rating);
    }
}
