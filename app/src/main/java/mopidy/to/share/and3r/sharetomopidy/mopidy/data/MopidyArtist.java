package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONException;
import org.json.JSONObject;




public class MopidyArtist extends MopidyData{

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
