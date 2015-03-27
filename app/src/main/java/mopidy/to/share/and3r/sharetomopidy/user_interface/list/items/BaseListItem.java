package mopidy.to.share.and3r.sharetomopidy.user_interface.list.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;

/**
 * Created by ander on 2/26/15.
 */
public abstract class BaseListItem extends BaseHolder {

    public MopidyData data;
    public int pos;
    public TextView text1;
    public ImageView button;
    public int group;
    public View mainItemView;

    public BaseListItem(View itemView, final OnBaseListItemClickListener listener) {
        super(itemView);

        mainItemView = itemView.findViewById(R.id.list_item_main_view);
        text1 = (TextView) itemView.findViewById(R.id.textViewUp);
        button = (ImageView) itemView.findViewById(R.id.moreButton);

        mainItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(BaseListItem.this, v, pos);
            }
        });
        mainItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(BaseListItem.this, v, pos);
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLongClick(BaseListItem.this, v, pos);
            }
        });
    }


    public void setMopidyData(MopidyData pData, int i, int pGroup){
        data = pData;
        pos = i;
        group = pGroup;
        text1.setText(pData.getTitle());
    }


    public void recycle() {

    }


}
