package ba.aljovic.amer.component.service;

import ba.aljovic.amer.database.entity.Genome;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.exception.SuspiciousMovieException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JinniParser
{
    public Movie parseMovie(Movie movie, String html)
            throws JinniMovieNotFoundException, IOException
    {
        Document doc = Jsoup.parse(html);
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
        return movie;
    }

    public String parseUrl(String html) throws SuspiciousMovieException
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
