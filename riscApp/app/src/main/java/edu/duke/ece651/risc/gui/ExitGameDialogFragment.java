package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import edu.duke.ece651.risc.shared.Connection;

public class ExitGameDialogFragment extends DialogFragment {
    private Activity activity;
    private Connection connection;
    private ExecuteClient executeClient;

    public ExitGameDialogFragment(Activity act, ExecuteClient executeClient){
        this.activity = act;
        connection = ParentActivity.getConnection();
        this.executeClient = executeClient;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Exit to Lobby") // get planet name
                .setMessage("Are you sure you want to leave the game? All data will be saved, " +
                        "but you'll have to log back in")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // take back to ConfirmLoginActivity.java
                        executeClient.endGame();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
