package mopidy.to.share.and3r.sharetomopidy.mopidy.data;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class used to create a json rpc request with defaul id set to 1
 */
public class DefaultJSON extends JSONObject {

    public DefaultJSON(){
        try {
            put("jsonrpc","2.0");
            put("id", 1);
            put("params", new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMethod(String pMethod){
        try {
            put("method",pMethod);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addParam(String pKey, String pValue){
        try {
            getJSONObject("params").put(pKey, pValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
