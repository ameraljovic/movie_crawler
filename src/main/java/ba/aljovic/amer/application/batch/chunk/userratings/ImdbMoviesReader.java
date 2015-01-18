package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.component.service.HttpRetriever;
import ba.aljovic.amer.application.component.service.ImdbMovieParser;
import ba.aljovic.amer.application.database.ImdbMoviesRepository;
import ba.aljovic.amer.application.database.entity.ImdbMovie;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public class ImdbMoviesReader implements ItemReader<ImdbMovie>
{
    public static final int MAX_NUMBER_OF_MOVIES = 250;
    public static final String IMDB_TOP_250_URL = "http://www.imdb.com/chart/top";
    @Autowired
    private HttpRetriever httpRetriever;

    @Autowired
    private ImdbMovieParser imdbMovieParser;

    @Autowired
    private ImdbMoviesRepository imdbMoviesRepository;

    private List<ImdbMovie> movies;

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
        if (movies != null && !movies.isEmpty()) {
            return movies.remove(0);
        }
        return null;
    }
}
