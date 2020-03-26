package com.example.aktuelmarketapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aktuelmarketapp.Interface.FavoriItemListener;
import com.example.aktuelmarketapp.Model.AktuelModel;
import com.example.aktuelmarketapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AktuelAdapter extends BaseAdapter {

    ArrayList<AktuelModel> aktuelModels;
    private Activity activity;
    LayoutInflater layoutInflater;
    DatabaseReference referenceMarket;
    private FavoriItemListener favoriItemListener;

    public AktuelAdapter(ArrayList<AktuelModel> aktuelModels, Activity activity, FavoriItemListener favoriItemListener) {
        this.aktuelModels = aktuelModels;
        this.activity = activity;
        this.favoriItemListener = favoriItemListener;
    }

    @Override
    public int getCount() {
        return aktuelModels.size();
    }

    @Override
    public Object getItem(int position) {
        return aktuelModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.aktuel_item, null);

        ImageView imageAktuel = view.findViewById(R.id.imageViewAktuel);
        TextView textAktuel = view.findViewById(R.id.textViewAktuel);
        final ImageView favAktuel = view.findViewById(R.id.favButton);
        final ImageView notFavAktuel = view.findViewById(R.id.notFavButton);

        Glide.with(view.getContext()).load(aktuelModels.get(position).getResim()).into(imageAktuel);
        textAktuel.setText(aktuelModels.get(position).getIsim());

        if (aktuelModels.get(position).getIdfavori().equals("0")) {
            favAktuel.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            notFavAktuel.setVisibility(View.VISIBLE);
        }


        favAktuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favAktuel.setImageResource(R.drawable.ic_favorite_black_24dp);

                favoriItemListener.onFavoriSelected(aktuelModels.get(position).getId());
                if (aktuelModels.get(position).getIdfavori().equals("0")) {
                    aktuelModels.get(position).setIdfavori("1");
                } else {
                    aktuelModels.get(position).setIdfavori("1");
                }
            }
        });

        notFavAktuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notFavAktuel.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                favoriItemListener.onFavoriDeleted(aktuelModels.get(position).getId());
                if (aktuelModels.get(position).getIdfavori().equals("1")) {
                    aktuelModels.get(position).setIdfavori("0");
                } else {
                    aktuelModels.get(position).setIdfavori("0");
                }

            }
        });


        return view;
    }
}
