package ba.aljovic.amer.application.database.entities.userratingsjob;

import ba.aljovic.amer.application.database.entities.jinnijob.Movie;

import javax.persistence.*;

@Entity
@Table(name = "movie_rating")
public class MovieRating
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ImdbUser user;

    public MovieRating(int rating, Movie movie, ImdbUser user)
    {
        this.rating = rating;
        this.movie = movie;
        this.user = user;
    }

    //region GETTERS & SETTERS
    public Long getId()
    {
        return id;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public Movie getMovie()
    {
        return movie;
    }

    public void setMovie(Movie movie)
    {
        this.movie = movie;
    }

    public ImdbUser getUser()
    {
        return user;
    }

    public void setUser(ImdbUser user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "MovieRating{" +
                "id=" + id +
                ", rating=" + rating +
                ", movie=" + movie +
                ", user=" + user +
                '}';
    }
    //endregion
}
