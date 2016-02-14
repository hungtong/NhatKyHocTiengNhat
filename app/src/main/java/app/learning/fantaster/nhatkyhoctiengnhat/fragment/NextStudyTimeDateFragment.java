package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import app.learning.fantaster.nhatkyhoctiengnhat.R;

public class NextStudyTimeDateFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.next_study_on));
        builder.setView(R.layout.fragment_next_study);

        builder.setPositiveButton(
                getString(R.string.agree),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }
        );

        builder.setNegativeButton(
                getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );

        return builder.create();
    }

}
