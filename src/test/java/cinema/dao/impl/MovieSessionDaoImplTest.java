package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieSessionDaoImplTest extends AbstractTest {
    private MovieSession movieSession;
    private MovieSessionDao movieSessionDao;
    private Movie movie;
    private LocalDate localDate;
    private LocalDateTime showTime;
    private CinemaHallDao cinemaHallDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {MovieSession.class, Movie.class, CinemaHall.class};
    }

    @BeforeEach
    void setUp() {
        MovieDao movieDao = new MovieDaoImpl(getSessionFactory());
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        movie = new Movie("Harry Potter", "Fantasy");
        movieDao.add(movie);
        movieSession = new MovieSession();
        movieSession.setMovie(movie);
        CinemaHall cinemaHall = new CinemaHall(100, "Thor");
        cinemaHallDao.add(cinemaHall);
        movieSession.setCinemaHall(cinemaHall);
        localDate = LocalDate.now();
        showTime = LocalDateTime.of(localDate, LocalTime.NOON);
        movieSession.setShowTime(showTime);
        movieSessionDao.add(movieSession);
    }

    @Test
    void findAvailableSessions_Ok() {
        List<MovieSession> sessions = movieSessionDao.findAvailableSessions(movie.getId(), localDate);
        // TODO 14:38:12.251 [main] ERROR org.hibernate.engine.jdbc.spi.SqlExceptionHelper - user lacks privilege or
        // object not found: DATE_FORMAT in statement [select moviesessi0_.id as id1_1_, moviesessi0_.cinema_hall_id
        // as cinema_h3_1_, moviesessi0_.movie_id as movie_id4_1_, moviesessi0_.show_time as show_tim2_1_
        // from movie_sessions moviesessi0_ where moviesessi0_.movie_id=? and DATE_FORMAT(moviesessi0_.show_time,'%Y-%m-%d')=?]
        // TODO cinema.exception.DataProcessingException: Session for movie with id 1 and show date 2022-08-26 not found
        assertFalse(sessions.isEmpty());
        assertTrue(sessions.contains(movieSession));
        MovieSession actual = sessions.get(Math.toIntExact(movieSession.getId()));
        assertEquals(movieSession, actual);
    }

    @Test
    void findAvailableSessions_WrongDay_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieSessionDao.findAvailableSessions(movie.getId(),
                localDate.plusDays(2L)));
    }

    @Test
    void findAvailableSessions_NonExistentMovie_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieSessionDao.findAvailableSessions(58L, localDate));
    }

    @Test
    void findAvailableSessions_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieSessionDao.findAvailableSessions(null, null));
    }

    @Test
    void add_Ok() {
        MovieSession expected = new MovieSession();
        expected.setMovie(movie);
        expected.setShowTime(showTime);
        movieSessionDao.add(expected);
        MovieSession actual = movieSessionDao.get(expected.getId()).get();
        assertNotNull(actual);
        assertEquals(expected, actual);
        // TODO AssertionFailedError (problem with milliseconds)
        // Expected :MovieSession{id=2, movie=Movie{id=1, title='Harry Potter', description='Fantasy'}, cinemaHall=null, showTime=2022-08-26T14:53:11.367867800}
        // Actual   :MovieSession{id=2, movie=Movie{id=1, title='Harry Potter', description='Fantasy'}, cinemaHall=null, showTime=2022-08-26T14:53:11.367867}
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieSessionDao.add(null));
    }

    @Test
    void get_Ok() {
        Optional<MovieSession> optionalMovieSession = movieSessionDao.get(movieSession.getId());
        assertFalse(optionalMovieSession.isEmpty());
        assertEquals(movieSession, optionalMovieSession.get());
        // TODO AssertionFailedError (problem with milliseconds)
    }

    @Test
    void get_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieSessionDao.get(null));
    }

    @Test
    void get_NonExistent_NotOk() {
        assertTrue(movieSessionDao.get(5L).isEmpty());
    }

    @Test
    void update_Ok() {
        CinemaHall newCinemaHall = new CinemaHall(100, "Loki");
        cinemaHallDao.add(newCinemaHall);
        movieSession.setCinemaHall(newCinemaHall);
        MovieSession actual = movieSessionDao.update(movieSession);
        assertNotNull(actual);
        assertNotEquals(movieSession, actual);
        // todo why AssertionFailedError
    }

    @Test
    void update_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieSessionDao.update(null));
    }

    @Test
    void update_NonExistent_NotOk() {
        assertThrows(NoSuchElementException.class, () -> movieSessionDao
                .update(movieSessionDao.get(6L).get()));
    }

    @Test
    void delete_Ok() {
        movieSessionDao.delete(movieSession.getId());
        assertTrue(movieSessionDao.get(movieSession.getId()).isEmpty());
    }

    @Test
    void delete_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieSessionDao.delete(null));
    }

    @Test
    void delete_NonExistent_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieSessionDao
                .delete(6L));
    }
}
