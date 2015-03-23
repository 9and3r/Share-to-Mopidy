package mopidy.to.share.and3r.sharetomopidy.user_interface.activity;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Observable;
import java.util.Observer;


import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfig;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;


public class NotConnectedActivity extends ActionBarActivity implements Observer {

    private View loadingLayout;
    private View selectLayout;
    private NsdManager mNsdManager;
    private NsdManager.ResolveListener mResolveListener;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_connected);
        loadingLayout = findViewById(R.id.loading_layout);
        selectLayout = findViewById(R.id.select_layout);
        initializeDiscoveryListener();
        initializeResolveListener();
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
    }

    private void onConnectionStateChange(){
        int status = MopidyStatus.get().getConnectionStatus();
        switch (status){
            case MopidyStatus.NOT_CONNECTED:
                loadingLayout.setVisibility(View.GONE);
                selectLayout.setVisibility(View.VISIBLE);
                break;
            case MopidyStatus.CONNECTING:
                loadingLayout.setVisibility(View.VISIBLE);
                selectLayout.setVisibility(View.GONE);
                break;
            case MopidyStatus.CONNECTED:
                Intent i = new Intent(this, ConnectedActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MopidyServerConfigManager.get().resetNotSaved();
        onConnectionStateChange();
        MopidyStatus.get().addObserver(this);
        mNsdManager.discoverServices("_mopidy-http._tcp", NsdManager.PROTOCOL_DNS_SD ,mDiscoveryListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MopidyStatus.get().deleteObserver(this);
        if (mNsdManager != null && mDiscoveryListener != null){
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
    }





    @Override
    public void update(Observable observable, Object data) {

        if (((int) data) == MopidyStatus.CONNECTION_STATE_CHANGED){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onConnectionStateChange();
                }
            });
        }

    }



    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d("Proba", "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                mNsdManager.resolveService(service, mResolveListener);
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {

            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {


            }

            @Override
            public void onServiceResolved(final NsdServiceInfo serviceInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MopidyServerConfigManager.get().foundServer(serviceInfo.getServiceName(), serviceInfo.getHost().getHostAddress(), serviceInfo.getPort());

                    }
                });


            }
        };
    }


}
