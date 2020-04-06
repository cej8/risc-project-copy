package edu.duke.ece651.risc.gui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DisplayOrderInfoDialogFragment extends DialogFragment {
    String fuel;
    public DisplayOrderInfoDialogFragment(String fuel){
        this.fuel = fuel;
    }

}
