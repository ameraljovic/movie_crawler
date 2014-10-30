package ba.aljovic.amer.application.utils;

public class Utils
{
    public static final String FROM_ID = "fromId";
    public static final String TO_ID = "toId";

    public static Integer toInteger(Boolean booleanValue)
    {
        return (booleanValue) ? 1 : 0;
    }
}
