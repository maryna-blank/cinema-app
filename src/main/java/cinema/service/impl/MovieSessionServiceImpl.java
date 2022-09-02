package cinema.service.impl;

import cinema.dao.MovieSessionDao;
import cinema.model.MovieSession;
import cinema.service.MovieSessionService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovieSessionServiceImpl implements MovieSessionService {
    private final MovieSessionDao movieSessionDao;

    public MovieSessionServiceImpl(MovieSessionDao movieSessionDao) {
        this.movieSessionDao = movieSessionDao;
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        if (movieId == null || date == null) {
            throw new RuntimeException("Movie id and date can't be null");
        }
        return movieSessionDao.findAvailableSessions(movieId, date);
    }

    @Override
    public MovieSession add(MovieSession session) {
        if (session == null) {
            throw new RuntimeException("Session can't be null");
        }
        return movieSessionDao.add(session);
    }

    @Override
    public MovieSession get(Long id) {
        return movieSessionDao.get(id).orElseThrow(
                () -> new RuntimeException("Session with id " + id + " not found"));
    }

    @Override
    public MovieSession update(MovieSession movieSession) {
        if (movieSession == null || movieSession.getId() == null) {
            throw new RuntimeException("There's no such a session: " + movieSession);
        }
        return movieSessionDao.update(movieSession);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new RuntimeException("ID can't be null");
        }
        movieSessionDao.delete(id);
    }
}
