package mopidy.to.share.and3r.sharetomopidy;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;


public class PlaybackControlManager {

    public static void playOrPause(Context c){
        Intent playIntent = new Intent(c, MopidyService.class);
        playIntent.setAction(MopidyService.ACTION_ONE_ACTION);
        DefaultJSON playJSON = new DefaultJSON();
        if (MopidyStatus.get().isPlaying()){
            playJSON.setMethod("core.playback.pause");
        }else{
            playJSON.setMethod("core.playback.play");
        }
        playIntent.putExtra(MopidyService.ONE_ACTION_DATA, playJSON.toString());
        c.startService(playIntent);
    }

    public static void next(Context c){
        Intent nextIntent = new Intent(c, MopidyService.class);
        nextIntent.setAction(MopidyService.ACTION_ONE_ACTION);
        DefaultJSON nextJSON = new DefaultJSON();
        nextJSON.setMethod("core.playback.next");
        nextIntent.putExtra(MopidyService.ONE_ACTION_DATA, nextJSON.toString());
        c.startService(nextIntent);
    }

    public static void previous(Context c){
        Intent previousIntent = new Intent(c, MopidyService.class);
        previousIntent.setAction(MopidyService.ACTION_ONE_ACTION);
        DefaultJSON previousJSON = new DefaultJSON();
        previousJSON.setMethod("core.playback.previous");
        previousIntent.putExtra(MopidyService.ONE_ACTION_DATA, previousJSON.toString());
        c.startService(previousIntent);
    }

    public static void changeSingle(Context c){

        Intent intent = new Intent(c, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_single");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isSingle());
            json.put("params", params);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void changeConsume(Context c){
        Intent intent = new Intent(c, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_consume");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isConsume());
            json.put("params", params);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void changeRepeat(Context c){
        Intent intent = new Intent(c, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_repeat");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isRepeat());
            json.put("params", params);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void changeRandom(Context c){
        Intent intent = new Intent(c, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.tracklist.set_random");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isRandom());
            json.put("params", params);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void changeMute(Context c){
        Intent intent = new Intent(c, MopidyService.class);
        intent.setAction(MopidyService.ACTION_ONE_ACTION);
        try {
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.playback.set_mute");
            JSONObject params = new JSONObject();
            params.put("value",!MopidyStatus.get().isMute());
            json.put("params", params);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void seekTo(Context c, int value){
        try{
            Intent intent = new Intent(c, MopidyService.class);
            intent.setAction(MopidyService.ACTION_ONE_ACTION);
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.playback.seek");
            JSONObject params = new JSONObject();
            params.put("time_position", value);
            json.put("params", params);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void changeVolume(Context c, int volume){
        try{
            Intent intent = new Intent(c, MopidyService.class);
            intent.setAction(MopidyService.ACTION_ONE_ACTION);
            DefaultJSON json = new DefaultJSON();
            json.setMethod("core.playback.set_volume");
            JSONObject params = new JSONObject();
            params.put("volume",volume);
            json.put("params", params);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void playTrackListTlTrack(Context c, MopidyTlTrack track){
        Intent nextIntent = new Intent(c, MopidyService.class);
        nextIntent.setAction(MopidyService.ACTION_ONE_ACTION);
        DefaultJSON nextJSON = new DefaultJSON();
        nextJSON.setMethod("core.playback.play");
        JSONObject params = new JSONObject();
        try {
            params.put("tl_track", new JSONObject(track.getTl_track()));
            nextJSON.put("params", params);
            nextIntent.putExtra(MopidyService.ONE_ACTION_DATA, nextJSON.toString());
            c.startService(nextIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void addToTracklist(Context c, MopidyData data, boolean clear){
        try{
            DefaultJSON json =  new DefaultJSON();
            String[] actions = null;
            if (clear){
                actions = new String[2];
                json.setMethod("core.tracklist.clear");
                actions[0] = json.toString();
            }

            json.setMethod("core.tracklist.add");
            JSONObject params = new JSONObject();
            params.put("uri", data.getUri());
            json.put("params", params);

            Intent i = new Intent(c, MopidyService.class);

            if(clear){
                actions[1] = json.toString();
                i.setAction(MopidyService.ACTION_MAKE_ARRAY);
                i.putExtra(MopidyService.MAKE_ARRAY_DATA, actions);
            }else{
                i.setAction(MopidyService.ACTION_ONE_ACTION);
                i.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            }
            c.startService(i);

        }catch (JSONException e){

        }

    }

    public static void removeTlTrackFromTracklist(Context c, MopidyTlTrack track){
        try{
            DefaultJSON json = new DefaultJSON();
            JSONObject params = new JSONObject();
            JSONObject criteria = new JSONObject();
            JSONArray tlid = new JSONArray();
            JSONArray uri = new JSONArray();
            tlid.put(track.getTlId());
            criteria.put("tlid",tlid );
            uri.put(track.getUri());
            criteria.put("uri", uri);
            params.put("criteria", criteria);
            json.put("params", params);
            json.setMethod("core.tracklist.remove");

            Intent i = new Intent(c, MopidyService.class);
            i.setAction(MopidyService.ACTION_ONE_ACTION);
            i.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(i);

        }catch (JSONException e){

        }
    }

    public static void playTrackListTlTrack(Context c, int pos){
        MopidyTlTrack track = MopidyStatus.get().getTrack(pos);
        if (track != null){
            playTrackListTlTrack(c, track);
        }
    }
}
