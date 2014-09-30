package ba.aljovic.amer.database;

import ba.aljovic.amer.database.entity.FailedMovie;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.database.entity.StatusEnum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class MovieRepository
{
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Movie movie)
    {
        entityManager.persist(movie);
    }

    public void saveFailed(Movie movie, StatusEnum status)
    {
        FailedMovie failedMovie = new FailedMovie();
        failedMovie.setTitle(movie.getTitle());
        failedMovie.setUrl(movie.getUrl());
        failedMovie.setImdbId(movie.getImdbId());
        failedMovie.setTmdbId(movie.getTmdbId());
        failedMovie.setStatusId(status.getStatusCode());

        entityManager.persist(failedMovie);
    }

    public List<FailedMovie> getFailedMovies()
    {
        return entityManager.createQuery("select fm from FailedMovie fm where fm.statusId = 0", FailedMovie.class).getResultList();
    }

    public void updateFailedMovieStatus(Integer id, StatusEnum status)
    {
        FailedMovie failedMovie = entityManager.find(FailedMovie.class, id);
        failedMovie.setStatusId(status.getStatusCode());
        entityManager.persist(failedMovie);
    }

    public void recoverFailedMovie(Movie movie)
    {
        Query query = entityManager.createQuery("select fm from FailedMovie fm where fm.title = :title");
        query.setParameter("title", movie.getTitle());

        FailedMovie failedMovie = (FailedMovie)query.getResultList().get(0);
        failedMovie.setStatusId(StatusEnum.RECOVERED.getStatusCode());
        failedMovie.setUrl(movie.getUrl());
        entityManager.persist(movie);
    }
}
