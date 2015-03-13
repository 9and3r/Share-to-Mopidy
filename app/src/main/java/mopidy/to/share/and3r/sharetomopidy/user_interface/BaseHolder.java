package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;

public class BaseHolder extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public void recycle(){

    }

    public void setMopidyData(MopidyData pData, int i){
    }
}
