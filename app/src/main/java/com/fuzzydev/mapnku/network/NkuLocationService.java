package com.fuzzydev.mapnku.network;

import com.fuzzydev.mapnku.model.LocationItem;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Dejan Ristic on 5/4/14.
 */
public interface NkuLocationService {
    @GET("/getLocations")
    void listLocationItems(Callback<List<LocationItem>> locationsCallback);
}