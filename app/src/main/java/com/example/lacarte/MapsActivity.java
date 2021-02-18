package com.example.lacarte;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.lacarte.R.id.drawerlayout2;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    List<Address> monadresse;
    List<Address> ladresse;
    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    SearchView searchView;
    SupportMapFragment mapFragment;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);



        drawerLayout = findViewById(R.id.drawer_layout);

        searchView = findViewById(R.id.recherche);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String rechadresse = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (rechadresse != null || !rechadresse.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(rechadresse, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(rechadresse));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }
    public FusedLocationProviderClient getFusedLocationProviderClient () {
        return fusedLocationProviderClient;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            markyourloc();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }




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




    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer((GravityCompat.START));
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        closeDrawer(drawerLayout);
    }

    public void Clickmenu(View view) {
        openDrawer(drawerLayout);
    }
}
