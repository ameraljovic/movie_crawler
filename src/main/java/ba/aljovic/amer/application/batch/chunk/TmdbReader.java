package ba.aljovic.amer.application.batch.chunk;

import ba.aljovic.amer.application.component.service.MovieRetriever;
import ba.aljovic.amer.application.database.entity.Movie;
import ba.aljovic.amer.application.exception.TmdbMovieNotFoundException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.logging.Logger;

public class TmdbReader implements ItemStreamReader<Movie>
{
    private Logger logger = Logger.getLogger(getClass().toString());

    private int currentItemCount;

    private int maxItemCount;

    @Autowired
    private MovieRetriever movieRetriever;

    public TmdbReader(Integer fromId, Integer toId)
    {
        currentItemCount = fromId;
        maxItemCount = toId;
    }

    @Override
    public Movie read() throws Exception
    {
        try
        {
            if (currentItemCount >= maxItemCount) return null;

            logger.info("READER:Retrieving movie from The Movie database with tmdb id = " + currentItemCount + ".");

            String title = movieRetriever.retrieveTitle(currentItemCount);
            String imdbId = movieRetriever.retrieveImdbId(currentItemCount);
            String titleUrl = movieRetriever.toUrl(title);
            Movie retrievedMovie = new Movie(title, imdbId, currentItemCount, titleUrl);

            logger.info("READER:Movie '" + title + "' successfully retrieved");

            return retrievedMovie;
        }
        catch (IOException exception)
        {
            logger.warning("READER:Time out for movie with tmdb id: ." + currentItemCount + "\n" +
                    "Error Message: " + exception.getMessage());
            currentItemCount--;
            throw exception;
        }
        catch (TmdbMovieNotFoundException exception)
        {
            logger.info("READER:Could not retrieve movie for id=" + exception.getTmdbId());
            throw exception;
        }
        catch (Throwable exception)
        {
            logger.severe("READER: Fatal error during reading");
            throw exception;
        }
        finally
        {
            if (currentItemCount <= maxItemCount)
                currentItemCount++;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException
    {
        if (executionContext.get("ITEM_COUNT") != null)
            currentItemCount = executionContext.getInt("ITEM_COUNT");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException
    {
        executionContext.put("ITEM_COUNT", currentItemCount);
    }

    @Override
    public void close() throws ItemStreamException {}
}
