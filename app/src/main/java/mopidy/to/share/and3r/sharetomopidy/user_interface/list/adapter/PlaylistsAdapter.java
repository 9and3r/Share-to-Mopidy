package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.view.View;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;

/**
 * Created by ander on 2/26/15.
 */
public class PlaylistsAdapter extends BaseListAdapter {

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

    }
}
