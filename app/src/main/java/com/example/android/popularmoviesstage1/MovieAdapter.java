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
import android.widget.TextView;


class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

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
        Log.d(LOG_TAG, " getting item");

        // Populate the data into the view using the data object
        assert movie != null;
        viewHolder.originalTitle.setText(movie.getOriginalTitle());
        viewHolder.imageView.setImageResource(movie.getPosterImageThumbnailResourceId());

        // Return the completed view to render on screen
        return convertView;

    }

    private class ViewHolder {
        TextView originalTitle;
        ImageView imageView;

        ViewHolder(View view) {
            this.originalTitle = view.findViewById(R.id.tv_grid);
            this.imageView = view.findViewById(R.id.iv_grid);
        }
    }

}
