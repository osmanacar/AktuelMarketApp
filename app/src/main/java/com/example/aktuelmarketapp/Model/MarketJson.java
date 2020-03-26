package com.example.aktuelmarketapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketJson {
    @SerializedName("marketjson")
    @Expose

    private List<AktuelModel> aktuelModels = null;

    public List<AktuelModel> getAktuelModels() {
        return aktuelModels;
    }

    public void setAktuelModels(List<AktuelModel> aktuelModels) {
        this.aktuelModels = aktuelModels;
    }
}
