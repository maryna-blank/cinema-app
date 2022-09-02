package cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cinema.dao.OrderDao;
import cinema.model.Order;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.service.OrderService;
import cinema.service.ShoppingCartService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderServiceImplTest {
    private OrderService orderService;
    private OrderDao orderDao;
    private User user;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orderDao = Mockito.mock(OrderDao.class);
        ShoppingCartService shoppingCartService = Mockito.mock(ShoppingCartService.class);
        orderService = new OrderServiceImpl(orderDao, shoppingCartService);
        user = new User();
        user.setId(1L);
        Order order = new Order();
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket();
        tickets.add(ticket);
        order.setTickets(tickets);
        order.setUser(user);
        order.setId(1L);
        order.setOrderTime(LocalDateTime.now());
        orders = new ArrayList<>();
        orders.add(order);
    }

    @Test
    void completeOrder_Ok() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setTickets(List.of(new Ticket()));
        Order actual = orderService.completeOrder(shoppingCart);
        assertNotNull(actual);
    }

    @Test
    void completeOrder_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> orderService.completeOrder(null));
    }

    @Test
    void completeOrder_NonExistentSC_NotOk() {
        assertThrows(RuntimeException.class, () -> orderService.completeOrder(new ShoppingCart()));
    }

    @Test
    void getOrdersHistory_Ok() {
        Mockito.when(orderDao.getOrdersHistory(user)).thenReturn(orders);
        List<Order> ordersHistory = orderService.getOrdersHistory(user);
        assertFalse(ordersHistory.isEmpty());
        assertEquals(orders, ordersHistory);
    }

    @Test
    void getOrdersHistory_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> orderService.getOrdersHistory(null));
    }

    @Test
    void getOrdersHistory_NonExistentUser_NotOk() {
        assertThrows(RuntimeException.class, () -> orderService.getOrdersHistory(new User()));
    }
}
