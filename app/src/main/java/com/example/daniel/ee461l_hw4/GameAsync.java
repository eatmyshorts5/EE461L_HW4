package com.example.daniel.ee461l_hw4;

import android.os.AsyncTask;
import android.util.Log;

import com.google.maps.model.DirectionsResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by daniel on 4/6/18.
 */

public class GameAsync extends AsyncTask<String, Void, GameInfo> {
    public static final String TAG = "RegClient";

    protected GameInfo doInBackground(String... params) {
        int currentIndex = 0;
        int nextIndex;
        double lat;
        double lng;
        GameInfo yeah = new GameInfo();

        JSONObject jsonRequest = null;

        String full_URL = new String(params[0]);
        String address = params[2];
        String hold;
        String result = "";
        String line;

        int indexOfLastSpace = address.lastIndexOf(" ");

        if(indexOfLastSpace == -1){
            full_URL = full_URL + address;
        } else {
            nextIndex = address.indexOf(" ", currentIndex);
            hold = address.substring(currentIndex, nextIndex);
            full_URL = full_URL + hold;
            currentIndex = nextIndex;

            while (currentIndex != indexOfLastSpace) {
                nextIndex = address.indexOf(" ", currentIndex + 1);
                hold = address.substring(currentIndex + 1, nextIndex);
                full_URL = full_URL + "+" + hold;
                currentIndex = nextIndex;
            }

            hold = address.substring(currentIndex + 1, address.length());
            full_URL = full_URL + "+" + hold;
        }

        full_URL = full_URL + params[1];

        Log.d(TAG, "Final Stop: " + full_URL);

        try {
            URL url = new URL(full_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            while ((line = bufferedReader.readLine()) != null)
                result += (line);
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d(TAG, result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jsonRequest = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray first = jsonRequest.getJSONArray("results");
            JSONObject second = first.getJSONObject(0);
            yeah.setAddress(second.getString("formatted_address"));
            JSONObject third = second.getJSONObject("geometry");
            JSONObject jsonObject = third.getJSONObject("location");
            yeah.setLat(Double.parseDouble(jsonObject.getString("lat")));
            yeah.setLng(Double.parseDouble(jsonObject.getString("lng")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return yeah;
    }
}
