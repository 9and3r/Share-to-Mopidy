package mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyPlaylist;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.PlaylistsAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseListItem;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.OnBaseListItemClickListener;

public class PlaylistsFragment extends SuperRecyclerViewBaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnBaseListItemClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter = new PlaylistsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshListener(this);
        return root;
    }


    @Override
    public void onRefresh() {
        if (mAdapter.currentPath.size()>0){
            mAdapter.notifyDataSetChanged();
        }else{
            PlaybackControlManager.refreshPlaylists(getActivity());
        }

    }

    @Override
    public void onClick(BaseHolder holder, View v, int item) {
        if (mAdapter.list[item] instanceof MopidyPlaylist){
            mAdapter.currentPath.addLast(mAdapter.list[item]);
            mAdapter.loadPath(v.getContext());
        }else{
            PlaybackControlManager.addToTracklist(v.getContext(), ((BaseListItem)holder).data, false);
            Toast toast = Toast.makeText(v.getContext(), v.getContext().getString(R.string.added_to_tracklist), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onLongClick(BaseListItem holder, View v, int item) {
        MopidyDataOptionsDialog dialog = new MopidyDataOptionsDialog();
        Bundle b = new Bundle();
        b.putSerializable(MopidyDataOptionsDialog.MOPIDY_DATA, holder.data);
        dialog.setArguments(b);
        dialog.show(getChildFragmentManager(), "DIALOG_OPTIONS");
    }
}
