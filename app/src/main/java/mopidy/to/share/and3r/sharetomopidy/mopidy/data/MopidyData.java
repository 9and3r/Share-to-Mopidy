package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ander on 2/26/15.
 */
public abstract class MopidyData {

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
}
