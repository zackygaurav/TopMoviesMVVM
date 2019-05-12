package in.itechvalley.topmovies.network;

import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.utils.Constants;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TopMoviesApiService
{
    @GET(Constants.Urls.ENDPOINT_ALL_MOVIES)
    Call<TopMoviesModel> getAllMovies();
}
