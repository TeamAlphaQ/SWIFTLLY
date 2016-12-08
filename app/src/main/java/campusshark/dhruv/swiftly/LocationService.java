package campusshark.dhruv.swiftly;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    //    protected LocationManager locationManager;
    public static double currentLat;
    public static double currentLong;
    ParseObject parseObjLocation;
    ParseGeoPoint point;
    final String trackingID = RegisterForTracking.trackingID;

    public LocationService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreateService");
        Log.d(TAG, "trackingID: " + trackingID);
        parseObjLocation = new ParseObject("location");
        point = new ParseGeoPoint(currentLat, currentLong);

        FindLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void FindLocation() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    void updateLocation(Location location) {
        currentLat = location.getLatitude();
        currentLong = location.getLongitude();
        point = new ParseGeoPoint(currentLat, currentLong);
//        Log.d(TAG, "location: " + currentLat + "  " + currentLong);

        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "beforeWhile");
                while (true) {
                    try {
                        Log.d(TAG, "firstRun");
                        Thread.sleep(60000);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "secondRun");
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
                                query.getInBackground(trackingID, new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {
                                            object.put("updatedLocation", point);
                                            object.saveInBackground();
                                        }
                                    }
                                });
                                Log.d(TAG, "locationInsideThread: " + currentLat + "\n" + currentLong);
                                parseObjLocation.put("location", point);
                                parseObjLocation.saveInBackground();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }
}
