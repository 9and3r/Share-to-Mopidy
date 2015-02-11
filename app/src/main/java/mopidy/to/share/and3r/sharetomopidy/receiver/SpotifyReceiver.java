package mopidy.to.share.and3r.sharetomopidy.receiver;

import android.net.Uri;
import android.os.Bundle;

import mopidy.to.share.and3r.sharetomopidy.receiver.BaseReciever;


public class SpotifyReceiver extends BaseReciever {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        String[] datas = uri.getQuery().split("/");
        String data;
        if (datas[datas.length-2].toLowerCase().equals("playlist")){
             data = "spotify:" + datas[datas.length-4] + ":" + datas[datas.length-3] +":"  + datas[datas.length-2] + ":" + datas[datas.length-1];
        }else{
            data = "spotify:" + datas[datas.length-2] + ":" + datas[datas.length-1];
        }
        sendNewTracklist(data);
    }

}
