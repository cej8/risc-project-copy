package edu.duke.ece651.risc.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class SpectateDialogFragment extends DialogFragment {
    ParentActivity pa;

    public SpectateDialogFragment(){
        this.pa = new ParentActivity();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        TextView ownerName;
        TextView unitNumbers;
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Would you like to keep watching?") // get planet name
                .setPositiveButton("Spectate", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            pa.setSpectate(true);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pa.setSpectate(false);
                        //executeClient.returnLogin();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
