package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.select;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfig;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;
import mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.tutorial.ConfigurationActivity;


public class ServerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private CardView cardView;
    private TextView availableText;
    private TextView nameText;
    private TextView ipText;
    private TextView portText;
    private ImageView settingsButton;
    private ImageView deleteButton;

    private MopidyServerConfig config;

    public ServerHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        availableText = (TextView) itemView.findViewById(R.id.availableText);
        nameText = (TextView) itemView.findViewById(R.id.nameText);
        ipText = (TextView) itemView.findViewById(R.id.ipText);
        portText = (TextView) itemView.findViewById(R.id.portText);
        settingsButton = (ImageView) itemView.findViewById(R.id.settings_button);
        deleteButton = (ImageView) itemView.findViewById(R.id.deleteButton);
        settingsButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    public void setConfig(int pNumber){
        config = MopidyServerConfigManager.get().getConfig(nameText.getContext(), pNumber);
        nameText.setText(config.getName());
        ipText.setVisibility(View.VISIBLE);
        portText.setVisibility(View.VISIBLE);
        ipText.setText(config.getIp());
        portText.setText(String.valueOf(config.getPort()));
        if (config.getId() < 0){
            settingsButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
            settingsButton.setImageResource(android.R.drawable.ic_menu_save);
        }else{
            settingsButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            settingsButton.setImageResource(R.drawable.ic_action_settings);
        }
        if (config.isAvailable()){
            availableText.setVisibility(View.VISIBLE);
        }else{
            availableText.setVisibility(View.GONE);
        }

    }

    public void setAddCardMode(String text){
        config = null;
        nameText.setText(text);
        ipText.setVisibility(View.GONE);
        portText.setVisibility(View.GONE);
        settingsButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        availableText.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(config == null){
            Intent intent = new Intent(v.getContext(), ConfigurationActivity.class);
            int id = MopidyServerConfigManager.get().createNewMopidyServer(v.getContext());
            intent.putExtra("id", id);
            v.getContext().startActivity(intent);
        }else if (v == settingsButton) {
            if (config.getId() < 0){
                MopidyServerConfigManager.get().saveServer(v.getContext(), config);
            }else{
                Intent intent = new Intent(v.getContext(), ConfigurationActivity.class);
                intent.putExtra("id", config.getId());
                v.getContext().startActivity(intent);
            }
        }else if (v == deleteButton){
            MopidyServerConfigManager.get().removeServer(v.getContext(), config.getId());
        }else{
            connect(v.getContext());
        }
    }

    public void connect(Context c){
        MopidyServerConfigManager.get().setCurrentServer(c, config);
        Intent intent = new Intent(nameText.getContext(), MopidyService.class);
        intent.setAction(MopidyService.ACTION_CONNECT);
        nameText.getContext().startService(intent);
    }
}
