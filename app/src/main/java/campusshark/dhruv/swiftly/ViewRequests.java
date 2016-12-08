package campusshark.dhruv.swiftly;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class ViewRequests extends AppCompatActivity {

    ArrayList<String> listViewContent;
    static ArrayList<ParseObject> requests;
    ArrayAdapter adapter;
    ListView listView;    Location currentLocation;
    double currentLatitude, currentLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);


        listViewContent = new ArrayList<>();
        requests = new ArrayList<>();
        listViewContent.add("Searching Nearby Requests");

        listView = (ListView) findViewById(R.id.listview);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewContent);
        listView.setAdapter(adapter);

        FindLocation();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ViewRequests.this,DriverSeesRequester.class);
                intent.putExtra("position",i);

                startActivity(intent);
            }
        });
    }

    public void FindLocation() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                updateLocation(location);

                final ParseGeoPoint userLocation = new ParseGeoPoint(currentLatitude,currentLongitude);
                ParseUser.getCurrentUser().put("location",userLocation);
                ParseUser.getCurrentUser().saveInBackground();

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("requests");

                query.whereDoesNotExist("driverUsername");
                query.setLimit(10);
                query.whereNear("requesterLocation",userLocation);

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e==null){
                            if (objects.size()>0){
                                listViewContent.clear();
                                requests.clear();
                                for (ParseObject object:objects){

                                    Double distanceInKm = userLocation.distanceInKilometersTo((ParseGeoPoint) object.get("requesterLocation"));
                                    Double roundedDistance = Math.round(distanceInKm*10)/10.0;

                                    listViewContent.add(String.valueOf(roundedDistance) + " Km" + "  :  "+object.getString("requesterUsername"));
                                    requests.add(object);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }


    void updateLocation(Location location) {
        currentLocation = location;
        currentLatitude = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();

    }
}