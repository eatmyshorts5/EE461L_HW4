package com.example.daniel.ee461l_hw4;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.maps.DirectionsApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.GeoApiContext;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 4/5/18.
 */

public class MyAsyncTask extends AsyncTask<String, Void, DirectionsResult> {
    protected DirectionsResult doInBackground(String... params){
        DateTime now = new DateTime();
        DirectionsResult result = null;

        try {
            result = DirectionsApi.newRequest(getGeoContext(params[2]))
                    .mode(TravelMode.DRIVING).origin(params[0])
                    .destination(params[1]).departureTime(now)
                    .await();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return result;
    }

    private GeoApiContext getGeoContext(String apiKey) {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(apiKey)
                .setConnectTimeout(10, TimeUnit.SECONDS)
                .setReadTimeout(10, TimeUnit.SECONDS)
                .setWriteTimeout(10, TimeUnit.SECONDS);
    }
}
