package com.example.aktuelmarketapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AktuelModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idfavori")
    @Expose
    private String idfavori;
    @SerializedName("isim")
    @Expose
    private String isim;
    @SerializedName("marketisim")
    @Expose
    private String marketisim;
    @SerializedName("resim")
    @Expose
    private String resim;

    public AktuelModel(String id, String idfavori, String isim, String marketisim, String resim) {
        this.id = id;
        this.idfavori = idfavori;
        this.isim = isim;
        this.marketisim = marketisim;
        this.resim = resim;
    }

    public AktuelModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdfavori() {
        return idfavori;
    }

    public void setIdfavori(String idfavori) {
        this.idfavori = idfavori;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getMarketisim() {
        return marketisim;
    }

    public void setMarketisim(String marketisim) {
        this.marketisim = marketisim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }
}
