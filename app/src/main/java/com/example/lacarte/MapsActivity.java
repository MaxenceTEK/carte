package com.example.lacarte;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    List<Address> monadresse;
    List<Address> ladresse;
    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return fusedLocationProviderClient;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            markyourloc();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }




        // Add a marker in Sydney and move the camera
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                MarkerOptions markeroption = new MarkerOptions();
                markeroption.position(latLng);

                Geocoder geocoder;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                     ladresse = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String adresse = ladresse.get(0).getAddressLine(0);

                markeroption.title(adresse);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.addMarker(markeroption);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markertitle = marker.getTitle();
                Intent i = new Intent(MapsActivity.this, DetailsActivity.class);
                i.putExtra("title", markertitle);
                startActivity(i);

                return false;
            }
        });

    }
    public void markyourloc() {
        LatLng douai = new LatLng(50.370259, 3.079917);
        MarkerOptions markeroption1 = new MarkerOptions();
        markeroption1.position(douai);
        markeroption1.title("CCAS Douai");
        mMap.addMarker(markeroption1);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){




            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        LatLng moi = new LatLng(location.getLatitude(), location.getLongitude());
                        Geocoder geocoder;
                        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                        try {
                            monadresse = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String adresse = monadresse.get(0).getAddressLine(0);
                        MarkerOptions markeroption2 = new MarkerOptions();
                        markeroption2.position(moi);
                        markeroption2.title(adresse);
                        mMap.addMarker(markeroption2);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moi, 15));


                    }
                }
            });// here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }

    }




}
