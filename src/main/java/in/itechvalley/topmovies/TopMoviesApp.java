package in.itechvalley.topmovies;

import android.app.Application;
import android.content.Context;

import in.itechvalley.topmovies.db.AppDatabase;
import in.itechvalley.topmovies.repo.TopMoviesRepo;

public class TopMoviesApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public AppDatabase provideAppDatabase(Context context)
    {
        return AppDatabase.getInstance(context);
    }

    public TopMoviesRepo provideTopMoviesRepo()
    {
        return TopMoviesRepo.getInstance(provideAppDatabase(getBaseContext()));
    }
}
