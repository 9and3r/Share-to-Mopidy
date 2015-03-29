package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MopidyArtist extends MopidyDataWithImage implements Serializable{

    private static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&api_key=59a04c6a73fb99d6e8996e01db306829&artist=%s&format=json";
    private static final String IMAGE_DOWNLOAD_FOLER = "artist";



    private String artistName;

    public MopidyArtist(JSONObject object){
        super(object);
        try{
            artistName = object.getString("name");
        }catch (JSONException e){
            artistName = "";
        }
    }

    @Override
    public String[] getCheckUrls() {
        String[] url = new String[1];
        try {
            String safeArtist = URLEncoder.encode(artistName, "utf-8");
            url[0] = String.format(BASE_URL, safeArtist);
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String getImageUrl(JSONObject object) {
        try {
            JSONArray array = object.getJSONObject("artist").getJSONArray("image");
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
        return artistName;
    }

    @Override
    public String getTitle() {
        return artistName;
    }

    public String getArtistName() {
        return artistName;
    }
}
