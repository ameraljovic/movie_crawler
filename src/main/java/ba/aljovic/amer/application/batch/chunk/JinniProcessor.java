package ba.aljovic.amer.application.batch.chunk;

import ba.aljovic.amer.application.component.service.HttpRetriever;
import ba.aljovic.amer.application.component.service.JinniParser;
import ba.aljovic.amer.application.database.entity.Movie;
import ba.aljovic.amer.application.exception.JinniMovieNotFoundException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;


public class JinniProcessor implements ItemProcessor<Movie, Movie>
{
    private static final String JINNI_BASE_URL = "http://www.jinni.com";

    private JinniParser parser;

    private HttpRetriever httpRetriever;

    @Override
    public Movie process(Movie movie) throws Exception
    {
        String html = httpRetriever.retrieveDocument(JINNI_BASE_URL + "/movies/" + movie.getUrl());
        if (html == null) throw new JinniMovieNotFoundException(movie.getTitle(), movie.getUrl());
        return parser.parseMovie(movie, html);
    }

    @Autowired
    public void setParser(JinniParser parser)
    {
        this.parser = parser;
    }

    @Autowired
    public void setHttpRetriever(HttpRetriever httpRetriever)
    {
        this.httpRetriever = httpRetriever;
    }
}
