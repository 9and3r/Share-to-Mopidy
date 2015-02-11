package mopidy.to.share.and3r.sharetomopidy.receiver;

import android.content.Intent;
import android.os.Bundle;

import mopidy.to.share.and3r.sharetomopidy.receiver.BaseReciever;

public class YoutubeReciever extends BaseReciever {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String value1 = extras.getString(Intent.EXTRA_TEXT);
        value1 = "yt:" + value1.substring(value1.indexOf("http://youtu"));
        sendNewTracklist(value1);
    }


}
