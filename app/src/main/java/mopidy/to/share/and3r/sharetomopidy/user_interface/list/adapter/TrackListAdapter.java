package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.ListItemHolderImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.TracklistTlTrackOptionsDialog;


public class TrackListAdapter extends BaseListAdapter implements Observer, DraggableItemAdapter {

    private Context context;


    public TrackListAdapter(Context c){
        super();
        setHasStableIds(true);
        list = MopidyStatus.get().getTracklist();
        context = c.getApplicationContext();
    }

    public void onDataChanged(){
        list = MopidyStatus.get().getTracklist();
        super.onDataChanged();

    }

    @Override
    public void onClick(View v, int item) {
        PlaybackControlManager.playTrackListTlTrack(v.getContext(), item);
    }

    public void onLongClick(View v, int item){
        TracklistTlTrackOptionsDialog dialog = new TracklistTlTrackOptionsDialog();
        Bundle b = new Bundle();
        b.putSerializable(MopidyDataOptionsDialog.MOPIDY_DATA, list[item]);
        dialog.setArguments(b);
        dialog.show(activity.getSupportFragmentManager(), "DIALOG_TL_TRACK_OPTIONS");
    }

    @Override
    public long getItemId(int position) {
        MopidyTlTrack track = (MopidyTlTrack) list[position];
        String id = String.valueOf(track.getTlId()) + track.getUri();
        return Math.abs(id.hashCode());
    }

    @Override
    public void onResume(ActionBarActivity pActivity) {
        super.onResume(pActivity);
        MopidyStatus.get().addObserver(this);
    }

    @Override
    public void onPause() {
        MopidyStatus.get().deleteObserver(this);
        super.onPause();
    }

    @Override
    public void update(Observable observable, Object data) {
        int event = (int) data;
        switch (event){
            case MopidyStatus.EVENT_TRACKLIST_CHANGED:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        onDataChanged();
                    }
                });
                break;
        }
    }


    @Override
    public boolean onCheckCanStartDrag(RecyclerView.ViewHolder viewHolder, int x, int y) {
        ImageView image = ((ListItemHolderImage)viewHolder).albumArt;
        if (x<image.getWidth() && y<image.getHeight()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(RecyclerView.ViewHolder viewHolder) {
        return null;
    }

    @Override
    public void onMoveItem(int i, int i2) {
        PlaybackControlManager.moveTrackTracklist(context, i, i2);
    }
}
