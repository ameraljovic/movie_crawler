package ba.aljovic.amer.application.exception;

public class PageNotFoundException extends Exception
{
    private String url;

    public PageNotFoundException(String url)
    {
        super("");
        this.url = url;
    }

    public PageNotFoundException(String message, String url)
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
