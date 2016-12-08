package campusshark.dhruv.swiftly;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class NearbyPlaces extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String TAG = "NearbyPlaces";
    public static String url;
    //    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public static double currentLat;
    public static double currentLong;
    public static final String APIKEY = "AIzaSyBXcP8MfI9I0juIW2xg7Dmtx63vh8WOTFA";
    public String temp = null;

//    public Map<String,Integer> imageId;
//    public Map<String,String> sourceId;

    String sourceId[] = new String[]{
            "ATM",
            "Bank",
            "Bus Station",
            "Cafe",
            "Gym",
            "Hospital",
            "Library",
            "Liquor Store",
            "Government Office",
            "Pharmacy",
            "Police",
            "Post Office",
            "Restaurant",
            "School"
    };

    Integer imageId[] = new Integer[]{
            R.drawable.atm,
            R.drawable.bank,
            R.drawable.bus,
            R.drawable.cafe,
            R.drawable.gym,
            R.drawable.hosital,
            R.drawable.library,
            R.drawable.liquor,
            R.drawable.govn,
            R.drawable.pharmacy,
            R.drawable.police,
            R.drawable.postoffice,
            R.drawable.restraunt,
            R.drawable.school
    };

    String apiKeyword[] = new String[]{
            "atm",
            "bank",
            "bus_station",
            "cafe",
            "gym",
            "hospital",
            "library",
            "liquor_store",
            "local_government_office",
            "pharmacy",
            "police",
            "post_office",
            "restaurant",
            "school"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        FindLocation();

        url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + currentLat + "," + currentLong + "&rankby=distance&type=" + temp + "&keyword=" + temp + "&key=" + APIKEY;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                temp = apiKeyword[position];
                Intent i = new Intent(NearbyPlaces.this, ThePlaceList.class);
                startActivity(i);
                Log.i(TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<CardObject> getDataSet() {
        ArrayList results = new ArrayList<CardObject>();
        for (int index = 0; index < 14; index++) {
            CardObject obj = new CardObject(imageId[index], sourceId[index]); // make a map of images and the service and provide that here
//            CardObject obj = new CardObject(R.drawable.uberlogo,"UBER");
            Log.d(TAG, "index:: " + index);
            results.add(index, obj);
        }
        return results;
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
        url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + currentLat + "," + currentLong + "&rankby=distance&type=" + temp + "&keyword=" + temp + "&key=" + APIKEY;
        Log.d(TAG, "location: " + currentLat + "  " + currentLong);
    }
}