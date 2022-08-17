package cinema.dao.impl;

import cinema.dao.MovieDao;
import cinema.exception.DataProcessingException;
import cinema.lib.Dao;
import cinema.model.Movie;
import cinema.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class MovieDaoImpl implements MovieDao {
    private static final Logger LOGGER = LogManager.getLogger(MovieDaoImpl.class);

    @Override
    public Movie add(Movie movie) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(movie);
            transaction.commit();
            LOGGER.info("A movie was created. Params: movie={}", movie);
            return movie;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Couldn't create a movie. Params: movie={}", movie);
            throw new DataProcessingException("Can't insert a movie: " + movie, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Movie> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LOGGER.info("Got a movie. Params: id={}", id);
            return Optional.ofNullable(session.get(Movie.class, id));
        } catch (Exception e) {
            LOGGER.error("Couldn't get a movie. Params: id={}", id);
            throw new DataProcessingException("Can't get a movie by id: " + id, e);
        }
    }

    @Override
    public List<Movie> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaQuery<Movie> criteriaQuery = session.getCriteriaBuilder()
                    .createQuery(Movie.class);
            criteriaQuery.from(Movie.class);
            LOGGER.info("Got all movies");
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            LOGGER.error("Couldn't get all movies from a DB");
            throw new DataProcessingException("Can't get all movies", e);
        }
    }
}
