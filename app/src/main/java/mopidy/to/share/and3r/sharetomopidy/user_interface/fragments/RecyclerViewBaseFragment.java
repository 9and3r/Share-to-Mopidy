package mopidy.to.share.and3r.sharetomopidy.user_interface.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;


import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.user_interface.activity.ConnectedActivity;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.LibraryAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.PlaylistsAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.TrackListAdapter;

public class RecyclerViewBaseFragment extends ConnectedBaseFragment{

    public static final int FRAGMENT_TRACKLIST = 0;
    public static final int FRAGMENT_PLAYLISTS = 1;
    public static final int FRAGMENT_LIBRARY = 2;
    public static final int BASE_ADAPTER = 3;



    int pos;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;




    private BaseListAdapter mAdapter;
    private RecyclerView.Adapter recyclerViewAdapter;


    public static RecyclerViewBaseFragment newInstance(int num) {
        RecyclerViewBaseFragment f = new RecyclerViewBaseFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments() != null ? getArguments().getInt("num") : 1;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_layout, container, false);


        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        switch (pos){
            case FRAGMENT_TRACKLIST:
                mAdapter = new TrackListAdapter(getActivity());
                RecyclerViewDragDropManager mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
                recyclerViewAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mAdapter);
                mRecyclerView.setAdapter(recyclerViewAdapter);
                mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
                break;
            case FRAGMENT_PLAYLISTS:
                mAdapter = new PlaylistsAdapter(getActivity().getApplicationContext());
                break;
            case FRAGMENT_LIBRARY:
                mAdapter = new LibraryAdapter(root.getContext());
                break;
            case BASE_ADAPTER:
                mAdapter = new BaseListAdapter();
                break;
        }

        if (recyclerViewAdapter == null){
            mRecyclerView.setAdapter(mAdapter);
        }



        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null){
            mAdapter.onResume((ActionBarActivity)getActivity());
        }
    }

    @Override
    public boolean onBackPressed(ConnectedActivity activity) {
        return mAdapter.onBackPressed(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.onPause();
        }
    }

}
