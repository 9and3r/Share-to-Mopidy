package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseListItem;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.ListItemHolderImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.TracklistTlTrackOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.OnBaseListItemClickListener;


public class TrackListAdapter extends BaseListAdapter implements DraggableItemAdapter, Observer {


    private Context context;

    public TrackListAdapter(Context c, OnBaseListItemClickListener listener){
        super(listener);
        context = c;
        setHasStableIds(true);
        list = MopidyStatus.get().getTracklist();
    }

    public void onDataChanged(){
        list = MopidyStatus.get().getTracklist();
        super.onDataChanged();
    }

    @Override
    public long getItemId(int position) {
        MopidyTlTrack track = (MopidyTlTrack) list[position];
        String id = String.valueOf(track.getTlId()) + track.getUri();
        return Math.abs(id.hashCode());
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        MopidyStatus.get().addObserver(this);
        onDataChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        MopidyStatus.get().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        int event = (int) data;
        switch (event){
            case MopidyStatus.EVENT_TRACKLIST_CHANGED:
                h.post(new Runnable() {
                    @Override
                    public void run() {
                       onDataChanged();
                    }
                });
                break;
        }
    }

}
