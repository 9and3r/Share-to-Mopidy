package mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyDataRef;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.LibraryAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;

public class LibraryFragment extends RecyclerViewBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter = new LibraryAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    @Override
    public void onClick(BaseHolder holder, View v, int item) {

        if (!((MopidyDataRef)mAdapter.list[item]).getType().equals(MopidyDataRef.TYPE_TRACK)){
            mAdapter.currentPath.addLast(mAdapter.list[item]);
            mAdapter.loadPath(v.getContext());
        }else{
            super.onClick(holder, v, item);
        }
    }




}
