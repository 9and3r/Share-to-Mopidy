package mopidy.to.share.and3r.sharetomopidy.preferences;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesManager {

    private static PreferencesManager myManager;

    private static final String MY_PREFERENCES_NAME = "PREFERENCES";

    private static final String IMAGE_URL_PREFERENCES = "IMAGE_PREFERENCES";

    private static final String IP_KEY = "IP";
    private static final String DEFAULT_IP = "192.168.1.2";

    private static final String PORT_KEY = "PORT_IP";
    private static final int DEFAULT_PORT = 6680;

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


    public void saveImageUrl(Context c, String key, String url){
        SharedPreferences p = c.getSharedPreferences(IMAGE_URL_PREFERENCES, Context.MODE_PRIVATE);
        p.edit().putString(key, url).commit();
    }

    public String getImageUrl(Context c , String key){
        SharedPreferences p = c.getSharedPreferences(IMAGE_URL_PREFERENCES, Context.MODE_PRIVATE);
        return p.getString(key, null);
    }





}
