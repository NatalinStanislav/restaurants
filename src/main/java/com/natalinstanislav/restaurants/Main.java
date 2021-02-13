package com.natalinstanislav.restaurants;

import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.restaurant.DataJpaRestaurantRepository;
import com.natalinstanislav.restaurants.repository.user.DataJpaUserRepository;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * @see <a href="http://topjava.herokuapp.com">Demo application</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml")) {

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            DataJpaRestaurantRepository repository = appCtx.getBean(DataJpaRestaurantRepository.class);
            System.out.println(repository.get(100006));

            DataJpaUserRepository repositoryUser = appCtx.getBean(DataJpaUserRepository.class);
            System.out.println(repositoryUser.get(100005));

            User user = new User(100024,"www", "www@mail.ru", "qwerty", Role.ADMIN);
            repositoryUser.save(user);

            System.out.println(repositoryUser.get(100024));

            User nextUser = repositoryUser.get(100005);
            System.out.println(nextUser.getVotes());

        }

        }
}
