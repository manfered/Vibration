package com.gilasak.larzeafzar;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactFragment extends Fragment {

    Integer[] textViews = {R.id.contactGilasakTitle,
            R.id.contactGilasakSiteContent,
            R.id.contactGilasakTelegramTitle,
            R.id.contactGilasakEmailTitle,
            R.id.contactGilasakEmailContent,
            R.id.contactGilasakTelTitle,
            R.id.contactGilasakSiteTitle,
            R.id.contactGilasakTelegramcontent,
            R.id.contactGilasakTelContent,
            R.id.contactGilasakDeveloperTitle,
            R.id.contactGilasakDeveloperContent};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnLayout = inflater.inflate(R.layout.fragment_contact, container, false);

        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Typeface ty1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ANegaar.ttf");

        for (int i = 0; i < textViews.length; ++i) {
            TextView t = (TextView) returnLayout.findViewById(textViews[i]);
            t.setTypeface(ty1);
            if (textViews[i] == R.id.contactGilasakTitle) {
                t.setTextSize(21);
            } else {
                t.setTextSize(18);
            }
            /*if (textViews[i] == R.id.contactGilasakSiteContent) {
                t.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (textViews[i] == R.id.contactGilasakTelegramcontent) {
                t.setMovementMethod(LinkMovementMethod.getInstance());
            }*/
        }


        return returnLayout;
    }
}
