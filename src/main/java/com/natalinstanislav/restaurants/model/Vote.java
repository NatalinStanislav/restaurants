package com.natalinstanislav.restaurants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"vote_date", "user_id"}, name = "votes_unique_date_user_idx")})
public class Vote extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
//    @NotNull
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "vote_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate voteDate;

    public Vote() {
    }

    public Vote(Integer id, LocalDate voteDate) {
        super(id);
        this.voteDate = voteDate;
    }

    public Vote(Restaurant restaurant, LocalDate voteDate, User user) {
        this(null, restaurant, voteDate, user);
    }

    public Vote(Integer id, Restaurant restaurant, LocalDate voteDate, User user) {
        super(id);
        this.restaurant = restaurant;
        this.voteDate = voteDate;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public LocalDate getDateTime() {
        return voteDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setDateTime(LocalDate date) {
        this.voteDate = date;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "restaurant=" + restaurant +
                ", date=" + voteDate +
                ", id=" + id +
                '}';
    }
}
