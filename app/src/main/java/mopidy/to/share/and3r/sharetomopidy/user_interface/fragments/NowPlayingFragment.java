package mopidy.to.share.and3r.sharetomopidy.user_interface.fragments;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyAlbum;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.OnImageAndPaletteReady;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.TaskImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.activity.ConnectedActivity;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.TrackPagerAdapter;
import mopidy.to.share.and3r.sharetomopidy.utils.PaletteManager;

public class NowPlayingFragment extends Fragment implements Observer, SeekBar.OnSeekBarChangeListener, SlidingUpPanelLayout.PanelSlideListener {

    private TrackPagerAdapter adapter;
    private ViewPager pager;
    private ImageView playButton;





    private TextView singleButton;
    private TextView consumeButton;
    private ImageView repeatButton;
    private ImageView randomButton;
    private ImageView nextButton;
    private ImageView previousButton;

    private TextView currentPosTextView;
    private TextView trackLenghtTextView;

    private ImageView mutedImageView;
    private SeekBar volumeSeekbar;

    private SeekBar positionSeekBar;
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

    private View nowPlayingContentBig;
    private int nowPlayingContentBigHeight;
    private View rootView;


    private MopidyAlbum previousAlbum;

    private View playBackControls;
    private int playBackControlsHeight;

    private View connectedContentLayout;
    private int connectedContentLayoutHeight;

    private float anchorPoint;

    private SlidingUpPanelLayout.PanelState panelState;

    public void setSlidingPanel(SlidingUpPanelLayout slidingPanel) {
        this.slidingPanel = slidingPanel;
    }

    public void setConnectedContentLayout(View pLayout, int pSize){
        connectedContentLayout = pLayout;
        connectedContentLayoutHeight = pSize;
    }

    private SlidingUpPanelLayout slidingPanel;

