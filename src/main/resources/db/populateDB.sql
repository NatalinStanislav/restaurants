DELETE FROM user_roles;
DELETE FROM restaurants;
DELETE FROM dishes;
DELETE FROM votes;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User0', 'user0@yandex.ru', '{noop}password0'),
       ('User1', 'user1@yandex.ru', '{noop}password1'),
       ('User2', 'user2@yandex.ru', '{noop}password2'),
       ('User3', 'user3@yandex.ru', '{noop}password3'),
       ('User4', 'user4@yandex.ru', '{noop}password4'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('USER', 100003),
       ('USER', 100004),
       ('ADMIN', 100005),
       ('USER', 100005);

INSERT INTO restaurants (name)
VALUES ('Pizza Hut'),
       ('Sushi Roll'),
       ('Kebab House');

INSERT INTO dishes (name, cost, dish_date, restaurant_id)
VALUES ('Mexican pizza', 999, '2020-01-30', 100006),
       ('Italian salad', 349, '2020-01-30', 100006),
       ('Cappuccino', 199, '2020-01-30', 100006),
       ('Philadelphia roll', 849, '2020-01-30', 100007),
       ('Fish salad', 249, '2020-01-30', 100007),
       ('Green tea', 99, '2020-01-30', 100007),
       ('Kebab XXL', 1249, '2020-01-30', 100008),
       ('Garlic sauce', 99, '2020-01-30', 100008),
       ('Beer', 399, '2020-01-30', 100008),
       ('HotDog', 500, '2020-01-31', 100006),
       ('Chips', 299, '2020-01-31', 100006),
       ('Tea', 99, '2020-01-31', 100006);


INSERT INTO votes (vote_date, user_id, restaurant_id)
VALUES ('2020-01-30', 100000, 100006),
       ('2020-01-30', 100001, 100008),
       ('2020-01-30', 100002, 100008),
       ('2020-01-30', 100003, 100006),
       ('2020-01-30', 100004, 100006),
       ('2020-01-30', 100005, 100007),
       ('2020-01-31', 100000, 100006),
       ('2020-01-31', 100003, 100006),
       ('2020-01-31', 100005, 100006);
