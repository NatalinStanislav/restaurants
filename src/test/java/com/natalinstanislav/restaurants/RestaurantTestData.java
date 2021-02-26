package com.natalinstanislav.restaurants;

import com.natalinstanislav.restaurants.model.Dish;
import com.natalinstanislav.restaurants.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Restaurant.class, "dishes");
    public static TestMatcher<Restaurant> RESTAURANT_WITH_MENU_MATCHER =
            TestMatcher.usingAssertions(Restaurant.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes.restaurant").ignoringAllOverriddenEquals().isEqualTo(e),
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes.restaurant").ignoringAllOverriddenEquals().isEqualTo(e));

    public static final int NOT_FOUND = 123456;
    public static final int PIZZA_HUT_ID = START_SEQ + 6;
    public static final int SUSHI_ROLL_ID = START_SEQ + 7;
    public static final int KEBAB_HOUSE_ID = START_SEQ + 8;

    public static final Restaurant PizzaHut = new Restaurant(PIZZA_HUT_ID, "Pizza Hut");
    public static final Restaurant SushiRoll = new Restaurant(SUSHI_ROLL_ID, "Sushi Roll");
    public static final Restaurant KebabHouse = new Restaurant(KEBAB_HOUSE_ID, "Kebab House");

    public static List<Restaurant> ALL_RESTAURANTS = List.of(KebabHouse, PizzaHut, SushiRoll);


    public static Restaurant getNew() {
        return new Restaurant(null, "NewRestaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(PIZZA_HUT_ID, "NEW Pizza Hut");
    }


}
