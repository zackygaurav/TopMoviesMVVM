package in.itechvalley.topmovies.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import in.itechvalley.topmovies.model.SingleMovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.network.RetrofitClient;
import in.itechvalley.topmovies.network.TopMoviesApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopMoviesViewModel extends AndroidViewModel
{
    /*
    * logt
    * */
    private static final String TAG = "TopMoviesViewModel";

    /*
    * MutalbleLiveData to observe List<SingleMovieModel>
    * */
    private MutableLiveData<List<SingleMovieModel>> moviesListObserver = new MutableLiveData<>();

    /*
    * MutalbleLiveData to observe API Errors or Message
    * */
    private MutableLiveData<String> apiObserver = new MutableLiveData<>();

    /*
    * Constructor
    * */
    public TopMoviesViewModel(@NonNull Application application)
    {
        super(application);
    }

    public void requestMovieList()
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
                    apiObserver.postValue("Server returned null");
                    return;
                }

                List<SingleMovieModel> moviesList = responseBody.getMoviesList();
                moviesListObserver.postValue(moviesList);
                apiObserver.postValue("");
            }

            @Override
            public void onFailure(@NonNull Call<TopMoviesModel> call, @NonNull Throwable throwable)
            {
                Log.e(TAG, "Failed to load Movies", throwable);

                if (throwable instanceof UnknownHostException)
                {
                    apiObserver.postValue("No Internet");
                }
                else if (throwable instanceof SocketTimeoutException)
                {
                    apiObserver.postValue("Socket Timeout");
                }
                else
                {
                    apiObserver.postValue(throwable.getMessage());
                }
            }
        });
    }

    public LiveData<List<SingleMovieModel>> getMoviesListObserver()
    {
        return moviesListObserver;
    }

    public LiveData<String> getApiObserver()
    {
        return apiObserver;
    }
}
