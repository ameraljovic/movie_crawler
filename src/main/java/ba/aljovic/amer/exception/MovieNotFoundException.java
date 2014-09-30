package ba.aljovic.amer.exception;

@SuppressWarnings ("UnusedDeclaration")
public class MovieNotFoundException extends Exception
{
    private final String movieTitle;
    private final String url;
    private final Integer id;

    public MovieNotFoundException(String movieTitle, String url, Integer id)
    {
        this.movieTitle = movieTitle;
        this.url = url;
        this.id = id;
    }

    public MovieNotFoundException(String movieTitle, String url)
    {
        this.movieTitle = movieTitle;
        this.url = url;
        this.id = null;
    }

    public MovieNotFoundException(String movieTitle)
    {
        this.movieTitle = movieTitle;
        this.url = null;
        this.id = null;
    }

    public String getMovieTitle()
    {
        return movieTitle;
    }

    public String getUrl()
    {
        return url;
    }

    public Integer getId()
    {
        return id;
    }
}
