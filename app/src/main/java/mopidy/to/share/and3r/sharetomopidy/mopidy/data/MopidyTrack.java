package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class MopidyTrack {


    private String tl_track;
    private String trackString;
    private MopidyAlbum album;
    private int length;



    public MopidyTrack(JSONObject pObject){
        tl_track = pObject.toString();
        try {
            trackString = pObject.getJSONObject("track").getString("name");
        } catch (JSONException e) {
            trackString = "";
        }
        try {
            album = new MopidyAlbum(pObject.getJSONObject("track").getJSONObject("album"));
        } catch (JSONException e) {
            album = new MopidyAlbum(new JSONObject());
        }
        try{
            length = pObject.getJSONObject("track").getInt("length");
        }catch (JSONException e){
            length = 0;
        }
    }

    public String getTl_track() {
        return tl_track;
    }

    public MopidyAlbum getAlbum() {
        return album;
    }

    public String getTrackString() {
        return trackString;
    }

    public String getAlbumString() {
        return album.getAlbumName();
    }

    public String getArtistsString() {
        return album.getArtistsString();
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  MopidyTrack){
            return ((MopidyTrack)o).tl_track.equals(tl_track);
        }else if (o instanceof JSONObject){
            return ((JSONObject)o).toString().equals(tl_track);
        }else{
            return false;
        }
    }
}
