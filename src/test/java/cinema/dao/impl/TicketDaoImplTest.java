package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.dao.RoleDao;
import cinema.dao.TicketDao;
import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.Role;
import cinema.model.Ticket;
import cinema.model.User;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketDaoImplTest extends AbstractTest {
    private TicketDao ticketDao;
    private Ticket ticket;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Ticket.class, User.class, MovieSession.class, Movie.class,
                Role.class, CinemaHall.class};
    }

    @BeforeEach
    void setUp() {
        ticketDao = new TicketDaoImpl(getSessionFactory());
        MovieSessionDao movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        MovieSession movieSession = new MovieSession();
        CinemaHallDao cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        CinemaHall cinemaHall = new CinemaHall(500, "Odin");
        MovieDao movieDao = new MovieDaoImpl(getSessionFactory());
        cinemaHallDao.add(cinemaHall);
        movieSession.setCinemaHall(cinemaHall);
        Movie movie = new Movie("Inception", "Science Fiction");
        movieDao.add(movie);
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.now().plusHours(2));
        movieSessionDao.add(movieSession);
        ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        User user = new User();
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.add(new Role(Role.RoleName.USER));
        roleDao.add(new Role(Role.RoleName.ADMIN));
        user.setRoles(Set.of(roleDao.getByName(Role.RoleName.USER.name())));
        user.setEmail("user@i.ua");
        user.setPassword("2345");
        UserDao userDao = new UserDaoImpl(getSessionFactory());
        userDao.add(user);
        ticket.setUser(user);
    }

    @Test
    void add_Ok() {
        Ticket actual = ticketDao.add(ticket);
        assertNotNull(actual);
        assertEquals(ticket, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> ticketDao.add(null));
    }
}
