package cinema.dao.impl;

import cinema.dao.OrderDao;
import cinema.exception.DataProcessingException;
import cinema.lib.Dao;
import cinema.model.Order;
import cinema.model.User;
import cinema.util.HibernateUtil;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    private static final Logger LOGGER = LogManager.getLogger(OrderDaoImpl.class);

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            LOGGER.info("An order was created. Params: order={}", order);
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Couldn't create an order. Params: order={}", order);
            throw new DataProcessingException("Can`t insert an order " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> orderQuery = session.createQuery("FROM Order o "
                    + "LEFT JOIN FETCH o.user "
                    + "LEFT JOIN FETCH o.tickets t "
                    + "LEFT JOIN FETCH t.user "
                    + "LEFT JOIN FETCH t.movieSession m "
                    + "LEFT JOIN FETCH m.movie "
                    + "LEFT JOIN FETCH m.cinemaHall "
                    + "WHERE o.user = :user", Order.class);
            orderQuery.setParameter("user", user);
            LOGGER.info("Got orders for user. Params: user={}", user);
            return orderQuery.getResultList();
        } catch (Exception e) {
            LOGGER.error("Couldn't get orders for user. Params: user={}", user);
            throw new DataProcessingException("Can't get order by user " + user, e);
        }
    }
}
