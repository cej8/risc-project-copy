package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import edu.duke.ece651.risc.shared.Connection;

public class BackButtonDialogFragment extends DialogFragment {
    private Activity activity;
    private Connection connection;

    public BackButtonDialogFragment(Activity act){
        this.activity = act;
        connection = ParentActivity.getConnection();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Warning") // get planet name
                .setMessage("Going back now will log you out of this game.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // take back to ConfirmLoginActivity.java
                        connection.closeAll();
                        Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
