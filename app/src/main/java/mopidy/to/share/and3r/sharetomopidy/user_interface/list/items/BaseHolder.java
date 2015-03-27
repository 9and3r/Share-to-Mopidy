package mopidy.to.share.and3r.sharetomopidy.user_interface.list.items;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;

public class BaseHolder extends AbstractDraggableItemViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public void setMopidyData(MopidyData pData, int i, int group){

    }

    public void recycle() {

    }


}

