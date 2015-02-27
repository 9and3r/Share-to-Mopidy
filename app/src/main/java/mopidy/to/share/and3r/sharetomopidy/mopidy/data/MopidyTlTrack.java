package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

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
            return ((MopidyTlTrack)o).tl_track.equals(tl_track);
        }else if (o instanceof JSONObject){
            return ((JSONObject)o).toString().equals(tl_track);
        }else{
            return false;
        }
    }

    public int getId(){
        return tlid;
    }
}
