package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyDataWithImage;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.OnImageAndPaletteReady;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.TaskImage;
import mopidy.to.share.and3r.sharetomopidy.user_interface.list.adapter.BaseListAdapter;

/**
 * Created by ander on 2/26/15.
 */
public class ListItemHolderImage extends BaseListItem implements OnImageAndPaletteReady {

    private ImageView albumArt;
    private TextView text2;

    private static int albumArtSize;

    private TaskImage task;

    public ListItemHolderImage(View itemView, BaseListAdapter adapter) {
        super(itemView, adapter);
        albumArt = (ImageView) itemView.findViewById(R.id.imageView);
        text2 = (TextView) itemView.findViewById(R.id.textViewDown);
        if (albumArtSize == 0){
            setAlbumArtSize();
        }
    }

    private void setAlbumArtSize(){
        Resources resources = itemView.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        albumArtSize = (int) (48 * (metrics.densityDpi / 160f));
    }

    @Override
    public void setMopidyData(MopidyData pData, int i) {
        super.setMopidyData(pData, i);
        text2.setText(pData.getSubTitle());
        albumArt.setImageResource(R.drawable.no_album);
        task = new TaskImage(ListItemHolderImage.this, (MopidyDataWithImage) data, albumArtSize, albumArtSize);
        task.execute(albumArt.getContext());

    }

    @Override
    public void recycle(){
        super.recycle();
        if (task != null){
            task.cancel(true);
            task = null;
        }
        albumArt.setImageResource(R.drawable.no_album);
    }

    @Override
    public void onImageAndPaletteReady(Bitmap bitmap, Palette palette) {
        albumArt.setImageBitmap(bitmap);
    }



}
