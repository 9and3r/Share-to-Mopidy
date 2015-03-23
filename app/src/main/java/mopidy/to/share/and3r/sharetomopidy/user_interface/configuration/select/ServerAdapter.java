package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.select;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;

/**
 * Created by ander on 2/4/15.
 */
public class ServerAdapter extends RecyclerView.Adapter<ServerHolder> {


    private String newServerText;

    public ServerAdapter(Context c){
        super();
        newServerText = c.getString(R.string.add_server);
        setHasStableIds(true);
    }

    @Override
    public ServerHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.server_card_view, viewGroup, false);
        ServerHolder vh = new ServerHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ServerHolder serverHolder, int i) {
        if (i == getItemCount()-1){
            serverHolder.setAddCardMode(newServerText);
        }else{
            serverHolder.setConfig(i);
        }
    }

    @Override
    public long getItemId(int position) {
        if (position == getItemCount()-1){
            return 0;
        }else{
            return MopidyServerConfigManager.get().getConfig(null, position).hashCode();
        }
    }

    @Override
    public int getItemCount() {
        return MopidyServerConfigManager.get().numberOfServers() + 1;
    }
}
