package cinema;

import cinema.exception.RegistrationException;
import cinema.lib.Injector;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.ShoppingCart;
import cinema.model.User;
import cinema.security.AuthenticationService;
import cinema.service.CinemaHallService;
import cinema.service.MovieService;
import cinema.service.MovieSessionService;
import cinema.service.OrderService;
import cinema.service.ShoppingCartService;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static final Injector INJECTOR = Injector.getInstance("cinema");
    public static final MovieService MOVIE_SERVICE =
            (MovieService) INJECTOR.getInstance(MovieService.class);
    public static final CinemaHallService CINEMA_HALL_SERVICE =
            (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);
    public static final MovieSessionService MOVIE_SESSION_SERVICE =
            (MovieSessionService) INJECTOR.getInstance(MovieSessionService.class);
    public static final ShoppingCartService SHOPPING_CART_SERVICE =
            (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);
    public static final OrderService ORDER_SERVICE =
            (OrderService) INJECTOR.getInstance(OrderService.class);
    public static final AuthenticationService AUTHENTICATION_SERVICE =
            (AuthenticationService) INJECTOR.getInstance(AuthenticationService.class);

    public static void main(String[] args) throws RegistrationException {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        MOVIE_SERVICE.add(fastAndFurious);
        System.out.println(MOVIE_SERVICE.get(fastAndFurious.getId()));
        MOVIE_SERVICE.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CINEMA_HALL_SERVICE.add(firstCinemaHall);
        CINEMA_HALL_SERVICE.add(secondCinemaHall);

        System.out.println(CINEMA_HALL_SERVICE.getAll());
        System.out.println(CINEMA_HALL_SERVICE.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MOVIE_SESSION_SERVICE.add(tomorrowMovieSession);
        MOVIE_SESSION_SERVICE.add(yesterdayMovieSession);

        System.out.println(MOVIE_SESSION_SERVICE.get(yesterdayMovieSession.getId()));
        System.out.println(MOVIE_SESSION_SERVICE.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        User user = AUTHENTICATION_SERVICE.register("user@gmail.com", "1234");
        SHOPPING_CART_SERVICE.addSession(tomorrowMovieSession, user);
        ShoppingCart shoppingCart = SHOPPING_CART_SERVICE.getByUser(user);
        System.out.println(shoppingCart);

        System.out.println(ORDER_SERVICE.completeOrder(shoppingCart));
        System.out.println(ORDER_SERVICE.getOrdersHistory(user));
    }
}
