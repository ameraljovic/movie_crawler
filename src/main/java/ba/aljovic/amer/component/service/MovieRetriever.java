package ba.aljovic.amer.component.service;

import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.exception.SuspiciousMovieException;
import ba.aljovic.amer.exception.TmdbMovieNotFoundException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class MovieRetriever
{
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=75b4eb6730f3f8109a55f643a052ef7d";
    private static final char[] DIRTY_CHARACTERS = {'.', '!', '?', '\'', '(', ')'};
    private static final Map<Character, String> charactersMapping = new HashMap<>();
    private static final Map<Character, String> latinExtendedMapping = new HashMap<>();
    private static final char SPACE = ' ';

    private HttpRetriever httpRetriever;
    private JSONParser jsonParser;
    private JinniParser jinniParser;

    public MovieRetriever()
    {
        Arrays.sort(DIRTY_CHARACTERS);
        initializeCharactersMap();
        initializeLatinExtendedMap();
    }

    private String retrieve(long id, String attributeName) throws TmdbMovieNotFoundException, IOException
    {
        String url = constructUrl(id);
        String json = httpRetriever.retrieveDocument(url);
        if (json == null)
            throw new TmdbMovieNotFoundException(id);

        return jsonParser.getByName(json, attributeName);
    }

    public String retrieveTitle(long id) throws TmdbMovieNotFoundException, IOException
    {
        return retrieve(id, "title");
    }

    public String retrieveImdbId(long id) throws TmdbMovieNotFoundException, IOException
    {
        return retrieve(id, "imdb_id");
    }

    public String retrieveUrlBySearch(String title)
            throws IOException, JinniMovieNotFoundException, SuspiciousMovieException
    {
        String searchUrl = "http://www.jinni.com/discovery/all/explore/";

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("search", "@#@" + title + "-[0]FT"));
        urlParameters.add(new BasicNameValuePair("tuners", "-1,-1,-1,-1"));
        urlParameters.add(new BasicNameValuePair("sources", "all"));
        urlParameters.add(new BasicNameValuePair("content", "All"));
        urlParameters.add(new BasicNameValuePair("fromYear", ""));
        urlParameters.add(new BasicNameValuePair("toYear", ""));
        urlParameters.add(new BasicNameValuePair("sizeby", "Relevance"));
        urlParameters.add(new BasicNameValuePair("switch_view", ""));
        urlParameters.add(new BasicNameValuePair("filterWatched", ""));
        urlParameters.add(new BasicNameValuePair("auditTextSearch", ""));
        urlParameters.add(new BasicNameValuePair("auditSuggestions", ""));
        urlParameters.add(new BasicNameValuePair("auditSelected", ""));

        String html = httpRetriever.searchMovie(searchUrl, urlParameters);
        String movieUrl = jinniParser.parseUrl(html);
        if (movieUrl == null)
            throw new JinniMovieNotFoundException(title);
        return movieUrl;
    }

    public String toUrl(String title)
    {
        title = removeDirtyCharacters(title);
        title = title.replace("   ", " ");
        title = title.replace("  ", " ");
        title = title.replace(' ', '-');
        String urlTitle = postProcessing(title);
        return urlTitle.toLowerCase();
    }

    private String postProcessing(String urlTitle)
    {
        if (urlTitle.length() == 0) return urlTitle;
        StringBuilder sb = new StringBuilder();
        sb.append(urlTitle.charAt(0));
        for (int i = 1; i < urlTitle.length() - 1; i++)
        {
            Character ch = urlTitle.charAt(i);
            if (ch == '-' && urlTitle.charAt(i - 1) == '-' && urlTitle.charAt(i + 1) == '-')
            {
                i++;
            }
            else
            {
                sb.append(ch);
            }
        }
        if (urlTitle.length() > 1)
            sb.append(urlTitle.charAt(urlTitle.length() - 1));
        return sb.toString();
    }

    private String removeDirtyCharacters(String title)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < title.length(); i++)
        {
            char ch = title.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
                    (ch >= '0' && ch <= '9') || ch == '-' || ch == SPACE)
            {
                sb.append(ch);
            }
            else if (Arrays.binarySearch(latinExtendedMapping.keySet().toArray(), ch) != -1)
            {
                sb.append(latinExtendedMapping.get(ch));
            }
            else if (Arrays.binarySearch(charactersMapping.keySet().toArray(), ch) != -1)
            {
                if (i > 0 && i < title.length() - 1)
                {
                    sb.append(charactersMapping.get(ch));
                }
                // First and last characters
                else
                {
                    sb.append("");
                }
            }
        }
        return sb.toString();
    }

    private String constructUrl(long id)
    {
        return BASE_URL + id + API_KEY;
    }

    private void initializeLatinExtendedMap()
    {
        latinExtendedMapping.put('A', "a");
        latinExtendedMapping.put('Á', "a");
        latinExtendedMapping.put('Â', "a");
        latinExtendedMapping.put('Ã', "a");
        latinExtendedMapping.put('Ä', "a");
        latinExtendedMapping.put('Å', "a");
        latinExtendedMapping.put('È', "e");
        latinExtendedMapping.put('É', "e");
        latinExtendedMapping.put('Ê', "e");
        latinExtendedMapping.put('Ë', "e");
        latinExtendedMapping.put('Ì', "i");
        latinExtendedMapping.put('Í', "i");
        latinExtendedMapping.put('Î', "i");
        latinExtendedMapping.put('Ï', "i");
        latinExtendedMapping.put('Đ', "d");
        latinExtendedMapping.put('Ñ', "n");
        latinExtendedMapping.put('Ò', "o");
        latinExtendedMapping.put('Ó', "o");
        latinExtendedMapping.put('Ô', "o");
        latinExtendedMapping.put('Õ', "o");
        latinExtendedMapping.put('Ö', "o");
        latinExtendedMapping.put('Ù', "u");
        latinExtendedMapping.put('Ú', "u");
        latinExtendedMapping.put('Û', "u");
        latinExtendedMapping.put('Ü', "z");

        latinExtendedMapping.put('à', "a");
        latinExtendedMapping.put('á', "a");
        latinExtendedMapping.put('â', "a");
        latinExtendedMapping.put('ã', "a");
        latinExtendedMapping.put('ä', "a");
        latinExtendedMapping.put('å', "a");
        latinExtendedMapping.put('è', "e");
        latinExtendedMapping.put('é', "e");
        latinExtendedMapping.put('ê', "e");
        latinExtendedMapping.put('ë', "e");
        latinExtendedMapping.put('ì', "i");
        latinExtendedMapping.put('í', "i");
        latinExtendedMapping.put('î', "i");
        latinExtendedMapping.put('ï', "i");
        latinExtendedMapping.put('ñ', "n");
        latinExtendedMapping.put('ò', "o");
        latinExtendedMapping.put('ó', "o");
        latinExtendedMapping.put('ô', "o");
        latinExtendedMapping.put('õ', "o");
        latinExtendedMapping.put('ö', "o");
        latinExtendedMapping.put('ù', "u");
        latinExtendedMapping.put('ú', "u");
        latinExtendedMapping.put('û', "u");
        latinExtendedMapping.put('ü', "u");
    }

    private void initializeCharactersMap()
    {
        charactersMapping.put('.', " ");
        charactersMapping.put('?', " ");
        charactersMapping.put('!', " ");
        charactersMapping.put(',', " ");
        charactersMapping.put(':', " ");
        charactersMapping.put('\'', "");
        charactersMapping.put('’', "");
        charactersMapping.put('(', " ");
        charactersMapping.put(')', " ");
        charactersMapping.put('/', " ");
        charactersMapping.put('&', "and");
    }

    public void setHttpRetriever(HttpRetriever httpRetriever)
    {
        this.httpRetriever = httpRetriever;
    }

    public void setJsonParser(JSONParser jsonParser)
    {
        this.jsonParser = jsonParser;
    }

    public void setJinniParser(JinniParser jinniParser)
    {
        this.jinniParser = jinniParser;
    }


}
