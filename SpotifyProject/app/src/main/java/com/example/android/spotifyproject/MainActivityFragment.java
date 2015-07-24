package com.example.android.spotifyproject;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private ArtistItemAdapter artistsAdapter;
    private List<Artist> artistsList;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        artistsList = new ArrayList<Artist>();

        artistsAdapter = new ArtistItemAdapter(getActivity(), R.layout.list_item_artist, artistsList);

        ListView artistsList = (ListView) rootView.findViewById(R.id.listViewArtists);
        artistsList.setAdapter(artistsAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText searchBar = (EditText) getView().findViewById(R.id.searchEditText);

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            ArtistsPager artists;

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    FetchArtistsTask artistsTaskCall = new FetchArtistsTask();
                    artistsTaskCall.execute(String.valueOf(v.getText()));
                    handled = true;
                }
                return handled;
            }
        });
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, ArtistsPager>{

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        protected ArtistsPager doInBackground(String... params) {
            if(params.length == 0) {
                Log.v(LOG_TAG, "No search text given.");
                return null;
            }

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            return results;
        }

        protected void onPostExecute(ArtistsPager result) {
            super.onPostExecute(result);
            if(result != null) {
                artistsAdapter.clear();
                for(Artist artist : result.artists.items) {
                    artistsAdapter.add(artist);
                }
            }
        }
    }
}
