package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.select.SelectFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial.ConnectingFragment;


public abstract class BaseViewPagerAdapterConnected extends FragmentStatePagerAdapter {


    public BaseViewPagerAdapterConnected(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        int status = MopidyStatus.get().getConnectionStatus();
        if (status == MopidyStatus.CONNECTING){
            return new ConnectingFragment();
        }else if (status == MopidyStatus.NOT_CONNECTED){
            return new SelectFragment();
        }else{
            return null;
        }
    }

    @Override
    public int getCount() {
        int status = MopidyStatus.get().getConnectionStatus();
        if (status != MopidyStatus.CONNECTED){
            return 1;
        }else{
            return -1;
        }
    }
}
