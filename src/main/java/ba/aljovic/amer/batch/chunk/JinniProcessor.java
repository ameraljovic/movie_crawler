package ba.aljovic.amer.batch.chunk;

import ba.aljovic.amer.component.service.HttpRetriever;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.component.service.JinniParser;
import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;


public class JinniProcessor implements ItemProcessor<Movie, Movie>
{
    private static final String JINNI_BASE_URL = "http://www.jinni.com";

    @Autowired
    JinniParser parser;

    @Autowired
    private HttpRetriever httpRetriever;

    @Override
    public Movie process(Movie movie) throws Exception
    {
        String html = httpRetriever.retrieveDocument(JINNI_BASE_URL + "/movies/" + movie.getTmdbId());
        if (html == null) throw new JinniMovieNotFoundException(movie.getTitle(), movie.getUrl());
        return parser.parseMovie(movie, html);
    }
}
