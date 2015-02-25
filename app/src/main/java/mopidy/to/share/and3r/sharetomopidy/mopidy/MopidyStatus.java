package mopidy.to.share.and3r.sharetomopidy.mopidy;

import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

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

    public static final int EVENT_SEEK = 7;

    private int connectionStatus;

    private MopidyTrack[] tracklist;
    private boolean repeat;
    private boolean random;
    private boolean consume;
    private boolean single;

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
        tracklist = new MopidyTrack[0];
        currentPos = 0;
    }


    public MopidyTrack getCurrentTrack(){
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

    public MopidyTrack getTrack(int pPos){
        if (pPos<tracklist.length && pPos>=0){
            return tracklist[pPos];
        }else{
            return null;
        }
    }



    public void trackChanged(JSONObject track){
        currentPos = positionInTracklist(track);
        setChanged();
        notifyObservers(EVENT_CURRENT_TRACK_CHANGED);
    }

    public void onSeek(long newPos){
        trackStartMillis = System.currentTimeMillis() - newPos;
        timePosition = newPos;
        setChanged();
        notifyObservers(EVENT_SEEK);
    }

    public int positionInTracklist(Object track){
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

    public MopidyTrack[] getTracklist() {
        return tracklist;
    }

    public void tracklistChanged(JSONArray array){
        if (array != null){
            MopidyTrack[] tracks = new MopidyTrack[array.length()];
            for (int i=0; i< array.length(); i++){
                try {
                    tracks[i] = new MopidyTrack(array.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            tracklist = tracks;
        }else{
            tracklist = new MopidyTrack[0];
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
