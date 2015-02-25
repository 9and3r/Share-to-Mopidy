package mopidy.to.share.and3r.sharetomopidy.user_interface.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;
import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.user_interface.TrackPagerAdapter;

import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.NowPlayingFragment;


public class NotConnectedActivity extends ActionBarActivity implements Observer {

    private View loadingLayout;
    private View selectLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_connected);
        loadingLayout = findViewById(R.id.loading_layout);
        selectLayout = findViewById(R.id.select_layout);
    }

    private void onConnectionStateChange(){
        int status = MopidyStatus.get().getConnectionStatus();
        switch (status){
            case MopidyStatus.NOT_CONNECTED:
                loadingLayout.setVisibility(View.GONE);
                selectLayout.setVisibility(View.VISIBLE);
                break;
            case MopidyStatus.CONNECTING:
                loadingLayout.setVisibility(View.VISIBLE);
                selectLayout.setVisibility(View.GONE);
                break;
            case MopidyStatus.CONNECTED:
                Intent i = new Intent(this, ConnectedActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onConnectionStateChange();
        MopidyStatus.get().addObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MopidyStatus.get().deleteObserver(this);
    }





    @Override
    public void update(Observable observable, Object data) {

        if (((int) data) == MopidyStatus.CONNECTION_STATE_CHANGED){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onConnectionStateChange();
                }
            });
        }

    }


}
