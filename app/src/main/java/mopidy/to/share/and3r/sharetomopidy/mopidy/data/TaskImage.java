package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import mopidy.to.share.and3r.sharetomopidy.preferences.PreferencesManager;


public class TaskImage extends AsyncTask<Context,Void,Bitmap> {

    private static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=59a04c6a73fb99d6e8996e01db306829&artist=%s&album=%s&format=json";

    private MopidyAlbum album;
    private OnImageAndPalleteReady callback;
    private Bitmap bitmap;
    private Palette palette;


    public TaskImage(OnImageAndPalleteReady pCallback, MopidyAlbum pAlbum){
        callback = pCallback;
        album = pAlbum;
    }


    @Override
    protected Bitmap doInBackground(Context... params) {
        MopidyArtist[] artists = album.getArtists();
        boolean found = false;
        int i = 0;
        String url = PreferencesManager.get().getImageUrl(params[0], album.getAlbumDownloadName());
        if (url == null){
            while(!found && i<artists.length && !isCancelled()){
                url = getDownloadURLWithArtist(artists[i]);
                if (url != null){
                    found = true;
                    PreferencesManager.get().saveImageUrl(params[0], album.getAlbumDownloadName(), url);
                }
            }
        }
        if (url!=null && !isCancelled()){
            bitmap = ImageLoader.getInstance().loadImageSync(url);
            if (bitmap != null){
                palette = Palette.generate(bitmap);
                album.setPalette(palette);
            }
            return bitmap;
        }else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (!isCancelled()){
            callback.onImageAndPalleteReady(bitmap, palette);
        }
    }

    private String getDownloadURLWithArtist(MopidyArtist pArtist){
        try {
            String safeAlbum = URLEncoder.encode(album.getAlbumName(), "utf-8");
            String safeArtist = URLEncoder.encode(pArtist.getArtistName(), "utf-8");
            JSONObject object = getJSONFromURL(String.format(BASE_URL, safeArtist, safeAlbum));
            JSONArray array = object.getJSONObject("album").getJSONArray("image");
            return array.getJSONObject(array.length()-1).getString("#text");
        } catch (UnsupportedEncodingException e) {
            return null;
        }catch (JSONException e){
            return null;
        }
    }

    private JSONObject getJSONFromURL(String pUrl){
        try {
            URL url = new URL(pUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String fullText = "";
            String line;
            while ( (line = br.readLine()) != null){
                fullText = fullText + line;
            }
            br.close();
            is.close();
            return new JSONObject(fullText);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
