package mopidy.to.share.and3r.sharetomopidy.mopidy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyPlaylist;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;

public class MopidyStatus extends Observable{

    private static MopidyStatus myStatus;



    public static final int ERROR_CONNECTING = -2;
    public static final int NOT_CONNECTED = -1;
    public static final int CONNECTING = 0;
    public static final int CONNECTED = 1;



    public static final int PAUSED = 0;
    public static final int PLAYING = 1;
    public static final int STOPPED = -1;


    public static final int CONNECTION_STATE_CHANGED = -2;
    public static final int EVENT_RESET = -1;
    public static final int EVENT_TRACKLIST_CHANGED = 0;
    public static final int EVENT_PLAYBACK_STATE_CHANGED = 1;
    public static final int EVENT_CURRENT_TRACK_CHANGED = 2;


    public static final int EVENT_REPEAT_OPTION_CHANGED = 3;
    public static final int EVENT_RANDOM_OPTION_CHANGED = 4;
    public static final int EVENT_SINGLE_OPTION_CHANGED = 5;
    public static final int EVENT_CONSUME_OPTION_CHANGED = 6;
    public static final int EVENT_MUTE_CHANGED = 7;
    public static final int EVENT_VOLUME_CHANGED = 8;
    public static final int EVENT_PLAYLISTS_CHANGED = 10;
    public static final int EVENT_MOPIDY_VERSION_LOADED = 11;

    public static final int EVENT_SEEK = 9;

    private int connectionStatus;

    private String mopidyVersion;
    private MopidyTlTrack[] tracklist;
    private boolean repeat;
    private boolean random;
    private boolean consume;
    private boolean single;
    private boolean mute;
    private int volume;
    private MopidyPlaylist[] playlists;

    private long trackStartMillis;
    private long timePosition;

    public int getCurrentPos() {
        return currentPos;
    }

    private int currentPos;
    private int playbackState;

    private MopidyStatus(){
        reset();
    }

    public static synchronized MopidyStatus get(){
        if (myStatus == null){
            myStatus = new MopidyStatus();
        }
        return myStatus;
    }

    public void reset(){
        setConnectionStatus(NOT_CONNECTED);
        playbackState = STOPPED;
        tracklist = new MopidyTlTrack[0];
        playlists = new MopidyPlaylist[0];
        currentPos = 0;
    }


    public MopidyTlTrack getCurrentTrack(){
        if (currentPos<tracklist.length && currentPos>=0){
            return tracklist[currentPos];
        }else{
            return null;
        }
    }

    public String getCurrentTrackName(){
        if (currentPos<tracklist.length && currentPos>=0){
            return tracklist[currentPos].getTrackString();
        }else{
            return "";
        }
    }

    public String getCurrentAlbumName(){
        if (currentPos<tracklist.length && currentPos>=0){
            return tracklist[currentPos].getAlbumString();
        }else{
            return "";
        }
    }

    public String getCurrentArtistsName(){
        if (currentPos<tracklist.length && currentPos>=0){
            return tracklist[currentPos].getArtistsString();
        }else{
            return "";
        }
    }

    public int getCurrentTrackLenght(){
        if (currentPos<tracklist.length && currentPos>=0){
            return tracklist[currentPos].getLength();
        }else{
            return 0;
        }
    }

    public MopidyTlTrack getTrack(int pPos){
        if (pPos<tracklist.length && pPos>=0){
            return tracklist[pPos];
        }else{
            return null;
        }
    }



