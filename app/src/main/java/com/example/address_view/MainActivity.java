package com.example.address_view;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
public class MainActivity extends AppCompatActivity implements LocationListener {

    Button button;
    List<Address> address = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        button = findViewById( R.id.address );
        progressBar = findViewById( R.id.progress_circular );
        button.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                address.clear();
                button.setText( "" );
                progressBar.setVisibility( View.VISIBLE );
                getLocation();
            }
        } );
        if (ContextCompat.checkSelfPermission( getApplicationContext(),
                                               android.Manifest.permission.ACCESS_FINE_LOCATION )
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission( getApplicationContext(),
                                                   android.Manifest.permission.ACCESS_COARSE_LOCATION )
               != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 101 );

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            Objects.requireNonNull( locationManager ).requestLocationUpdates
                    ( LocationManager.NETWORK_PROVIDER, 5000, 5, this );

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder( this, Locale.getDefault() );
        try {
            address = geocoder.getFromLocation( location.getLatitude(), location.getLongitude(), 1 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        String traced_actual_location = address.get( 0 ).getAddressLine( 0 );
        String traced_actual_latitude = String.valueOf( location.getLatitude() );
        final String traced_actual_longitude = String.valueOf( location.getLongitude() );
        final String locate = traced_actual_latitude + "° N," + " " + traced_actual_longitude + "° E";
        final String locatio = traced_actual_location + "\n" + locate;
        button.setText( "" );
        button.setText( locatio );
        progressBar.setVisibility( View.INVISIBLE );
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText( getApplicationContext()
                , "Location and Internet connection should be enabled to view location"
                , Toast.LENGTH_SHORT ).show();
    }
}
