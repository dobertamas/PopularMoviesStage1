package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.android.popularmoviesstage1.MainActivity.MOVIE_DATA;

public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @InjectView(R.id.tv_original_title) TextView mOriginalTitle;
    @InjectView(R.id.tv_overview) TextView mOverview;
    @InjectView(R.id.tv_vote_average) TextView mVoteAverage;
    @InjectView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @InjectView(R.id.tv_id) TextView mIdTextView;
    @InjectView(R.id.poster_image) ImageView mImageView;

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMDB_IMAGE_SIZE = "w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Movie movie = intent.getExtras().getParcelable(MOVIE_DATA);
        if (movie != null) {
            Log.d(LOG_TAG, movie.getOriginalTitle());
            Log.d(LOG_TAG, movie.getOverview());
        }
        assert movie != null;
        mOriginalTitle.setText(movie.getOriginalTitle());
        mOverview.setText(movie.getOverview());
        mVoteAverage.setText(movie.getVoteAverage());
        mReleaseDateTextView.setText(movie.getReleaseDateString());

        String posterImageURLString = IMAGE_BASE_URL + IMDB_IMAGE_SIZE + movie.getPosterPath();
        Log.d(LOG_TAG, " posterImageURLString " + posterImageURLString);
        Picasso.with(this).load(posterImageURLString).into(mImageView);
    }

}
