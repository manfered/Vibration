package com.gilasak.larzeafzar;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ConnectionFragment extends Fragment {

    LinearLayout linearLayoutConnectionAndroidBeacon;
    LinearLayout linearLayoutConnectionAndroidReceiver;
    LinearLayout linearLayoutConnectionPCBeacon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnLayout = inflater.inflate(R.layout.fragment_connection, container, false);


        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        linearLayoutConnectionAndroidBeacon = (LinearLayout) returnLayout.findViewById(R.id.linearLayoutConnectionAndroidBeacon);
        linearLayoutConnectionAndroidReceiver = (LinearLayout) returnLayout.findViewById(R.id.linearLayoutConnectionAndroidReceiver);
        linearLayoutConnectionPCBeacon = (LinearLayout) returnLayout.findViewById(R.id.linearLayoutConnectionPCBeacon);

        linearLayoutConnectionAndroidBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextUpdateInformer();
            }
        });

        linearLayoutConnectionAndroidReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextUpdateInformer();
            }
        });

        linearLayoutConnectionPCBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextUpdateInformer();
            }
        });

        return returnLayout;
    }

    public void nextUpdateInformer(){
        Snackbar snackbar = Snackbar
                .make(linearLayoutConnectionAndroidBeacon, getActivity().getResources().getString(R.string.menuConnectionUpdateInformerText), Snackbar.LENGTH_INDEFINITE)
                .setAction(getActivity().getResources().getString(R.string.menuConnectionUpdateInformerButton), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //
                    }
                });


        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textViewSnackText = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textViewSnackText.setTextSize(20);
        // set text to center
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            textViewSnackText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            textViewSnackText.setGravity(Gravity.CENTER_HORIZONTAL);



        TextView textViewSnackAction = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        textViewSnackAction.setTextSize(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            textViewSnackAction.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            textViewSnackAction.setGravity(Gravity.CENTER_HORIZONTAL);
        textViewSnackAction.setBackgroundColor(getResources().getColor(R.color.greenButton));

        snackbar.setActionTextColor(getResources().getColor(R.color.menuConnectionSnackBarActionColor));

        snackbar.show();
    }

}
