package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfig;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;
import mopidy.to.share.and3r.sharetomopidy.preferences.PreferencesManager;
import mopidy.to.share.and3r.sharetomopidy.R;

/**
 * Created by ander on 1/31/15.
 */
public class ConfigurationTest extends Fragment{

    private RelativeLayout loadingLayout;
    private RelativeLayout okLayout;
    private RelativeLayout errorLayout;
    private TextView errorTextView;
    private boolean viewCreated;
    private boolean testing;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.configuration_test, container, false);
        loadingLayout  = (RelativeLayout) rootView.findViewById(R.id.test_loading);
        okLayout  = (RelativeLayout) rootView.findViewById(R.id.test_ok_layout);
        errorLayout  = (RelativeLayout) rootView.findViewById(R.id.test_error_layout);
        errorTextView = (TextView) rootView.findViewById(R.id.errorTextView);
        rootView.findViewById(R.id.test__try_again_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testConnection(v);
            }
        });
        viewCreated = true;
        return rootView;
    }


    public void testConnection(View v){
        if (viewCreated && !testing){
            testing = true;
            changeVisibleLayout(loadingLayout);
            ConfigurationActivity activity = ((ConfigurationActivity)getActivity());
            MopidyServerConfig config = MopidyServerConfigManager.get().getConfig(activity, activity.getId());
            String ip = config.getIp();
            int port = config.getPort();
            AsyncHttpClient.getDefaultInstance().websocket("http://"+ip+":"+String.valueOf(port)+"/mopidy/ws", "ws", new AsyncHttpClient.WebSocketConnectCallback() {
                @Override
                public void onCompleted(Exception ex, final WebSocket pWebSocket) {
                    Activity a = getActivity();
                    testing = false;
                    if (a!=null){
                        final Exception exc = ex;
                        if (ex != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    connectionError(exc);
                                }
                            });

                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    connectionCorrect();
                                }
                            });

                        }
                    }

                }
            });
        }

    }

    private void changeVisibleLayout(RelativeLayout pLayout){
        if (viewCreated){
            if (pLayout == loadingLayout){
                loadingLayout.setVisibility(View.VISIBLE);
            }else{
                loadingLayout.setVisibility(View.GONE);
            }
            if (pLayout == okLayout){
                okLayout.setVisibility(View.VISIBLE);
            }else{
                okLayout.setVisibility(View.GONE);
            }
            if (pLayout == errorLayout){
                errorLayout.setVisibility(View.VISIBLE);
            }else{
                errorLayout.setVisibility(View.GONE);
            }
        }

    }

    private void connectionError(Exception ex){
        changeVisibleLayout(errorLayout);
        if (viewCreated){
            errorTextView.setText(ex.getMessage());
        }

    }

    private void connectionCorrect(){
        changeVisibleLayout(okLayout);
    }

    @Override
    public void onDestroyView() {
        viewCreated = false;
        super.onDestroyView();
    }
}
