package mopidy.to.share.and3r.sharetomopidy.user_interface.fragments;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.TrackListAdapter;
import mopidy.to.share.and3r.sharetomopidy.utils.MopidyDataFetch;
import mopidy.to.share.and3r.sharetomopidy.utils.OnRequestListener;

public class SearchFragment extends ConnectedBaseFragment implements OnRequestListener, SearchView.OnQueryTextListener {


    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.search_layout, container, false);
/*
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.configuration_pages, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        /*
        FragmentTabHost tabs=(FragmentTabHost)root.findViewById(R.id.tab_host);
        tabs.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        Bundle bundle;

        //Tracks
        TabHost.TabSpec tracksTab=tabs.newTabSpec("tracks_tab");
        tracksTab.setIndicator("Tracks");
        bundle = new Bundle();
        bundle.putInt("num", RecyclerViewBaseFragment.BASE_ADAPTER);
        tabs.addTab(tracksTab, RecyclerViewBaseFragment.class, bundle);


        //ALbums
        TabHost.TabSpec albumsTab=tabs.newTabSpec("albums_tab");
        albumsTab.setIndicator("Albums");
        bundle = new Bundle();
        bundle.putInt("num", RecyclerViewBaseFragment.BASE_ADAPTER);
        tabs.addTab(albumsTab, RecyclerViewBaseFragment.class, bundle);

        //Artists
        TabHost.TabSpec artistTab=tabs.newTabSpec("artists_tab");
        artistTab.setIndicator("Artists");
        bundle = new Bundle();
        bundle.putInt("num", RecyclerViewBaseFragment.BASE_ADAPTER);
        tabs.addTab(artistTab, RecyclerViewBaseFragment.class, bundle);

        searchView = (SearchView) root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        */
        return root;
    }

    private void doSearch(String query){
        MopidyDataFetch.get().search(getActivity(), this, query);
    }


    @Override
    public void OnRequestResult(JSONObject result) {
        final MopidyTrack[] tracks;


        try{
            // Load tracks
            JSONArray array = result.getJSONArray("result").getJSONObject(0).getJSONArray("tracks");
            tracks = new MopidyTrack[array.length()];
            for (int i=0; i< array.length(); i++){
                tracks[i] = new MopidyTrack(array.getJSONObject(i));
            }

            final RecyclerViewBaseFragment fragment = (RecyclerViewBaseFragment) getChildFragmentManager().findFragmentByTag("tracks_tab");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //fragment.setList(tracks);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {

        }

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(searchView.getQuery().toString());
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
