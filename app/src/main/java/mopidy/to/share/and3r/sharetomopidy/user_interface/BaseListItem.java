package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;

/**
 * Created by ander on 2/26/15.
 */
public abstract class BaseListItem extends BaseHolder{

    protected MopidyData data;
    protected int pos;
    protected TextView text1;
    protected ImageView button;

    public BaseListItem(View itemView, final BaseListAdapter adapter) {
        super(itemView);

        text1 = (TextView) itemView.findViewById(R.id.textViewUp);
        button = (ImageView) itemView.findViewById(R.id.moreButton);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.onClick(v, pos);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                adapter.onLongClick(v, pos);
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.onLongClick(v, pos);
            }
        });
    }


    @Override
    public void setMopidyData(MopidyData pData, int i){
        data = pData;
        pos = i;
        text1.setText(pData.getTitle());
    }
}
