package mopidy.to.share.and3r.sharetomopidy;

import android.content.Context;
import android.content.Intent;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;


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
        playIntent.putExtra(MopidyService.ACTION_DATA, playJSON.toString());
        c.startService(playIntent);
    }

    public static void next(Context c){
        Intent nextIntent = new Intent(c, MopidyService.class);
        nextIntent.setAction(MopidyService.ACTION_ONE_ACTION);
        DefaultJSON nextJSON = new DefaultJSON();
        nextJSON.setMethod("core.playback.next");
        nextIntent.putExtra(MopidyService.ACTION_DATA, nextJSON.toString());
        c.startService(nextIntent);
    }

    public static void previous(Context c){
        Intent previousIntent = new Intent(c, MopidyService.class);
        previousIntent.setAction(MopidyService.ACTION_ONE_ACTION);
        DefaultJSON previousJSON = new DefaultJSON();
        previousJSON.setMethod("core.playback.previous");
        previousIntent.putExtra(MopidyService.ACTION_DATA, previousJSON.toString());
        c.startService(previousIntent);
    }
}
