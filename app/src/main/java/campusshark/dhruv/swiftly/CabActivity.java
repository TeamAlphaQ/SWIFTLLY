package campusshark.dhruv.swiftly;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;
import com.uber.sdk.rides.client.model.PriceEstimate;

import java.util.Arrays;

public class CabActivity extends AppCompatActivity {

    private static final String TAG = "CabActivity";
    public RideRequestButton rideRequestButton;
    public TextView pickTxt;
    public TextView dropTxt;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DROP = 2;
    public RelativeLayout pickUp;
    public RelativeLayout drop;
    public LatLng pickLatLng;
    public LatLng dropLatLng;
    public double pickLat, pickLang;
    public double dropLat, dropLng;
    String pickName, pickAddr;
    String dropName, dropAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab);

        pickTxt = (TextView) findViewById(R.id.textView);
        dropTxt = (TextView) findViewById(R.id.textView1);

        pickTxt.setText(TravelEntry.pickAddr);
        dropTxt.setText(TravelEntry.dropAddr);

        pickUp = (RelativeLayout) findViewById(R.id.pickUpRl);
        drop = (RelativeLayout) findViewById(R.id.dropRL);
        pickTxt = (TextView) findViewById(R.id.textView);
        dropTxt = (TextView) findViewById(R.id.textView1);

//        Intent intent = null;
//        try {
//            intent =
//                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                            .build(CabActivity.this);
//        } catch (GooglePlayServicesRepairableException e) {
//            // TODO: Handle the error.
//        } catch (GooglePlayServicesNotAvailableException e) {
//            // TODO: Handle the error.
//        }
//
//        final Intent finalIntent = intent;

        pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CabActivity.this, TravelEntry.class);
                startActivity(i);
            }
        });

        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CabActivity.this, TravelEntry.class);
                startActivity(i);
            }
        });
        rideRequestButton = (RideRequestButton) findViewById(R.id.btn_uber_ride);

        SessionConfiguration config = new SessionConfiguration.Builder().
                setClientId("iDzPiy2blrRE1_6WOChWOkWJB4xH8mnD")
                .setServerToken("IzXB2Dt1dRiiFMbAS_GpaZIPZQf5qSpl60VtonRQ")
                .setRedirectUri("https://login.uber.com/oauth/v2/authorize")
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();
        UberSdk.initialize(config);

        Activity activity = this;
        int requestCode = 1234;
        rideRequestButton.setRequestBehavior(new RideRequestActivityBehavior(activity, requestCode));

        RideParameters rideParameters = new RideParameters.Builder()
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")          // product_id ka jhanjhat ho sakta hai
                .setPickupLocation(TravelEntry.pickLat, TravelEntry.pickLang, TravelEntry.pickName, TravelEntry.pickAddr)
                .setDropoffLocation(TravelEntry.dropLat, TravelEntry.dropLng, TravelEntry.dropName, TravelEntry.dropAddr)
                .build();

        rideRequestButton.setRideParameters(rideParameters);
        ServerTokenSession session = new ServerTokenSession(config);
        rideRequestButton.setSession(session);
        rideRequestButton.loadRideInformation();
        final RideRequestButtonCallback callback = new RideRequestButtonCallback() {
//            https://api.uber.com/v1/estimates/price?start_latitude=37.625732&start_longitude=-122.377807&end_latitude=37.785114&end_longitude=-122.406677&server_token=IzXB2Dt1dRiiFMbAS_GpaZIPZQf5qSpl60VtonRQ
            @Override
            public void onRideInformationLoaded() {
                String priceEstimate = new PriceEstimate().getEstimate();
                Log.d(TAG,"estimates:::; "+priceEstimate);
                // react to the displayed estimates
            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
            }

            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
            }
        };
        rideRequestButton.setCallback(callback);


//        rideRequestButton.
        Log.d(TAG, "rideParams:: " + TravelEntry.pickLang + "    " + TravelEntry.pickLat + "   " + TravelEntry.pickName + "   " + TravelEntry.pickAddr);
    }
}
