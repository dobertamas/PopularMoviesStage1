package com.example.android.popularmoviesstage1;

import java.util.Date;

public class Movie {

    /**
     * The resource ID to locate the image associated with the given Attraction.
     * The resource ID can be null.
     */
    private int mPosterImageThumbnailResourceId;

    /**
     * The original title.
     */
    private String originalTitle;

    /**
     * A plot synopsis.
     * Also called overview in the api.
     */
    private String overview;

    /**
     * The user rating.
     * Also called vote_average in the api.
     */
    private String voteAverage;

    /**
     * The release date.
     */
    private Date releaseDate;

}
