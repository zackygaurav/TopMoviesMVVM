package in.itechvalley.topmovies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "table_movies", indices = @Index(value = "title", unique = true))
public class SingleMovieModel implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    @SerializedName(value = "Title", alternate = "title")
    private String movieTitle;

    @ColumnInfo(name = "genre")
    @SerializedName(value = "Genre", alternate = "genre")
    private String movieGenre;

    @ColumnInfo(name = "year")
    @SerializedName(value = "Year", alternate = "year")
    private String movieYear;

    @Ignore
    @SerializedName(value = "Director", alternate = "director")
    private String movieDirector;

    /*
    * Constructor
    * */
    public SingleMovieModel(int id, String movieTitle, String movieGenre, String movieYear)
    {
        this.id = id;
        this.movieTitle = movieTitle;
        this.movieGenre = movieGenre;
        this.movieYear = movieYear;
    }

    /*
    * Getters
    * */
    public int getId()
    {
        return id;
    }

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

    public String getMovieDirector()
    {
        return movieDirector;
    }
}
