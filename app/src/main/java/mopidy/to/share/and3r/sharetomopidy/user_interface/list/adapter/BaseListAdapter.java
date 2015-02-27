package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.BaseListItem;
import mopidy.to.share.and3r.sharetomopidy.user_interface.ListItemHolderImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.ListItemHolderNoImage;


public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseListItem>{

    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int VIEW_TYPE_NO_IMAGE = 2;

    protected ArrayList<String> currentDir;
    protected MopidyData[] list;

    protected Activity activity;

    @Override
    public BaseListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        BaseListItem item = null;
        switch (viewType){
            case VIEW_TYPE_IMAGE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_with_image, parent, false);
                item = new ListItemHolderImage(v, this);
                break;
            case VIEW_TYPE_NO_IMAGE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_no_image, parent, false);
                item = new ListItemHolderNoImage(v, this);
        }
        return item;
    }

    @Override
    public void onBindViewHolder(BaseListItem holder, int position) {
        holder.setMopidyData(list[position], position);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    @Override
    public int getItemViewType(int position) {
        MopidyData data = list[position];
        if(data instanceof MopidyTrack){
            return VIEW_TYPE_IMAGE;
        }else{
            return VIEW_TYPE_NO_IMAGE;
        }
    }

    public abstract void onClick(View v, int item);

    public void onDataChanged(){
        notifyDataSetChanged();
    }

    public void onResume(Activity pActivity){
        activity = pActivity;
    }

    public void onPause(){
        activity = null;
    }
}
