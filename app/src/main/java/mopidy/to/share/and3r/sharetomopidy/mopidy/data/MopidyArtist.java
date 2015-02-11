package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONException;
import org.json.JSONObject;




public class MopidyArtist {

    private String artistName;

    public MopidyArtist(JSONObject object){
        try{
            artistName = object.getString("name");
        }catch (JSONException e){
            artistName = "";
        }
    }

    public String getArtistName() {
        return artistName;
    }
}
