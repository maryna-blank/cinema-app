# Cinema App

## Summary
This app simulates work of a cinema. Based on SOLID principles and N-tier architecture.

## Functionality
- Create and get your movies and cinema halls
- Register new users or log in if already existing
- Add, get and find available movie sessions
- Create, then fill and clear your shopping cart, find a shopping cart of a specific user
- Complete orders and get orders history 
- Keep a log of your actions

## Project Structure
- Dao level with CRUD operations for a direct work with a database
- Service level containing business logic
- Security level for a secure authentication while logging and registering

## Technologies Used
- Hibernate
- MySQL
- Log4j
- Maven Checkstyle Plugin

## Instructions on Start
Before doing anything, make sure you have all the necessary technologies installed and a local URL to work with MySQL database.
Set your db connection parameters in hibernate.cfg.xml.
In the Main class, there is already an example of the app usage. Set up your movies, cinema halls, sessions and users with their shopping carts.
Make sure there are no nulls or empty fields. 
Finally, run the Main class :D
