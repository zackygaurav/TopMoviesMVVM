package in.itechvalley.topmovies.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.itechvalley.topmovies.R;
import in.itechvalley.topmovies.model.SingleMovieModel;
import in.itechvalley.topmovies.utils.Constants;
import in.itechvalley.topmovies.viewmodel.TopMoviesViewModel;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    /*
     * LOG
     * */
    private static final String TAG = "MainActivity";

    @BindView(android.R.id.content)
    View currentView;

    @BindView(R.id.swipe_refresh_activity_main)
    SwipeRefreshLayout swipeRefreshLayout;

    /*
     * Instance of ViewModel
     * */
    private TopMoviesViewModel viewModel;

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
         * Attach onRefresh(...) to SwipeRefreshLayout
         * */
        swipeRefreshLayout.setOnRefreshListener(this);

        /*
         * Init the Global Instance of ViewModel
         * */
        viewModel = ViewModelProviders.of(this).get(TopMoviesViewModel.class);
        /*
         * Attemp to get movies list from API
         * */
        viewModel.requestMovieList();
        /*
        * Trigger swipeRefreshLayout
        * */
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        /*
        * Alt + Enter and select Replace with Lambda Funtion
        * */
        viewModel.getMoviesListObserver().observe(this, new Observer<List<SingleMovieModel>>()
        {
            @Override
            public void onChanged(List<SingleMovieModel> singleMovieModels)
            {
                if (singleMovieModels != null)
                {
                    if (singleMovieModels.size() > 0)
                    {
                        // TODO Hide Empty Animation or UI
                        // TODO Set this List to RecyclerView Adapter
                    }
                    else
                    {
                        // TODO Show Empty Animation or UI
                        // TODO Hide RecyclerView
                    }

                    Toast.makeText(MainActivity.this, "Size is " + singleMovieModels.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.observeNetworkErrors().observe(this, statusCode ->
        {
            Log.e(TAG, String.format("Get MoviesList Status Code: %d", statusCode));

            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);

            switch (statusCode)
            {
                case Constants.StatusCodes.STATUS_CODE_SUCCESS:
                {
                    /*
                     * This means Request was processed successfully. Remove the Observer.
                     * */

                    break;
                }

                case Constants.StatusCodes.ERROR_CODE_RESPONSE_FAILED:
                {
                    /*
                     * This means Request was processed successfully but value of success is false
                     * */

                    break;
                }

                case Constants.StatusCodes.ERROR_CODE_RESPONSE_BODY_NULL:
                {
                    /*
                     * This means the Response is NULL
                     * */

                    break;
                }

                case Constants.StatusCodes.ERROR_CODE_UNKNOWN_HOST_EXCEPTION:
                {
                    /*
                     * This means the onFailure was called and UnknownHostException occurred.
                     * */

                    break;
                }

                case Constants.StatusCodes.ERROR_CODE_SOCKET_TIMEOUT:
                {
                    /*
                     * This means the onFailure was called and SocketTimeout occurred.
                     * */

                    break;
                }

                case Constants.StatusCodes.ERROR_CODE_FAILURE_UNKNOWN:
                {
                    /*
                     * This means the onFailure was called due to some error. Error is Unknown. Check LogCat for details.
                     * */
                    // Tools.showSnackBar(rootView, "Some error Occurred. Please try again. \uD83D\uDE15", Snackbar.LENGTH_INDEFINITE);

                    break;
                }
            }
        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        viewModel.getMoviesListObserver().removeObservers(this);
        viewModel.observeNetworkErrors().removeObservers(this);
    }

    @Override
    public void onRefresh()
    {
        viewModel.requestMovieList();
    }
}
