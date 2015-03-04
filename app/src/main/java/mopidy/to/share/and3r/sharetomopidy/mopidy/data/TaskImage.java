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

    private MopidyDataWithImage data;
    private OnImageAndPaletteReady callback;
    private Bitmap bitmap;
    private Palette palette;
    private int width;
    private int height;


    public TaskImage(OnImageAndPaletteReady pCallback, MopidyDataWithImage pData,int pWidth,int pHeight){
        callback = pCallback;
        data = pData;
        width = pWidth;
        height = pHeight;
    }

    @Override
    protected Bitmap doInBackground(Context... params) {
        File f = new File(new File(params[0].getFilesDir(),"images"), data.getAlbum().getAlbumArtFileName());
        boolean correctlyDownloaded = true;
        if (!f.exists()){
            correctlyDownloaded = findUrlAndDownloadImageToStorage(params[0], f);
        }
        if (correctlyDownloaded && !isCancelled()){
            bitmap = decodeSampledBitmapFromResource(f, width, height);
            palette = data.getPalette();
            if (bitmap != null && palette == null){
                palette = Palette.generate(bitmap);
                data.setPalette(palette);
            }
            return bitmap;
        }else{
            return null;
        }
    }

    private boolean findUrlAndDownloadImageToStorage(Context c, File f){
        MopidyArtist[] artists = data.getAlbum().getArtists();
        boolean found = false;
        int i = 0;
        String url = PreferencesManager.get().getImageUrl(c, data.getAlbum().getAlbumDownloadName());
        if (url == null){
            while(!found && i<artists.length && !isCancelled()){
                url = getDownloadURLWithArtist(artists[i]);
                if (url != null){
                    found = true;
                    PreferencesManager.get().saveImageUrl(c, data.getAlbum().getAlbumDownloadName(), url);
                }else{
                    i++;
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
            //e.printStackTrace();
            Log.d("URL not valid", pUrl);
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
            callback.onImageAndPaletteReady(bitmap, palette);
        }
    }

    private String getDownloadURLWithArtist(MopidyArtist pArtist){
        try {
            String safeAlbum = URLEncoder.encode(data.getAlbum().getAlbumName(), "utf-8");
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

    private static Bitmap decodeSampledBitmapFromResource(File f,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(f.getAbsolutePath(), options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
