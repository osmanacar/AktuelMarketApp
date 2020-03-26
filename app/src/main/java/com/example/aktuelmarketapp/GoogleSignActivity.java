package com.example.aktuelmarketapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.aktuelmarketapp.Model.AktuelModel;
import com.example.aktuelmarketapp.Model.MarketJson;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;


import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleSignActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    SignInButton login_button;
    ArrayList<AktuelModel> aktuelModels = new ArrayList<>();
    List<String> data = new ArrayList<>();
    MarketJson marketJson = new MarketJson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign);

        login_button = (SignInButton) findViewById(R.id.login_button);

        try {
            //Load File
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.jsonmarket)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null; ) {
                jsonBuilder.append(line).append("\n");
            }

            Gson gson = new Gson();

            marketJson = gson.fromJson(jsonBuilder.toString(), MarketJson.class);

//            for (int i = 0; i < marketJson.getAktuelModels().size(); i++) {
//                data.add(marketJson.getAktuelModels().get(i).getId());
//                data.add(marketJson.getAktuelModels().get(i).getIdfavori());
//                data.add(marketJson.getAktuelModels().get(i).getIsim());
//                data.add(marketJson.getAktuelModels().get(i).getMarketisim());
//                data.add(marketJson.getAktuelModels().get(i).getResim());
//            }

        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        }


        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            updateUI(user);
        }
    }

    void SignInGoogle() {
        Intent signIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null)
                    firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());

        final AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signin success");

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String user_id = user.getUid();

                            HashMap<Object, String> hashMapUID = new HashMap<>();
                            hashMapUID.put("uid", user_id);

                            HashMap<Object, String> hashMapMarket = new HashMap<>();
                            for (int i = 0; i < marketJson.getAktuelModels().size(); i++) {
                                String id = marketJson.getAktuelModels().get(i).getId();
                                String idfavori = marketJson.getAktuelModels().get(i).getIdfavori();
                                String isim = marketJson.getAktuelModels().get(i).getIsim();
                                String marketisim = marketJson.getAktuelModels().get(i).getMarketisim();
                                String resim = marketJson.getAktuelModels().get(i).getResim();

                                hashMapMarket.put("id", id);
                                hashMapMarket.put("idfavori", idfavori);
                                hashMapMarket.put("isim", isim);
                                hashMapMarket.put("marketisim", marketisim);
                                hashMapMarket.put("resim", resim);
                            }


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");

                            reference.child(user_id).setValue(hashMapUID);
                            reference.child(user_id).child("marketjson").setValue(hashMapMarket);

                            Toast.makeText(getApplicationContext(), "signin success", Toast.LENGTH_SHORT).show();

                            updateUI(user);


                        } else {
                            Log.w("TAG", "signin failure", task.getException());
                            Toast.makeText(getApplicationContext(), "signin failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            login_button.setVisibility(View.INVISIBLE);
            startActivity(new Intent(GoogleSignActivity.this, MainActivity.class));
        } else {
            login_button.setVisibility(View.VISIBLE);
        }
    }
}


//    private String getListData() {
//        String jsonStr = "{ \"marketjson\" :[" +
//                "{\"id\":\"0\",\"idfavori\":\"0\",\"isim\":\"03-07 OCAK 2020\",\"resim\":\"https://www.aktuel-katalogu.com/kataloglar/bim/03-ocak-2020/bim-03-ocak-2020-2.jpg\",\"marketisim\":\"bim\"}" +
//                "{\"id\":\"1\",\"idfavori\":\"0\",\"isim\":\"03-07 OCAK 2020\",\"resim\":\"https://cdn.akakce.com/_bro/731/7139/7139_1_85461u.jpg\",\"marketisim\":\"bim\"}" +
//                "{\"id\":\"2\",\"idfavori\":\"0\",\"isim\":\"03-07 OCAK 2020\",\"resim\":\"https://cdn.akakce.com/_bro/731/7139/7139_2_85462u.jpg\",\"marketisim\":\"bim\"}]}";
//
//        return jsonStr;
//    }

//                            String jsonStr = getListData();
//                            ArrayList<AktuelModel> marketList = new ArrayList<>();
//
//                            try {
//                                JSONObject jsonObject = new JSONObject(jsonStr);
//                                JSONArray jsonArray = jsonObject.getJSONArray("marketjson");
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject object = jsonArray.getJSONObject(i);
//                                    AktuelModel model = new AktuelModel();
//
//                                    model.setId(object.getString("id"));
//                                    model.setIdfavori(object.getString("idfavori"));
//                                    model.setIsim(object.getString("isim"));
//                                    model.setMarketisim(object.getString("marketisim"));
//                                    model.setResim(object.getString("resim"));
//
//                                    marketList.add(model);

//                                    HashMap<String, String> market = new HashMap<>();
//                                    market.put("id", object.getString("id"));
//                                    market.put("idfavori", object.getString("idfavori"));
//                                    market.put("isim", object.getString("isim"));
//                                    market.put("marketisim", object.getString("marketisim"));
//                                    market.put("resim", object.getString("resim"));
//                                    marketList.add(market);

//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }


//    public class JSONTask extends AsyncTask<String, String, List<AktuelModel>> {
//
//        @Override
//        protected List<AktuelModel> doInBackground(String... urls) {
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//
//            try {
//                URL url = new URL(urls[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                InputStream stream = connection.getInputStream();
//                reader = new BufferedReader(new InputStreamReader(stream));
//                StringBuffer buffer = new StringBuffer();
//
//                String line = "";
//
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                }
//
//                String json = buffer.toString();
//
//                JSONObject parentObject = new JSONObject(json);
//                JSONArray parentArray = parentObject.getJSONArray("marketjson");
//
//                List<AktuelModel> aktuelModelList = new ArrayList<>();
//
//                Gson gson = new Gson();
//
//                for (int i = 0; i < parentArray.length(); i++) {
//                    JSONObject finalObject = parentArray.getJSONObject(i);
//
//                    AktuelModel aktuelModel = gson.fromJson(finalObject.toString(), AktuelModel.class);
//                    aktuelModel.setId(finalObject.getString("id"));
//                    aktuelModel.setIdfavori(finalObject.getString("idfavori"));
//                    aktuelModel.setIsim(finalObject.getString("isim"));
//                    aktuelModel.setMarketisim(finalObject.getString("marketisim"));
//                    aktuelModel.setResim(finalObject.getString("resim"));
//
//                    aktuelModelList.add(aktuelModel);
//                }
//
//                return aktuelModelList;
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return null;
//        }
//
//    }


