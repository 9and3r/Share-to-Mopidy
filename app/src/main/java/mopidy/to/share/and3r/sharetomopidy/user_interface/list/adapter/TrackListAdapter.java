package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.TracklistTlTrackOptionsDialog;


public class TrackListAdapter extends BaseListAdapter implements Observer{


    public TrackListAdapter(){
        list = MopidyStatus.get().getTracklist();
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
}
