package com.fuzzydev.mapnku.network;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Dejan on 5/6/2014.
 */
public interface NkuDirectionService {
    @FormUrlEncoded
    @POST("/maps/api/directions/json")
    void getDirections(@Field("origin") LatLng origin, @Field("destination") LatLng destination,@Field("mode") String mode, @Field("sensor") boolean sensor,  @Field("key") String key, Callback<JsonObject> callback);
}
