package mopidy.to.share.and3r.sharetomopidy.receiver;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;
import mopidy.to.share.and3r.sharetomopidy.MopidyService;


public abstract class BaseReciever extends ActionBarActivity {


    public void sendNewTracklist(String url){
        String[] actions = new String[3];
        DefaultJSON json = new DefaultJSON();
        json.setMethod("core.tracklist.clear");
        actions[0] = json.toString();
        json.setMethod("core.tracklist.add");
        json.addParam("uri", url);
        actions[1] = json.toString();
        json = new DefaultJSON();
        json.setMethod("core.playback.play");
        actions[2] = json.toString();
        Intent intent = new Intent(this, MopidyService.class);
        intent.putExtra(MopidyService.MAKE_ARRAY_DATA, actions);
        intent.setAction(MopidyService.ACTION_MAKE_ARRAY);
        startService(intent);
        finish();
    }

}
