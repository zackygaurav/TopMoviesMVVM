package in.itechvalley.topmovies.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.itechvalley.topmovies.R;
import in.itechvalley.topmovies.model.SingleMovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.network.TopMoviesApiService;
import in.itechvalley.topmovies.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    /*
    * LOG
    * */
    private static final String TAG = "MainActivity";

    @BindView(android.R.id.content)
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Attach ButterKnife to current Activity
        * */
        ButterKnife.bind(this);

        /*
        * HttpLoggingInterceptor to print the response in Logcat
        * */
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        /*
        * OkHttpClient
        * */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        /*
        * Retrofit's Builder
        * */
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Constants.Urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);

        /*
        * Get Instance of Retrofit from it's Builder
        * */
        Retrofit retrofit = retrofitBuilder.build();

        /*
        * Create the Instance of TopMoviesApiService
        * */
        TopMoviesApiService apiService = retrofit.create(TopMoviesApiService.class);
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
                    return;
                }

                List<SingleMovieModel> moviesList = responseBody.getMoviesList();
                Snackbar.make(
                        currentView,
                        String.format(Locale.ENGLISH,"Total Movies: %d", moviesList.size()),
                        Snackbar.LENGTH_INDEFINITE
                ).show();
            }

            @Override
            public void onFailure(@NonNull Call<TopMoviesModel> call, @NonNull Throwable throwable)
            {
                Log.e(TAG, "Failed to load Movies", throwable);
            }
        });
    }
}
