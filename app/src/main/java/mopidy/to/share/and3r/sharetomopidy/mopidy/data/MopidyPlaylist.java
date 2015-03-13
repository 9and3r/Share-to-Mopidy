package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MopidyPlaylist extends MopidyData implements Serializable{

    private String name;
    private MopidyTrack[] tracks;

    public MopidyPlaylist(JSONObject o) {
        super(o);
        try {
            name = o.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray array = o.getJSONArray("tracks");
            tracks = new MopidyTrack[array.length()];
            for (int i=0; i<array.length(); i++){
                tracks[i] = new MopidyTrack(array.getJSONObject(i));
            }
        } catch (JSONException e) {
            tracks = new MopidyTrack[0];
            //e.printStackTrace();
        }
    }

    @Override
    public String getTitle() {
        return name;
    }
}
