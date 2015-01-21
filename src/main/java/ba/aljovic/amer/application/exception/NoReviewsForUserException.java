package ba.aljovic.amer.application.exception;

public class NoReviewsForUserException extends Exception
{
    private String url;

    public NoReviewsForUserException(String url)
    {
        super("");
        this.url = url;
    }

    public NoReviewsForUserException(String message, String url)
    {
        super(message);
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
