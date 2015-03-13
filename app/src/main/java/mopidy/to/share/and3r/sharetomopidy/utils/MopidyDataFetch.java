package mopidy.to.share.and3r.sharetomopidy.utils;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.DefaultJSON;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyPlaylist;

public class MopidyDataFetch {

    private static MopidyDataFetch myData;


    private ArrayList<Request> requests;

    // Start from 20 to let some free for other uses.
    private int REQUEST_ID = 20;

    private MopidyDataFetch(){
        requests = new ArrayList<>();
    }

    public static MopidyDataFetch get(){
        if (myData == null){
            myData = new MopidyDataFetch();
        }
        return myData;
    }

    public void getPlaylist(Context c, OnRequestListener listener, MopidyPlaylist p){
        try{
            int id = addRequest(listener);
            DefaultJSON json = new DefaultJSON(id);
            json.setMethod("core.playlists.lookup");
            JSONObject params = new JSONObject();
            params.put("uri", p.getUri());
            json.put("params", params);
            Intent intent = new Intent(c, MopidyService.class);
            intent.setAction(MopidyService.ACTION_ONE_ACTION);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        }catch (JSONException e){

        }
    }

    public void browse(Context c, OnRequestListener listener, MopidyData data){
        try{
            int id = addRequest(listener);
            DefaultJSON json = new DefaultJSON(id);
            json.setMethod("core.library.browse");
            JSONObject params = new JSONObject();
            if (data != null){
                params.put("uri", data.getUri());
            }else{
                params.put("uri", JSONObject.NULL);
            }
            json.put("params", params);
            Intent intent = new Intent(c, MopidyService.class);
            intent.setAction(MopidyService.ACTION_ONE_ACTION);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        }catch (JSONException e){

        }
    }


    public void search(Context c, OnRequestListener listener, String query){
        try{
            int id = addRequest(listener);
            DefaultJSON json = new DefaultJSON(id);
            json.setMethod("core.library.search");
            JSONObject params = new JSONObject();
            JSONObject queryJson = new JSONObject();
            queryJson.put("any",query);
            params.put("query", queryJson);
            json.put("params", params);
            Intent intent = new Intent(c, MopidyService.class);
            intent.setAction(MopidyService.ACTION_ONE_ACTION);
            intent.putExtra(MopidyService.ONE_ACTION_DATA, json.toString());
            c.startService(intent);
        }catch (JSONException e){

        }
    }


    public void onResultReceived(JSONObject o){
        try {
            Request r = findRequest(o.getInt("id"));
            if (r != null){
                r.listener.OnRequestResult(o);
            }
            requests.remove(r);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Request findRequest(int pId){
        boolean found = false;
        int i=0;
        while(!found && i < requests.size()){
            if (requests.get(i).id == pId){
                found = true;
            }else{
                i++;
            }
        }
        if (found){
            return requests.get(i);
        }else{
            return null;
        }
    }

    private int addRequest(OnRequestListener pListener){
        Request r = new Request();
        r.id = REQUEST_ID;
        REQUEST_ID ++;
        r.listener = pListener;
        requests.add(r);
        return r.id;
    }

    private class Request{
        public int id;
        public OnRequestListener listener;
    }


}
