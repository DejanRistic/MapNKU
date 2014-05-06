package com.fuzzydev.mapnku.model;

import android.content.Context;

import com.fuzzydev.mapnku.provider.MapNkuContent.DbLocationItem;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Dejan on 5/4/2014.
 */
public class LocationItemDeserializer
        implements JsonDeserializer<LocationItem> {
    private Context mContext;

    public LocationItemDeserializer(Context context){
        mContext = context;
    }

    @Override
    public LocationItem deserialize(JsonElement json, Type type,
                                    JsonDeserializationContext context) throws JsonParseException {

        JsonObject locationItemJson = (JsonObject) json;


        LocationItem locationItem = new LocationItem(locationItemJson.get("Author").getAsString(),
                locationItemJson.get("Building").getAsString(),
                locationItemJson.get("Name").getAsString(),
                locationItemJson.get("Lat").getAsString(),
                locationItemJson.get("Long").getAsString(),
                locationItemJson.get("Floor").getAsString(),
                locationItemJson.get("Description").getAsString(),
                locationItemJson.get("MondayTime").getAsString(),
                locationItemJson.get("TuesdayTime").getAsString(),
                locationItemJson.get("WednesdayTime").getAsString(),
                locationItemJson.get("ThursdayTime").getAsString(),
                locationItemJson.get("FridayTime").getAsString(),
                locationItemJson.get("SaturdayTime").getAsString(),
                locationItemJson.get("SundayTime").getAsString(),
                locationItemJson.get("Date").getAsString());

        mContext.getContentResolver().insert(DbLocationItem.CONTENT_URI,locationItem.toContentValues());

        return locationItem;
    }
}

