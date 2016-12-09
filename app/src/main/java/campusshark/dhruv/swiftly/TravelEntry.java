package campusshark.dhruv.swiftly;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.uber.sdk.android.rides.RideRequestButton;

import org.apache.log4j.chainsaw.Main;

public class TravelEntry extends AppCompatActivity {

    private static final String TAG = "TravelEntry";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DROP = 2;
//    public RelativeLayout pickUp;
//    public RelativeLayout drop;
    public LatLng pickLatLng;
    public LatLng dropLatLng;
    public static double pickLat, pickLang;
    public static double dropLat, dropLng;
    public static String pickName, pickAddr;
    public static String dropName, dropAddr;
//    public FloatingActionButton fab1;
    public EditText pickTxt;
    public EditText dropTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_entry);

        getSupportActionBar().hide();
        final CircularProgressButton circularProgressButton = (CircularProgressButton) findViewById(R.id.btn_goToCabAct);

        ShimmerTextView shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_tv);

        final Shimmer shimmer = new Shimmer();
        shimmer.start(shimmerTextView);

        circularProgressButton.setProgress(0);
        circularProgressButton.setIndeterminateProgressMode(true);


//        pickUp = (RelativeLayout) findViewById(R.id.pickUpRl);
//        drop = (RelativeLayout) findViewById(R.id.dropRL);

//        fab1 = (FloatingActionButton) findViewById(R.id.btn_goToCabAct);
        pickTxt = (EditText) findViewById(R.id.textView);
        dropTxt = (EditText) findViewById(R.id.textView1);

//        fab1.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
        Intent intent = null;
        try {
            intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(TravelEntry.this);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

        final Intent finalIntent = intent;

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PARSE QUERY

                circularProgressButton.setProgress(50);
                new CountDownTimer(3000, 300) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        shimmer.cancel();
                        Intent i = new Intent(TravelEntry.this, CabActivity.class);
                        startActivity(i);
                    }
                }.start();
            }
        });


       pickTxt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivityForResult(finalIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP);
           }
       });

       dropTxt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivityForResult(finalIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DROP);
           }
       });
//
//        pickUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(finalIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP);
//            }
//        });

//        drop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(finalIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DROP);
//            }
//        });

//        final com.getbase.floatingactionbutton.FloatingActionButton actionA = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_a);
//        actionA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TravelEntry.this, DigitalStorage.class);
//                startActivity(i);
//            }
//        });
//
//        final com.getbase.floatingactionbutton.FloatingActionButton actionB = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_b);
//        actionB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TravelEntry.this, NearbyPlaces.class);
//                startActivity(i);
//            }
//        });
//
//        final com.getbase.floatingactionbutton.FloatingActionButton actionC = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_c);
//        actionC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TravelEntry.this, RegisterForTracking.class);
//                startActivity(i);
//            }
//        });
//
//        final com.getbase.floatingactionbutton.FloatingActionButton actionD = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_d);
//        actionD.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TravelEntry.this, WhoYouWantToTrack.class);
//                startActivity(i);
//            }
//        });
//
//        final com.getbase.floatingactionbutton.FloatingActionButton actionE = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_e);
//        actionE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TravelEntry.this, RegisterForTracking.class);
//                startActivity(i);
//            }
//        });
//
//        final com.getbase.floatingactionbutton.FloatingActionButton actionF = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_f);
//        actionF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TravelEntry.this, CrisisActivity.class);
//                startActivity(i);
//            }
//        });
//
//        final com.getbase.floatingactionbutton.FloatingActionButton actionG = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_g);
//        actionG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TravelEntry.this, MainActivity.class);
//                startActivity(i);
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP) {
            Log.d(TAG, "pickkkk");
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                Log.d(TAG, "LatLng: " + place.getLatLng());
                pickLatLng = place.getLatLng();
                pickLat = pickLatLng.latitude;
                pickLang = pickLatLng.longitude;
                pickName = (String) place.getName();
                pickAddr = (String) place.getAddress();
                pickTxt.setText(pickAddr);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, "There was an error locating the place.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {

            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DROP) {
            Log.d(TAG, "droppppp");
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                Log.d(TAG, "LatLng: " + place.getLatLng());
                dropLatLng = place.getLatLng();
                dropLat = dropLatLng.latitude;
                dropLng = dropLatLng.longitude;
                dropName = (String) place.getName();
                dropAddr = (String) place.getAddress();
                dropTxt.setText(dropAddr);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, "There was an error locating the place.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
}
