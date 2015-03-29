package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.support.v7.graphics.Palette;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ander on 2/26/15.
 */
public abstract class MopidyDataWithImage extends MopidyData implements Serializable{


    private transient Palette palette;

    public MopidyDataWithImage(JSONObject o) {
        super(o);
    }

    public abstract String[] getCheckUrls();

    public abstract String getImageUrl(JSONObject object);

    public abstract String getImageDownloadFolder();

    public abstract String getImageDonwloadName();

    public String getImageCompareString(){
        return getImageDownloadFolder() + "#" + getImageDonwloadName();
    }

    public boolean equalsImage(String compare){
        return (getImageCompareString().equals(compare));
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }
}
