package com.natalinstanislav.restaurants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"dish_date", "restaurant_id", "name"}, name = "dishes_unique_date_restaurant_name_idx")})
public class Dish extends AbstractNamedEntity {
    @Column(name = "cost", nullable = false)
    @NotNull
    @Range(min = 1, max = 1000000)
    private Integer cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
//    @NotNull
    @JsonBackReference
    private Restaurant restaurant;

    @Column(name = "dish_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate dishDate;

    public Dish() {
    }

    public Dish(Integer id, String name, Integer cost, LocalDate dishDate) {
        super(id, name);
        this.cost = cost;
        this.dishDate = dishDate;
    }

    public Dish(Integer id, String name, Integer cost, Restaurant restaurant, LocalDate dishDate) {
        super(id, name);
        this.cost = cost;
        this.restaurant = restaurant;
        this.dishDate = dishDate;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDishDate() {
        return dishDate;
    }

    public void setDishDate(LocalDate date) {
        this.dishDate = date;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "cost=" + cost +
                ", date=" + dishDate +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
