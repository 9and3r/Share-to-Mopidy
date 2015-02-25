package mopidy.to.share.and3r.sharetomopidy.user_interface.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.Channels;
import java.text.DateFormat;
import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyAlbum;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.OnImageAndPalleteReady;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.TaskImage;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfig;
import mopidy.to.share.and3r.sharetomopidy.user_interface.TrackPagerAdapter;

public class NowPlayingFragment extends Fragment implements Observer, SeekBar.OnSeekBarChangeListener, SlidingUpPanelLayout.PanelSlideListener {

    private TrackPagerAdapter adapter;
    private ViewPager pager;
    private ImageView playButton;
    //private View playbackControls;
    private TextView singleButton;
    private TextView consumeButton;
    private ImageView repeatButton;
    private ImageView randomButton;
    private ImageView nextButton;
    private ImageView previousButton;

    private TextView currentPosTextView;
    private TextView trackLenghtTextView;

    private SeekBar seekBar;
    private boolean fromUser;

    private Handler mHandler = new Handler();
    private Runnable updateTask = new Runnable () {
        private int times = 0;

        public void run() {
            if (!fromUser){
                if(times == 10){
                    updateSeek(true);
                    times = 0;
                }else{
                    updateSeek(false);
                    times++;
                }

            }
            mHandler.postDelayed(updateTask, 100);
        }
    };




    private View smallNowPlaying;
    private int smallNowPlayingHeight;

    private TaskImage taskImage;

