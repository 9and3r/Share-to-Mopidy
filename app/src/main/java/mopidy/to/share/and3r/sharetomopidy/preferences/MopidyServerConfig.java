package mopidy.to.share.and3r.sharetomopidy.preferences;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;


public class MopidyServerConfig {



    private String name;

    private String ip;

    private int port;

    private int id;

    public int getId() {
        return id;
    }

    public MopidyServerConfig(int pID, String defaultName, String defaultIP, int defaultPort){
        id = pID;
        name = defaultName;
        ip = defaultIP;
        port = defaultPort;
    }

    public MopidyServerConfig(String pString){
        try {
            JSONObject object = new JSONObject(pString);
            id = object.getInt("id");
            name = object.getString("name");
            ip = object.getString("ip");
            port = object.getInt("port");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(Context c, String name) {
        this.name = name;
        MopidyServerConfigManager.get().saveMopidyServer(c, this);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(Context c, String ip) {
        this.ip = ip;
        MopidyServerConfigManager.get().saveMopidyServer(c, this);
    }

    public int getPort() {
        return port;
    }

    public void setPort(Context c, int port) {
        this.port = port;
        MopidyServerConfigManager.get().saveMopidyServer(c, this);
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        try {
            object.put("id",id);
            object.put("name",name);
            object.put("ip",ip);
            object.put("port",port);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}

