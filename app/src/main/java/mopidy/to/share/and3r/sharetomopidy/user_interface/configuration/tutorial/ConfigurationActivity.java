package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import mopidy.to.share.and3r.sharetomopidy.user_interface.activity.NotConnectedActivity;
import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.R;

public class ConfigurationActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_layout);
        id = getIntent().getIntExtra("id", 0);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(this);

        // Stop Service if allready running
        Intent intent = new Intent(this, MopidyService.class);
        intent.setAction(MopidyService.ACTION_STOP_SERVICE);
        //startService(intent);

    }

    public void finishButton(View v){
        Intent intent = new Intent(this, NotConnectedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void goToFirstPage(View v){
        mPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void nextPage(View v){
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == NUM_PAGES-1){
            ((ConfigurationTest)mPagerAdapter.getItem(position)).testConnection(null);
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private Fragment[] fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            if (fragments == null){
                fragments = new Fragment[NUM_PAGES];
                fragments[0] = new ConfigurationWelcome();
                fragments[1] = new ConfigurationName();
                fragments[2] = new ConfigurationMopidy();
                fragments[3] = new ConfigurationIP();
                fragments[4] = new ConfigurationTest();
            }
            return fragments[position];
            /*
            switch (position){
                case 0:
                    return new ConfigurationWelcome();
                case 1:
                    return new ConfigurationMopidy();
                case 2:
                    return new ConfigurationIP();
                case 3:
                    return new ConfigurationTest();
            }
            return null;
            */

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.configuration_pages)[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }
}
