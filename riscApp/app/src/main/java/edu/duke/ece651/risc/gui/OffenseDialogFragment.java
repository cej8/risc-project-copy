package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class OffenseDialogFragment extends DialogFragment {
    ParentActivity pa;
    Activity activity;

    public OffenseDialogFragment(Activity activity) {
        this.pa = new ParentActivity();
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View travelView = inflater.inflate(R.layout.offense_dialog,null);
        Button attackButton = travelView.findViewById(R.id.attackButton);
        Button raidButton = travelView.findViewById(R.id.raidButton);

        attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attackSetup = new Intent(activity,OrderActivity.class);
                attackSetup.putExtra("ORDER","attack");
                startActivity(attackSetup);
            }
        });
        raidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: setup once raid implemented
            }
        });
        builder.setView(travelView)
                .setTitle("Pick your attack order")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // exit dialog popup
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}