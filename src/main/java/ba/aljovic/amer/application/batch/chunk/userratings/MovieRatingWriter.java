package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.database.repositories.MovieRatingRepository;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MovieRatingWriter implements ItemWriter<List<MovieRating>>
{
    @Autowired
    MovieRatingRepository repository;

    @Override
    public void write(List<? extends List<MovieRating>> movieRatings) throws Exception
    {
        try
        {
            movieRatings.forEach(repository::save);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
