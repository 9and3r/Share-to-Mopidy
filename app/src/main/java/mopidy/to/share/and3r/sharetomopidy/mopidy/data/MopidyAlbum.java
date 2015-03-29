package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.support.v7.graphics.Palette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MopidyAlbum extends MopidyDataWithImage implements Serializable{

    private static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=59a04c6a73fb99d6e8996e01db306829&artist=%s&album=%s&format=json";
    private static final String IMAGE_DOWNLOAD_FOLER = "album";


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
    public String[] getCheckUrls() {
        String[] urls = new String[artists.length];
        String safeAlbum;
        String safeArtist;
        for(int i=0; i< artists.length; i++){
            try {
                safeAlbum = URLEncoder.encode(albumName, "utf-8");
                safeArtist = URLEncoder.encode(artists[i].getArtistName(), "utf-8");
                urls[i] = String.format(BASE_URL, safeArtist, safeAlbum);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return urls;
    }

    @Override
    public String getImageUrl(JSONObject object) {
        try {
            JSONArray array = object.getJSONObject("album").getJSONArray("image");
            return array.getJSONObject(array.length()-1).getString("#text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getImageDownloadFolder() {
        return IMAGE_DOWNLOAD_FOLER;
    }

    @Override
    public String getImageDonwloadName() {
        return albumName;
    }

    @Override
    public String getTitle() {
        return albumName;
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
