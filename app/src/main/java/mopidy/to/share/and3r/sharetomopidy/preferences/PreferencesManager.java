package mopidy.to.share.and3r.sharetomopidy.preferences;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesManager {

    private static PreferencesManager myManager;

    private static final String MY_PREFERENCES_NAME = "PREFERENCES";

    private static final String FIRS_TIME_KEY = "FIRST_TIME";

    private PreferencesManager(){}

    public static synchronized PreferencesManager get(){
        if (myManager == null){
            myManager = new PreferencesManager();
        }
        return myManager;
    }

    public boolean isFirsTime(Context c){
        SharedPreferences p = c.getSharedPreferences(MY_PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean firstTime = p.getBoolean(FIRS_TIME_KEY, true);
        if (firstTime){
            p.edit().putBoolean(FIRS_TIME_KEY, false).commit();
        }
        return firstTime;
    }

}
