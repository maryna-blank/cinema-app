package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cinema.dao.MovieDao;
import cinema.exception.DataProcessingException;
import cinema.model.Movie;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieDaoImplTest extends AbstractTest {
    private MovieDao movieDao;
    private Movie movie;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Movie.class};
    }

    @BeforeEach
    void setUp() {
        movieDao = new MovieDaoImpl(getSessionFactory());
        movie = new Movie("Harry Potter", "Fantasy");
        movieDao.add(movie);
    }

    @Test
    void add_Ok() {
        Movie expected = new Movie("Pride and Prejudice", "Romantic Drama");
        movieDao.add(expected);
        Movie actual = movieDao.get(expected.getId()).get();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieDao.add(null));
    }

    @Test
    void get_Ok() {
        Optional<Movie> movieOptional = movieDao.get(movie.getId());
        assertFalse(movieOptional.isEmpty());
        assertEquals(movie, movieOptional.get());
    }

    @Test
    void get_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> movieDao.get(null));
    }

    @Test
    void get_NonExistent_NotOk() {
        assertTrue(movieDao.get(41L).isEmpty());
    }

    @Test
    void getAll_Ok() {
        Movie forrest = new Movie("Forrest Gump", "Comedy Drama");
        movieDao.add(forrest);
        List<Movie> movies = movieDao.getAll();
        assertTrue(movies.contains(forrest) && movies.contains(movie));
    }
}
