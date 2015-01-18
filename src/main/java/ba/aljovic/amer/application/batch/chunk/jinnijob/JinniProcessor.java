package ba.aljovic.amer.application.batch.chunk.jinnijob;

import ba.aljovic.amer.application.component.service.HttpRetriever;
import ba.aljovic.amer.application.component.service.JinniParser;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import ba.aljovic.amer.application.exception.JinniMovieNotFoundException;
import org.jsoup.HttpStatusException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.logging.Logger;


public class JinniProcessor implements ItemProcessor<Movie, Movie>
{
    private static final String JINNI_BASE_URL = "http://www.jinni.com";

    private Logger logger = Logger.getLogger(getClass().toString());

    private JinniParser parser;

    private HttpRetriever httpRetriever;

    @Override
    public Movie process(Movie movie) throws Exception
    {

        try
        {
            logger.info("PROCESSOR:Parsing url for movie '" + movie.getTitle() + "'.");
            String html = httpRetriever.retrieveDocument(JINNI_BASE_URL + "/movies/" + movie.getUrl());
            if (html == null) throw new JinniMovieNotFoundException(movie.getTitle(), movie.getUrl());
            Movie parsedMovie = parser.parseMovie(movie, html);
            logger.info("PROCESSOR:Successfully extracted genomes from movie '" + movie.getTitle() + "'");
            return parsedMovie;
        }
        catch (JinniMovieNotFoundException exception)
        {
            logger.warning("PROCESSOR:Could not construct url for movie '" + movie.getTitle() +
                    "' with url '" + movie.getUrl() + "'");
            throw exception;
        }
        catch (HttpStatusException exception)
        {
            logger.severe(
                    "PROCESSOR:Http error constructing url for movie '" + movie.getTitle() + "'. " +
                            "URL: " + exception.getUrl() + "\n" +
                            "Http status: " + exception.getStatusCode() + "\n"
            );
            throw exception;
        }
        catch (IOException exception)
        {
            logger.warning("PROCESSOR:Time out for movie '" + movie.getTitle() + "'.");
            throw exception;
        }
        catch (Throwable exception)
        {
            logger.severe("PROCESSOR:Fatal error constructing url for movie '" + movie.getTitle() + "'. " +
                    "Error message: " + exception.getMessage());
            throw exception;
        }
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
