package cinema.dao.impl;

import cinema.dao.AbstractDao;
import cinema.dao.MovieSessionDao;
import cinema.exception.DataProcessingException;
import cinema.model.MovieSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MovieSessionDaoImpl extends AbstractDao<MovieSession> implements MovieSessionDao {
    private static final Logger LOGGER = LogManager.getLogger(MovieSessionDaoImpl.class);

    public MovieSessionDaoImpl(SessionFactory factory) {
        super(factory, MovieSession.class);
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        try (Session session = factory.openSession()) {
            Query<MovieSession> getAvailableSessions = session.createQuery(
                    "FROM MovieSession m WHERE m.movie.id = :id "
                            + "AND m.showTime BETWEEN :startTime AND :endTime", MovieSession.class);
            getAvailableSessions.setParameter("id", movieId);
            getAvailableSessions.setParameter("startTime", date.atTime(LocalTime.MIN));
            getAvailableSessions.setParameter("endTime", date.atTime(LocalTime.MAX));
            List<MovieSession> sessions = getAvailableSessions.getResultList();
            LOGGER.info("Found available movie sessions, id:{}, date:{}",
                    movieId, date);
            return sessions;
        } catch (Exception e) {
            LOGGER.error("Can't find available movie sessions, movie id:{}, date:{}",
                    movieId, date);
            throw new DataProcessingException("Session for movie with id "
                    + movieId + " and show date " + date + " not found", e);
        }
    }
}
