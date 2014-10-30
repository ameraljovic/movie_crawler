package ba.aljovic.amer.application.exception;

@SuppressWarnings ("UnusedDeclaration")
public class JinniMovieNotFoundException extends Exception
{
    private final String movieTitle;
    private final String url;
    private final Integer id;

    public JinniMovieNotFoundException(String movieTitle, String url, Integer id)
    {
        this.movieTitle = movieTitle;
        this.url = url;
        this.id = id;
    }

    public JinniMovieNotFoundException(String movieTitle, String url)
    {
        this.movieTitle = movieTitle;
        this.url = url;
        this.id = null;
    }

    public JinniMovieNotFoundException(String movieTitle)
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
