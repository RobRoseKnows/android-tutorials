package com.example.android.spotifyproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Robert on 7/30/2015.
 */
public class TrackItemAdapter extends ArrayAdapter<Track> {
    private int layout;
    private List<Track> tracks;
    private final String LOG_TAG = TrackItemAdapter.class.getSimpleName();

    public TrackItemAdapter(Context context, int resource, List<Track> items) {
        super(context, resource, items);
        layout = resource;
        tracks = items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Track getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        RelativeLayout itemView;
        Track track = tracks.get(position);

        if(convertView == null) {
            itemView = new RelativeLayout(getContext());

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            row = layoutInflater.inflate(layout, itemView, true);
        } else {
            row = convertView;
        }

        if(track != null) {
            TextView trackView = (TextView) row.findViewById(R.id.songTextView);
            String trackText = track.name + "\n" + track.album.name;
            trackView.setText(trackText);

            //Log.v(LOG_TAG, "Artist: " + position + " Num Images: " + artist.images.size());
            if(track.album.images.size() > 1) {
                Picasso.with(getContext()).load(track.album.images.get(track.album.images.size() - 1).url).into((ImageView) row.findViewById(R.id.songImageView));
            }
            row.setTag(track.id);
        }

        return row;
    }
}
