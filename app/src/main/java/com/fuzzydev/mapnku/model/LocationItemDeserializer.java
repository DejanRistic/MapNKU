package com.fuzzydev.mapnku.model;

import com.google.gson.JsonArray;
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

    @Override
    public LocationItem deserialize(JsonElement json, Type type,
                                    JsonDeserializationContext context) throws JsonParseException {

        JsonArray jArray = (JsonArray) json;

        LocationItem locationItem = null;

        for (int i = 1; i < jArray.size(); i++) {
            JsonObject jObject = (JsonObject) jArray.get(i);

        }

        return locationItem;
    }
}
}
