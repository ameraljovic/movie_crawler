package ba.aljovic.amer.application.exception;

public class NoPageException extends Exception
{
    private String url;

    public NoPageException(String url)
    {
        super("");
        this.url = url;
    }

    public NoPageException(String message, String url)
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
