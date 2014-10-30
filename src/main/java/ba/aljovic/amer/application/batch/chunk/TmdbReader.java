package ba.aljovic.amer.application.batch.chunk;

import ba.aljovic.amer.application.component.service.MovieRetriever;
import ba.aljovic.amer.application.database.entity.Movie;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;

public class TmdbReader implements ItemStreamReader<Movie>
{
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

            String title = movieRetriever.retrieveTitle(currentItemCount);
            String imdbId = movieRetriever.retrieveImdbId(currentItemCount);
            String titleUrl = movieRetriever.toUrl(title);
            return new Movie(title, imdbId, currentItemCount, titleUrl);
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
