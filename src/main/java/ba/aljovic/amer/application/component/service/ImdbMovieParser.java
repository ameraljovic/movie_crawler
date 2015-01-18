package ba.aljovic.amer.application.component.service;

import ba.aljovic.amer.application.database.entity.ImdbMovie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImdbMovieParser
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
}
