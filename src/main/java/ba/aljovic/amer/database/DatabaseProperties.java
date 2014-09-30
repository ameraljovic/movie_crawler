package ba.aljovic.amer.database;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db")
public class DatabaseProperties
{
    private String driverClassName = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/genome";
    private String username;
    private String password;

    //region GETTERS & SETTERS
    public String getDriverClassName()
    {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName)
    {
        this.driverClassName = driverClassName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    //endregion
}
