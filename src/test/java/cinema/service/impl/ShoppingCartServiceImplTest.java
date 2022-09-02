package cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cinema.dao.MovieSessionDao;
import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.model.MovieSession;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.service.ShoppingCartService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ShoppingCartServiceImplTest {
    private ShoppingCartDao shoppingCartDao;
    private ShoppingCartService shoppingCartService;
    private User user;
    private ShoppingCart shoppingCart;
    private MovieSession movieSession;

    @BeforeEach
    void setUp() {
        shoppingCartDao = Mockito.mock(ShoppingCartDao.class);
        TicketDao ticketDao = Mockito.mock(TicketDao.class);
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartDao, ticketDao);
        user = new User();
        user.setId(1L);
        Ticket ticket = new Ticket();
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        movieSession = new MovieSession();
        movieSession.setId(1L);
        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setTickets(tickets);
    }

    @Test
    void addSession_Ok() {
        Mockito.when(shoppingCartDao.getByUser(user)).thenReturn(shoppingCart);
        shoppingCartService.addSession(movieSession, user);
    }

    @Test
    void addSession_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> shoppingCartService.addSession(null, null));
    }

    @Test
    void getByUser_Ok() {
        Mockito.when(shoppingCartDao.getByUser(user)).thenReturn(shoppingCart);
        assertNotNull(shoppingCartService.getByUser(user));
    }

    @Test
    void getByUser_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> shoppingCartService.getByUser(null));
    }

    @Test
    void getByUser_NonExistent_NotOk() {
        assertThrows(RuntimeException.class, () -> shoppingCartService.getByUser(new User()));
    }

    @Test
    void registerNewShoppingCart_Ok() {
        Mockito.when(shoppingCartDao.getByUser(user)).thenReturn(shoppingCart);
        Mockito.when(shoppingCartDao.add(shoppingCart)).thenReturn(shoppingCart);
        shoppingCartService.registerNewShoppingCart(user);
        assertEquals(shoppingCart, shoppingCartDao.getByUser(user));
    }

    @Test
    void registerNewShoppingCart_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> shoppingCartService.registerNewShoppingCart(null));
    }

    @Test
    void registerNewShoppingCart_NonExistent_NotOk() {
        assertThrows(RuntimeException.class, () -> shoppingCartService.registerNewShoppingCart(new User()));
    }

    @Test
    void clear_Ok() {
        Mockito.when(shoppingCartDao.getByUser(user)).thenReturn(shoppingCart);
        shoppingCartService.clear(shoppingCartDao.getByUser(user));
        assertNull(shoppingCart.getTickets());
    }

    @Test
    void clear_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> shoppingCartService.clear(null));
    }

    @Test
    void clear_NonExistent_NotOk() {
        assertThrows(RuntimeException.class, () -> shoppingCartService.clear(new ShoppingCart()));
    }
}
