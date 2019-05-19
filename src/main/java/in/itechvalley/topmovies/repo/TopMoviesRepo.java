package in.itechvalley.topmovies.repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import in.itechvalley.topmovies.db.AppDatabase;
import in.itechvalley.topmovies.model.SingleMovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.network.RetrofitClient;
import in.itechvalley.topmovies.network.TopMoviesApiService;
import in.itechvalley.topmovies.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopMoviesRepo
{
    /*
     * LOG
     * */
    private static final String TAG = "TopMoviesRepo";

    /*
     * Global Instance of TopMoviesRepo
     * */
    private static TopMoviesRepo INSTANCE;

    /*
     * Global Instance of MediatorLiveData to observe Database (Single Source of Truth)
     * */
    private MediatorLiveData<List<SingleMovieModel>> databaseObserver;

    /*
     * MutalbleLiveData to observe API Errors or Message
     * */
    private MutableLiveData<Integer> networkErrorObserver = new MutableLiveData<>();

    /*
     * Global Instance of Database (AppDatabase)
     * */
    private AppDatabase appDatabase;

    /*
     * A Method to return the Instance of TopMoviesRepo
     * */
    public static TopMoviesRepo getInstance(AppDatabase appDatabase)
    {
        if (INSTANCE == null)
        {
            /*
             * Init TopMoviesRepo
             * */
            INSTANCE = new TopMoviesRepo(appDatabase);
        }

        return INSTANCE;
    }

    /*
     * Constructor
     * */
    public TopMoviesRepo(AppDatabase appDatabase)
    {
        this.appDatabase = appDatabase;

        /*
         * Init Global Instance of MediatorLiveData to observe Database (Single Source of Truth)
         * */
        databaseObserver = new MediatorLiveData<>();
        databaseObserver.addSource(appDatabase.getTopMoviesDao().getCachedData(), new Observer<List<SingleMovieModel>>()
        {
            @Override
            public void onChanged(List<SingleMovieModel> movieListCachedFromDb)
            {
                if (movieListCachedFromDb == null)
                    return;

                databaseObserver.postValue(movieListCachedFromDb);
            }
        });
    }

    /**
     * This method will call API and store data to Database using Room
     */
    public void getMovieData()
    {
        /*
         * Create the Instance of TopMoviesApiService
         * */
        TopMoviesApiService apiService = new RetrofitClient()
                .getRetrofit()
                .create(TopMoviesApiService.class);
        /*
         * Make a call to API
         * */
        apiService.getAllMovies().enqueue(new Callback<TopMoviesModel>()
        {
            @Override
            public void onResponse(@NonNull Call<TopMoviesModel> call, @NonNull Response<TopMoviesModel> response)
            {
                TopMoviesModel responseBody = response.body();
                if (responseBody == null)
                {
                    networkErrorObserver.postValue(Constants.StatusCodes.ERROR_CODE_RESPONSE_BODY_NULL);
                    return;
                }

                if (responseBody.isSuccess())
                {
                    networkErrorObserver.postValue(Constants.StatusCodes.STATUS_CODE_SUCCESS);

                    /*
                     * 1. Get the List
                     * 2. Truncate Existing Table in Room
                     * 3. Pass the List to Room
                     * */

                    new Thread(() ->
                    {
                        /*
                         * Get the List
                         * */
                        List<SingleMovieModel> moviesList = responseBody.getMoviesList();

                        /*
                         * Truncate the Table
                         * */
                        appDatabase.getTopMoviesDao().truncateTable();

                        /*
                         * Pass the List to Room
                         * */
                        appDatabase.getTopMoviesDao().cacheDataLocally(moviesList);
                    }).start();
                }
                else
                {
                    networkErrorObserver.postValue(Constants.StatusCodes.ERROR_CODE_RESPONSE_FAILED);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TopMoviesModel> call, @NonNull Throwable throwable)
            {
                Log.e(TAG, "Failed to load Movies", throwable);

                if (throwable instanceof UnknownHostException)
                {
                    networkErrorObserver.postValue(Constants.StatusCodes.ERROR_CODE_UNKNOWN_HOST_EXCEPTION);
                }
                else if (throwable instanceof SocketTimeoutException)
                {
                    networkErrorObserver.postValue(Constants.StatusCodes.ERROR_CODE_SOCKET_TIMEOUT);
                }
                else
                {
                    networkErrorObserver.postValue(Constants.StatusCodes.ERROR_CODE_FAILURE_UNKNOWN);
                }
            }
        });
    }

    public LiveData<List<SingleMovieModel>> getDatabaseObserver()
    {
        return databaseObserver;
    }

    public LiveData<Integer> getNetworkErrorObserver()
    {
        return networkErrorObserver;
    }
}
