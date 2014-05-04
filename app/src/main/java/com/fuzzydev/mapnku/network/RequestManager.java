package com.fuzzydev.mapnku.network;

import android.content.Context;

import com.fuzzydev.mapnku.model.LocationItem;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by Dejan Ristic on 5/4/14.
 */
public class RequestManager {

    private static RequestManager sSingleton;
    private static Context sApplicationContext;

    private static RestAdapter sRestAdapter;
    private static NkuLocationService sLocationService;

    private RequestManager(Context context) {
        sApplicationContext = context.getApplicationContext();
        sRestAdapter = new RestAdapter.Builder()
                .setEndpoint("http://fuzzydev-nku-map.appspot.com/")
                .build();
        sLocationService = sRestAdapter.create(NkuLocationService.class);
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
    public static RequestManager getInstance(Context context) {
        if (sSingleton == null)
            sSingleton = new RequestManager(context);
        return sSingleton;
    }

    public void getLocationData(Context context, Callback<List<LocationItem>> callback) {
        sLocationService.listLocationItems(callback);
    }


}
