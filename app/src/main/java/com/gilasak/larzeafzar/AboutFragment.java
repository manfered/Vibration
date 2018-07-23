package com.gilasak.larzeafzar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluejamesbond.text.DocumentView;


public class AboutFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View returnView = inflater.inflate(R.layout.fragment_about, container, false);

        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        DocumentView d = (DocumentView) returnView.findViewById(R.id.aboutUSFragmentText);
        //d.setText(Html.fromHtml(getResources().getString(R.string.aboutLaserAfzar)));
        d.setText(Html.fromHtml(getResources().getString(R.string.aboutGilasak)));

        return returnView;
    }
}
