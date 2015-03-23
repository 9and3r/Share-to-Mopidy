package mopidy.to.share.and3r.sharetomopidy.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Observable;

import mopidy.to.share.and3r.sharetomopidy.R;


public class MopidyServerConfigManager extends Observable{

    private static MopidyServerConfigManager myManager;


    private ArrayList<MopidyServerConfig> configs;
    private ArrayList<MopidyServerConfig> configsNoSave;
    private MopidyServerConfig currentServer;

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
        configsNoSave = new ArrayList<>();
        for (int i=0; i<size; i++){
            config = new MopidyServerConfig(p.getString(SERVER+String.valueOf(i), "{}"));
            configs.add(i , config);
        }
        int currentServerPos = p.getInt(CURRENT_SERVER, 0);
        if (currentServerPos < configs.size()){
            currentServer = configs.get(currentServerPos);
        }
    }

    public void setCurrentServer(Context c, MopidyServerConfig currentServer) {
        this.currentServer = currentServer;
        if (currentServer.getId() > -1){
            setCurrentID(c, currentServer.getId());
        }
    }

    public MopidyServerConfig getConfig(Context c, int pID){
        if (pID < configs.size()){
            return configs.get(pID);
        }else{
            return configsNoSave.get(pID-configs.size());
        }
    }

    public MopidyServerConfig getCurrentServer(Context c){
        return currentServer;
    }

    public void saveMopidyServer(Context c, MopidyServerConfig config){
        SharedPreferences p = c.getSharedPreferences(MY_SERVERS, Context.MODE_PRIVATE);
        config.setId(configs.indexOf(config));
        p.edit().putString(SERVER+String.valueOf(config.getId()), config.toString())
                .commit();
        saveServerCount(c);
        setChanged();
        notifyObservers();
    }

    private void saveServerCount(Context c){
        SharedPreferences p = c.getSharedPreferences(MY_SERVERS, Context.MODE_PRIVATE);
        p.edit().putInt(SERVER_COUNT, configs.size()).commit();
    }

    public void saveServer(Context c, MopidyServerConfig config){
        configsNoSave.remove(config);
        configs.add(config);
        saveMopidyServer(c, config);
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
        saveServerCount(c);
        setChanged();
        notifyObservers();
    }

    public int numberOfServers(){
        return configs.size() + configsNoSave.size();
    }

    private void setCurrentID(Context c, int currentID) {
        if (currentID < configs.size()){
            currentServer  = configs.get(currentID);
            SharedPreferences p = c.getSharedPreferences(MY_SERVERS, Context.MODE_PRIVATE);
            p.edit().putInt(CURRENT_SERVER, currentID).commit();
        }
    }

    public void foundServer(String name, String host, int port){
        MopidyServerConfig config = new MopidyServerConfig(-1, name, host, port);
        if (configs.indexOf(config) != -1){
            configs.get(configs.indexOf(config)).setAvailable(true);
        }else if (configsNoSave.indexOf(config) == -1){
            config.setAvailable(true);
            configsNoSave.add(config);
        }
        setChanged();
        notifyObservers();
    }

    public void resetNotSaved(){
        configsNoSave.clear();
        for (int i=0; i<configs.size(); i++){
            configs.get(i).setAvailable(false);
        }
    }
}
