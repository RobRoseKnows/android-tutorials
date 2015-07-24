package com.example.android.spotifyproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Robert on 7/21/2015.
 */
public class ArtistItemAdapter extends ArrayAdapter<Artist> {
    private int layout;
    private List<Artist> artists;
    private final String LOG_TAG = ArtistItemAdapter.class.getSimpleName();

    public ArtistItemAdapter(Context context, int resource, List<Artist> items) {
        super(context, resource, items);
        layout = resource;
        artists = items;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return artists.size();
    }

    @Override
    public Artist getItem(int position) {
        // TODO Auto-generated method stub
        return artists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout itemView;
        Artist artist = getItem(position);
        View row = null;

        if(convertView == null) {
            itemView = new RelativeLayout(getContext());

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            row = layoutInflater.inflate(layout, itemView, true);
        } else {
            row = convertView;
        }

        if(artist != null) {
            ((TextView) row.findViewById(R.id.artistNameTextView)).setText(artist.name);
            //Log.v(LOG_TAG, "Artist: " + position + " Num Images: " + artist.images.size());
            if(artist.images.size() > 1) {
                Picasso.with(getContext()).load(artist.images.get(artist.images.size() - 1).url).into((ImageView) row.findViewById(R.id.artistImageView));
            }
            row.setTag(artist.id);
        }
        return row;
    }
}
