package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.support.v7.graphics.Palette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class MopidyAlbum implements Serializable{

    private String albumName;
    private MopidyArtist[] artists;
    private transient Palette palette;

    public MopidyAlbum(JSONObject object){
        try{
            albumName = object.getString("name");
            JSONArray artistsJSON = object.getJSONArray("artists");
            artists = new MopidyArtist[artistsJSON.length()];
            for (int i=0; i<artistsJSON.length(); i++){
                artists[i] = new MopidyArtist(artistsJSON.getJSONObject(i));
            }
        }catch (JSONException e){
            artists = new MopidyArtist[0];
            albumName = "";
        }
    }

    public String getAlbumName() {
        return albumName;
    }

    public MopidyArtist[] getArtists() {
        return artists;
    }

    public String getArtistsString(){
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

    public String getAlbumDownloadName(){
        return getAlbumName() + "#" + getArtistsString();
    }


    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public Palette.Swatch getSwatch(){
        Palette.Swatch s;
        s = palette.getVibrantSwatch();
        if (s != null){
            return s;
        }else{
            return palette.getMutedSwatch();
        }
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
