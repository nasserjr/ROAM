package com.parse.starter;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static com.parse.starter.Home.badge;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {




    MapView mMapView;
    GoogleMap googleMap;
    LocationManager locationManager;
    ArrayList<Event> eventarray = new ArrayList<>();
    static LatLng currentlocation;
    private HashMap<Marker, Event> mHashMap = new HashMap<Marker,Event>();
     int x;
    Marker marker;
    private View rootView;





    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        eventarray.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("state","notyet");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    {

                        if (objects.size() > 0) {

                            for (ParseObject events : objects) {

                                Event event = new Event();
                                event.setId(events.getObjectId());
                                event.setInterest(events.getString("intereset"));
                                event.setName(events.getString("eventname"));
                                event.setCreatedby(events.getString("createdby"));
                                event.setOthermanagers(events.<String>getList("othermanagers"));

                                /*



                                */
                                ParseGeoPoint point = events.getParseGeoPoint("location");
                                Double lat = point.getLatitude();
                                Double log = point.getLongitude();
                                LatLng eventlocation = new LatLng(lat, log);
                                event.setLocation(eventlocation);
                                eventarray.add(event);

                            }
                        }
                    }
                }
                else{
                    //e.printStackTrace();
                }
            }
        });


        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        //check if network provider is enabeled

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get the latitude and longitude from the location
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    currentlocation = new LatLng(latitude, longitude);
                    updateMap(currentlocation);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get the latitude and longitude from the location
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng currentlocation = new LatLng(latitude, longitude);
                    updateMap(currentlocation);

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }



        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately



        Button listview = (Button) rootView.findViewById(R.id.listview);
        listview.setOnClickListener(this);
        Button go = (Button) rootView.findViewById(R.id.gobutton);
        go.setOnClickListener(this);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;
                x=0;
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        // TODO Auto-generated method stub
                        Event event;
                        event= mHashMap.get(marker);
                        if(event!=null) {

                            if (event.getOthermanagers().contains(ParseUser.getCurrentUser().getUsername())) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                intent.putExtra("eventId", event.getId());
                                intent.putExtra("eventname", event.getName());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                intent.putExtra("eventId", event.getId());
                                intent.putExtra("eventname", event.getName());
                                startActivity(intent);
                            }
                        }



                    }
                });


              /*
                GPStracker gt = new GPStracker(getActivity().getApplicationContext());
                Location l = gt.getLocation();
                if( l == null){
                    Toast.makeText(getActivity().getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
                }else {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    LatLng userLocation = new LatLng(lat, lon);
                    googleMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mappointer)));
                     googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                }
                */
            }



        });



        return rootView;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void updateMap(LatLng userLocation) {

        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mappointer)));
      if(x==0) {
          googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
      }
      x=1;

      mHashMap.clear();
      for(int counter = 0; counter < eventarray.size(); counter++) {

          marker = googleMap.addMarker(new MarkerOptions().position(eventarray.get(counter).getLocation()).title(eventarray.get(counter).getName()).snippet(eventarray.get(counter).getInterest()).icon(BitmapDescriptorFactory.fromResource(R.drawable.flagpointer)));
          marker.showInfoWindow();
          mHashMap.put(marker, eventarray.get(counter));

      }

    }



    @Override
    public void onClick(View v) {
        View parent = (View)v.getParent();

        if(v.getId()==R.id.listview){
            listfragment fragment3 = new listfragment();
            FragmentTransaction fragmentTransaction3 = getFragmentManager().beginTransaction();
            fragmentTransaction3.replace(R.id.content, fragment3);
            fragmentTransaction3.commit();
        }
        else if(v.getId()==R.id.gobutton){
            EditText location_tf = (EditText)parent.findViewById(R.id.go);
            String location = location_tf.getText().toString();
            List<Address> addressList = null;
            if(!location.equals(""))
            {
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocationName(location , 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addressList.isEmpty()){
                    Toast.makeText(getActivity(), "Can not find location", Toast.LENGTH_SHORT).show();
                }
                else {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }


            }
    }
}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null; // now cleaning up!
    }

}
