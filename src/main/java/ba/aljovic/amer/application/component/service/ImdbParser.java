package ba.aljovic.amer.application.component.service;

import ba.aljovic.amer.application.database.repositories.MoviesRepository;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.exception.PageNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImdbParser
{
    public static final String IMDB_BASE_URL = "http://www.imdb.com";

    private MoviesRepository moviesRepository;

    @Autowired
    public ImdbParser(MoviesRepository moviesRepository)
    {
        this.moviesRepository = moviesRepository;
    }

    public List<ImdbMovie> parseTop250(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements movieElements = doc.select(".titleColumn");
        List<ImdbMovie> movies = new ArrayList<>();
        for (Element element : movieElements)
        {

            ImdbMovie movie = new ImdbMovie();
            movie.setUrl(IMDB_BASE_URL + element.child(1).attr("href"));
            movie.setTitle(element.child(1).childNode(0).toString());
            movies.add(movie);
        }
        return movies;
    }

    public String getUserRatingsPage(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[href]");
        for (Element link : links)
        {
            if (link.text().equals("User Reviews"))
            {
                return IMDB_BASE_URL + link.attr("href");
            }
        }
        return null;
    }

    public List<ImdbUser> getUsersOnPage(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements userElements = doc.select("a[href]");
        List<ImdbUser> users = new ArrayList<>();
        for (Element userElement : userElements)
        {
            if (userElement.attr("href").contains("/user/ur"))
            {
                if (!userElement.text().equals(""))
                {
                    ImdbUser user = new ImdbUser();
                    user.setUrl(IMDB_BASE_URL + userElement.attr("href"));
                    user.setUsername(userElement.text());
                    users.add(user);
                }
            }
        }
        return users;
    }

    public String nextMovieReviewPage(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements pagingLinks = doc.select("a[href]");
        for (Element link : pagingLinks)
        {
            if (link.attr("href").contains("reviews?start"))
            {
                if (link.children().size() > 0 &&
                        link.children().attr("src").equals("http://i.media-imdb.com/f86.gif"))
                {
                    return link.attr("href");
                }
            }
        }
        return null;
    }

    public String getRatingsPage(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[href]");
        for (Element link : links)
        {
            if (link.attr("href").contains("/ratings"))
            {
                return link.attr("href");
            }
        }
        return null;
    }

    String findRatingElement(Element element) throws PageNotFoundException
    {
        for (int i = 0; i < element.children().size(); i++)
        {
            if (element.child(i) != null && !element.child(i).attr("id").equals(""))
            {
                return element.child(i).attr("id");
            }
        }
        throw new PageNotFoundException("could not find rating for html:\n" + element.toString());
    }

    public List<MovieRating> getRatingsForPage(String html, ImdbUser user) throws PageNotFoundException
    {
        List<MovieRating> movieRatings = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements movieDivs = doc.select("div.info");
        for (Element movieDiv : movieDivs)
        {
            String ratingElement = findRatingElement(movieDiv);
            String imdbId = ratingElement.replaceAll("\\|your.*", "");

            Integer rating = Integer.valueOf(ratingElement.replaceAll(".*\\|your\\|", "").replaceAll("\\|.*", ""));
            Movie movie = moviesRepository.findByImdbId(imdbId);

            if (movie != null)
            {
                MovieRating movieRating = new MovieRating(rating, movie, user);
                movieRatings.add(movieRating);
            }
        }
        return movieRatings;
    }

    public String nextUserRatingsPage(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements pagingLinks = doc.select("a[href]");
        for (Element link : pagingLinks)
        {
            if (link.attr("href").contains("?start") && link.text().contains("Next"))
            {
                return link.attr("href");

            }
        }
        return null;
    }
}
