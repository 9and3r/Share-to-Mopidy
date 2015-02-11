package mopidy.to.share.and3r.sharetomopidy.user_interface.activity;

import android.content.Intent;
import android.graphics.Color;
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


public class MainActivity extends ActionBarActivity implements Observer {

    private TrackPagerAdapter adapter;
    private ViewPager pager;
    private ImageView playButton;
    private View playbackControls;
    private TextView singleButton;
    private TextView consumeButton;
    private ImageView repeatButton;
    private ImageView randomButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MopidyStatus.get().addObserver(this);
        adapter.notifyDataSetChanged();
        updateCurrentTrack();
        updatePlayState();
        onRepeatChanged();
        onRandomChanged();
        onConsumeChanged();
        onSingleChanged();
        updateConnectionStatus();
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

    private void setMainView(){
        setContentView(R.layout.activity_main);
        playButton = (ImageView) findViewById(R.id.playPauseButton);
        pager = (ViewPager) findViewById(R.id.tracks_pager);
        playbackControls = findViewById(R.id.playback_controls);
        singleButton = (TextView) findViewById(R.id.singleButton);
        consumeButton = (TextView) findViewById(R.id.consumeButton);
        repeatButton = (ImageView) findViewById(R.id.repeatButton);
        randomButton = (ImageView) findViewById(R.id.shuffleButton);
        adapter = new TrackPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (MopidyStatus.get().getConnectionStatus() == MopidyStatus.CONNECTED){
                    if (position != MopidyStatus.get().getCurrentPos()){
                        Intent nextIntent = new Intent(MainActivity.this, MopidyService.class);
                        nextIntent.setAction(MopidyService.ACTION_ONE_ACTION);
                        DefaultJSON nextJSON = new DefaultJSON();
                        nextJSON.setMethod("core.playback.change_track");
                        JSONObject params = new JSONObject();
                        try {
                            params.put("tl_track", new JSONObject(MopidyStatus.get().getTrack(position).getTl_track()));
                            nextJSON.put("params", params);
                            nextIntent.putExtra(MopidyService.ACTION_DATA, nextJSON.toString());
                            startService(nextIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateCurrentTrack();
        updatePlayState();
    }

    private void updatePlayState(){
        if (MopidyStatus.get().isPlaying()){
            playButton.setImageResource(R.drawable.ic_action_pause);
        }else{
            playButton.setImageResource(R.drawable.ic_action_play);
        }
    }

    private void updateCurrentTrack(){
        if (pager.getCurrentItem() != MopidyStatus.get().getCurrentPos()){
            pager.setCurrentItem(MopidyStatus.get().getCurrentPos());
        }
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

    public void updateConnectionStatus(){
        if (MopidyStatus.get().getConnectionStatus() == MopidyStatus.CONNECTED){
            playbackControls.setVisibility(View.VISIBLE);
        }else{
            playbackControls.setVisibility(View.GONE);
        }
    }

    public void onSingleChanged(){
        if (MopidyStatus.get().isSingle()){
            singleButton.setTextColor(getResources().getColor(R.color.accent));
        }else{
            singleButton.setTextColor(Color.WHITE);
        }
    }

    public void onRepeatChanged(){
        if (MopidyStatus.get().isRepeat()){
            repeatButton.setImageResource(R.drawable.ic_action_repeat_active);
        }else{
            repeatButton.setImageResource(R.drawable.ic_action_repeat);
        }
    }

    public void onRandomChanged(){
        if (MopidyStatus.get().isRandom()){
            randomButton.setImageResource(R.drawable.ic_action_shuffle_active);
        }else{
            randomButton.setImageResource(R.drawable.ic_action_shuffle);
        }
    }


    public void onConsumeChanged(){
        if (MopidyStatus.get().isConsume()){
            consumeButton.setTextColor(getResources().getColor(R.color.accent));
        }else{
            consumeButton.setTextColor(Color.WHITE);
        }
    }

    public void changeRandom(View v){
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_random");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isRandom());
            json.put("params", params);
            intent.putExtra(MopidyService.ACTION_DATA, json.toString());
            startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeRepeat(View v){
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_repeat");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isRepeat());
            json.put("params", params);
            intent.putExtra(MopidyService.ACTION_DATA, json.toString());
            startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void changeSingle(View v){
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_single");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isSingle());
            json.put("params", params);
            intent.putExtra(MopidyService.ACTION_DATA, json.toString());
            startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeConsume(View v){
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_consume");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isConsume());
            json.put("params", params);
            intent.putExtra(MopidyService.ACTION_DATA, json.toString());
            startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_STOP_SERVICE);
        startService(intent);
    }

    public void playOrPause(View v){
        PlaybackControlManager.playOrPause(getApplicationContext());
    }

    public void next(View v){
        PlaybackControlManager.next(getApplicationContext());
    }

    public void previous(View v){
        PlaybackControlManager.previous(getApplicationContext());
    }

    @Override
    public void update(Observable observable, Object data) {
        final int event = (int) data;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (event) {
                    case MopidyStatus.EVENT_CURRENT_TRACK_CHANGED:
                        adapter.notifyDataSetChanged();
                        updateCurrentTrack();
                        break;
                    case MopidyStatus.EVENT_PLAYBACK_STATE_CHANGED:
                        updatePlayState();
                        break;
                    case MopidyStatus.CONNECTION_STATE_CHANGED:
                        adapter.notifyDataSetChanged();
                        updateConnectionStatus();
                        break;
                    case MopidyStatus.EVENT_TRACKLIST_CHANGED:
                        adapter.notifyDataSetChanged();
                        break;
                    case MopidyStatus.EVENT_SINGLE_OPTION_CHANGED:
                        onSingleChanged();
                        break;
                    case MopidyStatus.EVENT_CONSUME_OPTION_CHANGED:
                        onConsumeChanged();
                        break;
                    case MopidyStatus.EVENT_RANDOM_OPTION_CHANGED:
                        onRandomChanged();
                        break;
                    case MopidyStatus.EVENT_REPEAT_OPTION_CHANGED:
                        onRepeatChanged();
                        break;
                }
            }
        });

    }


}
