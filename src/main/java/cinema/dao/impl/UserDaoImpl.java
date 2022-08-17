package cinema.dao.impl;

import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
import cinema.lib.Dao;
import cinema.model.User;
import cinema.util.HibernateUtil;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);

    @Override
    public User add(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            LOGGER.info("A user was created. Params: {}", user);
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Couldn't create a user. Params: {}", user);
            throw new DataProcessingException("Can't insert a user: " + user, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User u "
                    + "WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            LOGGER.info("Found a user. Params: email={}", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            LOGGER.error("Couldn't find a user by email. Params: email={}", email);
            throw new DataProcessingException("Can't find a user by email: " + email, e);
        }
    }
}
