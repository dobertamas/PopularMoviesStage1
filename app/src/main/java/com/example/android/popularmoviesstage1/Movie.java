package com.example.android.popularmoviesstage1;

import java.net.URL;
import java.util.Date;

public class Movie {

    /**
     * The resource ID to locate the image associated with the given Attraction.
     * The resource ID can be null.
     */
    private int mPosterImageThumbnailResourceId;

    /**
     * The HTTP URl for the movie poster image.
     */
    private URL mPosterImageImdbUrl;

    /**
     * The original title.
     */
    private String mOriginalTitle;

    /**
     * A plot synopsis.
     * Also called overview in the api.
     */
    private String mOverview;

    /**
     * The user rating.
     * Also called vote_average in the api.
     */
    private String mVoteAverage;

    /**
     * The release date.
     */
    private Date mReleaseDate;

    public int getPosterImageThumbnailResourceId() {
        return mPosterImageThumbnailResourceId;
    }

    public void setPosterImageThumbnailResourceId(int posterImageThumbnailResourceId) {
        mPosterImageThumbnailResourceId = posterImageThumbnailResourceId;
    }

    public URL getPosterImageImdbUrl() {
        return mPosterImageImdbUrl;
    }

    public void setPosterImageImdbUrl(URL posterImageImdbUrl) {
        mPosterImageImdbUrl = posterImageImdbUrl;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        mReleaseDate = releaseDate;
    }

// TODO: Generate toString()
}
