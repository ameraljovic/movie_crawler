package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.component.service.HttpRetriever;
import ba.aljovic.amer.application.component.service.ImdbParser;
import ba.aljovic.amer.application.database.repositories.ImdbMoviesRepository;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public class ImdbMoviesReader implements ItemReader<ImdbMovie>, ItemStream
{
    public static final int MAX_NUMBER_OF_MOVIES = 10;
    public static final String IMDB_TOP_250_URL = "http://www.imdb.com/chart/top";
    public static final String MOVIE_INDEX = "movie_index";

    @Autowired
    private HttpRetriever httpRetriever;

    @Autowired
    private ImdbParser imdbMovieParser;

    @Autowired
    private ImdbMoviesRepository imdbMoviesRepository;

    private List<ImdbMovie> movies;

    private Integer movieIndex = 0;

    @PostConstruct
    public void fetchHtml() throws IOException
    {
        if (imdbMoviesRepository.count() >= MAX_NUMBER_OF_MOVIES)
        {
            return;
        }
        String top250Html = httpRetriever.retrieveDocument(IMDB_TOP_250_URL);
        movies = imdbMovieParser.parseTop250(top250Html);
    }

    @Override
    public ImdbMovie read()
    {
        try
        {
            if (movieIndex <= MAX_NUMBER_OF_MOVIES)
            {
                return movies.get(movieIndex);
            }
            return null;
        }
        finally
        {
            movieIndex++;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException
    {
        if (executionContext.containsKey(MOVIE_INDEX))
        {
            movieIndex = executionContext.getInt(MOVIE_INDEX);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException
    {
        executionContext.putInt(MOVIE_INDEX, movieIndex);
    }

    @Override
    public void close() throws ItemStreamException
    {

    }
}
