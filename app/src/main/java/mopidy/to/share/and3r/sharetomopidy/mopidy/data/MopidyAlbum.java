package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.support.v7.graphics.Palette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class MopidyAlbum extends MopidyDataWithImage implements Serializable{

    private String albumName;
    private MopidyArtist[] artists;




    public MopidyAlbum(JSONObject object){
        super(object);
        try{
            albumName = object.getString("name");

        }catch (JSONException e){
            albumName = "";
        }
        try{
            JSONArray artistsJSON = object.getJSONArray("artists");
            artists = new MopidyArtist[artistsJSON.length()];
            for (int i=0; i<artistsJSON.length(); i++){
                artists[i] = new MopidyArtist(artistsJSON.getJSONObject(i));
            }
        }catch (JSONException e){
            artists = new MopidyArtist[0];
        }

    }

    @Override
    public String getTitle() {
        return albumName;
    }

    @Override
    public MopidyAlbum getAlbum() {
        return this;
    }

    public String getAlbumName() {
        return albumName;
    }

    public MopidyArtist[] getArtists() {
        return artists;
    }



    public String getAlbumDownloadName(){
        return getAlbumName() + "#" + MopidyData.getArtistsString(artists);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MopidyAlbum){
            return getAlbumDownloadName().equals(((MopidyAlbum) o).getAlbumDownloadName());
        }else{
            return false;
        }
    }

    public String getAlbumArtFileName(){
        String fileName = albumName.replace("[^\\d]", "-");
        fileName = fileName.replace("\\W", "-");
        fileName = fileName.replace(" ", "_");
        fileName = fileName.replace("/", "-");
        return fileName;
    }


}
