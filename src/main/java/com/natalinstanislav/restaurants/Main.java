package com.natalinstanislav.restaurants;

import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.restaurant.RestaurantRepository;
import com.natalinstanislav.restaurants.repository.user.UserRepository;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml")) {

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            RestaurantRepository repository = appCtx.getBean(RestaurantRepository.class);
            System.out.println(repository.get(100006));

            UserRepository userRepository = appCtx.getBean(UserRepository.class);
            System.out.println(userRepository.get(100005));

            User user = new User(null,"www", "www@mail.ru", "qwerty", Role.ADMIN);
            userRepository.save(user);

            System.out.println(userRepository.get(100030));

            User nextUser = userRepository.get(100005);
//            System.out.println(nextUser.getVotes());

        }
        }
}
