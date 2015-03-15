package mopidy.to.share.and3r.sharetomopidy.user_interface.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;


public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Class<? extends Activity> activityClass;
        if(MopidyStatus.get().getConnectionStatus() == MopidyStatus.CONNECTED)
            activityClass = ConnectedActivity.class;
        else
            activityClass = NotConnectedActivity.class;

        Intent newActivity = new Intent(this, activityClass);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(newActivity);
    }
}
