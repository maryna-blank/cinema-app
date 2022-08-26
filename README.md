# Cinema App (In progress)

## Summary
This app simulates work of a cinema. Based on SOLID principles, N-tier architecture, and REST architectural style.

## Functionality
- Create and get your movies and cinema halls
- Register new users or log in if already existing
- Validate email and password
- Check your session ID on the main page
- Add, get and find available movie sessions
- Create, then fill and clear your shopping cart, find a shopping cart of a specific user
- Complete orders and get orders history 
- Keep a log of your actions

## Project Structure
- Controllers for authentication, cinema halls, main page, movies, movie sessions, orders, users and shopping carts
- Dao level with CRUD operations for a direct work with a database
- Service level containing business logic
- Security level for a secure authentication while logging, registering, and work with web tokens

## Technologies Used
- Hibernate
- JSON Web Token (JWT)
- JUnit
- Log4j
- Maven Checkstyle Plugin
- Mockito
- MySQL
- Postman
- Servlet
- Spring
- Tomcat 9.0.50

## Instructions on Start
Before doing anything, make sure you have all the necessary technologies installed and a local URL to work with MySQL database.
Set your db connection parameters in db.properties.
If you want to try working with a predefined admin or user, you can check InjectController, fill with needed username/password, and after running the app, go to `/inject` web page (it's not a necessary step).
Otherwise, register as a new user and log in to continue working with the app. Keep in mind you need a specific access level for different kinds of actions. Some available for users only, some - for admins, some - available for both.
To see your current session ID, log in, and it'll be displayed on the main page. For everything else, use Postman to send HTTP requests.
