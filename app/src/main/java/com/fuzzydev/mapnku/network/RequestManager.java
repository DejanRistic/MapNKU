package com.fuzzydev.mapnku.network;

import android.content.Context;

import com.fuzzydev.mapnku.model.LocationItem;
import com.fuzzydev.mapnku.model.LocationItemDeserializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Dejan Ristic on 5/4/14.
 */
public class RequestManager {

    private static RequestManager sSingleton;
    private static Context sApplicationContext;

    private static RestAdapter sRestAdapter;
    private static NkuLocationService sLocationService;
    private static NkuDirectionService sDirectionService;

    private RequestManager(Context context) {
        sApplicationContext = context.getApplicationContext();
    }

    /**
     * Create a RequestManager singleton.
     *
     * @param context Some operational context for the RequestManager to use. This
     *                can be any context, but the RequestManager will always call
     *                getApplicationContext() so that only one RequestQueue is
     *                created for the application.
     * @return This {@link RequestManager} singleton.
     */
    public static RequestManager getInstance(Context context, boolean directions) {
        if (sSingleton == null)
            sSingleton = new RequestManager(context);



        if (!directions) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocationItem.class, new LocationItemDeserializer(context))
                    .create();

            sRestAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://fuzzydev-nku-map.appspot.com")
                    .setConverter(new GsonConverter(gson))
                    .build();
            sLocationService = sRestAdapter.create(NkuLocationService.class);

        } else {
            sRestAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://maps.googleapis.com")
                    .build();
            sDirectionService = sRestAdapter.create(NkuDirectionService.class);
        }

        return sSingleton;
    }

    public void getLocationData(Callback<List<LocationItem>> callback) {
        sLocationService.listLocationItems(callback);
    }

    public void getDirectionData(LatLng origin, LatLng destination, Callback<JsonObject> callback) {
       sDirectionService.getDirections(origin,destination,"walking",false,"AIzaSyA8MWCeSLCDz1HIvoll-uZ2ZD8CJBvo_lw",callback);
    }

}
