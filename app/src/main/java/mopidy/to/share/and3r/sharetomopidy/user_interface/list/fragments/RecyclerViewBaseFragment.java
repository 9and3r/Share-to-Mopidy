package mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.activity.ConnectedActivity;
import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.ConnectedBaseFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.LibraryAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.PlaylistsAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.TrackListAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseListItem;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.OnBaseListItemClickListener;

public abstract class RecyclerViewBaseFragment extends ConnectedBaseFragment implements OnBaseListItemClickListener{


    protected RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    protected BaseListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_layout, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
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

    public void onClick(BaseHolder holder, View v, int item){
        PlaybackControlManager.addToTracklist(v.getContext(), ((BaseListItem)holder).data, false);
        Toast toast = Toast.makeText(v.getContext(), v.getContext().getString(R.string.added_to_tracklist), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onLongClick(BaseListItem holder, View v, int item){
        MopidyDataOptionsDialog dialog = new MopidyDataOptionsDialog();
        Bundle b = new Bundle();
        b.putSerializable(MopidyDataOptionsDialog.MOPIDY_DATA, holder.data);
        dialog.setArguments(b);
        dialog.show(getChildFragmentManager(), "DIALOG_OPTIONS");
    }
}
