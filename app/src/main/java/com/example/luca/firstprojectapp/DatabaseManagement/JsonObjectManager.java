package com.example.luca.firstprojectapp.DatabaseManagement;

import android.util.Log;

import com.example.luca.firstprojectapp.Interfaces.IJsonObject;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by luca on 25/05/15.
 */
public class JsonObjectManager {

    private JsonObjectManager() {
    }

    public static String toJsonList(List<LatLng> list) {
        JSONArray array = new JSONArray();
        Gson gson = new Gson();

        if (list != null && list.size() > 0) {
            for (LatLng obj : list) {
                try {
                    array.put(gson.toJson(obj));
                } catch (Exception e) {
                    Log.e("saveInstance", "Errore durante l'aggiunta all'array", e);
                }
            }
        }
        return array.toString();
    }

    public static List<LatLng> fromJson(String data) {

        List<LatLng> list = new LinkedList<>();
        Gson gson = new Gson();

        if (data != null && !data.isEmpty()) {
            try {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.optJSONObject(i);
                    if (json != null) {
                        try {
                            list.add(gson.fromJson(json.toString(),LatLng.class));
                        } catch (Exception e) {
                            Log.e("loadInstance", "Errore durante il parse del json", e);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("loadInstance", "Errore durante il parse della lista", e);
            }
        }

        return list;
    }

}