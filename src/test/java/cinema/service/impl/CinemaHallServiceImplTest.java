package cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cinema.dao.CinemaHallDao;
import cinema.model.CinemaHall;
import cinema.service.CinemaHallService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CinemaHallServiceImplTest {
    private CinemaHallService cinemaHallService;
    private CinemaHallDao cinemaHallDao;
    private CinemaHall cinemaHall;

    @BeforeEach
    void setUp() {
        cinemaHallDao = Mockito.mock(CinemaHallDao.class);
        cinemaHallService = new CinemaHallServiceImpl(cinemaHallDao);
        cinemaHall = new CinemaHall(60, "Hel");
        cinemaHall.setId(1L);
    }

    @Test
    void add_Ok() {
        CinemaHall expected = new CinemaHall(65, "Valhalla");
        Mockito.when(cinemaHallDao.add(any())).thenReturn(expected);
        CinemaHall actual = cinemaHallService.add(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> cinemaHallService.add(null));
    }

    @Test
    void get_Ok() {
        Mockito.when(cinemaHallDao.get(anyLong())).thenReturn(Optional.of(cinemaHall));
        CinemaHall actual = cinemaHallService.get(cinemaHall.getId());
        assertNotNull(actual);
        assertEquals(cinemaHall, actual);
    }

    @Test
    void get_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> cinemaHallService.get(null));
    }

    @Test
    void get_NonExistent_NotOk() {
        assertThrows(RuntimeException.class, () -> cinemaHallService.get(5L));
    }

    @Test
    void getAll_Ok() {
        cinemaHallService.getAll();
        verify(cinemaHallDao, times(1)).getAll();
    }
}
