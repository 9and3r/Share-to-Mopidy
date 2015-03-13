package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;


public class TrackListAdapter extends BaseListAdapter implements Observer{


    public TrackListAdapter(){
        list = MopidyStatus.get().getTracklist();
        setHasStableIds(true);
    }

    public void onDataChanged(){
        list = MopidyStatus.get().getTracklist();
        super.onDataChanged();
    }

    @Override
    public long getItemId(int position) {
        return ((MopidyTlTrack) list[position]).getId();
    }

    @Override
    public void onClick(View v, int item) {
        PlaybackControlManager.playTrackListTlTrack(v.getContext(), (MopidyTlTrack)list[item]);
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
