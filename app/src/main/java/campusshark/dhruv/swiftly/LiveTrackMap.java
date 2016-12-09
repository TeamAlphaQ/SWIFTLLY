package campusshark.dhruv.swiftly;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LiveTrackMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_track_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        ParseGeoPoint bravoLoc = WhoYouWantToTrack.bravoLoc;

        // Add a marker in Sydney and move the camera
        final LatLng[] bravo = {new LatLng(bravoLoc.getLatitude(), bravoLoc.getLongitude())};
        mMap.addMarker(new MarkerOptions().position(bravo[0]).title("START"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bravo[0]));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bravo[0], 14.0f));
//        Circle circle = mMap.addCircle(new CircleOptions().center(bravo).radius(50).fillColor(Color.CYAN));


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
        query.getInBackground(WhoYouWantToTrack.toBeTrackedID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseGeoPoint currPoint = object.getParseGeoPoint("updatedLocation");
                    LatLng currLatLng = new LatLng(currPoint.getLatitude(), currPoint.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 14.0f));
//                    mMap.addMarker(new MarkerOptions().position(currLatLng).title(object.getString("userName")+" " + (++count)));   // place a time stamp in the title
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
////                                            Circle circle = mMap.addCircle(new CircleOptions().center(currLatLng).radius(1000).fillColor(Color.CYAN));
//
//                    Polyline line = mMap.addPolyline(new PolylineOptions().add(currLatLng, bravo).width(5).color(Color.RED));
                }
            }
        });


        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);     // change it later (current = 1 min)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
                                query.getInBackground(WhoYouWantToTrack.toBeTrackedID, new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {
                                            ParseGeoPoint currPoint = object.getParseGeoPoint("updatedLocation");
                                            LatLng currLatLng = new LatLng(currPoint.getLatitude(), currPoint.getLongitude());
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 14.0f));
                                            mMap.addMarker(new MarkerOptions().position(currLatLng).title(object.getString("userName") + " " + (++count)));   // place a time stamp in the title
                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
//                                            Circle circle = mMap.addCircle(new CircleOptions().center(currLatLng).radius(1000).fillColor(Color.CYAN));

                                            Polyline line = mMap.addPolyline(new PolylineOptions().add(currLatLng, bravo[0]).width(5).color(Color.RED));
                                            bravo[0] = currLatLng;
                                        }
                                    }
                                });
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
