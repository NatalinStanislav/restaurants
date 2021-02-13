package com.natalinstanislav.restaurants;

import com.natalinstanislav.restaurants.model.Vote;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.natalinstanislav.restaurants.RestaurantTestData.*;
import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestData {
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

    public static Vote getNew() {
        return new Vote(null, PizzaHut, LocalDate.of(2020, 1, 29), user4);
    }

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("restaurant", "user").isEqualTo(expected);

    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant", "user").isEqualTo(expected);
    }

}
