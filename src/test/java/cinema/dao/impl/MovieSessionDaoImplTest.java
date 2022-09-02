package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        assertFalse(sessions.isEmpty());
        assertTrue(sessions.contains(movieSession));
        MovieSession actual = sessions.get(0);
        assertEquals(movieSession, actual);
    }

    @Test
    void findAvailableSessions_WrongDay_NotOk() {
        assertTrue(movieSessionDao.findAvailableSessions(movie.getId(),
                localDate.plusDays(2L)).isEmpty());
    }

    @Test
    void findAvailableSessions_NonExistentMovie_NotOk() {
        assertTrue(movieSessionDao.findAvailableSessions(58L, localDate).isEmpty());
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
        assertEquals(movieSession, actual);
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
