package cinema.dao;

import cinema.exception.DataProcessingException;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class AbstractDao<T> {
    private static final Logger LOGGER = LogManager.getLogger(AbstractDao.class);
    protected final SessionFactory factory;
    private final Class<T> clazz;

    public AbstractDao(SessionFactory factory, Class<T> clazz) {
        this.factory = factory;
        this.clazz = clazz;
    }

    public T add(T t) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(t);
            transaction.commit();
            LOGGER.info("Added " + clazz.getSimpleName() + " " + t);
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Can't add " + clazz.getSimpleName() + " " + t);
            throw new DataProcessingException("Can't insert "
                    + clazz.getSimpleName() + " " + t, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Optional<T> get(Long id) {
        try (Session session = factory.openSession()) {
            Optional<T> optional = Optional.ofNullable(session.get(clazz, id));
            LOGGER.info("Got " + clazz.getSimpleName() + ", id: " + id);
            return optional;
        } catch (Exception e) {
            LOGGER.error("Can't get " + clazz.getSimpleName() + ", id: " + id);
            throw new DataProcessingException("Can't get "
                    + clazz.getSimpleName() + ", id: " + id, e);
        }
    }

    public List<T> getAll() {
        try (Session session = factory.openSession()) {
            List<T> resultList = session.createQuery("from " + clazz.getSimpleName(), clazz)
                    .getResultList();
            LOGGER.info("Got all " + clazz.getSimpleName() + "s");
            return resultList;
        } catch (Exception e) {
            LOGGER.error("Can't get all " + clazz.getSimpleName() + "s from db");
            throw new DataProcessingException("Can't get all "
                    + clazz.getSimpleName() + "s from db", e);
        }
    }

    public T update(T t) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.update(t);
            transaction.commit();
            LOGGER.info("Updated " + clazz.getSimpleName() + " " + t);
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Can't update " + clazz.getSimpleName() + " " + t);
            throw new DataProcessingException("Can't update "
                    + clazz.getSimpleName() + " " + t, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            T movieSession = session.get(clazz, id);
            session.delete(movieSession);
            transaction.commit();
            LOGGER.info("Deleted " + clazz.getSimpleName() + ", id: " + id);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Can't delete " + clazz.getSimpleName() + ", id: " + id);
            throw new DataProcessingException("Can't delete "
                    + clazz.getSimpleName() + " with id: " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
