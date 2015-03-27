package mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.TracklistTlTrackOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.TrackListAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseListItem;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.OnBaseListItemClickListener;

public class TracklistFragment extends RecyclerViewBaseFragment implements OnBaseListItemClickListener {


    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private RecyclerView.Adapter wrapedRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter = new TrackListAdapter(getActivity(), this);
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        wrapedRecycler = mRecyclerViewDragDropManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(wrapedRecycler);



        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        return root;
    }

    @Override
    public void onClick(BaseHolder holder, View v, int item) {
        PlaybackControlManager.playTrackListTlTrack(v.getContext(), item);
    }

    public void onLongClick(BaseListItem holder, View v, int item){
        TracklistTlTrackOptionsDialog dialog = new TracklistTlTrackOptionsDialog();
        Bundle b = new Bundle();
        b.putSerializable(MopidyDataOptionsDialog.MOPIDY_DATA, mAdapter.list[item]);
        dialog.setArguments(b);
        dialog.show(getChildFragmentManager(), "DIALOG_TL_TRACK_OPTIONS");
    }


}
