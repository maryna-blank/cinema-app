package cinema.dao.impl;

import cinema.dao.CinemaHallDao;
import cinema.exception.DataProcessingException;
import cinema.lib.Dao;
import cinema.model.CinemaHall;
import cinema.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class CinemaHallDaoImpl implements CinemaHallDao {
    private static final Logger LOGGER = LogManager.getLogger(CinemaHallDaoImpl.class);

    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(cinemaHall);
            transaction.commit();
            LOGGER.info("A cinema hall was created. Params: cinema hall={}", cinemaHall);
            return cinemaHall;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Couldn't create a cinema hall. Params: cinema hall={}", cinemaHall);
            throw new DataProcessingException("Can't insert a cinema hall: " + cinemaHall, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<CinemaHall> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LOGGER.info("Got a cinema hall. Params: id={}", id);
            return Optional.ofNullable(session.get(CinemaHall.class, id));
        } catch (Exception e) {
            LOGGER.error("Couldn't get a cinema hall. Params: id={}", id);
            throw new DataProcessingException("Can't get a cinema hall by id: " + id, e);
        }
    }

    @Override
    public List<CinemaHall> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaQuery<CinemaHall> criteriaQuery = session.getCriteriaBuilder()
                    .createQuery(CinemaHall.class);
            criteriaQuery.from(CinemaHall.class);
            LOGGER.info("Got all cinema halls");
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            LOGGER.error("Couldn't get all cinema halls from a DB");
            throw new DataProcessingException("Can't get all cinema halls", e);
        }
    }
}
