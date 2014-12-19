package ba.aljovic.amer.application.logging;

import ba.aljovic.amer.application.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.application.exception.SuspiciousMovieException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Aspect
@Component
public class ReaderLogger
{
    private Logger logger = Logger.getLogger(getClass().toString());

    @Around("execution(* ba.aljovic.amer.application.component.service.MovieRetriever.retrieveUrlBySearch(String)) && args(title)")
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
        catch (IOException exception)
        {
            logger.warning("READER:Time out." + "\n" +
                    "Error Message: " + exception.getMessage());
            throw exception;
        }
        catch (Throwable exception)
        {
            logger.severe("READER: Fatal error during reading");
            throw exception;
        }
    }
}
