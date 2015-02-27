package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONException;
import org.json.JSONObject;

public class MopidyPlaylist extends MopidyData {

    private String name;

    public MopidyPlaylist(JSONObject o) {
        super(o);
        try {
            name = o.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTitle() {
        return name;
    }
}
