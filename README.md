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
    
#####USER-available operations:  
- get list of restaurants with menu for current date (or any specified date)
- vote for the restaurant (once a day)
- get today's USER vote
- update USER vote if he changes his mind (only before 11:00)
- CRUD operations with USER account(register, delete, update, get)
- get USER account with all votes for all time
    
#####ADMIN-available operations:  
- CRUD operations for creating and redacting restaurants
- CRUD operations for creating and redacting dishes for restaurants 
- get restaurant (or restaurants) with it's rating for given date. Rating based on count of votes for the restaurant for given date 
- CRUD operations for admining users accounts
- get all votes from specified user
- get all votes from specified date
- get all votes for specified restaurant from specified date

        
### REST API for USER:
#### Get list of Restaurants with menu
**Request**  
GET http://localhost:8080/restaurants/restaurants/withMenu?date=2020-01-30  
curl "http://localhost:8080/restaurants/restaurants/withMenu?date=2020-01-30" --user user3@yandex.ru:password3  

**Response**  
Request Method: GET  
Status Code: 200   
Content-Type: application/json  

`[{"id":100006,"name":"Pizza Hut","dishes":[{"id":100009,"name":"Mexican pizza","cost":999,"date":"2020-01-30"},{"id":100010,"name":"Italian salad","cost":349,"date":"2020-01-30"},{"id":100011,"name":"Cappuccino","cost":199,"date":"2020-01-30"}]},  
{"id":100007,"name":"Sushi Roll","dishes":[{"id":100012,"name":"Philadelphia roll","cost":849,"date":"2020-01-30"},{"id":100013,"name":"Fish salad","cost":249,"date":"2020-01-30"},{"id":100014,"name":"Green tea","cost":99,"date":"2020-01-30"}]},  
{"id":100008,"name":"Kebab House","dishes":[{"id":100015,"name":"Kebab XXL","cost":1249,"date":"2020-01-30"},{"id":100016,"name":"Garlic sauce","cost":99,"date":"2020-01-30"},{"id":100017,"name":"Beer","cost":399,"date":"2020-01-30"}]}]`  

#### Create vote
**Request**  
POST http://localhost:8080/restaurants/profile/votes?restaurantId=100006  
curl -X POST -H "Content-Type: application/json" --user user3@yandex.ru:password3  "http://localhost:8080/restaurants/profile/votes?restaurantId=100006"

**Response**
Request Method: POST  
Status Code: 201   
Content-Type: application/json  

`{"id":100043,"restaurant":null,"voteDate":"***CURRENT DATE***"}`

#### Create duplicate vote
**Request**  
POST http://localhost:8080/restaurants/profile/votes?restaurantId=100006  
curl -X POST -H "Content-Type: application/json" --user user4@yandex.ru:password4  "http://localhost:8080/restaurants/profile/votes?restaurantId=100006"

**Response**
Request Method: POST  
Status Code: 422   
Content-Type: application/json  

`{"url":"http://localhost:8080/restaurants/profile/votes","type":"VALIDATION_ERROR","details":["com.natalinstanislav.restaurants.util.exception.VoteDuplicateException: You already voted today! If you want to change your choice, you can do it in special section in your personal account."]}`

#### Get today's vote  
**Request**  
GET http://localhost:8080/restaurants/profile/votes/today 
curl "http://localhost:8080/restaurants/profile/votes/today" --user user0@yandex.ru:password0  

**Response**  
Request Method: GET  
Status Code: 200   
Content-Type: application/json  

`{"id":100039,"restaurant":{"id":100007,"name":"Sushi Roll","dishes":null},"voteDate":"***CURRENT DATE***"}`

#### Update today's vote
**Request**  
PUT http://localhost:8080/restaurants/profile/votes/100039?restaurantId=100006  
curl -X PUT -H "Content-Type: application/json" --user user0@yandex.ru:password0 -d '{"id":"100039", "voteDate":"2021-04-10"}' "http://localhost:8080/restaurants/profile/votes/100039?restaurantId=100006"

**Response (if current time is BEFORE 11:00)**
Request Method: PUT  
Status Code: 204   
Content-Type: no content  

**Response (if current time is AFTER 11:00)**
Request Method: PUT  
Status Code: 422   
Content-Type: application/json  

`{"url":"http://localhost:8080/restaurants/profile/votes/100039","type":"VALIDATION_ERROR","details":["com.natalinstanislav.restaurants.util.exception.TimeValidationException: Can't revote after 11:00"]}`

