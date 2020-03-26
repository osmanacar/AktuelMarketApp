package com.example.aktuelmarketapp.ui.Favori;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.aktuelmarketapp.Adapter.FavoriAdapter;
import com.example.aktuelmarketapp.Interface.FavoriItemListener;
import com.example.aktuelmarketapp.Model.AktuelModel;
import com.example.aktuelmarketapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriFragment extends Fragment {

    private FavoriViewModel galleryViewModel;
    ArrayList<AktuelModel> aktuelModels;
    FavoriAdapter favoriAdapter;
    ImageView imageViewFav;
    TextView textViewFav;
    ListView favoriList;
    private DatabaseReference referenceMarket;
    String id, idfavori, isim, resim, marketisim;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(FavoriViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favori, container, false);

        textViewFav = root.findViewById(R.id.textViewAktuelFav);
        imageViewFav = root.findViewById(R.id.imageViewAktuelFav);
        favoriList = root.findViewById(R.id.listViewFav);

        referenceMarket = FirebaseDatabase.getInstance().getReference("marketjson");

        aktuelModels = getFirebaseData();

        return root;
    }

    public ArrayList<AktuelModel> getFirebaseData() {
        final ArrayList<AktuelModel> aktuelModels = new ArrayList<>();

        referenceMarket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aktuelModels.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("idfavori").getValue().toString().equals("1")) {
                        id = ds.child("id").getValue().toString();
                        isim = ds.child("isim").getValue().toString();
                        resim = ds.child("resim").getValue().toString();
                        idfavori = ds.child("idfavori").getValue().toString();
                        marketisim = ds.child("marketisim").getValue().toString();
                        aktuelModels.add(new AktuelModel(id, isim, resim, idfavori, marketisim));
                        favoriAdapter = new FavoriAdapter(aktuelModels, getActivity(), new FavoriItemListener() {
                            @Override
                            public void onFavoriDeleted(String id) {
                                referenceMarket.child(id).child("idfavori").setValue("0");
                            }

                            @Override
                            public void onFavoriSelected(String id) {

                            }
                        });
                    }
                    if (favoriAdapter != null) {
                        favoriList.setAdapter(favoriAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return aktuelModels;
    }


}