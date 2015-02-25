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
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.select.ServerSelectFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial.ConnectingFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.NowPlayingFragment;


public class MainActivity extends ActionBarActivity implements Observer {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ServerSelectFragment()).commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onConnectionStatusChanged();
        MopidyStatus.get().addObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MopidyStatus.get().deleteObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_disconnect){
            disconnect();
            return true;
        }else if (id == R.id.action_about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    public void disconnect(){
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_STOP_SERVICE);
        startService(intent);
    }

    private void onConnectionStatusChanged(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (MopidyStatus.get().getConnectionStatus()){
                    case MopidyStatus.CONNECTED:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NowPlayingFragment()).commit();
                        break;
                    case MopidyStatus.NOT_CONNECTED:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ServerSelectFragment()).commit();
                        break;
                    case MopidyStatus.CONNECTING:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectingFragment()).commit();
                        break;
                }
            }
        });
    }



    @Override
    public void update(Observable observable, Object data) {

        if (((int) data) == MopidyStatus.CONNECTION_STATE_CHANGED){
            onConnectionStatusChanged();

        }

    }


}
