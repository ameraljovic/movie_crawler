package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import ba.aljovic.amer.application.database.repositories.FailedMoviesRepository;
import ba.aljovic.amer.application.database.repositories.ImdbMoviesRepository;
import ba.aljovic.amer.application.database.repositories.MoviesRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UtilsFacade
{
    private MoviesRepository moviesRepository;

    private FailedMoviesRepository failedMoviesRepository;

    private ImdbMoviesRepository imdbMoviesRepository;

    @Autowired
    public UtilsFacade(MoviesRepository moviesRepository, FailedMoviesRepository failedMoviesRepository,
                       ImdbMoviesRepository imdbMoviesRepository)
    {
        this.moviesRepository = moviesRepository;
        this.failedMoviesRepository = failedMoviesRepository;
        this.imdbMoviesRepository = imdbMoviesRepository;
    }

    public void deleteALl()
    {
        moviesRepository.deleteAll();
        failedMoviesRepository.deleteAll();
    }

    public long getNumberOfMovies()
    {
        return moviesRepository.count();
    }

    public List<Movie> findAllMovies()
    {
        return Lists.newArrayList(moviesRepository.findAll());
    }

    public List<ImdbMovie> findAllImdbMovies()
    {
        return (List<ImdbMovie>)imdbMoviesRepository.findAll();
    }
}
