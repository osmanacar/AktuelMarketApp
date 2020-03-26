package com.example.aktuelmarketapp.ui.Market;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.aktuelmarketapp.MarketlerActivity.A101Activity;
import com.example.aktuelmarketapp.MarketlerActivity.BimActivity;
import com.example.aktuelmarketapp.R;

public class MarketFragment extends Fragment {

    private MarketViewModel homeViewModel;
    androidx.gridlayout.widget.GridLayout gridLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(MarketViewModel.class);
        View root = inflater.inflate(R.layout.fragment_market, container, false);

        gridLayout = (androidx.gridlayout.widget.GridLayout) root.findViewById(R.id.gridlayout);
        setSingleEvent(gridLayout);

        return root;
    }

    public void setSingleEvent(GridLayout layout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final CardView cardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI == 0) {
                        Intent intent = new Intent(getContext(), BimActivity.class);
                        startActivity(intent);
                    }
                    else if(finalI == 1){
                        Intent intent = new Intent(getContext(), A101Activity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}