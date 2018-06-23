package com.parse.starter;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapCreate extends Fragment implements OnMapReadyCallback, View.OnClickListener{

    MapView mMapView;
    GoogleMap googleMap;
    LocationManager locationManager;
    int x;


    public MapCreate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_create, container, false);


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


        Button listview = (Button) rootView.findViewById(R.id.createbutton);
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

        //googleMap.clear();
        //googleMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mappointer)));
        if(x==0) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        }
        x=1;

    }


    @Override
    public void onClick(View v) {
        View parent = (View)v.getParent();

        if(v.getId()==R.id.gobutton){
            EditText location_tf = (EditText)parent.findViewById(R.id.go);
            String location = location_tf.getText().toString();
            List<Address> addressList = null;
            if(!location.equals(""))
            {
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
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
        else if(v.getId()==R.id.createbutton){


            LatLng eventLocation = googleMap.getCameraPosition().target;
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());

            String address = "";


            try {

                List<Address> listAddresses = geocoder.getFromLocation(eventLocation.latitude, eventLocation.longitude, 1);

                if (listAddresses != null && listAddresses.size() > 0) {


                   /* if(listAddresses.get(0).getAdminArea()!=null) {
                        if (listAddresses.get(0).getSubLocality() != null) {

                            if (listAddresses.get(0).getThoroughfare() != null) {

                                if (listAddresses.get(0).getSubThoroughfare() != null) {

                                    address += listAddresses.get(0).getSubThoroughfare() + " ";

                                }

                                address += listAddresses.get(0).getThoroughfare()+ " ";

                            }

                            address += listAddresses.get(0).getSubLocality()+ " ";
                        }
                        address += listAddresses.get(0).getSubAdminArea();
                    }*/
                    if (listAddresses.get(0).getSubThoroughfare() != null) {

                        address += listAddresses.get(0).getSubThoroughfare() + " ";

                    }
                    if (listAddresses.get(0).getThoroughfare() != null) {

                        address += listAddresses.get(0).getThoroughfare() + " ";

                    }
                    if (listAddresses.get(0).getSubLocality() != null) {

                        address += listAddresses.get(0).getSubLocality() + " ";

                    }
                    if (listAddresses.get(0).getAdminArea() != null) {

                        address += listAddresses.get(0).getAdminArea() + " ";

                    }




                    //Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), CreateEvent.class);
                    intent.putExtra("eventLocation",eventLocation);
                    intent.putExtra("address",address);
                    startActivity(intent);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }




}

