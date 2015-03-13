package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyPlaylist;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;
import mopidy.to.share.and3r.sharetomopidy.utils.MopidyDataFetch;
import mopidy.to.share.and3r.sharetomopidy.utils.OnRequestListener;


public class PlaylistsAdapter extends BaseListAdapter implements OnRequestListener, Observer {



    public PlaylistsAdapter(){
        list = MopidyStatus.get().getPlaylists();
    }

    @Override
    public void onDataChanged() {
        list = MopidyStatus.get().getPlaylists();
        super.onDataChanged();
    }

    @Override
    public void onClick(View v, int item) {
        if (list[item] instanceof MopidyPlaylist){
            currentPath.addLast(list[item]);
            loadPath(v.getContext());
        }else{
            super.onClick(v, item);
        }
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
    public void loadPath(Context c) {
        if (currentPath.size() == 0){
            list = MopidyStatus.get().getPlaylists();
            setStatus(LOADED);
        }else{
            setStatus(LOADING);
            MopidyDataFetch.get().getPlaylist(c, this, (MopidyPlaylist)currentPath.getLast());
        }
    }

    @Override
    public void OnRequestResult(final JSONObject result) {
        h.post(new Runnable() {
            @Override
            public void run() {
                JSONArray array;
                try {
                    array = result.getJSONObject("result").getJSONArray("tracks");
                    MopidyTrack[] tracks = new MopidyTrack[array.length()];
                    for (int i=0; i<array.length(); i++){
                        tracks[i] = new MopidyTrack(array.optJSONObject(i));
                    }
                    list = tracks;
                    setStatus(LOADED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void update(Observable observable, Object data) {
        int event = (int) data;
        switch (event){
            case MopidyStatus.EVENT_PLAYLISTS_CHANGED:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPath.size() == 0){
                            list = MopidyStatus.get().getPlaylists();
                            onDataChanged();
                        }
                    }
                });
                break;
        }
    }
}
