package com.example.luca.firstprojectapp.DatabaseManagement;

import android.util.Log;

import com.example.luca.firstprojectapp.Interfaces.IJsonObject;

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

    public static String toJsonList(List<IJsonObject> list) {
        JSONArray array = new JSONArray();

        if (list != null && list.size() > 0) {
            for (IJsonObject obj : list) {
                try {
                    array.put(obj.toJsonObject());
                } catch (Exception e) {
                    Log.e("saveInstance", "Errore durante l'aggiunta all'array", e);
                }
            }
        }
        return array.toString();
    }

    public static List<IJsonObject> fromJson(String data) {

        List<IJsonObject> list = new LinkedList<>();

        if (data != null && !data.isEmpty()) {
            try {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.optJSONObject(i);
                    if (json != null) {
                        try {
                            //TODO add specific obj to convert
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