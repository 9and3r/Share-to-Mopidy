package mopidy.to.share.and3r.sharetomopidy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;
import mopidy.to.share.and3r.sharetomopidy.preferences.PreferencesManager;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial.ConfigurationActivity;


public class ApplicationShareToMopidy extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        init(getApplicationContext());
    }


    public static void init(Context context) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(options).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        MopidyServerConfigManager.get().init(context);
        if(PreferencesManager.get().isFirsTime(context)){
            MopidyServerConfigManager.get().createNewMopidyServer(context);
            Intent intent = new Intent(context, ConfigurationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
