package mopidy.to.share.and3r.sharetomopidy.user_interface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import mopidy.to.share.and3r.sharetomopidy.MopidyService;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;

public class OldMopidyDialog extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(String.format(getString(R.string.old_mopidy_message), MopidyStatus.get().getMopidyVersion()));

        builder.setTitle(getString(R.string.old_mopidy));
        builder.setPositiveButton(R.string.more_info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.more_info_url)));
                startActivity(i);
            }
        });

        builder.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                disconnect();
            }
        });
        return builder.create();
    }




    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        disconnect();
    }



    public void disconnect() {
        Intent intent = new Intent(getActivity(), MopidyService.class);
        intent.setAction(MopidyService.ACTION_STOP_SERVICE);
        getActivity().startService(intent);
    }
}
