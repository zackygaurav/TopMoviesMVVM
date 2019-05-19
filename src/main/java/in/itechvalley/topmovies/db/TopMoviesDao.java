package in.itechvalley.topmovies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import in.itechvalley.topmovies.model.SingleMovieModel;

@Dao
public interface TopMoviesDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] cacheDataLocally(List<SingleMovieModel> moviesList);

    @Query("SELECT * FROM table_movies")
    LiveData<List<SingleMovieModel>> getCachedData();

    @Query("DELETE FROM table_movies")
    int truncateTable();

    /*
    * int
    * long or long[]
    * List
    * LiveData
    * */

    /*
    * @Query("SELECT * FROM table_movies WHERE title = :query")
    * void getCachedData(String query);
    * */
}
