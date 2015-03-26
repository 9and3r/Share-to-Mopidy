package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class MopidyTlTrack extends MopidyTrack implements Serializable{


    private String tl_track;


    private int tlid;



    public MopidyTlTrack(JSONObject pObject) throws JSONException{
        super(pObject.getJSONObject("track"));
        tl_track = pObject.toString();
        tlid = pObject.getInt("tlid");
    }

    public String getTl_track() {
        return tl_track;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MopidyTlTrack){
            return (((MopidyTlTrack) o).getUri().equals(this.getUri()) && tlid == ((MopidyTlTrack) o).tlid);
        }else{
            return super.equals(o);
        }
    }

    @Override
    public String toString() {
        return tl_track.toString();
    }

    public int getTlId(){
        return tlid;
    }


}
