package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class MopidyArtist extends MopidyData implements Serializable{

    private String artistName;

    public MopidyArtist(JSONObject object){
        super(object);
        try{
            artistName = object.getString("name");
        }catch (JSONException e){
            artistName = "";
        }
    }

    @Override
    public String getTitle() {
        return artistName;
    }

    public String getArtistName() {
        return artistName;
    }
}
