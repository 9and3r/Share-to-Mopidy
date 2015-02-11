package mopidy.to.share.and3r.sharetomopidy.user_interface.configuration.select;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.preferences.MopidyServerConfigManager;

/**
 * Created by ander on 2/4/15.
 */
public class SelectFragment extends Fragment implements Observer {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ServerAdapter adapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.select_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.servers_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ServerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MopidyServerConfigManager.get().addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MopidyServerConfigManager.get().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        adapter.notifyDataSetChanged();
    }
}
