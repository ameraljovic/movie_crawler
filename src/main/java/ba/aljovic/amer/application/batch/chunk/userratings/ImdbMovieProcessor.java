package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.component.service.HttpRetriever;
import ba.aljovic.amer.application.component.service.ImdbParser;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.exception.PageNotFoundException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ImdbMovieProcessor implements ItemProcessor<ImdbMovie, List<ImdbUser>>
{
    @Autowired
    ImdbParser imdbParser;

    @Autowired
    HttpRetriever httpRetriever;

    @Override
    public List<ImdbUser> process(ImdbMovie movie) throws Exception
    {
        String movieHtml = httpRetriever.retrieveDocument(movie.getUrl());
        String userRatingsUrl = imdbParser.getUserRatingsPage(movieHtml);
        if (userRatingsUrl == null)
        {
            throw new PageNotFoundException("User reviews could not be found for imdb movie with url " + movieHtml, movieHtml);
        }

        List<ImdbUser> imdbUsers = new ArrayList<>();
        String userHtml;
        do
        {
            userHtml = httpRetriever.retrieveDocument(userRatingsUrl);
            List<ImdbUser> imdbUsersForPage = imdbParser.getUsersOnPage(userHtml);
            imdbUsers.addAll(imdbUsersForPage);

            userRatingsUrl = movie.getUrl().replaceAll("\\?.*", "") + imdbParser.nextMovieReviewPage(userHtml);
        } while (imdbParser.nextMovieReviewPage(userHtml) != null);
        return imdbUsers;
    }
}
