package mopidy.to.share.and3r.sharetomopidy.user_interface.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.fragments.NowPlayingFragment;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.PlaylistsAdapter;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.TrackListAdapter;


public class ConnectedActivity extends ActionBarActivity implements  Observer {


    private SlidingUpPanelLayout slidingUpPanelLayout;
    private NowPlayingFragment nowPlayingFragment;
    private ViewPager mPager;
    private MyAdapter mPagerAdapter;


    private static final int FRAGMENT_TRACKLIST = 0;
    private static final int FRAGMENT_PLAYLISTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connected_layout);

        mPager = (ViewPager) findViewById(R.id.connected_pager);
        mPagerAdapter = new MyAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.now_playing_fragment);
        slidingUpPanelLayout.setPanelSlideListener(nowPlayingFragment);
        nowPlayingFragment.setSlidingPanel(slidingUpPanelLayout);


    }

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
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onConnectionStateChange();
        MopidyStatus.get().addObserver(this);
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
            super.onBackPressed();
        }

    }

    public void onConnectionStateChange() {
        if (MopidyStatus.get().getConnectionStatus() != MopidyStatus.CONNECTED) {
            Intent i = new Intent(this, NotConnectedActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    public void disconnect() {
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_STOP_SERVICE);
        startService(intent);
    }


    @Override
    public void update(Observable observable, Object data) {
        if (((int) data) == MopidyStatus.CONNECTION_STATE_CHANGED) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onConnectionStateChange();
                }
            });
        }
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

    public static class ArrayListFragment extends Fragment {
        int pos;
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private BaseListAdapter mAdapter;


        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            pos = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.recycler_layout, container, false);


            mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            switch (pos){
                case FRAGMENT_TRACKLIST:
                    mAdapter = new TrackListAdapter();
                    break;
                case FRAGMENT_PLAYLISTS:
                    mAdapter = new PlaylistsAdapter();
                    break;
            }
            mRecyclerView.setAdapter(mAdapter);
            return root;
        }

        @Override
        public void onResume() {
            super.onResume();
            if (mAdapter != null){
                mAdapter.onResume(getActivity());
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            if (mAdapter != null) {
                mAdapter.onPause();
            }
        }
    }
}
