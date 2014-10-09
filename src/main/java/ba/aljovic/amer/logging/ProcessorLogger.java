package ba.aljovic.amer.logging;

import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jsoup.HttpStatusException;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.logging.Logger;

@Aspect
@Component
public class ProcessorLogger
{
    private Logger logger = Logger.getLogger(getClass().toString());

    @Around (value = "execution(* ba.aljovic.amer.component.service.JinniParser.parseMovie(ba.aljovic.amer.database.entity.Movie, String))" +
            "&& args(movie, html)", argNames = "joinPoint,movie,html")
    public Movie parseMovie(ProceedingJoinPoint joinPoint, Movie movie, String html)
            throws Throwable
    {
        try
        {
            logger.info("PROCESSOR:Parsing movie '" + movie.getTitle() + "'.");

            movie = (Movie) joinPoint.proceed();

            logger.info("PROCESSOR:Successfully extracted genomes from movie '" + movie.getTitle() + "'");
        }
        catch (JinniMovieNotFoundException exception)
        {
            logger.warning("PROCESSOR:Could not construct url for movie '" + movie.getTitle() +
                    "' with url '" + movie.getUrl() + "'");
            throw exception;
        }
        catch (SocketTimeoutException exception)
        {
            logger.warning("PROCESSOR:Socket time out for movie '" + movie.getTitle() + "'.");
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
        catch (Throwable exception)
        {
            logger.severe("PROCESSOR:Fatal error constructing url for movie '" + movie.getTitle() + "'. " +
                    "Error message: " + exception.getMessage());
            throw exception;
        }
        return movie;
    }
}