    private float smallNowPlayingElevation;
    private float actionBarElevation;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.now_playing, container, false);
        playButton = (ImageView) rootView.findViewById(R.id.playPauseButton);
        pager = (ViewPager) rootView.findViewById(R.id.tracks_pager);
        singleButton = (TextView) rootView.findViewById(R.id.singleButton);
        consumeButton = (TextView) rootView.findViewById(R.id.consumeButton);
        repeatButton = (ImageView) rootView.findViewById(R.id.repeatButton);
        randomButton = (ImageView) rootView.findViewById(R.id.shuffleButton);
        previousButton = (ImageView) rootView.findViewById(R.id.previousButton);
        nextButton = (ImageView) rootView.findViewById(R.id.nextButton);
        positionSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        volumeSeekbar = (SeekBar) rootView.findViewById(R.id.volume_seekbar);
        mutedImageView = (ImageView) rootView.findViewById(R.id.muted_image_view);
        currentPosTextView = (TextView) rootView.findViewById(R.id.currenSeekPositionTextView);
        trackLenghtTextView = (TextView) rootView.findViewById(R.id.trackLenghtTextView);

        smallNowPlaying = rootView.findViewById(R.id.small_now_playing);
        smallAlbumArt = (ImageView) rootView.findViewById(R.id.small_album_art);
        smallTrackName = (TextView) rootView.findViewById(R.id.small_track_name);
        smallArtistName = (TextView) rootView.findViewById(R.id.small_artist_name);
        smallNextButton = (ImageView) rootView.findViewById(R.id.nextButtonSmallView);
        smallPlayPauseButton = (ImageView) rootView.findViewById(R.id.playPauseButtonSmallView);
        smallNowPlayingHeight = smallNowPlaying.getLayoutParams().height;

        playBackControls = rootView.findViewById(R.id.playback_controls);
        playBackControlsHeight = playBackControls.getLayoutParams().height;
        nowPlayingContentBig = rootView.findViewById(R.id.now_playing_content);
        nowPlayingContentBig.getViewTreeObserver().addOnGlobalLayoutListener(playingBigContentListener);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smallNowPlayingElevation = smallNowPlaying.getElevation();
            actionBarElevation = ((ConnectedActivity)getActivity()).getSupportActionBar().getElevation();
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != MopidyStatus.get().getCurrentPos()) {
                    PlaybackControlManager.playTrackListTlTrack(getActivity(), position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        adapter = new TrackPagerAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager());
        pager.setAdapter(adapter);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.changeRepeat(v.getContext());
            }
        });
        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.changeSingle(v.getContext());
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.playOrPause(v.getContext());
            }
        });
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.changeRandom(v.getContext());
            }
        });
        consumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.changeConsume(v.getContext());
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.next(v.getContext());
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.previous(v.getContext());
            }
        });
        smallPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.playOrPause(v.getContext());
            }
        });
        smallNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.next(v.getContext());
            }
        });
        mutedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackControlManager.changeMute(v.getContext());
            }
        });
        smallNowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        positionSeekBar.setOnSeekBarChangeListener(this);
        volumeSeekbar.setOnSeekBarChangeListener(this);
        updateCurrentTrack();
        updatePlayState();
        onSeek();
        return rootView;
    }

    private ViewTreeObserver.OnGlobalLayoutListener playingBigContentListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            nowPlayingContentBig.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            nowPlayingContentBigHeight = rootView.getHeight() - smallNowPlaying.getHeight();
            anchorPoint = (float)playBackControlsHeight / (float)nowPlayingContentBigHeight;
            slidingPanel.setAnchorPoint(anchorPoint);
            if (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                onPanelSlide(slidingPanel, 0);
            }else{
                onPanelSlide(slidingPanel, 1);
            }
        }
    };






    @Override
    public void onResume() {
        super.onStart();
        MopidyStatus.get().addObserver(this);
        updateCurrentTrack();
        updatePlayState();
        onRepeatChanged();
        onRandomChanged();
        onConsumeChanged();
        onSingleChanged();
        onVolumeChanged();
        onMuteChanged();
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
        positionSeekBar.setProgress(MopidyStatus.get().getCurrentSeekPos());
    }

    private void updateSeek(boolean changeText){
        positionSeekBar.setProgress(MopidyStatus.get().getCurrentSeekPos());
        if (changeText){
            currentPosTextView.setText(MopidyStatus.get().getCurrentSeekPosString());
        }
    }

    private void updateCurrentTrack(){
        if (pager.getCurrentItem() != MopidyStatus.get().getCurrentPos()){
            pager.setCurrentItem(MopidyStatus.get().getCurrentPos());
        }
        positionSeekBar.setMax(MopidyStatus.get().getCurrentTrackLenght());
        trackLenghtTextView.setText(MopidyStatus.milisToHumanTime(MopidyStatus.get().getCurrentTrackLenght()));
        smallTrackName.setText(MopidyStatus.get().getCurrentTrackName());
        smallArtistName.setText(MopidyStatus.get().getCurrentArtistsName());
        MopidyTlTrack track = MopidyStatus.get().getCurrentTrack();

        if (!isSameAlbumPrevious(track)){

            smallAlbumArt.setImageResource(R.drawable.no_album);

            if (track != null){
                previousAlbum = MopidyStatus.get().getCurrentTrack().getAlbum();
            }else{
                previousAlbum = null;
            }


            //Cancel previous load
            if (taskImage != null && !taskImage.isCancelled()){
                taskImage.cancel(true);
            }

            if (previousAlbum != null){
                // Load new Image
                TaskImage task = new TaskImage(new OnImageAndPaletteReady() {
                    @Override
                    public void onImageAndPaletteReady(Bitmap bitmap, Palette palette) {
                        smallAlbumArt.setImageBitmap(bitmap);
                        taskImage = null;
                        if (palette != null) {
                            Palette.Swatch s = PaletteManager.getVibrantDarkSwatch(palette);
                            if (s != null) {
                                smallNowPlaying.setBackgroundColor(s.getRgb());
                                playBackControls.setBackgroundColor(s.getRgb());
                                smallTrackName.setTextColor(s.getTitleTextColor());
                                smallArtistName.setTextColor(s.getBodyTextColor());
                            }
                        }
                    }
                } , previousAlbum, smallNowPlayingHeight, smallNowPlayingHeight);
                task.execute(getActivity());
            }


        }
    }

    private boolean isSameAlbumPrevious(MopidyTrack track){
        if (previousAlbum != null){
            if (track != null){
                if (track.getAlbum() != null){
                    return previousAlbum.equals(track.getAlbum());
                }
            }
        }
        return false;
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

    private void onVolumeChanged(){
        volumeSeekbar.setProgress(MopidyStatus.get().getVolume());
    }

    private void onMuteChanged(){
        if(MopidyStatus.get().isMute()){
            mutedImageView.setImageResource(R.drawable.ic_action_volume_muted);
        }else{
            mutedImageView.setImageResource(R.drawable.ic_action_volume_on);
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
                    case MopidyStatus.EVENT_VOLUME_CHANGED:
                        onVolumeChanged();
                        break;
                    case MopidyStatus.EVENT_MUTE_CHANGED:
                        onMuteChanged();
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
            if (seekBar == positionSeekBar){
                PlaybackControlManager.seekTo(seekBar.getContext(), seekBar.getProgress());
            }else{
                PlaybackControlManager.changeVolume(seekBar.getContext(), seekBar.getProgress());
            }
            fromUser = false;
        }

    }

    @Override
    public void onPanelSlide(View view, float v) {


        float alpha = 0;
        if (v < 1.0f) {
            alpha = (1-v/anchorPoint);
        }else{
            alpha = 0;
        }
        if (alpha > 0){
            smallNextButton.setVisibility(View.VISIBLE);
            smallPlayPauseButton.setVisibility(View.VISIBLE);
        }else{
            smallNextButton.setVisibility(View.GONE);
            smallPlayPauseButton.setVisibility(View.GONE);
        }
        smallNextButton.setAlpha(alpha);
        smallPlayPauseButton.setAlpha(alpha);

        ConnectedActivity activity = (ConnectedActivity) getActivity();

        //Elevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (anchorPoint < v){
                float pos = (v-anchorPoint)/(1-anchorPoint);
                smallNowPlaying.setElevation(pos*smallNowPlayingElevation);
                activity.getSupportActionBar().setElevation((1-pos)*actionBarElevation);
            }else{
                smallNowPlaying.setElevation(0);
                activity.getSupportActionBar().setElevation(actionBarElevation);
            }
        }


        LinearLayout.LayoutParams paramsBig = (LinearLayout.LayoutParams) nowPlayingContentBig.getLayoutParams();
        paramsBig.height = (int) (v * (nowPlayingContentBigHeight));
        nowPlayingContentBig.setLayoutParams(paramsBig);



        final TypedArray styledAttributes = getActivity().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });




        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        int contentSize = 0;
        if (v>anchorPoint){
            contentSize = connectedContentLayoutHeight -(int) (((float)connectedContentLayoutHeight + mActionBarSize)*anchorPoint);
        }else{
            contentSize = connectedContentLayoutHeight -(int) (((float)connectedContentLayoutHeight + mActionBarSize)*v);
        }

        LinearLayout.LayoutParams paramsContent = (LinearLayout.LayoutParams) connectedContentLayout.getLayoutParams();
        paramsContent.height = contentSize;
        connectedContentLayout.setLayoutParams(paramsContent);






        float changePoint = (float) connectedContentLayoutHeight/ (float) (connectedContentLayoutHeight + mActionBarSize);
        int offset = 0;
        if (v > changePoint){
            offset = (int)((v-changePoint)/(1-changePoint) * activity.getSupportActionBar().getHeight());
        }
        activity.getSupportActionBar().setHideOffset(offset);


    }

    @Override
    public void onPanelCollapsed(View view) {
    }

    @Override
    public void onPanelExpanded(View view) {
    }

    @Override
    public void onPanelAnchored(View view) {
        smallNextButton.setVisibility(View.GONE);
        smallPlayPauseButton.setVisibility(View.GONE);
        onPanelSlide(slidingPanel, anchorPoint);
    }

    @Override
    public void onPanelHidden(View view) {

    }


}
