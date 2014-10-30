package ba.aljovic.amer.application.exception;

public class TmdbMovieNotFoundException extends Exception
{
    private final Long tmdbId;

    public TmdbMovieNotFoundException(Long tmdbId)
    {
        this.tmdbId = tmdbId;
    }

    public Long getTmdbId()
    {
        return tmdbId;
    }
}
