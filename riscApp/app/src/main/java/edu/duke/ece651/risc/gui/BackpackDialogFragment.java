package edu.duke.ece651.risc.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.List;

public class BackpackDialogFragment extends DialogFragment {
    List<Drawable> levelDrawable;

    public BackpackDialogFragment(List<Drawable> list){
        this.levelDrawable = list;
    }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View backpackView = inflater.inflate(R.layout.display_backpack,null);
            TextView fuelAmount = backpackView.findViewById(R.id.fuelAmount);
            TextView techAmount = backpackView.findViewById(R.id.techAmount);
            TextView techLevel = backpackView.findViewById(R.id.techLevel);
            String fuel = Integer.toString(ParentActivity.getPlayer().getResources().getFuelResource().getFuel());
            String tech = Integer.toString(ParentActivity.getPlayer().getResources().getTechResource().getTech());
            int levelInt = ParentActivity.getPlayer().getMaxTechLevel().getMaxTechLevel();
            String level = Integer.toString(levelInt);
            fuelAmount.setText(fuel);
            techAmount.setText(tech);
            techLevel.setText(level);
            ImageView levelImage = backpackView.findViewById(R.id.techLevelPic);
            levelImage.setImageDrawable(levelDrawable.get(levelInt));
            builder.setView(backpackView)
                .setTitle("Backpack")
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

}
