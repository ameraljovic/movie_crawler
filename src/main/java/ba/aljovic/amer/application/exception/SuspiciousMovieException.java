package ba.aljovic.amer.application.exception;

public class SuspiciousMovieException extends Exception
{
    private Integer id;

    public SuspiciousMovieException(Integer id)
    {
        this.id = id;
    }
    public SuspiciousMovieException() {}

    public Integer getId()
    {
        return id;
    }
}
