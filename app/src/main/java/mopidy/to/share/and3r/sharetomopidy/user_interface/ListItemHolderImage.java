package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyDataWithImage;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.OnImageAndPalleteReady;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.TaskImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;

/**
 * Created by ander on 2/26/15.
 */
public class ListItemHolderImage extends BaseListItem implements OnImageAndPalleteReady{

    private ImageView albumArt;
    private TextView text2;

    private TaskImage task;

    public ListItemHolderImage(View itemView, BaseListAdapter adapter) {
        super(itemView, adapter);
        albumArt = (ImageView) itemView.findViewById(R.id.imageView);
        text2 = (TextView) itemView.findViewById(R.id.textViewDown);
    }

    @Override
    public void setMopidyData(MopidyData pData, int i) {
        super.setMopidyData(pData, i);
        text2.setText(pData.getSubTitle());
        if (task != null){
            task.cancel(true);
            task = null;
        }
        albumArt.setImageResource(R.drawable.no_album);
        albumArt.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            albumArt.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            task = new TaskImage(ListItemHolderImage.this, (MopidyDataWithImage) data, albumArt.getWidth(), albumArt.getHeight());
            task.execute(albumArt.getContext());
        }
    };

    public void recycle(){
        if (task != null){
            task.cancel(true);
            task = null;
        }
        albumArt.setImageResource(R.drawable.no_album);
    }

    @Override
    public void onImageAndPalleteReady(Bitmap bitmap, Palette palette) {
        albumArt.setImageBitmap(bitmap);
    }



}
