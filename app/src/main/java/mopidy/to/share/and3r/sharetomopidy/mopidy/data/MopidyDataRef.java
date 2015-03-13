package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MopidyDataRef extends MopidyData implements Serializable{

    public static final String TYPE_TRACK = "track";
    public static final String TYPE_DIRECTORY = "directory";

    private String name;
    private String type;



    public MopidyDataRef(JSONObject o) {
        super(o);
        try{
            name = o.getString("name");
        }catch (JSONException e){

        }
        try{
            type = o.getString("type");
        }catch (JSONException e){

        }
    }

    @Override
    public String getTitle() {
        return name;
    }

    public String getType() {
        return type;
    }
}
