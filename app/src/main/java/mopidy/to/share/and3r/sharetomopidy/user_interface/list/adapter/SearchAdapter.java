package mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter;

import android.app.LauncherActivity;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemAdapter;

import java.util.ArrayList;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyAlbum;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyArtist;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTrack;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseHolder;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.BaseListItem;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.ListItemHolderImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.ListItemHolderNoImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.items.OnBaseListItemClickListener;

public class SearchAdapter extends BaseListAdapter implements ExpandableItemAdapter<BaseHolder,ListItemHolderImage>{

    public ArrayList<MopidyData>[] data;

    public SearchAdapter(OnBaseListItemClickListener listener) {
        super(listener);
        setHasStableIds(true);
        data = new ArrayList[3];
        data[0] = new ArrayList<>();
        data[1] = new ArrayList<>();
        data[2] = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return data.length;
    }

    @Override
    public int getChildCount(int i) {
        return data[i].size();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return data[i].hashCode();
    }

    @Override
    public int getGroupItemViewType(int i) {
        return BaseListAdapter.VIEW_TYPE_NO_IMAGE;
    }

    @Override
    public int getChildItemViewType(int i, int i2) {
        return BaseListAdapter.VIEW_TYPE_IMAGE;
    }

    @Override
    public BaseHolder onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
        return new BaseHolder(v);
    }

    @Override
    public ListItemHolderImage onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        return (ListItemHolderImage) super.onCreateViewHolder(viewGroup, i);
    }

    @Override
    public void onBindGroupViewHolder(BaseHolder viewHolder, int i, int viewType) {
        String type = "";
        switch (i){
            case 0:
                type = "Tracks";
                break;
            case 1:
                type = "Albums";
                break;
            case 2:
                type = "Artists";
                break;
        }
        type = type + " (" + String.valueOf(data[i].size()) +")";
        ((TextView)viewHolder.itemView.findViewById(android.R.id.text1)).setText(type);
    }



    @Override
    public void onBindChildViewHolder(ListItemHolderImage viewHolder, int group, int i, int type) {
        viewHolder.setMopidyData(data[group].get(i), i, group);
    }


    @Override
    public boolean onCheckCanExpandOrCollapseGroup(BaseHolder viewHolder, int i, int i2, int i3, boolean b) {
        return true;
    }

    public void setData(ArrayList<MopidyData> tracks, ArrayList<MopidyData> albums, ArrayList<MopidyData> artists){
        data[0] = tracks;
        data[1] = albums;
        data[2] = artists;
        notifyDataSetChanged();
    }

}
