package com.natalinstanislav.restaurants;

import com.natalinstanislav.restaurants.model.Dish;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.RestaurantTestData.*;
import static com.natalinstanislav.restaurants.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Dish.class, "restaurant");

    public static final int NOT_FOUND = 123456;
    public static final int MEXICAN_PIZZA_ID = START_SEQ + 9;
    public static final int ITALIAN_SALAD_ID = START_SEQ + 10;
    public static final int CAPPUCCINO_ID = START_SEQ + 11;
    public static final int PHILADELPHIA_ROLL_ID = START_SEQ + 12;
    public static final int FISH_SALAD_ID = START_SEQ + 13;
    public static final int GREEN_TEA_ID = START_SEQ + 14;
    public static final int KEBAB_XXL_ID = START_SEQ + 15;
    public static final int GARLIC_SAUCE_ID = START_SEQ + 16;
    public static final int BEER_ID = START_SEQ + 17;
    public static final int HOT_DOG_ID = START_SEQ + 18;
    public static final int CHIPS_ID = START_SEQ + 19;
    public static final int TEA_ID = START_SEQ + 20;
    public static final int TODAY_MEXICAN_PIZZA_ID = START_SEQ + 21;
    public static final int TODAY_ITALIAN_SALAD_ID = START_SEQ + 22;
    public static final int TODAY_CAPPUCCINO_ID = START_SEQ + 23;
    public static final int TODAY_PHILADELPHIA_ROLL_ID = START_SEQ + 24;
    public static final int TODAY_FISH_SALAD_ID = START_SEQ + 25;
    public static final int TODAY_GREEN_TEA_ID = START_SEQ + 26;
    public static final int TODAY_KEBAB_XXL_ID = START_SEQ + 27;
    public static final int TODAY_GARLIC_SAUCE_ID = START_SEQ + 28;
    public static final int TODAY_BEER_ID = START_SEQ + 29;

    public static final Dish MexicanPizza = new Dish(MEXICAN_PIZZA_ID, "Mexican pizza", 999, PizzaHut, LocalDate.of(2020, 1, 30));
    public static final Dish ItalianSalad = new Dish(ITALIAN_SALAD_ID, "Italian salad", 349, PizzaHut, LocalDate.of(2020, 1, 30));
    public static final Dish Cappuccino = new Dish(CAPPUCCINO_ID, "Cappuccino", 199, PizzaHut, LocalDate.of(2020, 1, 30));
    public static final Dish PhiladelphiaRoll = new Dish(PHILADELPHIA_ROLL_ID, "Philadelphia roll", 849, SushiRoll, LocalDate.of(2020, 1, 30));
    public static final Dish FishSalad = new Dish(FISH_SALAD_ID, "Fish salad", 249, SushiRoll, LocalDate.of(2020, 1, 30));
    public static final Dish GreenTea = new Dish(GREEN_TEA_ID, "Green tea", 99, SushiRoll, LocalDate.of(2020, 1, 30));
    public static final Dish KebabXXL = new Dish(KEBAB_XXL_ID, "Kebab XXL", 1249, KebabHouse, LocalDate.of(2020, 1, 30));
    public static final Dish GarlicSauce = new Dish(GARLIC_SAUCE_ID, "Garlic sauce", 99, KebabHouse, LocalDate.of(2020, 1, 30));
    public static final Dish Beer = new Dish(BEER_ID, "Beer", 399, KebabHouse, LocalDate.of(2020, 1, 30));
    public static final Dish HotDog = new Dish(HOT_DOG_ID, "HotDog", 500, PizzaHut, LocalDate.of(2020, 1, 31));
    public static final Dish Chips = new Dish(CHIPS_ID, "Chips", 299, PizzaHut, LocalDate.of(2020, 1, 31));
    public static final Dish Tea = new Dish(TEA_ID, "Tea", 99, PizzaHut, LocalDate.of(2020, 1, 31));
    public static final Dish TodayMexicanPizza = new Dish(TODAY_MEXICAN_PIZZA_ID, "Today Mexican pizza", 999, PizzaHut, LocalDate.now());
    public static final Dish TodayItalianSalad = new Dish(TODAY_ITALIAN_SALAD_ID, "Today Italian salad", 349, PizzaHut, LocalDate.now());
    public static final Dish TodayCappuccino = new Dish(TODAY_CAPPUCCINO_ID, "Today Cappuccino", 199, PizzaHut, LocalDate.now());
    public static final Dish TodayPhiladelphiaRoll = new Dish(TODAY_PHILADELPHIA_ROLL_ID, "Today Philadelphia roll", 849, SushiRoll, LocalDate.now());
    public static final Dish TodayFishSalad = new Dish(TODAY_FISH_SALAD_ID, "Today Fish salad", 249, SushiRoll, LocalDate.now());
    public static final Dish TodayGreenTea = new Dish(TODAY_GREEN_TEA_ID, "Today Green tea", 99, SushiRoll, LocalDate.now());
    public static final Dish TodayKebabXXL = new Dish(TODAY_KEBAB_XXL_ID, "Today Kebab XXL", 1249, KebabHouse, LocalDate.now());
    public static final Dish TodayGarlicSauce = new Dish(TODAY_GARLIC_SAUCE_ID, "Today Garlic sauce", 99, KebabHouse, LocalDate.now());
    public static final Dish TodayBeer = new Dish(TODAY_BEER_ID, "Today Beer", 399, KebabHouse, LocalDate.now());

    public static final Dish MexicanPizzaClone = new Dish(null, "Mexican pizza", 888, PizzaHut, LocalDate.of(2020, 1, 30));

    public static String ISO_30_OF_JANUARY = "2020-01-30";
    public static LocalDate LOCALDATE_30_OF_JANUARY = LocalDate.of(2020, 1, 30);

    public static List<Dish> ALL_DISHES = List.of(MexicanPizza, ItalianSalad, Cappuccino, PhiladelphiaRoll, FishSalad, GreenTea,
            KebabXXL, GarlicSauce, Beer, HotDog, Chips, Tea, TodayMexicanPizza, TodayItalianSalad, TodayCappuccino,
            TodayPhiladelphiaRoll, TodayFishSalad, TodayGreenTea, TodayKebabXXL, TodayGarlicSauce, TodayBeer);

    public static List<Dish> ALL_DISHES_FROM_30_OF_JANUARY = List.of(MexicanPizza, ItalianSalad, Cappuccino, PhiladelphiaRoll, FishSalad, GreenTea,
            KebabXXL, GarlicSauce, Beer);

    public static List<Dish> ALL_DISHES_FROM_30_OF_JANUARY_FROM_PIZZA_HUT = List.of(MexicanPizza, ItalianSalad, Cappuccino);

    public static List<Dish> ALL_DISHES_FROM_PIZZA_HUT_30_OF_JANUARY = List.of(MexicanPizza, ItalianSalad, Cappuccino);

    public static List<Dish> ALL_DISHES_FROM_SUSHI_ROLL_30_OF_JANUARY = List.of(PhiladelphiaRoll, FishSalad, GreenTea);

    public static List<Dish> ALL_DISHES_FROM_KEBAB_HOUSE_30_OF_JANUARY = List.of(KebabXXL, GarlicSauce, Beer);

    public static Dish getNew() {
        return new Dish(null, "NewDish", 444, LocalDate.of(2020, 1, 29));
    }

    public static Dish getUpdated() {
        return new Dish(MEXICAN_PIZZA_ID, MexicanPizza.getName(), 555, MexicanPizza.getRestaurant(), MexicanPizza.getDishDate());
    }
}
