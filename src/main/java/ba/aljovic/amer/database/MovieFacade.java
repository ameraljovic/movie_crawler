package ba.aljovic.amer.database;

import ba.aljovic.amer.database.entity.FailedMovie;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.database.entity.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieFacade
{
    @Autowired
    MovieRepository movieRepository;

    public void insertMovie(Movie movie)
    {
        movieRepository.save(movie);
    }

    public void insertFailedMovie(Movie movie, StatusEnum status)
    {
        movieRepository.saveFailed(movie, status);
    }

    public List<FailedMovie> getFailedMovies()
    {
        return movieRepository.getFailedMovies();
    }

    public void updateFailedMovieStatus(Integer id, StatusEnum status)
    {
        movieRepository.updateFailedMovieStatus(id, status);
    }

    public void recoverFailedMovie(Movie movie)
    {
        movieRepository.recoverFailedMovie(movie);
        movieRepository.save(movie);
    }
}
