package com.fuzzydev.mapnku;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fuzzydev.mapnku.model.LocationItem;
import com.fuzzydev.mapnku.network.RequestManager;
import com.fuzzydev.mapnku.provider.MapNkuContent;
import com.fuzzydev.mapnku.utiil.GoogleDirection;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.support.v4.view.MenuItemCompat.getActionView;


public class MapFragment extends Fragment implements Callback<List<LocationItem>>, GoogleMap.OnInfoWindowClickListener {
    private static final double nkuLat = 39.032018;
    private static final double nkuLng = -84.464103;

    private IconGenerator mIconGenerator;

    private Activity mActivity;
    private GoogleMap mMap;
    private List<LocationItem> mLocations;
    private Hashtable<String, Marker> mHashTable;

    OnDirectionRequestedListener mCallback;

    // Container Activity must implement this interface
    public interface OnDirectionRequestedListener {
        public void onDirectionsRequested(Bundle bundle);
    }

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

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDirectionRequestedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDirectionRequestedListener");
        }

        mActivity = activity;
        mIconGenerator = new IconGenerator(mActivity);
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE)
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng origin = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        LatLng destination = marker.getPosition();

        LocationItem item = new LocationItem();
        item.setLat(String.valueOf(destination.latitude));
        item.setLng(String.valueOf(destination.longitude));

        marker.hideInfoWindow();
        final String name = marker.getTitle();
        final String description = marker.getSnippet();

        hideOtherMarkers(marker);
        updateCameraToCoverTwoPoints(mMap.getMyLocation(), item);

        GoogleDirection gd = new GoogleDirection(mActivity);

        gd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                Toast.makeText(mActivity.getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();

                bundle.putString("name", name);
                bundle.putString("description", description);
                bundle.putString("distance", gd.getTotalDistanceText(doc));
                bundle.putString("duration", gd.getTotalDurationText(doc));
                bundle.putString("start_address", gd.getStartAddress(doc));
                bundle.putString("end_address", gd.getEndAddress(doc));

                mCallback.onDirectionsRequested(bundle);

                gd.setOnAnimateListener(new GoogleDirection.OnAnimateListener() {
                    @Override
                    public void onFinish() {
                        ((NavigationActivity) getActivity()).openDrawer();
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onProgress(int progress, int total) {
                    }

                });

                gd.animateDirection(mMap, gd.getDirection(doc), GoogleDirection.SPEED_NORMAL
                        , true, true, true, false, null, false, true, new PolylineOptions().width(4));
            }
        });

        gd.request(origin, destination, GoogleDirection.MODE_WALKING);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView;
        searchView = (SearchView)
                getActionView(searchItem);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    searchItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem.collapseActionView();
                searchView.clearFocus();
                filterBySearchQuery(query, true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBySearchQuery(newText, false);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle:
                changeMapType();
                return true;
            case R.id.action_refresh:
                mCallback.onDirectionsRequested(null);

                if (mLocations == null || mLocations.isEmpty()) {
                    RequestManager.getInstance(mActivity, false).getLocationData(this);
                } else {
                    addMarkers(mLocations);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(View rootView) {
        mHashTable = new Hashtable<>();
        setHasOptionsMenu(true);
        mActivity.getContentResolver().delete(MapNkuContent.DbLocationItem.CONTENT_URI, null, null);
        RequestManager.getInstance(mActivity, false).getLocationData(this);
    }

    private void hideOtherMarkers(Marker marker) {
        Enumeration<String> enumKey = mHashTable.keys();
        while (enumKey.hasMoreElements()) {
            String key = enumKey.nextElement();
            Marker value = mHashTable.get(key);
            if (!(value.getTitle() + value.getSnippet()).equalsIgnoreCase(marker.getTitle() + marker.getSnippet())) {
                value.setVisible(false);
            }
        }
    }


    private void filterBySearchQuery(String query, boolean isSubmit) {
        List<LocationItem> filteredList = new ArrayList<>();
        if (!query.isEmpty()) {
            Cursor c = mActivity.getContentResolver().query(MapNkuContent.DbLocationItem.CONTENT_URI,
                    MapNkuContent.DbLocationItem.PROJECTION,
                    MapNkuContent.DbLocationItem.Columns.NAME.getName() + " LIKE ?",
                    new String[]{"%" + query + "%"}, null);
            while (c != null && c.moveToNext()) {
                filteredList.add(new LocationItem(c));
            }
            c.close();
            addMarkers(filteredList);
            if (filteredList.size() == 1 && isSubmit) {
                updateCameraToCoverTwoPoints(mMap.getMyLocation(), filteredList.get(0));
            }
        } else {
            addMarkers(mLocations);
        }
    }


    private void addMarkers(List<LocationItem> items) {
        mMap.clear();
        mHashTable.clear();
        for (LocationItem location : items) {
            if (!location.getLng().isEmpty() && !location.getLat().isEmpty()) { // Building or Parking Lot
                addBuildingAndParkingLot(location);
            } else {
                addServiceItem(location);
            }
        }
    }

    private void addBuildingAndParkingLot(LocationItem location) {
        LatLng latLng = new LatLng(Double.valueOf(location.getLat()), Double.valueOf(location.getLng()));

        BitmapDescriptor markerIcon = getMarkerIcon(location.getName(), false);

        Marker locationMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Directions to " + location.getName())
                .snippet(location.getDescription())
                .icon(markerIcon));
        mHashTable.put(locationMarker.getTitle() + locationMarker.getSnippet(), locationMarker);

    }

    private void addServiceItem(LocationItem location) {
        Cursor c = queryBuilding(location.getBuilding());
        if (c.moveToFirst()) {
            LocationItem temp = new LocationItem(c);
            LatLng latLng = new LatLng(Double.valueOf(temp.getLat()), Double.valueOf(temp.getLng()));

            BitmapDescriptor markerIcon = getMarkerIcon(location.getName(), true);

            Marker locationMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Directions to " + location.getName())
                    .snippet(location.getDescription())
                    .flat(true)
                    .icon(markerIcon));
            mHashTable.put(locationMarker.getTitle() + locationMarker.getSnippet(), locationMarker);
        }
        c.close();
    }

    private Cursor queryBuilding(String query) {
        Cursor c = mActivity.getContentResolver().query(MapNkuContent.DbLocationItem.CONTENT_URI,
                MapNkuContent.DbLocationItem.PROJECTION,
                MapNkuContent.DbLocationItem.Columns.NAME.getName() + " = ?",
                new String[]{query}, null);
        return c;
    }

    private BitmapDescriptor getMarkerIcon(String name, boolean isService) {
        if (name == null) {
            mIconGenerator.setStyle(IconGenerator.STYLE_PURPLE);
            return BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(name));
        } else if (name.contains("Parking")) {
            mIconGenerator.setStyle(IconGenerator.STYLE_ORANGE);
            return BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(name));
        } else if (isService) {
            mIconGenerator.setStyle(IconGenerator.STYLE_RED);
            return BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(name));
        } else {
            mIconGenerator.setStyle(IconGenerator.STYLE_BLUE);
            return BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(name));
        }
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
                .zoom(17)                 // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setPadding(0, 200, 0, 0);
        UiSettings ui = mMap.getUiSettings();
        ui.setMyLocationButtonEnabled(true);
        ui.setZoomControlsEnabled(false);

        mMap.setOnInfoWindowClickListener(this);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                updateCameraToCoverPoints(mMap.getMyLocation());
                return true;
            }
        });
    }

    private void updateCameraToCoverTwoPoints(android.location.Location myLocation, LocationItem destination) {

        LatLngBounds bounds = null;

        if (myLocation != null) {

            double LAT = myLocation.getLatitude();
            double LONG = myLocation.getLongitude();

            bounds = new LatLngBounds(new LatLng(LAT, LONG), new LatLng(LAT, LONG));

        }

        if (bounds != null) {
            if (!destination.getLng().equalsIgnoreCase("") && !destination.getLat().equalsIgnoreCase("")) { // Building or Parking Lot
                double LAT = Double.valueOf(destination.getLat());
                double LONG = Double.valueOf(destination.getLng());

                bounds = bounds.including(new LatLng(LAT, LONG));
            } else {
                Cursor c = queryBuilding(destination.getBuilding());
                if (c.moveToFirst()) {
                    LocationItem temp = new LocationItem(c);

                    double LAT = Double.valueOf(temp.getLat());
                    double LONG = Double.valueOf(temp.getLng());

                    bounds = bounds.including(new LatLng(LAT, LONG));
                }
            }

            updateCamera(bounds);
        }
    }

    private void updateCameraToCoverPoints(android.location.Location myLocation) {

        LatLngBounds bounds = new LatLngBounds(new LatLng(nkuLat, nkuLng), new LatLng(nkuLat, nkuLng));


        if (myLocation != null) {

            double LAT = myLocation.getLatitude();
            double LONG = myLocation.getLongitude();

            bounds = bounds.including(new LatLng(LAT, LONG));

        }

        for (LocationItem mLoc : mLocations) {

            if (!mLoc.getLng().isEmpty() && !mLoc.getLat().isEmpty()) { // Building or Parking Lot
                double LAT = Double.valueOf(mLoc.getLat());
                double LONG = Double.valueOf(mLoc.getLng());

                bounds = bounds.including(new LatLng(LAT, LONG));
            }

        }
        updateCamera(bounds);
    }

    private void updateCamera(LatLngBounds bounds) {

        android.location.Location myLocation = mMap.getMyLocation();
        CameraUpdate update = null;

        if (myLocation == null || !bounds.contains(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))) {
            CameraPosition position = new CameraPosition(bounds.getCenter(), 15, 0, 51);
            update = CameraUpdateFactory.newCameraPosition(position);
        } else {
            update = CameraUpdateFactory.newLatLngBounds(bounds, 150);
        }

        mMap.animateCamera(update, 700, null);
    }

    private void changeMapType() {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE)
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public void success(List<LocationItem> locationItems, Response response) {
        mLocations = locationItems;
        addMarkers(mLocations);
    }

    @Override
    public void failure(RetrofitError error) {
        Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
