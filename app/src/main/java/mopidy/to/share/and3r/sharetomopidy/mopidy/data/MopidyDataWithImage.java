package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.support.v7.graphics.Palette;

import org.json.JSONObject;

/**
 * Created by ander on 2/26/15.
 */
public abstract class MopidyDataWithImage extends MopidyData {


    private Palette palette;

    public MopidyDataWithImage(JSONObject o) {
        super(o);
    }


    public abstract MopidyAlbum getAlbum();

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }
}
