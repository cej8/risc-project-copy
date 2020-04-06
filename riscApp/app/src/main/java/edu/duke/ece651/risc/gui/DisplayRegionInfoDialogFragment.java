package edu.duke.ece651.risc.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import edu.duke.ece651.risc.shared.AbstractPlayer;

public class DisplayRegionInfoDialogFragment extends DialogFragment {
    String planetName;
    int unit;
    String owner;

    public DisplayRegionInfoDialogFragment(String planetName, int unit, AbstractPlayer owner) {
        this.planetName = planetName;
        this.unit = unit;
        this.owner = owner.getName();
    }
        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            TextView ownerName;
            TextView unitNumbers;
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View planetView = inflater.inflate(R.layout.planet_dialog,null);
            unitNumbers = planetView.findViewById(R.id.unitNumbers);
            ownerName = planetView.findViewById(R.id.ownerName);
            String unitString = Integer.toString(unit);
            unitNumbers.setText(unitString);
            ownerName.setText(owner);
            builder.setView(planetView)
                .setTitle(planetName) // get planet name
                    .setPositiveButton(R.string.more, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // popup builder 2
                            DialogTwo(savedInstanceState).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
        public Dialog DialogTwo(final Bundle savedInstanceState){
            AlertDialog.Builder unitBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            unitBuilder.setView(inflater.inflate(R.layout.select_unit_dialog, null))
                    .setTitle("Available Units")
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // popup builder 2
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            //onCreateDialog(savedInstanceState).show();
                        }
                    });
            return unitBuilder.create();
        }
}

