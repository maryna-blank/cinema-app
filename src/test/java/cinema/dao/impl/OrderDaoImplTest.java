package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.dao.OrderDao;
import cinema.dao.RoleDao;
import cinema.dao.TicketDao;
import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.Order;
import cinema.model.Role;
import cinema.model.Ticket;
import cinema.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderDaoImplTest extends AbstractTest {
    private Order order;
    private OrderDao orderDao;
    private User user;
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Order.class, User.class, Ticket.class, MovieSession.class,
                Movie.class, CinemaHall.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        orderDao = new OrderDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        TicketDao ticketDao = new TicketDaoImpl(getSessionFactory());
        MovieSessionDao movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        MovieDao movieDao = new MovieDaoImpl(getSessionFactory());
        CinemaHallDao cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        Movie movie = new Movie("The Da Vinci Code", "Mystery Thriller");
        movieDao.add(movie);
        CinemaHall cinemaHall = new CinemaHall(50, "Freyja");
        cinemaHallDao.add(cinemaHall);
        MovieSession movieSession = new MovieSession();
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.of(LocalDate.now(), LocalTime.NOON));
        movieSessionDao.add(movieSession);
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.add(new Role(Role.RoleName.USER));
        roleDao.add(new Role(Role.RoleName.ADMIN));
        user = new User();
        user.setRoles(Set.of(roleDao.getByName(Role.RoleName.USER.name())));
        user.setEmail("user@i.ua");
        user.setPassword("2345");
        userDao.add(user);
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        LocalDateTime orderTime = LocalDateTime.of(LocalDate.now(), LocalTime.NOON.minusMinutes(30));
        order = new Order();
        order.setOrderTime(orderTime);
        order.setTickets(tickets);
        order.setUser(user);
        orderDao.add(order);
    }

    @Test
    void add_Ok() {
        List<Order> ordersHistory = orderDao.getOrdersHistory(user);
        assertFalse(ordersHistory.isEmpty());
        assertTrue(ordersHistory.contains(order));
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> orderDao.add(null));
    }

    @Test
    void getOrdersHistory_Ok() {
        List<Order> ordersHistory = orderDao.getOrdersHistory(user);
        assertFalse(ordersHistory.isEmpty());
        assertTrue(ordersHistory.contains(order));
    }

    @Test
    void getOrdersHistory_Null_NotOk() {
        List<Order> ordersHistory = orderDao.getOrdersHistory(null);
        assertTrue(ordersHistory.isEmpty());
    }

    @Test
    void getOrdersHistory_NonExistentUser_NotOk() {
        assertThrows(NoSuchElementException.class, () -> orderDao.getOrdersHistory(userDao.get(7L).get()));
    }
}
