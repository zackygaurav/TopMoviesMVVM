package in.itechvalley.topmovies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SingleMovieModel implements Serializable
{
    @SerializedName(value = "Title", alternate = "title")
    private String movieTitle;

    @SerializedName(value = "Genre", alternate = "genre")
    private String movieGenre;

    @SerializedName(value = "Year", alternate = "year")
    private String movieYear;

    /*
    * Constructor
    * */
    public SingleMovieModel(String movieTitle, String movieGenre, String movieYear)
    {
        this.movieTitle = movieTitle;
        this.movieGenre = movieGenre;
        this.movieYear = movieYear;
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

    public String getMovieYear()
    {
        return movieYear;
    }
}
