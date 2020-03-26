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

import java.util.ArrayList;

public class FavoriAdapter extends BaseAdapter {

    ArrayList<AktuelModel> aktuelModels;
    LayoutInflater layoutInflater;
    Activity activity;
    private FavoriItemListener favoriItemListener;

    public FavoriAdapter(ArrayList<AktuelModel> aktuelModels, Activity activity, FavoriItemListener favoriItemListener) {
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
        final View view = layoutInflater.inflate(R.layout.aktuel_favori_item, null);
        final ImageView imageFavAktuel = view.findViewById(R.id.imageViewAktuelFav);
        final TextView textFavAktuel = view.findViewById(R.id.textViewAktuelFav);
        final ImageView notFavAktuel2 = view.findViewById(R.id.notFavButton2);

        Glide.with(view.getContext()).load(aktuelModels.get(position).getResim()).into(imageFavAktuel);
        textFavAktuel.setText(aktuelModels.get(position).getIsim());

        notFavAktuel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriItemListener.onFavoriDeleted(aktuelModels.get(position).getId());
            }
        });

        return view;
    }
}
