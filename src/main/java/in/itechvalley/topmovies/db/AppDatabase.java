package in.itechvalley.topmovies.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import in.itechvalley.topmovies.model.SingleMovieModel;

@Database(entities = {SingleMovieModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    private static final String DATABASE_NAME = "topmovies.db";

    /*
    * Singleton Approach to return the Instance of this class (AppDatabase)
    * */
    private static AppDatabase INSTANCE;

    /*
    * An Abstract Method (without body) to get the Instance of Dao
    * */
    public abstract TopMoviesDao getTopMoviesDao();

    public static AppDatabase getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        }

        return INSTANCE;
    }
}
