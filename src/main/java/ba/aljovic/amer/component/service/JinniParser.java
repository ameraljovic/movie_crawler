package ba.aljovic.amer.component.service;

import ba.aljovic.amer.database.entity.Genome;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.exception.SuspiciousMovieException;
import org.apache.http.HttpStatus;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JinniParser
{
    private static final String JINNI_BASE_URL = "http://www.jinni.com";
    public static final int TIMEOUT_IN_MILLIS = 1500;

    public Movie parse(String title, String titleUrl, String imdbId, Integer tmdbId)
            throws JinniMovieNotFoundException, IOException
    {
        Document doc;
        Movie movie = new Movie(title, imdbId, tmdbId, titleUrl);
        try
        {
            doc = Jsoup.connect(JINNI_BASE_URL + "/movies/" + titleUrl).timeout(TIMEOUT_IN_MILLIS).get();
            Elements elGenomes = doc.select(".right_genomeGroup");

            for (Element elGenome : elGenomes)
            {
                Element elTitle = elGenome.getElementsByClass("right_genomeTitle").get(0);
                Genome genome = new Genome(elTitle.text());
                Elements genomeValues = elGenome.getElementsByClass("right_genomeLink");

                for (Element genomeValue : genomeValues)
                {
                    genome.add(genomeValue.childNode(0).toString().replace(",", ""));
                }

                movie.add(genome);
            }
        }
        catch (HttpStatusException exception)
        {
            if (exception.getStatusCode() == HttpStatus.SC_NOT_FOUND)
                throw new JinniMovieNotFoundException(title, titleUrl);
            else
                throw exception;
        }
        return movie;
    }

    public String parseHtml(String html) throws SuspiciousMovieException
    {
        Document doc = Jsoup.parse(html);
        if (doc.select("#discovery_main").size() == 0)
            return null;
        Elements movieElements = doc.select("#discovery_main");
        Elements movieLinks = movieElements.select("a[href]");
        if (movieLinks.size() < 4)
        {
            return movieElements.get(0).getElementsByTag("a").attr("href").replace("movies/", "").replace("/", "");
        }
        else
            throw new SuspiciousMovieException();
    }
}