    public void trackChanged(JSONObject track){
        try {
            currentPos = positionInTracklist(new MopidyTlTrack(track));
            setChanged();
            notifyObservers(EVENT_CURRENT_TRACK_CHANGED);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onSeek(long newPos){
        trackStartMillis = System.currentTimeMillis() - newPos;
        timePosition = newPos;
        setChanged();
        notifyObservers(EVENT_SEEK);
    }

    public int positionInTracklist(MopidyTrack track){
        int pos = 0;
        boolean found = false;
        while(!found && pos<tracklist.length){
            if (tracklist[pos].equals(track)){
                found = true;
            }else{
                pos++;
            }
        }
        if (found){
            return pos;
        }else{
            return -1;
        }
    }

    public MopidyTlTrack[] getTracklist() {
        return tracklist;
    }

    public void tracklistChanged(JSONArray array){
        if (array != null){
            MopidyTlTrack[] tracks = new MopidyTlTrack[array.length()];
            for (int i=0; i< array.length(); i++){
                try {
                    tracks[i] = new MopidyTlTrack(array.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            tracklist = tracks;
        }else{
            tracklist = new MopidyTlTrack[0];
        }
        setChanged();
        notifyObservers(EVENT_TRACKLIST_CHANGED);
    }

    public int getConnectionStatus() {
        return connectionStatus;
    }



    public void setConnectionStatus(int pConnectionStatus) {
        boolean notify = (connectionStatus != pConnectionStatus);
        connectionStatus = pConnectionStatus;
        if (notify){
            setChanged();
            notifyObservers(CONNECTION_STATE_CHANGED);
        }
    }

    public void playbackStateChanged(int newStatus){
        playbackState = newStatus;
        switch (newStatus){
            case PLAYING:
                trackStartMillis = System.currentTimeMillis() - timePosition;
                break;
            case PAUSED:
                timePosition = System.currentTimeMillis() - trackStartMillis;
                break;
            case STOPPED:
                timePosition = 0;
                break;
        }
        setChanged();
        notifyObservers(EVENT_PLAYBACK_STATE_CHANGED);
    }

    public MopidyPlaylist[] getPlaylists() {
        return playlists;
    }

    public void onPlaylistsChanged(JSONArray array){
        try {
            MopidyPlaylist[] newPlaylists = new MopidyPlaylist[array.length()];
            for (int i=0; i<array.length(); i++){
                newPlaylists[i] = new MopidyPlaylist(array.getJSONObject(i));
            }
            playlists = newPlaylists;
            setChanged();

            notifyObservers(EVENT_PLAYLISTS_CHANGED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying(){
        return (playbackState == PLAYING);
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean pRepeat) {
        boolean notify = (repeat != pRepeat);
        repeat = pRepeat;
        if (notify){
            setChanged();
            notifyObservers(EVENT_REPEAT_OPTION_CHANGED);
        }
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean pMute) {
        boolean notify = (mute != pMute);
        mute = pMute;
        if (notify){
            setChanged();
            notifyObservers(EVENT_MUTE_CHANGED);
        }
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int pVolume) {
        boolean notify = (volume != pVolume);
        this.volume = pVolume;
        if (notify){
            setChanged();
            notifyObservers(EVENT_VOLUME_CHANGED);
        }
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean pRandom) {
        boolean notify = (random != pRandom);
        random = pRandom;
        if (notify){
            setChanged();
            notifyObservers(EVENT_RANDOM_OPTION_CHANGED);
        }
    }

    public boolean isConsume() {
        return consume;
    }

    public void setConsume(boolean pConsume) {
        boolean notify = (consume != pConsume);
        consume = pConsume;
        if (notify){
            setChanged();
            notifyObservers(EVENT_CONSUME_OPTION_CHANGED);
        }
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean pSingle) {
        boolean notify = (single != pSingle);
        single = pSingle;
        if (notify){
            setChanged();
            notifyObservers(EVENT_SINGLE_OPTION_CHANGED);
        }
    }

    public int getCurrentSeekPos(){
        if (playbackState == PLAYING){
            return (int) (System.currentTimeMillis() - trackStartMillis);
        }else{
            return (int) timePosition;
        }
    }

    public MopidyTlTrack findTrackInTracklist(MopidyData data){
        boolean found = false;
        int i=0;
        MopidyTlTrack track = null;
        while(!found && i<tracklist.length){
            track = tracklist[i];
            if (track.equals(data)){
                found = true;
            }
        }
        if (found){
            return track;
        }else{
            return null;
        }
    }

    public String getMopidyVersion() {
        return mopidyVersion;
    }

    public void setMopidyVersion(String mopidyVersion) {
        this.mopidyVersion = mopidyVersion;
        setChanged();
        notifyObservers(EVENT_MOPIDY_VERSION_LOADED);
    }

    public String getCurrentSeekPosString(){
        return milisToHumanTime(getCurrentSeekPos());
    }

    public static String milisToHumanTime(long pTime){
        int seconds = ((int) pTime/1000) % 60;
        int minutes = (int) (pTime / 60000);
        String time = String.valueOf(minutes) + ":";
        if (seconds == 0){
            time = time + "00";
        }else if (seconds<10){
            time = time + "0" + String.valueOf(seconds);
        }else{
            time = time + String.valueOf(seconds);
        }
        return time;
    }
}