    private ImageView smallAlbumArt;
    private TextView smallTrackName;
    private TextView smallArtistName;
    private ImageView smallPlayPauseButton;
    private ImageView smallNextButton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.now_playing, container, false);
        playButton = (ImageView) rootView.findViewById(R.id.playPauseButton);
        pager = (ViewPager) rootView.findViewById(R.id.tracks_pager);
        //playbackControls = rootView.findViewById(R.id.playback_controls);
        singleButton = (TextView) rootView.findViewById(R.id.singleButton);
        consumeButton = (TextView) rootView.findViewById(R.id.consumeButton);
        repeatButton = (ImageView) rootView.findViewById(R.id.repeatButton);
        randomButton = (ImageView) rootView.findViewById(R.id.shuffleButton);
        previousButton = (ImageView) rootView.findViewById(R.id.previousButton);
        nextButton = (ImageView) rootView.findViewById(R.id.nextButton);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        currentPosTextView = (TextView) rootView.findViewById(R.id.currenSeekPositionTextView);
        trackLenghtTextView = (TextView) rootView.findViewById(R.id.trackLenghtTextView);

        smallNowPlaying = rootView.findViewById(R.id.small_now_playing);
        smallAlbumArt = (ImageView) rootView.findViewById(R.id.small_album_art);
        smallTrackName = (TextView) rootView.findViewById(R.id.small_track_name);
        smallArtistName = (TextView) rootView.findViewById(R.id.small_artist_name);
        smallNextButton = (ImageView) rootView.findViewById(R.id.nextButtonSmallView);
        smallPlayPauseButton = (ImageView) rootView.findViewById(R.id.playPauseButtonSmallView);
        smallNowPlayingHeight = smallNowPlaying.getLayoutParams().height;
        adapter = new TrackPagerAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (MopidyStatus.get().getConnectionStatus() == MopidyStatus.CONNECTED) {
                    if (position != MopidyStatus.get().getCurrentPos()) {
                        Intent nextIntent = new Intent(getActivity(), MopidyService.class);
                        nextIntent.setAction(MopidyService.ACTION_ONE_ACTION);
                        DefaultJSON nextJSON = new DefaultJSON();
                        nextJSON.setMethod("core.playback.change_track");
                        JSONObject params = new JSONObject();
                        try {
                            params.put("tl_track", new JSONObject(MopidyStatus.get().getTrack(position).getTl_track()));
                            nextJSON.put("params", params);
                            nextIntent.putExtra(MopidyService.ACTION_DATA, nextJSON.toString());
                            getActivity().startService(nextIntent);
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
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRepeat();
            }
        });
        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSingle();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrPause();
            }
        });
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRandom();
            }
        });
        consumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeConsume();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });
        smallPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrPause();
            }
        });
        smallNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        seekBar.setOnSeekBarChangeListener(this);
        updateCurrentTrack();
        updatePlayState();
        onSeek();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        MopidyStatus.get().addObserver(this);
        adapter.notifyDataSetChanged();
        updateCurrentTrack();
        updatePlayState();
        onRepeatChanged();
        onRandomChanged();
        onConsumeChanged();
        onSingleChanged();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (taskImage != null){
            taskImage.cancel(true);
            taskImage = null;
        }
        MopidyStatus.get().deleteObserver(this);
    }

    private void updatePlayState(){
        if (MopidyStatus.get().isPlaying()){
            playButton.setImageResource(R.drawable.ic_action_pause);
            smallPlayPauseButton.setImageResource(R.drawable.ic_action_pause);
            mHandler.postDelayed(updateTask, 100);
        }else{
            playButton.setImageResource(R.drawable.ic_action_play);
            smallPlayPauseButton.setImageResource(R.drawable.ic_action_play);
            mHandler.removeCallbacks(updateTask);
        }
        onSeek();
    }

    private void onSeek(){
        seekBar.setProgress(MopidyStatus.get().getCurrentSeekPos());
    }

    private void updateSeek(boolean changeText){
        seekBar.setProgress(MopidyStatus.get().getCurrentSeekPos());
        if (changeText){
            currentPosTextView.setText(MopidyStatus.get().getCurrentSeekPosString());
        }
    }

    private void updateCurrentTrack(){
        if (pager.getCurrentItem() != MopidyStatus.get().getCurrentPos()){
            pager.setCurrentItem(MopidyStatus.get().getCurrentPos());
        }
        seekBar.setMax(MopidyStatus.get().getCurrentTrackLenght());
        trackLenghtTextView.setText(MopidyStatus.milisToHumanTime(MopidyStatus.get().getCurrentTrackLenght()));
        smallTrackName.setText(MopidyStatus.get().getCurrentTrackName());
        smallArtistName.setText(MopidyStatus.get().getCurrentArtistsName());
        if (taskImage != null && !taskImage.isCancelled()){
            taskImage.cancel(true);
            taskImage = null;
        }
        MopidyTrack track = MopidyStatus.get().getCurrentTrack();
        if (track != null){
            MopidyAlbum album = MopidyStatus.get().getCurrentTrack().getAlbum();
            if (album != null){
                TaskImage task = new TaskImage(new OnImageAndPalleteReady() {
                    @Override
                    public void onImageAndPalleteReady(Bitmap bitmap, Palette palette) {
                        smallAlbumArt.setImageBitmap(bitmap);
                        taskImage = null;
                    }
                } , album);
                task.execute(getActivity());
            }
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

    public void changeRandom(){
        Intent intent = new Intent(getActivity(), MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_random");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isRandom());
            json.put("params", params);
            intent.putExtra(MopidyService.ACTION_DATA, json.toString());
            getActivity().startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeRepeat(){
        Intent intent = new Intent(getActivity(), MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_repeat");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isRepeat());
            json.put("params", params);
            intent.putExtra(MopidyService.ACTION_DATA, json.toString());
            getActivity().startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void playOrPause(){
        PlaybackControlManager.playOrPause(getActivity());
    }

    public void next(){
        PlaybackControlManager.next(getActivity());
    }

    public void previous(){
        PlaybackControlManager.previous(getActivity());
    }


    public void changeSingle(){
        Intent intent = new Intent(getActivity(), MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_single");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isSingle());
            json.put("params", params);
            intent.putExtra(MopidyService.ACTION_DATA, json.toString());
            getActivity().startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeConsume(){
        Intent intent = new Intent(getActivity(), MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_consume");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isConsume());
            json.put("params", params);
            intent.putExtra(MopidyService.ACTION_DATA, json.toString());
            getActivity().startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        final int event = (int) data;
        getActivity().runOnUiThread(new Runnable() {
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
                    case MopidyStatus.EVENT_SEEK:
                        onSeek();
                        break;
                }
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean pFromUser) {
        fromUser = pFromUser;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (fromUser){
            Intent intent = new Intent(getActivity(), MopidyService.class);
            intent.setAction(MopidyService.ACTION_ONE_ACTION);
            try {
                DefaultJSON json = new DefaultJSON();
                json.setMethod("core.playback.seek");
                JSONObject params = new JSONObject();
                params.put("time_position",seekBar.getProgress());
                json.put("params", params);
                intent.putExtra(MopidyService.ACTION_DATA, json.toString());
                getActivity().startService(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fromUser = false;
    }

    @Override
    public void onPanelSlide(View view, float v) {

        if (v < 1.0f) {
            smallNowPlaying.setVisibility(View.VISIBLE);
        } else {
            smallNowPlaying.setVisibility(View.GONE);
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) smallNowPlaying.getLayoutParams();
        params.height = (int) ((1-v) * smallNowPlayingHeight);
        smallNowPlaying.setLayoutParams(params);
        //smallNowPlaying.setAlpha(1.0f - v);
    }

    @Override
    public void onPanelCollapsed(View view) {

    }

    @Override
    public void onPanelExpanded(View view) {

    }

    @Override
    public void onPanelAnchored(View view) {

    }

    @Override
    public void onPanelHidden(View view) {

    }
}
