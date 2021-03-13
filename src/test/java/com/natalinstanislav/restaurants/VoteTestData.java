package com.natalinstanislav.restaurants;

import com.natalinstanislav.restaurants.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static com.natalinstanislav.restaurants.RestaurantTestData.*;
import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Vote.class, "restaurant", "user");

    public static final int NOT_FOUND = 123456;
    public static final int VOTE_USER0_30_OF_JANUARY_ID = START_SEQ + 21;
    public static final int VOTE_USER1_30_OF_JANUARY_ID = START_SEQ + 22;
    public static final int VOTE_USER2_30_OF_JANUARY_ID = START_SEQ + 23;
    public static final int VOTE_USER3_30_OF_JANUARY_ID = START_SEQ + 24;
    public static final int VOTE_USER4_30_OF_JANUARY_ID = START_SEQ + 25;
    public static final int VOTE_ADMIN_30_OF_JANUARY_ID = START_SEQ + 26;
    public static final int VOTE_USER0_31_OF_JANUARY_ID = START_SEQ + 27;
    public static final int VOTE_USER3_31_OF_JANUARY_ID = START_SEQ + 28;
    public static final int VOTE_ADMIN_31_OF_JANUARY_ID = START_SEQ + 29;

    public static final Vote VoteUser0January30 = new Vote(VOTE_USER0_30_OF_JANUARY_ID, PizzaHut, LocalDate.of(2020, 1, 30), user0);
    public static final Vote VoteUser1January30 = new Vote(VOTE_USER1_30_OF_JANUARY_ID, KebabHouse, LocalDate.of(2020, 1, 30), user1);
    public static final Vote VoteUser2January30 = new Vote(VOTE_USER2_30_OF_JANUARY_ID, KebabHouse, LocalDate.of(2020, 1, 30), user2);
    public static final Vote VoteUser3January30 = new Vote(VOTE_USER3_30_OF_JANUARY_ID, PizzaHut, LocalDate.of(2020, 1, 30), user3);
    public static final Vote VoteUser4January30 = new Vote(VOTE_USER4_30_OF_JANUARY_ID, PizzaHut, LocalDate.of(2020, 1, 30), user4);
    public static final Vote VoteAdminJanuary30 = new Vote(VOTE_ADMIN_30_OF_JANUARY_ID, SushiRoll, LocalDate.of(2020, 1, 30), admin);
    public static final Vote VoteUser0January31 = new Vote(VOTE_USER0_31_OF_JANUARY_ID, PizzaHut, LocalDate.of(2020, 1, 31), user0);
    public static final Vote VoteUser3January31 = new Vote(VOTE_USER3_31_OF_JANUARY_ID, PizzaHut, LocalDate.of(2020, 1, 31), user3);
    public static final Vote VoteAdminJanuary31 = new Vote(VOTE_ADMIN_31_OF_JANUARY_ID, PizzaHut, LocalDate.of(2020, 1, 31), admin);

    public static List<Vote> ALL_VOTES = List.of(VoteUser0January30, VoteUser1January30, VoteUser2January30, VoteUser3January30,
            VoteUser4January30, VoteAdminJanuary30, VoteUser0January31, VoteUser3January31, VoteAdminJanuary31);

    public static List<Vote> ALL_VOTES_FROM_USER3 = List.of(VoteUser3January30, VoteUser3January31);

    public static List<Vote> ALL_VOTES_FROM_30_OF_JANUARY = List.of(VoteUser0January30, VoteUser1January30, VoteUser2January30,
            VoteUser3January30, VoteUser4January30, VoteAdminJanuary30);

    public static List<Vote> ALL_VOTES_FROM_30_OF_JANUARY_FOR_PIZZA_HUT = List.of(VoteUser0January30, VoteUser3January30, VoteUser4January30);

    public static LocalDate LOCAL_DATE_30_OF_JANUARY = LocalDate.of(2020, 1, 30);

    public static String ISO_30_OF_JANUARY = "2020-01-30";

    public static String ISO_29_OF_JANUARY_TIME = "2020-01-29T10:20:30";

    public static Vote getNew() {
        return new Vote(null, LocalDate.of(2020, 1, 29));
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_USER3_30_OF_JANUARY_ID, SushiRoll, LocalDate.of(2020, 1, 29), user3);
    }
}
