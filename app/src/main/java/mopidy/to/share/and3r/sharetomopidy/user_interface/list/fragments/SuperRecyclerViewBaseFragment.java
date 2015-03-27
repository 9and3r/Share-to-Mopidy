package mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.user_interface.activity.ConnectedActivity;
import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.ConnectedBaseFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;

public abstract class SuperRecyclerViewBaseFragment extends ConnectedBaseFragment {


    protected SuperRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    protected BaseListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.super_recycler_layout, container, false);
        mRecyclerView = (SuperRecyclerView) root.findViewById(R.id.super_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return root;
    }

    @Override
    public boolean onBackPressed(ConnectedActivity activity) {
        if (mAdapter != null && mAdapter instanceof BaseListAdapter){
            return ((BaseListAdapter) mAdapter).onBackPressed(activity);
        }else{
            return super.onBackPressed(activity);
        }
    }


}
