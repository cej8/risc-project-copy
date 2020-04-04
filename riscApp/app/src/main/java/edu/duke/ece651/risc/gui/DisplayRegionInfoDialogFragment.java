package edu.duke.ece651.risc.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;

import edu.duke.ece651.risc.shared.Unit;

public class DisplayRegionInfoDialogFragment extends DialogFragment {
    String planetName;
    int unit;
    public DisplayRegionInfoDialogFragment(String planetName, int unit) {
        this.planetName = planetName;
        this.unit = unit;
    }
        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(planetName) // get planet name
                .setMessage(unit + " Units")
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
        public Dialog DialogTwo(Bundle savedInstanceState){
            AlertDialog.Builder unitBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            unitBuilder.setView(inflater.inflate(R.layout.planet_dialog, null))
                    .setTitle("Available Units")
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // popup builder 2
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            return unitBuilder.create();
        }
}

