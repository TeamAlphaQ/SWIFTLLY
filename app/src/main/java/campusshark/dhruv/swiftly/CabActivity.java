package campusshark.dhruv.swiftly;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CabActivity extends AppCompatActivity {

    private static final String TAG = "CabActivity";
    public RideRequestButton rideRequestButton;
    public TextView pickTxt;
    public TextView dropTxt;

    public TextView tvUberDis;
    public TextView tvUbertime;
    public TextView tvUberCost;

    public RelativeLayout pickUp;
    public RelativeLayout drop;
    public String jsonUber;
    public String jsonOla;

    public double uberDistance;
    public int uberLowEstimate;
    public int uberhighEstimate;
    public float uberDuration;

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

        tvUberCost = (TextView) findViewById(R.id.txt_uber_cost);
        tvUberDis = (TextView) findViewById(R.id.txt_uber_dis);
        tvUbertime = (TextView) findViewById(R.id.txt_uber_time);

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
        new CabActivity.getUberConcept().execute();
        request();
//        OLA();
        Log.d(TAG, "rideParams:: " + TravelEntry.pickLang + "    " + TravelEntry.pickLat + "   " + TravelEntry.pickName + "   " + TravelEntry.pickAddr);
    }

    public void OLA()
    {
//        String url = "https://devapi.olacabs.com/v1/products?pickup_lat=12.9491416&pickup_lng=77.64298&drop_lat=12.96&drop_lng=77.678&category=prime";
        String url = "http://sandbox-t.olacabs.com/v1/products?pickup_lat=12.950072&pickup_lng=77.642684";
        try {
            URL urlString = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) urlString.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("CONTENT_TYPE", "application/json");
            conn.setRequestProperty("X-APP-TOKEN", "07cb4d02434e41bebc1fc8b3bd3c3121");
            conn.setRequestMethod("GET");
            conn.connect();
            int httpStatus = conn.getResponseCode();
            Log.d(TAG, "httpStatus " + httpStatus);
        } catch (Exception e) {
            Log.d(TAG,"errorInOlaFunction");
            e.printStackTrace();
        }
    }


    public void request() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://IND.ola.gemius.com/api/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         Log.d(TAG,"volleyError: "+error);
                    }
                }
        )


        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("country_code","hu");
                map.put("email","dhruvrathi15@gmail.com");
                map.put("password","dhruv2601");
//                map.put("apikey",apiKey);
//                map.put("url",url);
                return map;
            }
        };

        Volley.newRequestQueue(CabActivity.this).add(request);
    }



    private class getUberConcept extends AsyncTask<Void, Void, Void> {
        HttpHandler sh = new HttpHandler();
        HttpHandler sh1 = new HttpHandler();
        String reqUrl;
        String reqOlaUrl;
        JSONObject jsonObject;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CabActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Estimating Ride Cost and Time...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            reqUrl = "https://api.uber.com/v1/estimates/price?start_latitude=" + TravelEntry.pickLat + "&start_longitude=" + TravelEntry.pickLang + "&end_latitude=" + TravelEntry.dropLat + "&end_longitude=" + TravelEntry.dropLng + "&server_token=IzXB2Dt1dRiiFMbAS_GpaZIPZQf5qSpl60VtonRQ";
            reqOlaUrl = "https://devapi.olacabs.com/v1/products?pickup_lat=12.9491416&pickup_lng=77.64298&category=mini";
            jsonUber = sh.makeServiceCall(reqUrl);
//            jsonOla = sh.makeServiceCall(reqOlaUrl);
            Log.d(TAG, "jsonConcept" + jsonUber);
//            Log.d(TAG,"jsonOLAAAAConcept: "+jsonOla);

            try {
                jsonObject = new JSONObject(jsonUber);
                JSONArray price = jsonObject.getJSONArray("prices");
                JSONObject uberGo = price.getJSONObject(1);
                Log.d(TAG, "UberGo: " + uberGo);
                uberDistance = uberGo.getDouble("distance");
                uberhighEstimate = uberGo.getInt("high_estimate");
                uberLowEstimate = uberGo.getInt("low_estimate");
//                uberEstimate = (high+low)/2;
                int duration = uberGo.getInt("duration");
                uberDuration = (float) duration / 60;
//                Log.d(TAG, "dis,est,dur: " + uberDistance + " " + uberEstimate + " " + uberDuration);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            tvUberDis.setText(uberDistance + " Km.");
            tvUbertime.setText((int) uberDuration + " min.");
            tvUberCost.setText("Rs. " + uberLowEstimate + " - " + uberhighEstimate);
            super.onPostExecute(aVoid);
        }
    }
}

