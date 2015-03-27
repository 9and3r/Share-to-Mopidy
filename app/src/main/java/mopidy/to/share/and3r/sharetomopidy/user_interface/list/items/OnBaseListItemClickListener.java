package mopidy.to.share.and3r.sharetomopidy.user_interface.list.items;

import android.view.View;

public interface OnBaseListItemClickListener {

    public void onClick(BaseHolder holder, View v, int item);

    public void onLongClick(BaseListItem holder, View v, int item);

}
