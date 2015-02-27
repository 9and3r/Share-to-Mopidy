package mopidy.to.share.and3r.sharetomopidy.mopidy;

import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyPlaylist;


public class MopidyConnection {

    private static MopidyConnection myConnection;

    private WebSocket webSocket;
    private LinkedList<String> actionsNoReturn;

    private MopidyConnection() {

    }

    public static synchronized MopidyConnection get(){
        if (myConnection == null){
            myConnection = new MopidyConnection();
        }
        return myConnection;
    }

    private void reset(){
        stop();
        actionsNoReturn = new LinkedList<>();
        DefaultJSON action = new DefaultJSON();
        try {
            action.put("id", 3);
            action.setMethod("core.playback.get_state");
            actionsNoReturn.addLast(action.toString());

            action.put("id",4);
            action.setMethod("core.tracklist.get_tl_tracks");
            actionsNoReturn.addLast(action.toString());

            action.put("id",2);
            action.setMethod("core.playback.get_current_tl_track");
            actionsNoReturn.addLast(action.toString());

            checkOptions();

            action.put("id",9);
            action.setMethod("core.playback.get_time_position");
            actionsNoReturn.addLast(action.toString());

            action.put("id",10);
            action.setMethod("core.playback.get_volume");
            actionsNoReturn.addLast(action.toString());

            action.put("id",11);
            action.setMethod("core.playback.get_mute");
            actionsNoReturn.addLast(action.toString());

            DefaultJSON action2 = new DefaultJSON();
            action2.put("id",12);
            action2.setMethod("core.playlists.get_playlists");
            JSONObject params = new JSONObject();
            params.put("include_tracks", false);
            action2.put("params", params);
            actionsNoReturn.addLast(action2.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void connect(String ip, int port){
        reset();
        MopidyStatus.get().setConnectionStatus(MopidyStatus.CONNECTING);
        AsyncHttpClient.getDefaultInstance().websocket("http://"+ip+":"+String.valueOf(port)+"/mopidy/ws", "ws", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, final WebSocket pWebSocket) {
                if (ex != null) {
                    MopidyStatus.get().setConnectionStatus(MopidyStatus.ERROR_CONNECTING);
                    ex.printStackTrace();
                } else {
                    webSocket = pWebSocket;
                    MopidyStatus.get().setConnectionStatus(MopidyStatus.CONNECTED);
                    webSocket.setStringCallback(new WebSocket.StringCallback() {
                        @Override
                        public void onStringAvailable(String s) {
                            onStringReceived(s);
                        }
                    });
                    webSocket.setClosedCallback(new CompletedCallback() {
                        @Override
                        public void onCompleted(Exception ex) {
                            Log.d("Received:", "Web socket closed");
                            MopidyStatus.get().setConnectionStatus(MopidyStatus.ERROR_CONNECTING);
                        }
                    });
                    makeAction();
                }
            }
        });

    }

    public void addActions(String[] actions){
        for(int i=0; i< actions.length; i++){
            actionsNoReturn.addLast(actions[i]);
        }
        makeAction();
    }

    public void addAction(String action){
        actionsNoReturn.addLast(action);
        makeAction();
    }

    public void stop(){
        MopidyStatus.get().setConnectionStatus(MopidyStatus.NOT_CONNECTED);
        if (webSocket != null){
            Log.d("User_command", "Closing socket");
            webSocket.setStringCallback(null);
            webSocket.setClosedCallback(null);
            webSocket.close();
            webSocket = null;
        }
    }

    public void onStringReceived(String s) {

        Log.d("Received: ", s);
        try {
            JSONObject object = new JSONObject(s);
            if (object.has("event")){
                String event = object.getString("event").toLowerCase();
                if (event.equals("playback_state_changed")){
                    if (object.getString("new_state").equals("playing")){
                        MopidyStatus.get().playbackStateChanged(MopidyStatus.PLAYING);
                    }else if (object.getString("new_state").equals("paused")){
                        MopidyStatus.get().playbackStateChanged(MopidyStatus.PAUSED);
                    }else{
                        MopidyStatus.get().playbackStateChanged(MopidyStatus.STOPPED);
                    }
                }else if (event.equals("track_playback_ended")){
                    //service.mopidyStatus.onTrackChanged(0);
                }else if (event.equals("track_playback_started") || event.equals("track_playback_paused") || event.equals("track_playback_resumed")){
                    MopidyStatus.get().trackChanged(object.getJSONObject("tl_track"));
                }else if (event.equals("tracklist_changed")){
                    MopidyStatus.get().tracklistChanged(null);
                    DefaultJSON json = new DefaultJSON();
                    json.put("id",4);
                    json.setMethod("core.tracklist.get_tl_tracks");
                    addAction(json.toString());
                }else if (event.equals("options_changed")){
                    checkOptions();
                }else if (event.equals("seeked")){
                    MopidyStatus.get().onSeek(object.getLong("time_position"));
                }else if (event.equals("mute_changed")){
                    MopidyStatus.get().setMute(object.getBoolean("mute"));
                }else if (event.equals("volume_changed")){
                    MopidyStatus.get().setVolume(object.getInt("volume"));
                }
            }else if (object.has("id") && !object.isNull("id")){
                int id = object.getInt("id");
                switch (id){
                    case 2:
                        if (!object.isNull("result")){
                            MopidyStatus.get().trackChanged(object.getJSONObject("result"));
                        }
                        break;
                    case 3:
                        String status = object.getString("result").toLowerCase();
                        if (status.equals("playing")){
                            MopidyStatus.get().playbackStateChanged(MopidyStatus.PLAYING);
                        }else if (status.equals("paused")){
                            MopidyStatus.get().playbackStateChanged(MopidyStatus.PAUSED);
                        }else{
                            MopidyStatus.get().playbackStateChanged(MopidyStatus.STOPPED);
                        }
                        break;
                    case 4:
                        JSONArray list = object.getJSONArray("result");
                        MopidyStatus.get().tracklistChanged(list);
                        DefaultJSON json = new DefaultJSON();
                        json.put("id",2);
                        json.setMethod("core.playback.get_current_tl_track");
                        addAction(json.toString());
                        break;
                    case 5:
                        MopidyStatus.get().setRandom(object.getBoolean("result"));
                        break;
                    case 6:
                        MopidyStatus.get().setRepeat(object.getBoolean("result"));
                        break;
                    case 7:
                        MopidyStatus.get().setSingle(object.getBoolean("result"));
                        break;
                    case 8:
                        MopidyStatus.get().setConsume(object.getBoolean("result"));
                        break;
                    case 9:
                        MopidyStatus.get().onSeek(object.getInt("result"));
                        break;
                    case 10:
                        MopidyStatus.get().setVolume(object.getInt("result"));
                        break;
                    case 11:
                        MopidyStatus.get().setMute(object.getBoolean("result"));
                        break;
                    case 12:
                        JSONArray playlists = object.getJSONArray("result");
                        MopidyStatus.get().onPlaylistsChanged(playlists);
                    }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private synchronized void makeAction(){
        if (MopidyStatus.get().getConnectionStatus() == MopidyStatus.CONNECTED && actionsNoReturn.size()>0){
            webSocket.send(actionsNoReturn.pollFirst());
            if (actionsNoReturn.size()>0){
                makeAction();
            }
        }
    }

    public void checkOptions(){
        DefaultJSON action = new DefaultJSON();
        try {
            action.put("id",5);
            action.setMethod("core.tracklist.get_random");
            actionsNoReturn.addLast(action.toString());

            action.put("id",6);
            action.setMethod("core.tracklist.get_repeat");
            actionsNoReturn.addLast(action.toString());

            action.put("id",7);
            action.setMethod("core.tracklist.get_single");
            actionsNoReturn.addLast(action.toString());

            action.put("id",8);
            action.setMethod("core.tracklist.get_consume");
            actionsNoReturn.addLast(action.toString());

        }catch (JSONException e){

        }
        makeAction();

    }


}
