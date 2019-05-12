package in.itechvalley.topmovies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TopMoviesModel implements Serializable
{
    @SerializedName("movies")
    private List<SingleMovieModel> moviesList;

    /*
    * Constructor
    * */
    public TopMoviesModel(List<SingleMovieModel> moviesList)
    {
        this.moviesList = moviesList;
    }

    /*
    * Getters
    * */
    public List<SingleMovieModel> getMoviesList()
    {
        return moviesList;
    }
}
