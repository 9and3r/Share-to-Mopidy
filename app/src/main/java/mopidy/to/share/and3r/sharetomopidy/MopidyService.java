package mopidy.to.share.and3r.sharetomopidy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.media.RemoteController;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.graphics.Palette;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyConnection;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyAlbum;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfig;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.OnImageAndPalleteReady;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.TaskImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial.ConfigurationActivity;
import mopidy.to.share.and3r.sharetomopidy.user_interface.activity.MainActivity;

public class MopidyService extends Service implements Observer {




    public static final String ACTION_CONNECT="CONNECT_MOPIDY";
    public static final String ACTION_STOP_SERVICE="SERVICE_STOP";
    public static final String ACTION_MAKE_ARRAY ="ACTION_MAKE_ARRAY";
    public static final String ACTION_ARRAY_DATA ="ACTION_ARRAY_DATA";
    public static final String ACTION_DATA="ACTION_DATA";
    public static final String ACTION_ONE_ACTION="ACTION_ONE_ACTION";

    public static final String CONFIG_ID="CONFIG_ID";
    public static final int NOTIFICATION_ID=1;
    public static final int NOTIFICATION_ID_ERROR=2;
    private RemoteControlClient mRemoteControlClient;
    private ComponentName mRemoteControlClientReceiverComponent;

    private Bitmap lastBitmap;
    private MopidyAlbum lastAlbum;
    private TaskImage task;
    private android.media.AudioManager.OnAudioFocusChangeListener audioFocusManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MopidyServerConfig config;
        if(intent!=null && intent.hasExtra(CONFIG_ID)){
            config = MopidyServerConfigManager.get().getConfig(getApplicationContext(), intent.getIntExtra(CONFIG_ID, 0), true);
        }else{
            config = MopidyServerConfigManager.get().getCurrentServer(getApplicationContext());
        }
        if (intent == null || intent.getAction() == ACTION_CONNECT){
            startConnection(config);
        }else if (intent.getAction() == ACTION_STOP_SERVICE){
            stopConnection();
        }else if (intent.getAction() == ACTION_MAKE_ARRAY){
            startConnection(config);
            MopidyConnection.get().addActions(intent.getStringArrayExtra(ACTION_ARRAY_DATA));
        }else if (intent.getAction() == ACTION_ONE_ACTION){
            startConnection(config);
            MopidyConnection.get().addAction(intent.getStringExtra(ACTION_DATA));
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void update(Observable observable, Object data) {
        int event = (int) data;
        switch (event){
            case MopidyStatus.CONNECTION_STATE_CHANGED:
                onConnectionStateChanged();
                break;
            case MopidyStatus.EVENT_CURRENT_TRACK_CHANGED:
                onTrackChanged();
                break;
            case MopidyStatus.EVENT_PLAYBACK_STATE_CHANGED:
                changeMetadata();
                changeNotification();
                break;
        }
    }

    private void onConnectionStateChanged(){
        int state = MopidyStatus.get().getConnectionStatus();
        switch (state){
            case MopidyStatus.NOT_CONNECTED:
                stopConnection();
                break;
            case MopidyStatus.CONNECTING:
                onConnecting();
                break;
            case MopidyStatus.CONNECTED:
                onConnected();
                break;
            case MopidyStatus.ERROR_CONNECTING:
                onErrorConnecting();
                break;
        }
    }

    private void startConnection(MopidyServerConfig config){
        if (MopidyStatus.get().getConnectionStatus() != MopidyStatus.CONNECTED){
            MopidyStatus.get().addObserver(this);
            MopidyConnection.get().connect(config.getIp(),config.getPort());

            // Remove error notification if exists
            NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(NOTIFICATION_ID_ERROR);



            startForeground(NOTIFICATION_ID, loadingNotification());
        }
    }

    private void onConnecting(){
        NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, loadingNotification());
    }

