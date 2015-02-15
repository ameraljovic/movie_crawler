package ba.aljovic.amer.application.database.entities.userratingsjob;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "imdb_users")
public class ImdbUser
{
    @Id
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "user")
    private List<MovieRating> ratings;

    //region GETTERS & SETTERS
    public Long getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public List<MovieRating> getRatings()
    {
        return ratings;
    }

    public void setRatings(List<MovieRating> ratings)
    {
        this.ratings = ratings;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImdbUser imdbUser = (ImdbUser)o;

        if (id != null ? !id.equals(imdbUser.id) : imdbUser.id != null) return false;
        if (ratings != null ? !ratings.equals(imdbUser.ratings) : imdbUser.ratings != null) return false;
        if (url != null ? !url.equals(imdbUser.url) : imdbUser.url != null) return false;
        if (username != null ? !username.equals(imdbUser.username) : imdbUser.username != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (ratings != null ? ratings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "ImdbUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", url='" + url + '\'' +
                ", ratings=" + ratings +
                '}';
    }
    //endregion
}