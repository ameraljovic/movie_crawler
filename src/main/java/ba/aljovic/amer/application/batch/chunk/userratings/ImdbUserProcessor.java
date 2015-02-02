package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.component.service.HttpRetriever;
import ba.aljovic.amer.application.component.service.ImdbParser;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.exception.PageNotFoundException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ImdbUserProcessor implements ItemProcessor<ImdbUser, List<MovieRating>>
{
    @Autowired
    private HttpRetriever httpRetriever;

    @Autowired
    private ImdbParser imdbParser;

    @Override
    public List<MovieRating> process(ImdbUser user) throws Exception
    {
        String userHtml = httpRetriever.retrieveDocument(user.getUrl());
        if (userHtml == null)
        {
            throw new PageNotFoundException("User page could not be found for imbd user with url " + user.getUrl());
        }
        String ratingsUrl = ImdbParser.IMDB_BASE_URL +  imdbParser.getRatingsPage(userHtml);
        if (imdbParser.getRatingsPage(userHtml) == null)
        {
            throw new PageNotFoundException("User ratings could not be found for imdb user with url " + userHtml, userHtml);
        }
        List<MovieRating> movieRatings = new ArrayList<>();

        String ratingsHtml;
        do
        {
            ratingsHtml = httpRetriever.retrieveDocument(ratingsUrl);
            List<MovieRating> movieRatingsForPage = imdbParser.getRatingsForPage(ratingsHtml, user);
            movieRatings.addAll(movieRatingsForPage);

            ratingsUrl = user.getUrl() + "ratings" + imdbParser.nextUserRatingsPage(ratingsHtml);
        } while (imdbParser.nextUserRatingsPage(ratingsHtml) != null);
        return movieRatings;
    }
}
