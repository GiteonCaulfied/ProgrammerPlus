package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserActivityDAO;

public class MyLocationManager {
    /**
     * This class will manage the location permission and
     * provider of a user, and records his or her location.
     *
     * We refer to the Android Developer Document
     * https://developer.android.com/things/sdk/drivers/location
     * https://developer.android.com/training/location
     */

    public static final int LOCATION_CODE = 301;
    private final Context context;
    private LocationManager locationManager;
    private String locationProvider = null;

    public MyLocationManager(Context context) {
        this.context = context;
    }

    /**
     * Get the Location of the current user and store as a map.
     *
     * @return a map contains location data
     */
    public HashMap<String, Object> getLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
        }

        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Log.v("Location", "Network");
        }
        else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.v("Location", "GPS");
        } else {
            Toast.makeText(context, "Cannot find a location provider!", Toast.LENGTH_LONG).show();
            return null;
        }

        HashMap<String, Object> res = new HashMap<>();

        locationManager.requestLocationUpdates(locationProvider, 5000, 10, locationListener);

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            Log.v("TAG", "The latest GPS information: " + location.getLongitude() + "   " + location.getLatitude());
            res.put("Longitude", Math.round(location.getLongitude()));
            res.put("Latitude", Math.round(location.getLatitude()));

            UserActivityDAO userActivityDAO = UserActivityDAO.getInstance();
            userActivityDAO.updateLocation(res);

            List<Address> addresses = getAddress(location);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                res.put("Address", address.getAddressLine(0));
            }
            else {
                res.put("Address", "Unknown");
            }
        } else {
            return null;
        }

        return res;
    }

    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            HashMap<String, Object> res = new HashMap<>();
            res.put("Longitude", Math.round(location.getLongitude()));
            res.put("Latitude", Math.round(location.getLatitude()));

            UserActivityDAO userActivityDAO = UserActivityDAO.getInstance();
            userActivityDAO.updateLocation(res);
        }
    };
}

