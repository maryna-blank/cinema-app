package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cinema.dao.CinemaHallDao;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CinemaHallDaoImplTest extends AbstractTest {
    private CinemaHallDao cinemaHallDao;
    private CinemaHall cinemaHall;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {CinemaHall.class};
    }

    @BeforeEach
    void setUp() {
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        cinemaHall = new CinemaHall(100, "Fafnir");
        cinemaHallDao.add(cinemaHall);
    }

    @Test
    void add_Ok() {
        CinemaHall expected = new CinemaHall(150, "Sigurd");
        cinemaHallDao.add(expected);
        Optional<CinemaHall> hallOptional = cinemaHallDao.get(expected.getId());
        CinemaHall actual = hallOptional.get();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> cinemaHallDao.add(null));
    }

    @Test
    void get_Ok() {
        Optional<CinemaHall> hallOptional = cinemaHallDao.get(cinemaHall.getId());
        assertFalse(hallOptional.isEmpty());
        assertEquals(cinemaHall, hallOptional.get());
    }

    @Test
    void get_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> cinemaHallDao.get(null));
    }

    @Test
    void get_NonExistent_NotOk() {
        assertTrue(cinemaHallDao.get(41L).isEmpty());
    }

    @Test
    void getAll_Ok() {
        CinemaHall hreidmar = new CinemaHall(150, "Hreidmar");
        cinemaHallDao.add(hreidmar);
        List<CinemaHall> cinemaHalls = cinemaHallDao.getAll();
        assertTrue(cinemaHalls.contains(hreidmar) && cinemaHalls.contains(cinemaHall));
    }
}