    private void onErrorConnecting(){

            PendingIntent intent = PendingIntent.getActivity(this,5, new Intent(this, ConfigurationActivity.class), 0);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_notification)
                            .setContentTitle(getString(R.string.error_connecting_title))
                            .setContentText(getString(R.string.error_connecting_text))
                            .setContentIntent(intent)
                            .setTicker(getString(R.string.error_connecting_title));
            NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID_ERROR, mBuilder.build());
            stopConnection();


    }

    private void onConnected(){

            AudioManager audioManager = (AudioManager)getApplicationContext().getSystemService(AUDIO_SERVICE);
            mRemoteControlClientReceiverComponent = new ComponentName(getPackageName(), MediaButtonsReceiver.class.getName());
            audioManager.registerMediaButtonEventReceiver(mRemoteControlClientReceiverComponent);

            if (mRemoteControlClient == null) {
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(mRemoteControlClientReceiverComponent);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);

                // create and register the remote control client
                mRemoteControlClient = new RemoteControlClient(mediaPendingIntent);
                audioManager.registerRemoteControlClient(mRemoteControlClient);
            }
            audioFocusManager = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {

                }};
            audioManager.requestAudioFocus(audioFocusManager
            ,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            mRemoteControlClient.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP|
                            RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE|
                            RemoteControlClient.FLAG_KEY_MEDIA_POSITION_UPDATE);



    }

    private Notification loadingNotification(){
        PendingIntent intent = PendingIntent.getActivity(this, 4, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setContentTitle(getString(R.string.connecting))
                        .setProgress(0, 0, true)
                        .setContentIntent(intent);
        return mBuilder.build();
    }

    public void onTrackChanged(){
        MopidyTrack track = MopidyStatus.get().getCurrentTrack();
        changeMetadata();
        changeNotification();
        if (track != null){
            if(task != null){
                if(lastAlbum != null && lastAlbum.equals(track.getAlbum())){
                    // We are loading image for previous track but as both of them have the same album we can reuse when finishes to load
                }else{
                    // We must load new album bitmap
                    task.cancel(true);
                    loadBitmap(track.getAlbum());
                }
            }else{
                if (lastBitmap != null){
                    if (lastAlbum.equals(track.getAlbum())){
                        // We can use previous loaded image
                        onBitmapLoaded();
                    }else{
                        loadBitmap(track.getAlbum());
                    }
                }else{
                    loadBitmap(track.getAlbum());
                }
            }
        }

    }

    private void loadBitmap(final MopidyAlbum pAlbum){
        lastBitmap = null;
        lastAlbum = null;
        task = new TaskImage(new OnImageAndPalleteReady() {
            @Override
            public void onImageAndPalleteReady(Bitmap bitmap, Palette palette) {
                lastBitmap = bitmap;
                lastAlbum = pAlbum;
                task = null;
                onBitmapLoaded();
            }
        }, pAlbum);
        task.execute(getApplicationContext());
    }

    private void onBitmapLoaded(){
        changeNotification();
        changeMetadata();
    }

    private void changeMetadata(){


        RemoteControlClient.MetadataEditor editor = mRemoteControlClient.editMetadata(true);
        editor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, MopidyStatus.get().getCurrentAlbumName());
        editor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, MopidyStatus.get().getCurrentArtistsName());
        editor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, MopidyStatus.get().getCurrentTrackName());
        editor.putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, MopidyStatus.get().getCurrentTrackLenght());
        editor.putBitmap(RemoteController.MetadataEditor.BITMAP_KEY_ARTWORK, lastBitmap);
        editor.apply();

        int status = MopidyStatus.get().getConnectionStatus();
        float speed = 0.0f;
        int playStatus = RemoteControlClient.PLAYSTATE_ERROR;
        switch (status){
            case MopidyStatus.PLAYING:
                playStatus = RemoteControlClient.PLAYSTATE_PLAYING;
                speed = 1.0f;
                break;
            case MopidyStatus.PAUSED:
                playStatus = RemoteControlClient.PLAYSTATE_PAUSED;
                speed = 0.0f;
                break;
            case MopidyStatus.STOPPED:
                playStatus = RemoteControlClient.PLAYSTATE_STOPPED;
                speed = 0.0f;
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mRemoteControlClient.setPlaybackState(playStatus,MopidyStatus.get().getCurrentSeekPos(),speed);
        }else{
            mRemoteControlClient.setPlaybackState(playStatus);
        }



    }


    private void changeNotification(){

        // Main intent
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingMain = PendingIntent.getActivity(this,0,intentMain,0);

        // Previous intent
        Intent previousIntent = new Intent(this, MopidyService.class);
        previousIntent.setAction(ACTION_ONE_ACTION);
        DefaultJSON previousJSON = new DefaultJSON();
        previousJSON.setMethod("core.playback.previous");
        previousIntent.putExtra(ACTION_DATA, previousJSON.toString());
        PendingIntent pendingPrevious = PendingIntent.getService(this,1,previousIntent,0);

        // Next intent
        Intent nextIntent = new Intent(this, MopidyService.class);
        nextIntent.setAction(ACTION_ONE_ACTION);
        DefaultJSON nextJSON = new DefaultJSON();
        nextJSON.setMethod("core.playback.next");
        nextIntent.putExtra(ACTION_DATA, nextJSON.toString());
        PendingIntent pendingNext = PendingIntent.getService(this,2,nextIntent,0);


        // Play/Pause intent
        Intent playIntent = new Intent(this, MopidyService.class);
        playIntent.setAction(ACTION_ONE_ACTION);
        DefaultJSON playJSON = new DefaultJSON();
        int playPauseDrawable;
        if (MopidyStatus.get().isPlaying()){
            playJSON.setMethod("core.playback.pause");
            playPauseDrawable = R.drawable.ic_action_pause;
        }else{
            playJSON.setMethod("core.playback.play");
            playPauseDrawable = R.drawable.ic_action_play;
        }
        playIntent.putExtra(ACTION_DATA, playJSON.toString());
        PendingIntent pendingPlayPause = PendingIntent.getService(this,3,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);

        builder
                .setContentTitle(MopidyStatus.get().getCurrentTrackName())
                .setContentText(MopidyStatus.get().getCurrentArtistsName())
                .setContentInfo(MopidyStatus.get().getCurrentAlbumName())
                .setTicker(MopidyStatus.get().getCurrentTrackName() + " " +MopidyStatus.get().getCurrentArtistsName())
                .setContentIntent(pendingMain)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .addAction(R.drawable.ic_action_previous, null, pendingPrevious)
                .addAction(playPauseDrawable, null, pendingPlayPause)
                .addAction(R.drawable.ic_action_next, null, pendingNext)
                .setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification.MediaStyle style = new Notification.MediaStyle();
            style.setShowActionsInCompactView(0,1,2);
            builder.setCategory(Notification.CATEGORY_TRANSPORT);
            builder.setStyle(style);
        }
        if (lastBitmap != null){
            builder.setLargeIcon(Bitmap.createScaledBitmap(lastBitmap,(int) getResources().getDimension(android.R.dimen.notification_large_icon_width),(int) getResources().getDimension(android.R.dimen.notification_large_icon_height), false));
        }
        NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void stopConnection(){

        MopidyStatus.get().deleteObserver(this);
        MopidyStatus.get().reset();
        MopidyConnection.get().stop();
        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(audioFocusManager);

        stopForeground(true);
        stopSelf();
    }


}
