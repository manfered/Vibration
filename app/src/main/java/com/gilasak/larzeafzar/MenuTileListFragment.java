package com.gilasak.larzeafzar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MenuTileListFragment extends Fragment {

    Integer[] textViews = {R.id.menuListGraphTitleTextView,
            R.id.menuListCompareTitleTextView,
            R.id.menuListReviewTitleTextView,
            R.id.menuListMobilePCTitleTextView};

    LinearLayout linearLayoutListGraph;
    LinearLayout linearLayoutListReview;
    LinearLayout linearLayoutListCompare;
    LinearLayout linearLayoutListConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnLayout = inflater.inflate(R.layout.fragment_menu_tile_list, container, false);

        linearLayoutListGraph = (LinearLayout) returnLayout.findViewById(R.id.linearLayoutListGraph);
        linearLayoutListReview = (LinearLayout) returnLayout.findViewById(R.id.linearLayoutListReview);
        linearLayoutListCompare = (LinearLayout) returnLayout.findViewById(R.id.linearLayoutListCompare);
        linearLayoutListConnection = (LinearLayout) returnLayout.findViewById(R.id.linearLayoutListConnection);

        Typeface ty1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mehdi.ttf");

        for (int i = 0; i < textViews.length; ++i) {
            TextView t = (TextView) returnLayout.findViewById(textViews[i]);
            t.setTypeface(ty1);
            t.setTextSize(21);
        }

        linearLayoutListGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , GraphActivityComplete.class);
                intent.putExtra("TYPE","GraphComplete");
                startActivity(intent);
            }
        });

        linearLayoutListReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , GraphActivityComplete.class);
                intent.putExtra("TYPE","Review");
                startActivity(intent);
            }
        });

        linearLayoutListCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , GraphActivityComplete.class);
                intent.putExtra("TYPE","Compare");
                startActivity(intent);
            }
        });

        linearLayoutListConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_content, new ConnectionFragment());
                fragmentTransaction.commit();
            }
        });

        return returnLayout;
    }


}
