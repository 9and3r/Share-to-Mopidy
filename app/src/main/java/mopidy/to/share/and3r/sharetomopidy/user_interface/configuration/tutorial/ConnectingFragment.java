package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mopidy.to.share.and3r.sharetomopidy.R;

/**
 * Created by ander on 2/1/15.
 */
public class ConnectingFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.loading, container, false);
        return rootView;
    }
}
