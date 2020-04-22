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

public class BoostDialogFragment extends DialogFragment {
    ParentActivity pa;
    Activity activity;

    public BoostDialogFragment(Activity activity) {
        this.pa = new ParentActivity();
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View travelView = inflater.inflate(R.layout.boost_dialog,null);
        Button unitButton = travelView.findViewById(R.id.unitButton);
        Button techButton = travelView.findViewById(R.id.techButton);
        Button resourceButton = travelView.findViewById(R.id.resourceButton);

        unitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent unitBoost = new Intent(activity,BoostUnitActivity.class);
                unitBoost.putExtra("ORDER","boost units");
                startActivity(unitBoost);
            }
        });
        techButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent techBoostSetup = new Intent(activity,TechBoostActivity.class);
                techBoostSetup.putExtra("ORDER","techBoost");
                startActivity(techBoostSetup);
            }
        });
        resourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resourceBoost = new Intent(activity,ResourceBoostActivity.class);
                startActivity(resourceBoost);


            }
        });
        builder.setView(travelView)
                .setTitle("What would you like to boost?") // get planet name
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // exit dialog popup
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
