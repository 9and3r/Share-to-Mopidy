package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import mopidy.to.share.and3r.sharetomopidy.PlaybackControlManager;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyData;

public class MopidyDataOptionsDialog extends DialogFragment implements DialogInterface.OnClickListener{

    public static final String MOPIDY_DATA = "MOPIDY_DATA";

    private MopidyData data;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        data = (MopidyData) getArguments().getSerializable(MOPIDY_DATA);
        builder.setTitle(data.getTitle());
        builder.setItems(getResources().getStringArray(R.array.mopidy_data_options), this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case 0:
                PlaybackControlManager.addToTracklist(getActivity(), data, false);
                break;
            case 1:
                PlaybackControlManager.addToTracklist(getActivity(), data, true);
                break;
        }
    }
}
