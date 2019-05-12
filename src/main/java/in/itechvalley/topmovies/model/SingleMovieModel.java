package in.itechvalley.topmovies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SingleMovieModel implements Serializable
{
    @SerializedName(value = "Title", alternate = "title")
    private String movieTitle;

    @SerializedName(value = "Genre", alternate = "genre")
    private String movieGenre;

    /*
    * Constructor
    * */
    public SingleMovieModel(String movieTitle, String movieGenre)
    {
        this.movieTitle = movieTitle;
        this.movieGenre = movieGenre;
    }

    /*
    * Getters
    * */
    public String getMovieTitle()
    {
        return movieTitle;
    }

    public String getMovieGenre()
    {
        return movieGenre;
    }
}
