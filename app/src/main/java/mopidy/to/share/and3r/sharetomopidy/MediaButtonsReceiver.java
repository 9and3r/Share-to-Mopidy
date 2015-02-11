package mopidy.to.share.and3r.sharetomopidy;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class MediaButtonsReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)){
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event != null && event.getAction() == KeyEvent.ACTION_UP){

                if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
                    PlaybackControlManager.playOrPause(context);

                }else if(event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY){
                    PlaybackControlManager.playOrPause(context);

                }else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT){
                    PlaybackControlManager.next(context);

                }else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
                    PlaybackControlManager.previous(context);
                }
            }
        }
    }
}
