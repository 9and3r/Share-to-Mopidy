package mopidy.to.share.and3r.sharetomopidy.user_interface.fragments;

import android.support.v4.app.Fragment;

import mopidy.to.share.and3r.sharetomopidy.user_interface.activity.ConnectedActivity;

public abstract class ConnectedBaseFragment extends Fragment {

    public static final int FRAGMENT_SEARCH = 3;

    public boolean onBackPressed(ConnectedActivity activity){
        return false;
    }

}
