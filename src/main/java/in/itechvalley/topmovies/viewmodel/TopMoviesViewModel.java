package in.itechvalley.topmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import in.itechvalley.topmovies.TopMoviesApp;
import in.itechvalley.topmovies.model.SingleMovieModel;
import in.itechvalley.topmovies.repo.TopMoviesRepo;

public class TopMoviesViewModel extends AndroidViewModel
{
    /*
    * logt
    * */
    private static final String TAG = "TopMoviesViewModel";

    /*
    * Inject the Dependency of TopMoviesRepo
    * */
    private TopMoviesRepo repo;

    /*
    * Constructor
    * */
    public TopMoviesViewModel(@NonNull Application application)
    {
        super(application);

        /*
        * Inject the Dependency from Application class (TopMoviesApp)
        * */
        repo = ((TopMoviesApp) application).provideTopMoviesRepo();
    }

    public void requestMovieList()
    {
        repo.getMovieData();
    }

    public LiveData<List<SingleMovieModel>> getMoviesListObserver()
    {
        return repo.getDatabaseObserver();
    }

    public LiveData<Integer> observeNetworkErrors()
    {
        return repo.getNetworkErrorObserver();
    }
}
