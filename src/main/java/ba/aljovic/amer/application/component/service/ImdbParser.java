package ba.aljovic.amer.application.component.service;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImdbParser
{

    public static final String IMDB_BASE_URL = "http://www.imdb.com";

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

    public String nextPage(String html)
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
}
