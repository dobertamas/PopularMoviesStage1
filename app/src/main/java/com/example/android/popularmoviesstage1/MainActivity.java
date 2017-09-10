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

    private static final int EXISTING_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isOnline()) {
            // Initialize a loader to read movie data from themoviedb.org
            // and display the current values in the editor.
            // The first parameter is a unique ID that identifies the loader.
            // Optional arguments to supply to the loader at construction - null in this case.
            getLoaderManager().initLoader(EXISTING_LOADER, null, this).forceLoad();
        }
        else {
            Toast.makeText(MainActivity.this, "We have no connectivity!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    // Using anonymous inner class to provide AsyncTaskLoader functionality
    // We create a Loader object and return it to the system inside this callback method.
    @Override public Loader<Movie[]> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Movie[]>(MainActivity.this) {
            @Override public Movie[] loadInBackground() {

                // Create URL object
                // Perform HTTP request to the URL and receive a JSON response back
                // extract data from Json with error handling. You should get back a Movie[]
                // return that Array
                return new Movie[0];
            }
        };
    }

    // With this callback method we'll want display the data to the user.
    @Override public void onLoadFinished(Loader<Movie[]> loader, Movie[] movies) {

        // Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);

    }

    // We should remove any references the activity has to the loader's data.
    @Override public void onLoaderReset(Loader<Movie[]> loader) {

    }
}
