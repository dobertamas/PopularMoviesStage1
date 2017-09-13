package com.example.android.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

class Movie implements Parcelable {


   /* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
    original title
    movie poster image thumbnail
    A plot synopsis (called overview in the api)
    user rating (called vote_average in the api)
    release date*/

    /**
     * The poster path value.
     */
    private String mPosterPath;

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
    private String mReleaseDateString;

    /**
     * The mId of the movie.
     */
    private String mId;


    String getPosterPath() {
        return mPosterPath;
    }

    void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    String getOriginalTitle() {
        return mOriginalTitle;
    }

    void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    String getOverview() {
        return mOverview;
    }

    void setOverview(String overview) {
        mOverview = overview;
    }

    String getVoteAverage() {
        return mVoteAverage;
    }

    void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    String getReleaseDateString() {
        return mReleaseDateString;
    }

    void setReleaseDateString(String releaseDateString) {
        mReleaseDateString = releaseDateString;
    }

    String getId() {
        return mId;
    }

    void setId(String id) {
        this.mId = id;
    }

    Movie() {
    }

    private Movie(Parcel in) {
        mPosterPath = in.readString();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readString();
        mReleaseDateString = in.readString();
        mId = in.readString();
    }

    @Override public String toString() {
        return "Movie{" +
                "mPosterPath='" + mPosterPath + '\'' +
                ", mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mVoteAverage='" + mVoteAverage + '\'' +
                ", mReleaseDateString='" + mReleaseDateString + '\'' +
                ", mId='" + mId + '\'' +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPosterPath);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mVoteAverage);
        parcel.writeString(mReleaseDateString);
        parcel.writeString(mId);
    }

    // We need to create the `Parcelable.Creator<MyParcelable> CREATOR` constant for our class.
    // An interface that must be implemented and provided as a public CREATOR field
    // that generates instances of the Parcelable class from a Parcel.
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object
        @Override public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
