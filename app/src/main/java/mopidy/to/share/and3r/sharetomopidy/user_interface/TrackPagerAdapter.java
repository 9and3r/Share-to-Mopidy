package mopidy.to.share.and3r.sharetomopidy.user_interface;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;


public class TrackPagerAdapter extends FragmentStatePagerAdapter {


    private MopidyTrack[] tracks;



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


    @Override
    public int getItemPosition(Object object) {
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

    }

    @Override
    public void notifyDataSetChanged() {
        tracks = MopidyStatus.get().getTracklist();
        super.notifyDataSetChanged();
    }
}
