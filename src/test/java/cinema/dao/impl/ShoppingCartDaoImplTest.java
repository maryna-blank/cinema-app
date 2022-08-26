package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cinema.dao.ShoppingCartDao;
import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingCartDaoImplTest extends AbstractTest {
    private ShoppingCartDao shoppingCartDao;
    private ShoppingCart shoppingCart;
    private List<Ticket> tickets;
    private User user;
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Ticket.class, User.class, ShoppingCart.class};
    }

    @BeforeEach
    void setUp() {
        shoppingCartDao = new ShoppingCartDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        shoppingCart = new ShoppingCart();
        user = new User();
        tickets = new ArrayList<>();
        shoppingCart.setTickets(tickets);
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Test
    void add_Ok() {
        ShoppingCart expected = new ShoppingCart();
        User newUser = new User();
        expected.setUser(newUser);
        expected.setTickets(tickets);
        shoppingCartDao.add(expected);
        ShoppingCart actual = shoppingCartDao.getByUser(newUser);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.add(null));
    }

    @Test
    void getByUser_Ok() {
        ShoppingCart actual = shoppingCartDao.getByUser(user);
        assertNotNull(actual);
        assertEquals(shoppingCart, actual);
    }

    @Test
    void getByUser_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.getByUser(null));
    }

    @Test
    void getByUser_NonExistentUser_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.getByUser(userDao.get(7L).get()));
    }

    @Test
    void update_Ok() {
        User newUser = new User();
        shoppingCart.setUser(newUser);
        shoppingCart.setTickets(tickets);
        shoppingCartDao.update(shoppingCart);
        ShoppingCart byUser = shoppingCartDao.getByUser(newUser);
        assertNotNull(byUser);
        assertNotEquals(shoppingCart, byUser);
        // TODO org.hibernate.AnnotationException: @OneToOne or @ManyToOne on cinema.model.Ticket.movieSession
        //  references an unknown entity: cinema.model.MovieSession
    }

    @Test
    void update_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> shoppingCartDao.update(null));
    }

    @Test
    void update_NonExistent_NotOk() { // another way?
        ShoppingCart newShoppingCart = new ShoppingCart();
        assertThrows(NoSuchElementException.class, () -> shoppingCartDao
                .update(newShoppingCart));
    }
}
