package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entity.FailedMovie;
import ba.aljovic.amer.application.database.entity.Movie;
import ba.aljovic.amer.application.database.entity.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieFacade
{
    @Autowired
    private MoviesRepository moviesRepository;

    @Autowired
    private FailedMoviesRepository failedMoviesRepository;

    public void insertMovie(Movie movie)
    {
        moviesRepository.save(movie);
    }

    public void insertFailedMovie(Movie movie, StatusEnum status)
    {
        FailedMovie failedMovie = new FailedMovie();
        failedMovie.setTitle(movie.getTitle());
        failedMovie.setUrl(movie.getUrl());
        failedMovie.setImdbId(movie.getImdbId());
        failedMovie.setTmdbId(movie.getTmdbId());
        failedMovie.setStatusId(status.getStatusCode());

        failedMoviesRepository.save(failedMovie);
    }

    public List<FailedMovie> getFailedMovies()
    {
        return failedMoviesRepository.findByStatusId(StatusEnum.UNKNOWN.getStatusCode());
    }

    public void updateFailedMovieStatus(Integer id, StatusEnum status)
    {
        FailedMovie failedMovie = failedMoviesRepository.findOne(id);
        failedMovie.setStatusId(status.getStatusCode());
        failedMoviesRepository.save(failedMovie);
    }

    public void recoverFailedMovie(Movie movie)
    {
        FailedMovie failedMovie = failedMoviesRepository.findByTitle(movie.getTitle());
        failedMovie.setStatusId(StatusEnum.RECOVERED.getStatusCode());
        failedMovie.setUrl(movie.getUrl());
    }

    public void deleteALl()
    {
        moviesRepository.deleteAll();
        failedMoviesRepository.deleteAll();
    }
}
