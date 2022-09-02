package cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cinema.dao.MovieDao;
import cinema.model.Movie;
import cinema.service.MovieService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MovieServiceImplTest {
    private MovieService movieService;
    private Movie movie;
    private MovieDao movieDao;

    @BeforeEach
    void setUp() {
        movieDao = Mockito.mock(MovieDao.class);
        movieService = new MovieServiceImpl(movieDao);
        movie = new Movie("The Intouchables", "Comedy Drama");
        movie.setId(1L);
    }

    @Test
    void add_Ok() {
        Movie expected = new Movie("Green Book", "Comedy Drama");
        Mockito.when(movieDao.add(any())).thenReturn(expected);
        Movie actual = movieService.add(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> movieService.add(null));
    }

    @Test
    void get_Ok() {
        Mockito.when(movieDao.get(anyLong())).thenReturn(Optional.of(movie));
        Movie actual = movieService.get(movie.getId());
        assertNotNull(actual);
        assertEquals(movie, actual);
    }

    @Test
    void get_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> movieService.get(null));
    }

    @Test
    void get_NonExistent_NotOk() {
        assertThrows(RuntimeException.class, () -> movieService.get(5L));
    }

    @Test
    void getAll_Ok() {
        movieService.getAll();
        verify(movieDao, times(1)).getAll();
    }
}
