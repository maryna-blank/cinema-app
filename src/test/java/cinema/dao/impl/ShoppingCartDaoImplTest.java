package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.dao.RoleDao;
import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.Role;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingCartDaoImplTest extends AbstractTest {
    private ShoppingCartDao shoppingCartDao;
    private ShoppingCart shoppingCart;
    private User user;
    private UserDao userDao;
    private TicketDao ticketDao;
    private MovieSession movieSession;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Ticket.class, User.class, ShoppingCart.class, MovieSession.class,
                CinemaHall.class, Movie.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        shoppingCartDao = new ShoppingCartDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        ticketDao = new TicketDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        MovieSessionDao movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        roleDao.add(new Role(Role.RoleName.USER));
        roleDao.add(new Role(Role.RoleName.ADMIN));
        user = new User();
        user.setRoles(Set.of(roleDao.getByName(Role.RoleName.USER.name())));
        user.setEmail("user@i.ua");
        user.setPassword("2345");
        userDao.add(user);
        MovieDao movieDao = new MovieDaoImpl(getSessionFactory());
        CinemaHallDao cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        Movie movie = new Movie("Titanic", "Romance and Disaster");
        movieDao.add(movie);
        CinemaHall cinemaHall = new CinemaHall(150, "Fenrir");
        cinemaHallDao.add(cinemaHall);
        movieSession = new MovieSession();
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.of(LocalDate.now(), LocalTime.NOON));
        movieSessionDao.add(movieSession);
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);
        shoppingCart = new ShoppingCart();
        shoppingCart.setTickets(List.of(ticket));
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Test
    void add_Ok() {
        ShoppingCart expected = new ShoppingCart();
        User newUser = new User();
        newUser.setEmail("newuser@i.ua");
        newUser.setPassword("5678");
        newUser.setRoles(Set.of(roleDao.getByName(Role.RoleName.USER.name())));
        userDao.add(newUser);
        expected.setUser(newUser);
        Ticket newTicket = new Ticket();
        newTicket.setMovieSession(movieSession);
        newTicket.setUser(newUser);
        ticketDao.add(newTicket);
        expected.setTickets(List.of(newTicket));
        shoppingCartDao.add(expected);
        ShoppingCart actual = shoppingCartDao.getByUser(newUser);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.add(null));
    }

    @Test
    void getByUser_Ok() {
        ShoppingCart actual = shoppingCartDao.getByUser(user);
        assertNotNull(actual);
        assertEquals(shoppingCart, actual);
    }

    @Test
    void getByUser_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.getByUser(null));
    }

    @Test
    void getByUser_NonExistentUser_NotOk() {
        assertThrows(NoSuchElementException.class, () -> shoppingCartDao.getByUser(userDao.get(7L).get()));
    }

    @Test
    void update_Ok() {
        Ticket newTicket = new Ticket();
        newTicket.setMovieSession(movieSession);
        newTicket.setUser(user);
        ticketDao.add(newTicket);
        shoppingCart.setTickets(List.of(newTicket));
        shoppingCartDao.update(shoppingCart);
        ShoppingCart byUser = shoppingCartDao.getByUser(user);
        assertNotNull(byUser);
        assertEquals(shoppingCart, byUser);
    }

    @Test
    void update_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.update(null));
    }

    @Test
    void update_NonExistent_NotOk() {
        ShoppingCart newShoppingCart = new ShoppingCart();
        assertThrows(DataProcessingException.class, () -> shoppingCartDao
                .update(newShoppingCart));
    }
}
