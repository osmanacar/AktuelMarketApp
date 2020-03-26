package com.example.aktuelmarketapp.MarketlerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.aktuelmarketapp.Adapter.AktuelAdapter;
import com.example.aktuelmarketapp.Interface.FavoriItemListener;
import com.example.aktuelmarketapp.Model.AktuelModel;
import com.example.aktuelmarketapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class A101Activity extends AppCompatActivity {

    ArrayList<AktuelModel> aktuelModels = new ArrayList<>();
    AktuelAdapter adapter;
    ListView listView;
    DatabaseReference referenceMarket;
    Activity activity;
    String id, idfavori, isim, resim, marketisim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a101);

        listView = findViewById(R.id.listViewA101);
        getSupportActionBar().setTitle("A101");

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        addFavori();
    }

    private void addFavori() {

        referenceMarket = FirebaseDatabase.getInstance().getReference("marketjson");

        Query query = FirebaseDatabase.getInstance().getReference("marketjson")
                .orderByChild("marketisim")
                .equalTo("a101");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aktuelModels.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {

                        id = String.valueOf(ds.child("id").getValue());
                        idfavori = String.valueOf(ds.child("idfavori").getValue());
                        isim = String.valueOf(ds.child("isim").getValue());
                        resim = String.valueOf(ds.child("resim").getValue());
                        marketisim = String.valueOf(ds.child("marketisim").getValue());
                        aktuelModels.add(new AktuelModel(id, isim, resim, idfavori, marketisim));
                        adapter = new AktuelAdapter(aktuelModels, activity, new FavoriItemListener() {
                            @Override
                            public void onFavoriDeleted(String id) {
                                referenceMarket.child(id).child("idfavori").setValue("0");
                            }

                            @Override
                            public void onFavoriSelected(String id) {
                                referenceMarket.child(id).child("idfavori").setValue("1");
                            }
                        });

                    }
                    if (adapter != null) {
                        listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}