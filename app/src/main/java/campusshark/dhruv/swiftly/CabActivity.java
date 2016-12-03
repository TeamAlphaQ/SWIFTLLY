package campusshark.dhruv.swiftly;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;

import java.util.Arrays;

public class CabActivity extends AppCompatActivity {

    public RideRequestButton rideRequestButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab);

        AutoCompleteTextView autocompleteView = (AutoCompleteTextView) findViewById(R.id.pickUpLocation);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));


        SessionConfiguration config = new SessionConfiguration.Builder().
                setClientId("iDzPiy2blrRE1_6WOChWOkWJB4xH8mnD")
                .setServerToken("IzXB2Dt1dRiiFMbAS_GpaZIPZQf5qSpl60VtonRQ")
                .setRedirectUri("https://login.uber.com/oauth/v2/authorize")
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();

        UberSdk.initialize(config);

        rideRequestButton = (RideRequestButton) findViewById(R.id.btn_uber_ride);
        Activity activity = this;
        int requestCode = 1234;
        rideRequestButton.setRequestBehavior(new RideRequestActivityBehavior(activity, requestCode));

        RideParameters rideParameters = new RideParameters.Builder()
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")          // product_id ka jhanjhat ho sakta hai
                .setPickupLocation(28.750319, 77.117676, "", "")
                .setDropoffLocation(28.717077, 77.142753, "", "")
                .build();

        rideRequestButton.setRideParameters(rideParameters);


    }
}
