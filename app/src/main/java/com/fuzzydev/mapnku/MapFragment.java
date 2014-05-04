package com.fuzzydev.mapnku;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fuzzydev.mapnku.model.LocationItem;
import com.fuzzydev.mapnku.network.RequestManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MapFragment extends Fragment implements Callback<List<LocationItem>> {
    private static final double nkuLat = 39.032018;
    private static final double nkuLng = -84.464103;

    private Activity mActivity;
    private GoogleMap mMap;
    private List<LocationItem> mLocations;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        init(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void init(View rootView) {
        RequestManager.getInstance(getActivity()).getLocationData(mActivity, this);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setupMap();
            }
        }
    }

    private void setupMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(nkuLat, nkuLng))      // Sets the center of the map to Mountain View
                .zoom(14)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void success(List<LocationItem> locationItems, Response response) {
        mLocations = locationItems;
    }

    @Override
    public void failure(RetrofitError error) {
        Toast.makeText(mActivity, "There was a network problem, check your connection and try again.", Toast.LENGTH_SHORT).show();
    }
}
