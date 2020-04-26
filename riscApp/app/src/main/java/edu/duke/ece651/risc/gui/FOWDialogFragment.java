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

public class FOWDialogFragment extends DialogFragment {
    ParentActivity pa;
    Activity activity;

    public FOWDialogFragment(Activity activity) {
        this.pa = new ParentActivity();
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View travelView = inflater.inflate(R.layout.fow_dialog,null);
        Button spyButton = travelView.findViewById(R.id.spyButton);
        Button cloakButton = travelView.findViewById(R.id.cloakButton);
        if(this.pa.getPlayer().getMaxTechLevel().getMaxTechLevel()<3){
            cloakButton.setVisibility(View.INVISIBLE);
        }
        else{
            cloakButton.setVisibility(View.VISIBLE);
        }

        spyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent spyUpgrade = new Intent(activity,SpyUpgradeActivity.class);
                startActivity(spyUpgrade);
            }
        });
        cloakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: set cloak functionality
            }
        });
        builder.setView(travelView)
                .setTitle("Pick your Fog of War Orders") // get planet name
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // exit dialog popup
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

