package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;


public interface OnImageAndPaletteReady {

    public void onImageAndPaletteReady(Bitmap bitmap, Palette palette, MopidyDataWithImage dataImage);
}
