package com.example.android.spotifyproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopSongsActivityFragment extends Fragment {
    private final String LOG_TAG = TopSongsActivityFragment.class.getSimpleName();
    private TrackItemAdapter tracksAdapter;
    private List<Track> trackList;


    public TopSongsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        trackList = new ArrayList<Track>();

        tracksAdapter = new TrackItemAdapter(getActivity(), R.layout.list_item_songs, trackList);

        ListView artistsListView = (ListView) rootView.findViewById(R.id.listViewArtists);
        artistsListView.setAdapter(tracksAdapter);
        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                //TODO Open the player here

                //Intent showSongListIntent = new Intent(getActivity(), TopSongsActivity.class);
                //showSongListIntent.putExtra(Intent.EXTRA_TEXT, tracksAdapter.getItem(position).id);
                //startActivity(showSongListIntent);
            }
        });

        String artistId;
        if(savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if(extras == null) {
                artistId = null;
            } else {
                artistId = extras.getString("ARTIST_ID");
            }
        } else {
            artistId = (String) savedInstanceState.getSerializable("ARTIST_ID");
        }

        FetchTopTracksTask fetch = new FetchTopTracksTask();
        fetch.execute(artistId);

        return rootView;
    }

    public class FetchTopTracksTask extends AsyncTask<String, Void, Tracks> {
        private final String LOG_TAG = FetchTopTracksTask.class.getSimpleName();

        protected Tracks doInBackground(String... params) {
            Log.v(LOG_TAG, params[0]);
            if(params.length == 0) {
                Log.v(LOG_TAG, "No artist id given");
                return null;
            }

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            //TODO Give users preferences of what country to select.
            Tracks results = spotify.getArtistTopTrack(params[0], "us"); //TODO Figure out what the correct call is here.
            return results;
        }

        protected void onPostExecute(Tracks result) {
            super.onPostExecute(result);
            if(result != null) {
                tracksAdapter.clear();
                for(Track item : result.tracks) {
                    tracksAdapter.add(item);
                }

                if(result.tracks.size() <= 0) {
                    Toast.makeText(getActivity(), R.string.no_tracks_found, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.w(LOG_TAG, "Something went wrong fetching the top tracks.");
            }
        }
    }
}
