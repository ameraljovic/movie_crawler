package ba.aljovic.amer.database.entity;

public enum StatusEnum
{
    UNKNOWN(0),
    SUSPICIOUS(1),
    RECOVERED(2),
    FAILED(3);

    private int statusCode;

    private StatusEnum(int s)
    {
        statusCode = s;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
