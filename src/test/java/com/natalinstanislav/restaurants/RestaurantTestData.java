package com.natalinstanislav.restaurants;

import com.natalinstanislav.restaurants.model.Restaurant;
import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static com.natalinstanislav.restaurants.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static final int PIZZA_HUT_ID = START_SEQ + 6;
    public static final int SUSHI_ROLL_ID = START_SEQ + 7;
    public static final int KEBAB_HOUSE_ID = START_SEQ + 8;

    public static final Restaurant PizzaHut = new Restaurant(PIZZA_HUT_ID, "Pizza Hut");
    public static final Restaurant SushiRoll = new Restaurant(SUSHI_ROLL_ID, "Sushi Roll");
    public static final Restaurant KebabHouse = new Restaurant(KEBAB_HOUSE_ID, "Kebab House");

    public static Restaurant getNew() {
        return new Restaurant(null, "NewRestaurant");
    }

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("dishes").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("dishes").isEqualTo(expected);
    }

}
