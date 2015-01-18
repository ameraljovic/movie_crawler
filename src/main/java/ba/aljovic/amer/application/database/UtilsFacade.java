package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UtilsFacade
{
    @Autowired
    private MoviesRepository moviesRepository;

    @Autowired
    private FailedMoviesRepository failedMoviesRepository;

    @Autowired
    private ImdbMoviesRepository imdbMoviesRepository;

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
