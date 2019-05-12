package in.itechvalley.topmovies.network;

import in.itechvalley.topmovies.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{
    private Retrofit retrofit;

    /*
    * Constructor
    * */
    public RetrofitClient()
    {
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
        retrofit = retrofitBuilder.build();
    }

    public Retrofit getRetrofit()
    {
        return retrofit;
    }
}
