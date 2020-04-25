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
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class MoveDialogFragment extends DialogFragment {
    ParentActivity pa;
    Activity activity;

    public MoveDialogFragment(Activity activity) {
        this.pa = new ParentActivity();
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View travelView = inflater.inflate(R.layout.travel_dialog,null);
        Button moveButton = travelView.findViewById(R.id.moveButton);
        Button teleportButton = travelView.findViewById(R.id.teleportButton);
        if(this.pa.getPlayer().getMaxTechLevel().getMaxTechLevel()<4){
            teleportButton.setEnabled(false);//button is locked until appropriate tech level is reached
        }
        else{
            teleportButton.setEnabled(true);
        }

        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attackSetup = new Intent(activity,OrderActivity.class);
                attackSetup.putExtra("ORDER","move");
                startActivity(attackSetup);
            }
        });
        teleportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teleport = new Intent(activity,OrderActivity.class);
                teleport.putExtra("ORDER","teleport");
                startActivity(teleport);
            }
        });
        builder.setView(travelView)
        .setTitle("Pick your move order") // get planet name
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // exit dialog popup
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
