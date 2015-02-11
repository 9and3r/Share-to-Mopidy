package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mopidy.to.share.and3r.sharetomopidy.R;

/**
 * Created by ander on 1/30/15.
 */
public class ConfigurationWelcome extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.configuration_welcome, container, false);

        return rootView;
    }
}
