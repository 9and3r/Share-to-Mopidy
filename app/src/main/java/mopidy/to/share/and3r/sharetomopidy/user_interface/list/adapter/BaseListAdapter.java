package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.LinkedList;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.ListItemHolderImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.ListItemHolderNoImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.MopidyDataOptionsDialog;


public class BaseListAdapter extends RecyclerView.Adapter<BaseHolder>{

    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int VIEW_TYPE_NO_IMAGE = 2;
    private static final int VIEW_TYPE_LOADING = 3;

    public static final int LOADING = -1;
    public static final int LOADED = 1;


    protected LinkedList<MopidyData> currentPath;
    protected MopidyData[] list;
    private int status;
    protected Handler h;

    protected ActionBarActivity activity;

    public BaseListAdapter(ActionBarActivity pActivity){
        super();
        status = LOADED;
        list = new MopidyData[0];
        currentPath = new LinkedList<>();
        h = new Handler(Looper.getMainLooper());
        activity = pActivity;
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
        }else if (status == LOADING){
            return 1;
        }else{
            return 0;
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
        Toast toast = Toast.makeText(v.getContext(), v.getContext().getString(R.string.added_to_tracklist), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onLongClick(View v, int item){
        MopidyDataOptionsDialog dialog = new MopidyDataOptionsDialog();
        Bundle b = new Bundle();
        b.putSerializable(MopidyDataOptionsDialog.MOPIDY_DATA, list[item]);
        dialog.setArguments(b);
        dialog.show(activity.getSupportFragmentManager(), "DIALOG_OPTIONS");
    }

    public void setStatus(int pStatus){
        status = pStatus;
        notifyDataSetChanged();
    }

    public void setList(MopidyData[] newList){
        list = newList;
        setStatus(LOADED);
    }

    public void onDataChanged(){
        notifyDataSetChanged();
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
