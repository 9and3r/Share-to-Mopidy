package mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyAlbum;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyArtist;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.activity.ConnectedActivity;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.select.ServerAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.ConnectedBaseFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.SearchAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments.RecyclerViewBaseFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseListItem;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.OnBaseListItemClickListener;
import mopidy.to.share.and3r.sharetomopidy.utils.MopidyDataFetch;
import mopidy.to.share.and3r.sharetomopidy.utils.OnRequestListener;

public class SearchFragment extends ConnectedBaseFragment implements OnRequestListener, SearchView.OnQueryTextListener ,OnBaseListItemClickListener{


    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    private static final String SAVED_STATE_SEARCH_QUERY = "SavedSearchQuery";

    private String searchQuery;

    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.search_layout, container, false);
        searchView = (SearchView) root.findViewById(R.id.searchView);
        progressBar = (ProgressBar) root.findViewById(R.id.search_progress);
        progressBar.setMax(1);
        searchView.setOnQueryTextListener(this);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search);
        adapter = new SearchAdapter(this);
        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);

        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(adapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mWrappedAdapter);
        recyclerView.setHasFixedSize(false);

        mRecyclerViewExpandableItemManager.attachRecyclerView(recyclerView);

        searchQuery = (savedInstanceState != null) ? savedInstanceState.getString(SAVED_STATE_SEARCH_QUERY) : null;

        if (searchQuery != null && searchQuery.length()>0){
            searchView.setQuery(searchQuery, true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_STATE_SEARCH_QUERY, searchQuery);
    }

    private void doSearch(){
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);
        MopidyDataFetch.get().search(getActivity(), this, searchQuery);
    }


    @Override
    public void OnRequestResult(JSONObject result) {
        final ArrayList<MopidyData> tracks = new ArrayList<>();
        final ArrayList<MopidyData> albums  = new ArrayList<>();
        final ArrayList<MopidyData> artists = new ArrayList<>();

        try{


            JSONArray resultsArray = result.getJSONArray("result");
            JSONArray array;

            for (int z=0; z < resultsArray.length(); z++){

                // Load tracks

                if (resultsArray.getJSONObject(z).has("tracks")){
                    array = resultsArray.getJSONObject(z).getJSONArray("tracks");
                    for (int i=0; i< array.length(); i++){
                        tracks.add(new MopidyTrack(array.getJSONObject(i)));
                    }
                }



                // Load albums

                if (resultsArray.getJSONObject(z).has("albums")){
                    array = result.getJSONArray("result").getJSONObject(z).getJSONArray("albums");
                    for (int i=0; i< array.length(); i++){
                        albums.add(new MopidyAlbum(array.getJSONObject(i)));
                    }


                }

                // Load artists

                if (resultsArray.getJSONObject(z).has("artists")){
                    array = result.getJSONArray("result").getJSONObject(z).getJSONArray("artists");
                    for (int i=0; i< array.length(); i++){
                        artists.add(new MopidyArtist(array.getJSONObject(i)));
                    }
                }



            }



            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setData(tracks, albums, artists);
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress(1);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {

        }

    }

    @Override
    public void onClick(BaseHolder holder, View v, int item) {
        BaseListItem listItem = (BaseListItem) holder;
        PlaybackControlManager.addToTracklist(v.getContext(), adapter.data[listItem.group].get(item), false);
        Toast toast = Toast.makeText(v.getContext(), v.getContext().getString(R.string.added_to_tracklist), Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onLongClick(BaseListItem holder, View v, int item) {
        MopidyDataOptionsDialog dialog = new MopidyDataOptionsDialog();
        Bundle b = new Bundle();
        b.putSerializable(MopidyDataOptionsDialog.MOPIDY_DATA, holder.data);
        dialog.setArguments(b);
        dialog.show(getChildFragmentManager(), "DIALOG_OPTIONS");
    }

    @Override
    public boolean onBackPressed(ConnectedActivity activity) {
        return super.onBackPressed(activity);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery = searchView.getQuery().toString();
        doSearch();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
