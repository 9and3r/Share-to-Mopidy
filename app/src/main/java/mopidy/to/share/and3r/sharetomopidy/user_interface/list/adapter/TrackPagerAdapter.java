package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.OneTrackFragment;


public class TrackPagerAdapter extends FragmentStatePagerAdapter {



    private MopidyTlTrack[] tracks;



    public TrackPagerAdapter(FragmentManager fm) {
        super(fm);
        tracks = MopidyStatus.get().getTracklist();
    }

    @Override
    public Fragment getItem(int position) {
            OneTrackFragment fragment = new OneTrackFragment();
            Bundle bundle=new Bundle();
            bundle.putInt("pos", position);
            fragment.setArguments(bundle);
            return fragment;
    }

    @Override
    public int getCount() {
        return tracks.length;
    }


    public int getItemPosition(Object object) {
        if (object instanceof OneTrackFragment){
            return indexOf(((OneTrackFragment) object).getTrack());
        }else{
            return POSITION_NONE;
        }

    }

    private int indexOf(MopidyTlTrack track){
        boolean found = false;
        int i = 0;
        while(!found && i < tracks.length){
            if (track.equals(tracks[i])){
                found = true;

            }else{
                i++;
            }
        }
        if (found){
            return i;
        }else{
            return POSITION_NONE;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        tracks = MopidyStatus.get().getTracklist();
        super.notifyDataSetChanged();
    }
}
