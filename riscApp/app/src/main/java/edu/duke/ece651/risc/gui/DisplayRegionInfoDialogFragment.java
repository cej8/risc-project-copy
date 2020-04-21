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

import java.util.List;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Region;

public class DisplayRegionInfoDialogFragment extends DialogFragment {
    String planetName;
    int unit;
    String owner;
    TextView unit0;
    TextView unit1;
    TextView unit2;
    TextView unit3;
    TextView unit4;
    TextView unit5;
    TextView unit6;
    Region region;
    TextView plague;
    private boolean hasPlague;

    public DisplayRegionInfoDialogFragment(Region region,String planetName, int unit, String owner) {
        this.planetName = planetName;
        this.unit = unit;
        this.owner = owner;
        this.region = region;
        this.hasPlague = region.getPlague();
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
            plague = planetView.findViewById(R.id.plague);
            if (hasPlague){
                plague.setText("Your planet has the plague, it will produce no resources");
            }
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
            View unitBonusView = inflater.inflate(R.layout.display_unit_dialog, null);
            List<Integer> unitList = region.getUnits().getUnits();
            unit0 = unitBonusView.findViewById(R.id.unit0);
            unit0.setText(Integer.toString(unitList.get(0)));
            unit1 = unitBonusView.findViewById(R.id.unit3);
            unit1.setText(Integer.toString(unitList.get(1)));
            unit2 = unitBonusView.findViewById(R.id.unit5);
            unit2.setText(Integer.toString(unitList.get(2)));
            unit3 = unitBonusView.findViewById(R.id.unit6);
            unit3.setText(Integer.toString(unitList.get(3)));
            unit4 = unitBonusView.findViewById(R.id.unit2);
            unit4.setText(Integer.toString(unitList.get(4)));
            unit5 = unitBonusView.findViewById(R.id.unit1);
            unit5.setText(Integer.toString(unitList.get(5)));
            unit6 = unitBonusView.findViewById(R.id.unit4);
            unit6.setText(Integer.toString(unitList.get(6)));
            unitBuilder.setView(unitBonusView)
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

