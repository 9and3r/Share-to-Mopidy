package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.select.SelectFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial.ConnectingFragment;


public class TrackPagerAdapter extends BaseViewPagerAdapterConnected{




    public TrackPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = super.getItem(position);
        if (f == null){
            OneTrackFragment fragment = new OneTrackFragment();
            Bundle bundle=new Bundle();
            bundle.putInt("pos", position);
            fragment.setArguments(bundle);
            return fragment;
        }else{
            return f;
        }

    }

    @Override
    public int getCount() {
        int cont = super.getCount();
        if (cont == -1){
            return MopidyStatus.get().getTracklist().length;
        }else{
            return cont;
        }
    }


    @Override
    public int getItemPosition(Object object) {
        int status = MopidyStatus.get().getConnectionStatus();
        if (status == MopidyStatus.CONNECTED){
            if (object instanceof OneTrackFragment){
                OneTrackFragment f = (OneTrackFragment) object;
                int pos = MopidyStatus.get().positionInTracklist(f.getTrack());
                if (pos == -1){
                    return POSITION_NONE;
                }else{
                    return pos;
                }
            }else{
                return POSITION_NONE;
            }
        }else if (status == MopidyStatus.CONNECTING){
            if (object instanceof ConnectingFragment){
                return 0;
            }else{
                return POSITION_NONE;
            }
        }else if (status == MopidyStatus.NOT_CONNECTED){
            if (object instanceof SelectFragment){
                return 0;
            }else{
                return POSITION_NONE;
            }
        }else{
            return POSITION_UNCHANGED;
        }
    }


}
