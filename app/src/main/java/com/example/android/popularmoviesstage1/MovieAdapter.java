package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMDB_IMAGE_SIZE = "w185";

    MovieAdapter(@NonNull Context context, @NonNull Movie[] movieArray) {
        super(context, 0, movieArray);
    }

    @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie movie = getItem(position);

        // Populate the data into the view using the data object
        assert movie != null;
        Log.d(LOG_TAG, movie.getPosterPath());
        String posterImageURLString = IMAGE_BASE_URL + IMDB_IMAGE_SIZE + movie.getPosterPath();
        Context context = this.getContext();
        Picasso.with(context).load(posterImageURLString).into(viewHolder.imageView);

        // Return the completed view to render on screen
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            this.imageView = view.findViewById(R.id.iv_grid);
        }
    }

}
