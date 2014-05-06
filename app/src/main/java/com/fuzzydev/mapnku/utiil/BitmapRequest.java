package com.fuzzydev.mapnku.utiil;

public class BitmapRequest {

    GooglePlaceSearch.OnBitmapResponseListener listener = null;
    String url = null;
    String tag = null;

    public BitmapRequest(GooglePlaceSearch.OnBitmapResponseListener listener, String url, String tag) {
        this.listener = listener;
        this.url = url;
        this.tag = tag;
    }

    public String getURL() {
        return this.url;
    }

    public GooglePlaceSearch.OnBitmapResponseListener getListener() {
        return this.listener;
    }

    public String getTag() {
        return this.tag;
    }
}
