package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie[]> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.main_imageView) ImageView mImageView;

    private static final int EXISTING_LOADER = 0;
    public static final String IMDB_API_KEY = "HIDDEN";
    public static final String IMDB_BASE_URL = "https://api.themoviedb.org/3/movie/550";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private String imdbSize = "w185/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

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
            String urlString;

            @Override public Movie[] loadInBackground() {

                // Create URL object
                URL url = createUrl(IMDB_BASE_URL + "?api_key=" + IMDB_API_KEY);
                // Perform HTTP request to the URL and receive a JSON response back
                String jsonResponse = "";
                try {
                    jsonResponse = makeHttpRequest(url);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Problem making the HTTP request.", e);
                }
                // extract data from Json with error handling. You should get back the poster_path value.
                try {
                    urlString = extracturlStringFromJson(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Construct the poster image URL
                URL posterImageURL = createUrl(IMAGE_BASE_URL + imdbSize  + urlString);

                Movie testMovie = new Movie();
                testMovie.setPosterImageImdbUrl(posterImageURL);
                // return new Movie[0];
                return new Movie[]{testMovie};
            }

            /**
             * Returns new URL object from the given string URL.
             */
            private URL createUrl(String urlString) {
                URL url;
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException exception) {
                    Log.e(LOG_TAG, "Error with creating URL" + urlString, exception);
                    return null;
                }
                return url;
            }

            /**
             * Make an HTTP request to the given URL and return a String as the response.
             */
            private String makeHttpRequest(URL url) throws IOException {
                String jsonResponse = "";

                // If the URL is null, then return early.
                if (url == null) {
                    return jsonResponse;
                }

                HttpURLConnection urlConnection = null;
                InputStream inputStream = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // If the request was successful (response code 200),
                    // then read the input stream and parse the response.
                    if (urlConnection.getResponseCode() == 200) {
                        inputStream = urlConnection.getInputStream();
                        jsonResponse = readFromStream(inputStream);
                    }
                    else {
                        Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (inputStream != null) {
                        // function must handle java.io.IOException here
                        inputStream.close();
                    }
                }
                return jsonResponse;
            }

            /**
             * Convert the {@link InputStream} into a String which contains the
             * whole JSON response from the server.
             */
            private String readFromStream(InputStream inputStream) throws IOException {
                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    String line = reader.readLine();
                    while (line != null) {
                        output.append(line);
                        line = reader.readLine();
                    }
                }
                return output.toString();
            }

            private String extracturlStringFromJson(String jsonResponse) throws JSONException {
                // If the JSON string is empty or null, then return early.
                if (TextUtils.isEmpty(jsonResponse)) {
                    return null;
                }
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);
                return baseJsonResponse.getString("poster_path");
            }

        };
    }

    // With this callback method we'll want display the data to the user.
    @Override public void onLoadFinished(Loader<Movie[]> loader, Movie[] movies) {
        Context context = getApplication().getBaseContext();

        URL testUrl = movies[0].getPosterImageImdbUrl();
        Picasso.with(context).load(testUrl.toString()).into(mImageView);
    }

    // We should remove any references the activity has to the loader's data.
    @Override public void onLoaderReset(Loader<Movie[]> loader) {

    }
}
