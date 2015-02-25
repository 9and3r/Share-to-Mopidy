package mopidy.to.share.and3r.sharetomopidy.user_interface.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mopidy.to.share.and3r.sharetomopidy.R;

/**
 * Created by ander on 2/24/15.
 */
public class LoadingFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading, container, false);
    }
}
