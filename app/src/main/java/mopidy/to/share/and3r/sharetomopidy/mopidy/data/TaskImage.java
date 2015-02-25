package mopidy.to.share.and3r.sharetomopidy.mopidy.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
        File f = new File(new File(params[0].getFilesDir(),"images"), album.getAlbumArtFileName());
        Log.d("Proba", f.getAbsolutePath());
        boolean correctlyDownloaded = true;
        if (!f.exists()){
            correctlyDownloaded = findUrlAnddownloadImageToStorage(params[0], f);
        }
        if (correctlyDownloaded && !isCancelled()){
            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            palette = album.getPalette();
            if (bitmap != null && palette == null){
                palette = Palette.generate(bitmap);
                album.setPalette(palette);
            }
            return bitmap;
        }else{
            return null;
        }
    }

    private boolean findUrlAnddownloadImageToStorage(Context c, File f){
        MopidyArtist[] artists = album.getArtists();
        boolean found = false;
        int i = 0;
        String url = PreferencesManager.get().getImageUrl(c, album.getAlbumDownloadName());
        if (url == null){
            while(!found && i<artists.length && !isCancelled()){
                url = getDownloadURLWithArtist(artists[i]);
                if (url != null){
                    found = true;
                    PreferencesManager.get().saveImageUrl(c, album.getAlbumDownloadName(), url);
                }
            }
        }else{
            found = true;
        }
        if (found && !isCancelled()){
            return downloadToStorage(url, f);
        }else{
            return false;
        }
    }

    private boolean downloadToStorage(String pUrl, File f){
        boolean correctlyDonwloaded = false;
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(pUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                cancel(true);
            }

            // download the file
            input = connection.getInputStream();
            f.getParentFile().mkdirs();
            output = new FileOutputStream(f.getAbsolutePath());

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1 && !isCancelled()) {
                output.write(data, 0, count);
            }
            correctlyDonwloaded = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return correctlyDonwloaded;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (!isCancelled() && bitmap != null){
            callback.onImageAndPalleteReady(bitmap, palette);
        }
    }

    private String getDownloadURLWithArtist(MopidyArtist pArtist){
        try {
            Log.d("Proba", "Billatzen artista");
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
            Log.d("Proba url",pUrl);
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
            Log.d("Proba", fullText);
            return new JSONObject(fullText);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
