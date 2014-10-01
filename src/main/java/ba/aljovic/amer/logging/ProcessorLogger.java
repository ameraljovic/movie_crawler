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

    @Around (value = "execution(* ba.aljovic.amer.component.service.JinniParser.parse(String, String, String, Integer))" +
            "&& args(title, url, imdbId, tmdbId)", argNames = "joinPoint,title,url,imdbId,tmdbId")
    public Movie parseMovie(ProceedingJoinPoint joinPoint, String title, String url, String imdbId, Integer tmdbId)
            throws Throwable
    {
        Movie movie;
        try
        {
            logger.info("PROCESSOR:Parsing movie '" + title + "'.");

            movie = (Movie) joinPoint.proceed();

            logger.info("PROCESSOR:Successfully extracted genomes from movie '" + title + "'");
        }
        catch (JinniMovieNotFoundException exception)
        {
            logger.warning("PROCESSOR:Could not construct url for movie '" + title +
                    "' with url '" + url + "'");
            throw exception;
        }
        catch (SocketTimeoutException exception)
        {
            logger.warning("PROCESSOR:Socket time out for movie '" + title + "'.");
            throw exception;
        }
        catch (HttpStatusException exception)
        {
            logger.severe(
                    "PROCESSOR:Http error constructing url for movie '" + title + "'. " +
                            "URL: " + exception.getUrl() + "\n" +
                            "Http status: " + exception.getStatusCode() + "\n"
            );
            throw exception;
        }
        catch (Throwable exception)
        {
            logger.severe("PROCESSOR:Fatal error constructing url for movie '" + title + "'. " +
                    "Error message: " + exception.getMessage());
            throw exception;
        }
        return movie;
    }
}
