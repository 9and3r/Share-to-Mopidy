package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyDataRef;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.TracklistTlTrackOptionsDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.OnBaseListItemClickListener;
import mopidy.to.share.and3r.sharetomopidy.utils.MopidyDataFetch;
import mopidy.to.share.and3r.sharetomopidy.utils.OnRequestListener;

public class LibraryAdapter extends BaseListAdapter implements OnRequestListener {

    public LibraryAdapter(Context c, OnBaseListItemClickListener listener) {
        super(listener);
        loadPath(c);

    }

    @Override
    public void loadPath(Context c) {
        setStatus(LOADING);
        if (currentPath.size() == 0){
            MopidyDataFetch.get().browse(c, this, null);
        }else{
            MopidyDataFetch.get().browse(c, this, currentPath.getLast());
        }

    }

    @Override
    public void OnRequestResult(final JSONObject o) {
        h.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray array = o.getJSONArray("result");
                    MopidyDataRef[] refs = new MopidyDataRef[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        refs[i] = new MopidyDataRef(array.getJSONObject(i));
                    }
                    list = refs;
                    setStatus(LOADED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
