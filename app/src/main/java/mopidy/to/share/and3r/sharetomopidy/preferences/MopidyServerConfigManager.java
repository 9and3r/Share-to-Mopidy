package mopidy.to.share.and3r.sharetomopidy.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Observable;

import mopidy.to.share.and3r.sharetomopidy.R;


public class MopidyServerConfigManager extends Observable{

    private static MopidyServerConfigManager myManager;


    private ArrayList<MopidyServerConfig> configs;
    private int currentID;

    private static final String MY_SERVERS = "SERVERS_CONFIG";
    private static final String SERVER= "SERVER_";
    private static final String SERVER_COUNT = "SERVER_COUNT";
    private static final String CURRENT_SERVER = "CURRENT_SERVER";


    private MopidyServerConfigManager(){}

    public static synchronized MopidyServerConfigManager get(){
        if (myManager == null){
            myManager = new MopidyServerConfigManager();
        }
        return myManager;
    }

    public void init(Context c){
        SharedPreferences p = c.getSharedPreferences(MY_SERVERS, Context.MODE_PRIVATE);
        int size = p.getInt(SERVER_COUNT, 0);
        MopidyServerConfig config;
        configs = new ArrayList<>();
        for (int i=0; i<size; i++){
            config = new MopidyServerConfig(p.getString(SERVER+String.valueOf(i), "{}"));
            configs.add(i , config);
        }
        currentID = p.getInt(CURRENT_SERVER, 0);
    }

    public MopidyServerConfig getConfig(Context c, int pID, boolean setCurrent){
        if (setCurrent){
            setCurrentID(c, pID);
        }
        return configs.get(pID);
    }

    public MopidyServerConfig getCurrentServer(Context c){
        if (configs.size() == 0){
            createNewMopidyServer(c);
            return configs.get(0);
        }else{
            if (currentID < configs.size()){
                return configs.get(currentID);
            }else{
                return configs.get(0);
            }
        }
    }

    public void saveMopidyServer(Context c, MopidyServerConfig config){
        SharedPreferences p = c.getSharedPreferences(MY_SERVERS, Context.MODE_PRIVATE);
        config.setId(configs.indexOf(config));
        p.edit().putString(SERVER+String.valueOf(config.getId()), config.toString()).putInt(SERVER_COUNT, configs.size()).commit();
        setChanged();
        notifyObservers();
    }

    public int createNewMopidyServer(Context c){
        MopidyServerConfig conf = new MopidyServerConfig(configs.size(), c.getString(R.string.defaultName), c.getString(R.string.defaultIP), Integer.valueOf(c.getString(R.string.defaultPort)));
        configs.add(configs.size(), conf);
        saveMopidyServer(c, conf);
        return conf.getId();
    }

    public void removeServer(Context c, int id){
        configs.remove(id);
        for (int i=0; i<configs.size(); i++){
            saveMopidyServer(c, configs.get(i));
        }
        setChanged();
        notifyObservers();
    }

    public int numberOfServers(){
        return configs.size();
    }

    private void setCurrentID(Context c, int currentID) {
        this.currentID = currentID;
        SharedPreferences p = c.getSharedPreferences(MY_SERVERS, Context.MODE_PRIVATE);
        p.edit().putInt(CURRENT_SERVER, currentID).commit();
    }
}
