package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ander on 2/24/15.
 */
public class MopidyObjectBaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    AddAll adapter;
    int i;

    public MopidyObjectBaseHolder(View itemView, AddAll pAdapter) {
        super(itemView);
        adapter = pAdapter;
        //itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        adapter.addAll(i);
    }

    public interface AddAll{
        public void addAll(int i);
    }
}
