package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ander on 2/26/15.
 */
public abstract class MopidyData implements Serializable{

    private String uri;


    public MopidyData(JSONObject o){
        try {
            uri = o.getString("uri");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUri() {
        return uri;
    }

    public abstract String getTitle();

    public String getSubTitle(){
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MopidyData){
            return ((MopidyData) o).uri.equals(uri);
        }else{
            return false;
        }
    }

    public static String getArtistsString(MopidyArtist[] artists){
        String artistString = "";
        for (int i=0; i<artists.length; i++){
            artistString = artistString + artists[i].getArtistName() + ", ";
        }
        // Remove last ', '
        if (artistString.contains(", ")){
            artistString = artistString.substring(0, artistString.lastIndexOf(", "));
        }
        return artistString;
    }
}
