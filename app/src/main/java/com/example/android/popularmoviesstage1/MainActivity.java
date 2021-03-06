package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
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

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie[]> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String OPTION = "option";

    MovieAdapter mMovieAdapter;

    @InjectView(R.id.gridView) GridView mGridView;

    private static final int MOVIE_LOADER = 0;

    private static final String IMDB_API_KEY = BuildConfig.API_KEY;

    private static final String IMDB_POPULAR_URL_FIRST_PART = "https://api.themoviedb.org/3/movie/";
    private static final String SECOND_PART_POPULAR = "popular";
    private static final String SECOND_PART_TOP_RATED = "top_rated";
    private static final String IMDB_POPULAR_URL_SECOND_PART = "&language=en-US";

    private static final String UNKNOWN_POSTER_PATH = "unknown poster path";
    private static final String UNKNOWN_ORIGINAL_TITLE = "unknown original title";
    private static final String UNKNOWN_OVERVIEW = "unknown overview";
    public static final String UNKNOWN_VOTE_AVERAGE = "unknown vote_average";
    private static final String UNKNOWN_RELEASE_DATE = "unknown release date";
    private static final String UNKNOWN_ID = "unknown id";

    static final String MOVIE_DATA = "MOVIE_DATA";
    private String movieListUrlString;
    private String option = "popular";

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listing_options, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.top_rated) {
            option = "top_rated";
            loadMovieData(option);
        }
        if (itemThatWasClickedId == R.id.popular) {
            option = "popular";
            loadMovieData(option);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(String option) {
        Log.d(LOG_TAG, MainActivity.this.getResources().getString(R.string.option_value_inside_loadMovieData) + option);

        if (option.equals(SECOND_PART_TOP_RATED)) {
            movieListUrlString = IMDB_POPULAR_URL_FIRST_PART + SECOND_PART_TOP_RATED + "?api_key=" + IMDB_API_KEY + IMDB_POPULAR_URL_SECOND_PART;
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this).forceLoad();
        }
        if (option.equals(SECOND_PART_POPULAR)) {
            movieListUrlString = IMDB_POPULAR_URL_FIRST_PART + SECOND_PART_POPULAR + "?api_key=" + IMDB_API_KEY + IMDB_POPULAR_URL_SECOND_PART;
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this).forceLoad();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (savedInstanceState != null) {
            option = savedInstanceState.getString(OPTION);
        }

        if (isOnline()) {
            if (option.equals("top_rated")) {
                movieListUrlString = IMDB_POPULAR_URL_FIRST_PART + SECOND_PART_TOP_RATED + "?api_key=" + IMDB_API_KEY + IMDB_POPULAR_URL_SECOND_PART;
                getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
            }
            if (option.equals("popular")) {
                movieListUrlString = IMDB_POPULAR_URL_FIRST_PART + SECOND_PART_POPULAR + "?api_key=" + IMDB_API_KEY + IMDB_POPULAR_URL_SECOND_PART;
                getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
            }

            // Initialize a loader to read movie data from themoviedb.org
            // and display the current values in the editor.
            // The first parameter is a unique ID that identifies the loader.
            // Optional arguments to supply to the loader at construction - null in this case.
            getLoaderManager().initLoader(MOVIE_LOADER, null, this).forceLoad();
        }
        else {
            makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.no_connectivity), Toast.LENGTH_LONG).show();
        }

    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    // Using anonymous inner class to provide AsyncTaskLoader functionality
    // We create a Loader object and return it to the system inside this callback method
    @Override public Loader<Movie[]> onCreateLoader(int loaderId, Bundle bundle) {

        return new AsyncTaskLoader<Movie[]>(MainActivity.this) {

            //This will work as a cache variable.
            Movie[] moviesFromJson;

            @Override
            public void onStartLoading() {
                super.onStartLoading();
                if (moviesFromJson != null) {
                    deliverResult(moviesFromJson);
                }
                else {
                    forceLoad();
                }
            }

            // Sends the result of the load to the registered listener
            public void deliverResult(Movie[] data) {
                super.deliverResult(data);
            }

            @Override public Movie[] loadInBackground() {
                // Create URL object
                URL url = createUrl(movieListUrlString);
                // Perform HTTP request to the URL and receive a JSON response back
                String jsonResponse = "";
                try {
                    jsonResponse = makeHttpRequest(url);
                } catch (IOException e) {
                    Log.e(LOG_TAG, MainActivity.this.getResources().getString(R.string.error_message_making_http_request), e);
                }
                // Extract data from Json with error handling.
                try {
                    moviesFromJson = extracturlStringFromJson(jsonResponse);
                    assert moviesFromJson != null;
                    if (moviesFromJson.length != 0) {
                        Log.d(LOG_TAG, MainActivity.this.getResources().getString(R.string.posterPath_from_first_movie) + moviesFromJson[0].getPosterPath());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (moviesFromJson.length == 0) {
                    makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.empty_result_from_json), Toast.LENGTH_LONG).show();
                }
                return moviesFromJson;
            }

            /**
             * Returns new URL object from the given string URL.
             */
            private URL createUrl(String urlString) {
                URL url;
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException exception) {
                    Log.e(LOG_TAG, MainActivity.this.getResources().getString(R.string.error_creating_url) + urlString, exception);
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
                        Log.e(LOG_TAG, MainActivity.this.getResources().getString(R.string.error_response_code) + urlConnection.getResponseCode());
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, MainActivity.this.getResources().getString(R.string.problem_retrieving_json_results), e);
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

            private Movie[] extracturlStringFromJson(String jsonResponse) throws JSONException {
                // If the JSON string is empty or null, then return early.
                if (TextUtils.isEmpty(jsonResponse)) {
                    return null;
                }
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);
                JSONArray resultsArray = baseJsonResponse.getJSONArray("results");

                Movie[] movies = new Movie[resultsArray.length()];

                for (int x = 0; x < resultsArray.length(); x++) {
                    JSONObject movieItem = resultsArray.getJSONObject(x);
                    Movie movieLoopItem = new Movie();

                    // error handling when poster_path is not available - setting UNKNOWN_POSTER_PATH
                    try {
                        String posterPath = movieItem.getString("poster_path");
                        if (posterPath != null) {
                            movieLoopItem.setPosterPath(posterPath);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        movieLoopItem.setPosterPath(UNKNOWN_POSTER_PATH);
                    }

                    // error handling when original_title is not available - setting UNKNOWN_ORIGINAL_TITLE
                    try {
                        String originalTitle = movieItem.getString("original_title");
                        if (originalTitle != null) {
                            movieLoopItem.setOriginalTitle(originalTitle);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        movieLoopItem.setOriginalTitle(UNKNOWN_ORIGINAL_TITLE);
                    }

                    // error handling when poster_path is not available - setting UNKNOWN_OVERVIEW
                    try {
                        String overview = movieItem.getString("overview");
                        if (overview != null) {
                            movieLoopItem.setOverview(overview);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        movieLoopItem.setOverview(UNKNOWN_OVERVIEW);
                    }

                    // error handling when id is not available - setting UNKNOWN_VOTE_AVERAGE
                    try {
                        String voteAverage = movieItem.getString("vote_average");
                        if (voteAverage != null) {
                            movieLoopItem.setVoteAverage(voteAverage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        movieLoopItem.setVoteAverage(UNKNOWN_VOTE_AVERAGE);
                    }

                    // error handling when id is not available - setting UNKNOWN_RELEASE_DATE
                    try {
                        String releaseDate = movieItem.getString("release_date");
                        if (releaseDate != null) {
                            movieLoopItem.setReleaseDateString(releaseDate);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        movieLoopItem.setReleaseDateString(UNKNOWN_RELEASE_DATE);
                    }

                    // error handling when id is not available - setting UNKNOWN_ID
                    try {
                        String id = movieItem.getString("id");
                        if (id != null) {
                            movieLoopItem.setId(id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        movieLoopItem.setId(UNKNOWN_ID);
                    }

                    movies[x] = movieLoopItem;
                }

                return movies;
            }

        };

    }

    // With this callback method we'll want display the data to the user.
    @Override public void onLoadFinished(Loader<Movie[]> loader, final Movie[] movies) {

        if (movies.length == 0) {
            makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.empty_result_set), Toast.LENGTH_LONG).show();
        }

        mMovieAdapter = new MovieAdapter(this, movies);

        mMovieAdapter.notifyDataSetChanged();

        mGridView.setAdapter(mMovieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Movie movie = mMovieAdapter.getItem(position);

                Intent movieDetailstIntent = new Intent(MainActivity.this, DetailsActivity.class);
                movieDetailstIntent.putExtra(MOVIE_DATA, movie);
                startActivity(movieDetailstIntent);
            }
        });
    }

    // We should remove any references the activity has to the loader's data
    @Override public void onLoaderReset(Loader<Movie[]> loader) {
        switch (loader.getId()) {
            case MOVIE_LOADER:
                mMovieAdapter.notifyDataSetInvalidated();
                break;
            default:
                throw new UnsupportedOperationException(MainActivity.this.getResources().getString(R.string.unknown_loader_id) + loader.getId());
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(OPTION, option);
    }
}
