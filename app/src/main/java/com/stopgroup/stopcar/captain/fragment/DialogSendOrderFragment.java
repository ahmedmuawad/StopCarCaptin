package com.stopgroup.stopcar.captain.fragment;


import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.stopgroup.stopcar.captain.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogSendOrderFragment extends DialogFragment {

    View view  ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_dialog_send_order, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        return view  ;
    }

}
