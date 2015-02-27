package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONException;
import org.json.JSONObject;


public class MopidyTrack extends MopidyDataWithImage {

    private String trackString;
    private MopidyAlbum album;
    private int length;

    public MopidyTrack(JSONObject pObject){
        super(pObject);
        try {
            trackString = pObject.getString("name");
        } catch (JSONException e) {
            try {
                trackString = pObject.getString("uri");
            } catch (JSONException e1) {
                trackString = "No track";
                e1.printStackTrace();
            }

        }
        try {
            album = new MopidyAlbum(pObject.getJSONObject("album"));
        } catch (JSONException e) {
            album = new MopidyAlbum(new JSONObject());
        }
        try{
            length = pObject.getInt("length");
        }catch (JSONException e){
            length = 0;
        }


    }

    @Override
    public String getTitle() {
        return getTrackString();
    }


    @Override
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
    public String getSubTitle() {
        return getArtistsString();
    }
}
