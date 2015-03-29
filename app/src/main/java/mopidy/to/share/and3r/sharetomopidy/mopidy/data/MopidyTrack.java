package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class MopidyTrack extends MopidyDataWithImage implements Serializable{

    private MopidyArtist[] artists;
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
        try{
            JSONArray artistsJSON = pObject.getJSONArray("artists");
            artists = new MopidyArtist[artistsJSON.length()];
            for (int i=0; i<artistsJSON.length(); i++){
                artists[i] = new MopidyArtist(artistsJSON.getJSONObject(i));
            }
        }catch (JSONException e){
            artists = new MopidyArtist[0];
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
    public String[] getCheckUrls() {
        return album.getCheckUrls();
    }

    @Override
    public String getImageUrl(JSONObject object) {
        return album.getImageUrl(object);
    }

    @Override
    public String getImageDownloadFolder() {
        return album.getImageDownloadFolder();
    }

    @Override
    public String getImageDonwloadName() {
        return album.getImageDonwloadName();
    }

    @Override
    public String getTitle() {
        return getTrackString();
    }

    public String getTrackString() {
        return trackString;
    }

    public String getAlbumString() {
        return album.getAlbumName();
    }

    public String getArtistsString() {
        if (album.getArtists().length>0){
            return MopidyData.getArtistsString(album.getArtists());
        }else{
            return MopidyData.getArtistsString(artists);
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    public String getSubTitle() {
        return getArtistsString();
    }

    public MopidyAlbum getAlbum() {
        return album;
    }
}
