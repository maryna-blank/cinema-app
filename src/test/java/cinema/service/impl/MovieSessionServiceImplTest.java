package cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cinema.dao.MovieSessionDao;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.service.MovieSessionService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MovieSessionServiceImplTest {
    private MovieSession movieSession;
    private MovieSessionService movieSessionService;
    private MovieSessionDao movieSessionDao;

    @BeforeEach
    void setUp() {
        movieSessionDao = Mockito.mock(MovieSessionDao.class);
        movieSessionService = new MovieSessionServiceImpl(movieSessionDao);
        movieSession = new MovieSession();
        movieSession.setId(1L);
        Movie movie = new Movie("Interstellar", "Science Fiction");
        movie.setId(1L);
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.now());
        movieSession.setCinemaHall(new CinemaHall(80, "Surtr"));
    }

    @Test
    void findAvailableSessions_Ok() {
        List<MovieSession> expected = new ArrayList<>();
        expected.add(movieSession);
        Mockito.when(movieSessionDao.findAvailableSessions(anyLong(), any())).thenReturn(expected);
        List<MovieSession> actual = movieSessionService.findAvailableSessions(1L, LocalDate.now());
        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    void findAvailableSessions_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> movieSessionService.findAvailableSessions(null, null));
    }

    @Test
    void add_Ok() {
        Mockito.when(movieSessionDao.add(any())).thenReturn(movieSession);
        MovieSession actual = movieSessionService.add(movieSession);
        assertNotNull(actual);
        assertEquals(movieSession, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> movieSessionService.add(null));
    }

    @Test
    void get_Ok() {
        Mockito.when(movieSessionDao.get(anyLong())).thenReturn(Optional.ofNullable(movieSession));
        MovieSession actual = movieSessionService.get(1L);
        assertNotNull(actual);
        assertEquals(movieSession, actual);
    }

    @Test
    void get_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> movieSessionService.get(null));
    }

    @Test
    void get_NonExistent_NotOk() {
        assertThrows(RuntimeException.class, () -> movieSessionService.get(5L));
    }

    @Test
    void update_Ok() {
        Mockito.when(movieSessionDao.update(any())).thenReturn(movieSession);
        movieSession.setMovie(new Movie("Inception", "Science Fiction"));
        MovieSession actual = movieSessionService.update(movieSession);
        assertNotNull(actual);
        assertEquals(movieSession, actual);
    }

    @Test
    void update_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> movieSessionService.update(null));
    }

    @Test
    void update_NonExistent_NotOk() {
        assertThrows(RuntimeException.class, () -> movieSessionService.update(new MovieSession()));
    }

    @Test
    void delete_Ok() {
        movieSessionService.delete(1L);
        verify(movieSessionDao, times(1)).delete(1L);
        verify(movieSessionDao, times(1)).delete(anyLong());
    }

    @Test
    void delete_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> movieSessionService.delete(null));
    }
}
