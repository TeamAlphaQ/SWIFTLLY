package campusshark.dhruv.swiftly;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.uber.sdk.android.rides.RideRequestButton;

public class TravelEntry extends AppCompatActivity {

    private static final String TAG = "TravelEntry";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DROP = 2;
    public RelativeLayout pickUp;
    public RelativeLayout drop;
    public LatLng pickLatLng;
    public LatLng dropLatLng;
    public static double pickLat, pickLang;
    public static double dropLat, dropLng;
    public static String pickName, pickAddr;
    public static String dropName, dropAddr;
    public FloatingActionButton fab1;
    public TextView pickTxt;
    public TextView dropTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_entry);

        pickUp = (RelativeLayout) findViewById(R.id.pickUpRl);
        drop = (RelativeLayout) findViewById(R.id.dropRL);

        fab1 = (FloatingActionButton) findViewById(R.id.btn_goToCabAct);
        pickTxt = (TextView) findViewById(R.id.textView);
        dropTxt = (TextView) findViewById(R.id.textView1);

        fab1.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
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

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TravelEntry.this, CabActivity.class);
                startActivity(i);
            }
        });

        pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(finalIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP);
            }
        });

        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(finalIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DROP);
            }
        });


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
