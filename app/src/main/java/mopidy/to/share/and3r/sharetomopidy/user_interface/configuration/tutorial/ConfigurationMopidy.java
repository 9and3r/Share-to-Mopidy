package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfig;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;
import mopidy.to.share.and3r.sharetomopidy.preferences.PreferencesManager;
import mopidy.to.share.and3r.sharetomopidy.R;

/**
 * Created by ander on 1/31/15.
 */
public class ConfigurationMopidy extends Fragment implements TextWatcher {


    private EditText portEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.configuration_mopidy, container, false);
        portEditText = (EditText) rootView.findViewById(R.id.portEditText);
        ConfigurationActivity activity = ((ConfigurationActivity)getActivity());
        MopidyServerConfig config = MopidyServerConfigManager.get().getConfig(activity, activity.getId());
        portEditText.setText(String.valueOf(config.getPort()));
        portEditText.addTextChangedListener(this);
        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try{
            int port = Integer.valueOf(s.toString());
            ConfigurationActivity activity = ((ConfigurationActivity)getActivity());
            MopidyServerConfig config = MopidyServerConfigManager.get().getConfig(activity, activity.getId());
            config.setPort(getActivity(),port);
        }catch (NumberFormatException e){

        }


    }
}
