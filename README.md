REST API application for voting system for deciding where to have lunch.
------------------------------------------------------------------------

This application implements REST API using Hibernate/Spring/SpringMVC/Spring DATA-JPA technologies.
Security is provided by Spring Security and based on user roles.
The application provides the following requirements:
- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
- If user votes again the same day:
    - If it is before 11:00 it's asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed
        
### REST API for USER:
#### Get list of Restaurants with menu
**Request**  
GET http://localhost:8080/restaurants/restaurants/withMenu?date=2020-01-30  
curl "http://localhost:8080/restaurants/restaurants/withMenu?date=2020-01-30" --user user3@yandex.ru:password3  

**Response**  
Request Method: GET  
Status Code: 200   
Content-Type: application/json  

[{"id":100006,"name":"Pizza Hut","dishes":[{"id":100009,"name":"Mexican pizza","cost":999,"date":"2020-01-30"},{"id":100010,"name":"Italian salad","cost":349,"date":"2020-01-30"},{"id":100011,"name":"Cappuccino","cost":199,"date":"2020-01-30"}]},  
{"id":100007,"name":"Sushi Roll","dishes":[{"id":100012,"name":"Philadelphia roll","cost":849,"date":"2020-01-30"},{"id":100013,"name":"Fish salad","cost":249,"date":"2020-01-30"},{"id":100014,"name":"Green tea","cost":99,"date":"2020-01-30"}]},  
{"id":100008,"name":"Kebab House","dishes":[{"id":100015,"name":"Kebab XXL","cost":1249,"date":"2020-01-30"},{"id":100016,"name":"Garlic sauce","cost":99,"date":"2020-01-30"},{"id":100017,"name":"Beer","cost":399,"date":"2020-01-30"}]}]  

#### Get list of Restaurants with menu and rating
**Request**  
GET http://localhost:8080/restaurants/restaurants/withMenuAndRating?date=2020-01-30  
curl "http://localhost:8080/restaurants/restaurants/withMenuAndRating?date=2020-01-30" --user user3@yandex.ru:password3  

**Response**  
Request Method: GET  
Status Code: 200   
Content-Type: application/json  

[{"id":100006,"name":"Pizza Hut","dishes":[{"id":100009,"name":"Mexican pizza","cost":999,"date":"2020-01-30"},{"id":100010,"name":"Italian salad","cost":349,"date":"2020-01-30"},{"id":100011,"name":"Cappuccino","cost":199,"date":"2020-01-30"}],"rating":3},  
{"id":100007,"name":"Sushi Roll","dishes":[{"id":100012,"name":"Philadelphia roll","cost":849,"date":"2020-01-30"},{"id":100013,"name":"Fish salad","cost":249,"date":"2020-01-30"},{"id":100014,"name":"Green tea","cost":99,"date":"2020-01-30"}],"rating":1},  
{"id":100008,"name":"Kebab House","dishes":[{"id":100015,"name":"Kebab XXL","cost":1249,"date":"2020-01-30"},{"id":100016,"name":"Garlic sauce","cost":99,"date":"2020-01-30"},{"id":100017,"name":"Beer","cost":399,"date":"2020-01-30"}],"rating":2}]

#### Create vote
**Request**  
***!!!!!INSERT CURRENT DATE IN dateTime PARAMETER!!!!!***   
POST http://localhost:8080/restaurants/profile/votes?dateTime=2021-03-18T10:00:00&restaurantId=100006  
curl -X POST -H "Content-Type: application/json" --user user2@yandex.ru:password2  "http://localhost:8080/restaurants/profile/votes?dateTime=2021-03-18T10:00:00&restaurantId=100006"

**Response**
Request Method: POST  
Status Code: 201   
Content-Type: application/json  

{"id":100030,"restaurant":{"id":100006,"name":"Pizza Hut","dishes":null},"date":"2021-03-18"}  
