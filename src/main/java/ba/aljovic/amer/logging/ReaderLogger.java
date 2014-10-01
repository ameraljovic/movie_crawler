package ba.aljovic.amer.logging;

import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.exception.SuspiciousMovieException;
import ba.aljovic.amer.exception.TmdbMovieNotFoundException;
import org.apache.http.conn.ConnectTimeoutException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class ReaderLogger
{
    private Logger logger = Logger.getLogger(getClass().toString());

    @Around ("execution(* ba.aljovic.amer.component.service.MovieRetriever.retrieveTitle(long)) && args(tmdbId)")
    public String retrievingTmdbMovie(ProceedingJoinPoint joinPoint, long tmdbId) throws Throwable
    {
        String title;
        try
        {
            logger.info("READER:Retrieving movie from The Movie database with tmdb id = " + tmdbId + ".");

            title = (String) joinPoint.proceed();

            logger.info("READER:Movie '" + title + "' successfully retrieved");
        }
        catch (TmdbMovieNotFoundException exception)
        {
            logger.info("READER:Could not retrieve movie for id=" + exception.getTmdbId());
            throw exception;
        }
        catch (ConnectTimeoutException exception)
        {
            logger.warning("READER:Socket time out." + "\n" +
                    "Error Message: " + exception.getMessage());
            throw exception;
        }
        catch (Throwable exception)
        {
            logger.severe("READER: Fatal error during reading");
            throw exception;
        }
        return title;
    }

    @Around("execution(* ba.aljovic.amer.component.service.MovieRetriever.toUrl(String)) && args(title)")
    public String constructUrl(ProceedingJoinPoint joinPoint, String title) throws Throwable
    {
        logger.info("READER:Constructing url for movie '" + title + "'.");

        String url = (String) joinPoint.proceed();

        logger.info("READER:URL '" + url + "' for movie '" + title + "' successfully constructed");
        return url;
    }

    @Around("execution(* ba.aljovic.amer.component.service.MovieRetriever.retrieveUrlBySearch(String)) && args(title)")
    public String retrieveMovieBySearch(ProceedingJoinPoint joinPoint, String title) throws Throwable
    {
        try
        {
            logger.info("READER: Constructing url via movie site search request for movie '" + title + "'.");

            String url = (String) joinPoint.proceed();

            logger.info("READER:URL '" + url + "' for movie '" + title + "' successfully constructed");
            return url;
        }
        catch (JinniMovieNotFoundException exception)
        {
            logger.info("READER:Could not construct url for movie '" + title + "'");
            throw exception;
        }
        catch (SuspiciousMovieException exception)
        {
            logger.info("READER:Could not determine for sure the movie searching with title '" + title + "'");
            throw exception;
        }
        catch (Throwable exception)
        {
            logger.severe("READER: Fatal error during reading");
            throw exception;
        }
    }


}
