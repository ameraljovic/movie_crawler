package ba.aljovic.amer.batch.chunk;

import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.component.service.JinniParser;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;


public class JinniProcessor implements ItemProcessor<Movie, Movie>
{
    @Autowired
    JinniParser parser;

    @Override
    public Movie process(Movie movie) throws Exception
    {
        return parser.parse(movie.getTitle(), movie.getUrl(), movie.getImdbId(), movie.getTmdbId());
    }
}
