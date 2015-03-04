package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.LinkedList;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.BaseListItem;
import mopidy.to.share.and3r.sharetomopidy.user_interface.ListItemHolderImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.ListItemHolderNoImage;


public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseHolder>{

    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int VIEW_TYPE_NO_IMAGE = 2;
    private static final int VIEW_TYPE_LOADING = 3;

    public static final int LOADING = -1;
    public static final int LOADED = 1;


    protected LinkedList<MopidyData> currentPath;
    protected MopidyData[] list;
    private int status;
    protected Handler h;

    protected Activity activity;

    public BaseListAdapter(){
        status = LOADED;
        list = new MopidyData[0];
        currentPath = new LinkedList<>();
        h = new Handler(Looper.getMainLooper());
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        BaseHolder item = null;
        switch (viewType){
            case VIEW_TYPE_IMAGE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_with_image, parent, false);
                item = new ListItemHolderImage(v, this);
                break;
            case VIEW_TYPE_NO_IMAGE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_no_image, parent, false);
                item = new ListItemHolderNoImage(v, this);
                break;
            case VIEW_TYPE_LOADING:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false);
                item = new BaseHolder(v);
                break;
        }
        return item;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (position<list.length){
            holder.setMopidyData(list[position], position);
        }
    }

    @Override
    public void onViewRecycled(BaseHolder holder) {
        super.onViewRecycled(holder);
        holder.recycle();
    }

    @Override
    public int getItemCount() {
        if (status == LOADED){
            return list.length;
        }else{
            return 1;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (status == LOADED){
            MopidyData data = list[position];
            if(data instanceof MopidyTrack){
                return VIEW_TYPE_IMAGE;
            }else{
                return VIEW_TYPE_NO_IMAGE;
            }
        }else{
            return VIEW_TYPE_LOADING;
        }
    }

    public void onClick(View v, int item){
        PlaybackControlManager.addToTracklist(v.getContext(), list[item], false);
    }

    public void setStatus(int pStatus){
        status = pStatus;
        notifyDataSetChanged();
    }



    public void onDataChanged(){
        notifyDataSetChanged();
    }

    public void onResume(Activity pActivity){
        activity = pActivity;
    }

    public void onPause(){
        activity = null;
    }

    public boolean onBackPressed(Context c){
        if (currentPath.size() == 0){
            return false;
        }else{
            currentPath.removeLast();
            loadPath(c);
            return true;
        }
    }

    public void loadPath(Context c){
    }
}
