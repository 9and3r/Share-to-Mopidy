package mopidy.to.share.and3r.sharetomopidy.user_interface.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.mikepenz.aboutlibraries.Libs;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.user_interface.OldMopidyDialog;
import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.ConnectedBaseFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.NowPlayingFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments.SearchFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments.LibraryFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments.PlaylistsFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.fragments.TracklistFragment;


public class ConnectedActivity extends ActionBarActivity implements  Observer {


    private SlidingUpPanelLayout slidingUpPanelLayout;
    private NowPlayingFragment nowPlayingFragment;
    private ViewPager mPager;
    private MyAdapter mPagerAdapter;
    //private View connectedPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connected_layout);



        mPager = (ViewPager) findViewById(R.id.connected_pager);
        mPager.setOffscreenPageLimit(4);
        mPagerAdapter = new MyAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.now_playing_fragment);

        nowPlayingFragment.setSlidingPanel(slidingUpPanelLayout);
        slidingUpPanelLayout.setPanelSlideListener(nowPlayingFragment);


        //connectedPager = findViewById(R.id.connected_content);
        mPager.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

        //getSupportActionBar().setElevation(0);


    }


    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            mPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            nowPlayingFragment.setConnectedContentLayout(mPager, mPager.getHeight());

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_disconnect) {
            disconnect();
            return true;
        } else if (id == R.id.action_about) {
                new Libs.Builder().withFields(R.string.class.getFields())
                        .withAboutIconShown(true)
                        .withAboutVersionShown(true)
                        .withLibraries("androidslidinguppanel", "androidasync", "mopidy")
                        .withActivityTitle(getString(R.string.about))
                        .start(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MopidyStatus.get().addObserver(this);
        onConnectionStateChange();
        onMopidyVersionLoaded();
        /*
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else {
            nowPlayingFragment.onPanelSlide(null, 0);
        }
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        MopidyStatus.get().deleteObserver(this);
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            ConnectedBaseFragment f = getCurrentFragment();
            if (!f.onBackPressed(this)){
                super.onBackPressed();
            }
        }

    }

    public void onConnectionStateChange() {
        if (MopidyStatus.get().getConnectionStatus() != MopidyStatus.CONNECTED) {
            Intent i = new Intent(this, NotConnectedActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    public void onMopidyVersionLoaded(){
        String version = MopidyStatus.get().getMopidyVersion();
        if (version != null){
            Log.d("Proba", version);
            String[] versionNumbers = version.split("\\.");
            if (Integer.parseInt(versionNumbers[0]) == 0){
                OldMopidyDialog dialog = new OldMopidyDialog();
                dialog.show(getSupportFragmentManager(), "OLD_MOPIDY_ERROR");

            }
        }
    }

    public void disconnect() {
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_STOP_SERVICE);
        startService(intent);
    }


    @Override
    public void update(Observable observable, Object data) {
        int event = (int) data;
        switch (event){
            case MopidyStatus.CONNECTION_STATE_CHANGED:

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onConnectionStateChange();
                    }
                });
                break;
            case MopidyStatus.EVENT_MOPIDY_VERSION_LOADED:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onMopidyVersionLoaded();
                    }
                });
                break;

        }
    }

    private ConnectedBaseFragment getCurrentFragment(){
        return (ConnectedBaseFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(mPager.getCurrentItem()));
    }

    private String getFragmentTag(int fragmentPosition){
        return "android:switcher:" + mPager.getId() + ":" + fragmentPosition;
    }



    public class MyAdapter extends FragmentPagerAdapter {

        private String[] titles;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            titles = getResources().getStringArray(R.array.connected_fragments_title);

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new TracklistFragment();
                case 1:
                    return new PlaylistsFragment();
                case 2:
                    return new LibraryFragment();
                case 3:
                    return new SearchFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