#### Get account info with all votes
**Request**  
GET http://localhost:8080/restaurants/profile/withVotes  
curl "http://localhost:8080/restaurants/profile/withVotes" --user user3@yandex.ru:password3  

**Response**  
Request Method: GET  
Status Code: 200   
Content-Type: application/json  

`{"id":100003,"name":"User3","email":"user3@yandex.ru","enabled":true,"registered":"2021-04-10T14:22:10.864+00:00","roles":["USER"],"votes":[{"id":100033,"restaurant":{"id":100006,"name":"Pizza Hut","dishes":null},"voteDate":"2020-01-30"},{"id":100037,"restaurant":{"id":100006,"name":"Pizza Hut","dishes":null},"voteDate":"2020-01-31"}]}`


### REST API for ADMIN:
#### Get restaurant with menu for specified date
**Request**  
GET http://localhost:8080/restaurants/admin/restaurants/100006/withMenu?date=2020-01-30
curl "http://localhost:8080/restaurants/admin/restaurants/100006/withMenu?date=2020-01-30" --user admin@gmail.com:admin  

**Response**  
Request Method: GET  
Status Code: 200   
Content-Type: application/json  

`{"id":100006,"name":"Pizza Hut","dishes":[{"id":100009,"name":"Mexican pizza","cost":999,"dishDate":"2020-01-30"},{"id":100010,"name":"Italian salad","cost":349,"dishDate":"2020-01-30"},{"id":100011,"name":"Cappuccino","cost":199,"dishDate":"2020-01-30"}]}`

#### Get restaurant with menu and rating for specified date
**Request**  
GET http://localhost:8080/restaurants/admin/restaurants/100006/withMenuAndRating?date=2020-01-30
curl "http://localhost:8080/restaurants/admin/restaurants/100006/withMenuAndRating?date=2020-01-30" --user admin@gmail.com:admin  

**Response**  
Request Method: GET  
Status Code: 200   
Content-Type: application/json  

`{"id":100006,"name":"Pizza Hut","dishes":[{"id":100009,"name":"Mexican pizza","cost":999,"dishDate":"2020-01-30"},{"id":100010,"name":"Italian salad","cost":349,"dishDate":"2020-01-30"},{"id":100011,"name":"Cappuccino","cost":199,"dishDate":"2020-01-30"}],"rating":3}`

#### Create restaurant
**Request**  
POST http://localhost:8080/restaurants/admin/restaurants  
curl -X POST -H "Content-Type: application/json" --user admin@gmail.com:admin -d '{"id":null, "name":"NewRestaurant"}' "http://localhost:8080/restaurants/admin/restaurants"

**Response**
Request Method: POST  
Status Code: 201   
Content-Type: application/json  

`{"id":100043,"name":"NewRestaurant"}`

#### Delete dish
**Request**  
DELETE http://localhost:8080/restaurants/admin/dishes/100009  
curl -X DELETE -H "Accept: application/json" --user admin@gmail.com:admin "http://localhost:8080/restaurants/admin/dishes/100009"

**Response**
Request Method: DELETE  
Status Code: 204   
Content-Type: no content

#### Get all votes from specified date
**Request**  
GET http://localhost:8080/restaurants/admin/votes/byDate?date=2020-01-30
curl "http://localhost:8080/restaurants/admin/votes/byDate?date=2020-01-30" --user admin@gmail.com:admin  

**Response**  
Request Method: GET  
Status Code: 200   
Content-Type: application/json  

`[{"id":100030,"restaurant":{"id":100006,"name":"Pizza Hut","dishes":null},"voteDate":"2020-01-30"},{"id":100031,"restaurant":{"id":100008,"name":"Kebab House","dishes":null},"voteDate":"2020-01-30"},{"id":100032,"restaurant":{"id":100008,"name":"Kebab House","dishes":null},"voteDate":"2020-01-30"},{"id":100033,"restaurant":{"id":100006,"name":"Pizza Hut","dishes":null},"voteDate":"2020-01-30"},{"id":100034,"restaurant":{"id":100006,"name":"Pizza Hut","dishes":null},"voteDate":"2020-01-30"},{"id":100035,"restaurant":{"id":100007,"name":"Sushi Roll","dishes":null},"voteDate":"2020-01-30"}]`

