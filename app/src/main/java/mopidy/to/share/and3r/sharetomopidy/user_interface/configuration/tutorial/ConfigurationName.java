package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfig;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;


public class ConfigurationName extends Fragment implements TextWatcher{

    private EditText nameEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.configuration_name, container, false);
        nameEditText = (EditText) rootView.findViewById(R.id.nameEditText);
        ConfigurationActivity activity = ((ConfigurationActivity)getActivity());
        MopidyServerConfig config = MopidyServerConfigManager.get().getConfig(activity, activity.getId());
        nameEditText.setText(config.getName());
        nameEditText.addTextChangedListener(this);
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
        ConfigurationActivity activity = ((ConfigurationActivity)getActivity());
        MopidyServerConfig config = MopidyServerConfigManager.get().getConfig(activity, activity.getId());
        config.setName(getActivity(), s.toString());
    }
}
