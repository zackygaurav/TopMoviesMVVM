package in.itechvalley.topmovies.view;

import android.os.Bundle;
import android.text.TextUtils;
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

        viewModel.getMoviesListObserver().observe(this, new Observer<List<SingleMovieModel>>()
        {
            @Override
            public void onChanged(List<SingleMovieModel> singleMovieModels)
            {
                if (singleMovieModels != null)
                {
                    Toast.makeText(MainActivity.this, "Size is " + singleMovieModels.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getApiObserver().observe(this, message ->
        {
            if (!TextUtils.isEmpty(message))
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        viewModel.getMoviesListObserver().removeObservers(this);
        viewModel.getApiObserver().removeObservers(this);
    }

    @Override
    public void onRefresh()
    {
        viewModel.requestMovieList();
    }
}
