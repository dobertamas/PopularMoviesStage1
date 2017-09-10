package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie[]> {

    private static final int EXISTING_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isOnline()) {
            // Initialize a loader to read movie data from themoviedb.org
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_LOADER, null, this).forceLoad();
        }
        else {
            Toast.makeText(MainActivity.this, "No connectivity!", Toast.LENGTH_LONG).show();
        }
    }


    public boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    // Using anonymous inner class to provide AsyncTaskLoader functionality
    @Override public Loader<Movie[]> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Movie[]>(MainActivity.this) {
            @Override public Movie[] loadInBackground() {
                return new Movie[0];
            }
        };
    }

    @Override public void onLoadFinished(Loader<Movie[]> loader, Movie[] movies) {

    }

    @Override public void onLoaderReset(Loader<Movie[]> loader) {

    }
}
