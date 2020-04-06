package edu.duke.ece651.risc.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class DisplayOrderInfoDialogFragment extends DialogFragment {
    String fuel;
    public DisplayOrderInfoDialogFragment(String fuel){
        this.fuel = "This action will take " + fuel + "fuel";
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        TextView orderDisplay;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View planetView = inflater.inflate(R.layout.order_dialog,null);
        orderDisplay = planetView.findViewById(R.id.orderDisplay);
        orderDisplay.setText(fuel);
        builder.setView(planetView)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: add to order list
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, nothing happens
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
