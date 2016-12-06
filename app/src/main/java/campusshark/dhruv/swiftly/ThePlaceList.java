package campusshark.dhruv.swiftly;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.api.client.json.JsonString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ThePlaceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final String TAG = "ThePlaceList";
    public String jsonStr;
    public static String placeId[] = new String[100];
    public int currPlaceID = 0;
    public int index = 0;
    public String jsonPlaceDetails[] = new String[100];
    public JSONArray result = new JSONArray();
    public static String addressMKC[] = new String[100];
    public static String nameMKC[] = new String[100];
    public static double latMKC[] = new double[100];
    public static double longMKC[] = new double[100];
    public static String iconMKC[] = new String[100];
    public static String urlMKC[] = new String[100];
    public static boolean isOpenMKC[] = new boolean[100];
    ArrayList finalPlaceDetails = new ArrayList<CardObjectTypeSelected>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_place_list);
        new getPlaceId().execute();
        recyclerView = (RecyclerView) findViewById(R.id.place_list_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new rvAdapterType(getDataSet());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((rvAdapterType) adapter).setOnItemClickListener(new rvAdapterType.MyClickListener1() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d(TAG, "Clicked on Item " + position);
            }
        });
    }

    private ArrayList<CardObjectTypeSelected> getDataSet() {   // onPostExe mn bhi ye krna hai doabara

        ArrayList results = new ArrayList<CardObjectTypeSelected>();
        for (int i = 0; i < 10; i++) {
            CardObjectTypeSelected obj = new CardObjectTypeSelected("", "", false, "");
            results.add(i, obj);
        }
        return results;
    }

    private class getPlaceId extends AsyncTask<Void, Void, Void> {
        HttpHandler sh = new HttpHandler();
        HttpHandler sh1 = new HttpHandler();
        String reqUrl;
        JSONObject jsonObject;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ThePlaceList.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            reqUrl = NearbyPlaces.url;
            Log.d(TAG, "reqURL: " + reqUrl);
            jsonStr = sh.makeServiceCall(reqUrl);
            Log.d(TAG, "jsonConcept" + jsonStr);

            try {
                jsonObject = new JSONObject(jsonStr);
                result = jsonObject.getJSONArray("results");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject temp = result.getJSONObject(i);
//                    Log.d(TAG, "placeId: " + temp.get("place_id"));
                    placeId[i] = (String) temp.get("place_id");
                    Log.d(TAG, "placeId: " + placeId[i]);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            Log.d(TAG, "result.len: " + result.length());
            for (int i = 0; i < 10; i++) {
                Log.d(TAG, "placeIdMKC: " + placeId[i]);
                new getPlaceDetails().execute();
//                currPlaceID++;
            }
            super.onPostExecute(aVoid);
        }
    }

    private class getPlaceDetails extends AsyncTask<Void, Void, Void> {
        HttpHandler sh = new HttpHandler();
        HttpHandler sh1 = new HttpHandler();
        String reqUrl;
        String reqOlaUrl;
        JSONObject jsonObject;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ThePlaceList.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            reqUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId[currPlaceID] + "&key=" + NearbyPlaces.APIKEY;
//            Log.d(TAG, "reqURLForEachID: " + reqUrl);

            Log.d(TAG, "currPlaceIDMKC:: " + currPlaceID);
            jsonPlaceDetails[currPlaceID] = sh.makeServiceCall(reqUrl);
            currPlaceID++;
//            Log.d(TAG, "Places:: " + jsonPlaceDetails[currPlaceID]);

//            try {
//                jsonObject = new JSONObject(jsonStr);
//                JSONArray result = jsonObject.getJSONArray("results");
//                for (int i = 0; i < result.length(); i++) {
//                    JSONObject temp = result.getJSONObject(i);
//                    placeId[i] = (String) temp.get("place_id");
////                    Log.d(TAG, "placeId: " + a);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            return null;
        }

        protected void fillArrays(String jsonPlace, int i) {
//            for (int i = 0; i < 10; i++)
            {
                Log.d(TAG, "jsonPlace: " + jsonPlace);
                if(jsonPlace!=null) {
                    try {
                        JSONObject jsonObjectMaKiChu = new JSONObject(jsonPlace);
                        Log.d(TAG, "jsonMKC: " + jsonObjectMaKiChu);
                        JSONObject resultMKC = jsonObjectMaKiChu.getJSONObject("result");
                        Log.d(TAG, "resultMKC: " + resultMKC);
                        addressMKC[i] = resultMKC.getString("formatted_address");
                        iconMKC[i] = resultMKC.getString("icon");
                        nameMKC[i] = resultMKC.getString("name");
                        urlMKC[i] = resultMKC.getString("url");

                        JSONObject geom = resultMKC.getJSONObject("geometry");
                        JSONObject location = geom.getJSONObject("location");
                        latMKC[i] = location.getDouble("lat");
                        longMKC[i] = location.getDouble("lng");

                        JSONObject openHrs = resultMKC.getJSONObject("opening_hours");
                        isOpenMKC[i] = openHrs.getBoolean("open_now");
                    } catch (JSONException e) {
                        Log.d(TAG, "CatchException" + e);
                        e.printStackTrace();
                    }

                    Log.d(TAG, "eachPlaceDetails:: " + nameMKC[i] + "\n" + urlMKC[i] + "\n" + addressMKC[i] + "\n" + isOpenMKC[i] + "\n" + latMKC[i] + "\n" + longMKC[i] + "\n" + iconMKC[i]);
                    CardObjectTypeSelected obj = new CardObjectTypeSelected(nameMKC[i], addressMKC[i], isOpenMKC[i], iconMKC[i]);
                    Log.d(TAG, "index:: " + index);
                    Log.d(TAG, "indexSize:: " + finalPlaceDetails.size());
                    finalPlaceDetails.add(index, obj);
                    index++;
                }
            }

//            for (int i = 0; i < 10; i++)
            {
//                Log.d(TAG, "eachPlaceDetails:: " + nameMKC[i] + "\n" + urlMKC[i] + "\n" + addressMKC[i] + "\n" + isOpenMKC[i] + "\n" + latMKC[i] + "\n" + longMKC[i] + "\n" + iconMKC[i]);
//                CardObjectTypeSelected obj = new CardObjectTypeSelected(nameMKC[i], addressMKC[i], isOpenMKC[i], iconMKC[i]);
//                Log.d(TAG, "index:: " + index);
//                Log.d(TAG, "indexSize:: " + finalPlaceDetails.size());
//                finalPlaceDetails.add(index, obj);
//                index++;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Log.d(TAG, "length: " + result.length());
//            for(int i=0;i<placeId.length;i++)
//            {
//            Log.d(TAG, "Places:: " + jsonPlaceDetails[0]);
//            }

            for (int i = 0; i < jsonPlaceDetails.length; i++) {
                Log.d(TAG, "jsonPlaceDetails: " + jsonPlaceDetails[i]);
                fillArrays(jsonPlaceDetails[i], i);
            }

//            index=0;
            adapter = new rvAdapterType(finalPlaceDetails);
            recyclerView.setAdapter(adapter);
//            finalPlaceDetails.clear();

//            String jsonPPP;
//            for (int i = 0; i < 10; i++) {
//                try {
//                    JSONObject jsonObjectMaKiChu = new JSONObject(jsonPlaceDetails[i]);
////                    Log.d(TAG, "jsonMKC: " + jsonObjectMaKiChu);
//                    JSONObject resultMKC = jsonObjectMaKiChu.getJSONObject("result");
////                    Log.d(TAG, "resultMKC: " + resultMKC);
//                    addressMKC[i] = resultMKC.getString("formatted_address");
//                    iconMKC[i] = resultMKC.getString("icon");
//                    nameMKC[i] = resultMKC.getString("name");
//                    urlMKC[i] = resultMKC.getString("url");
//
//                    JSONObject geom = resultMKC.getJSONObject("geometry");
//                    JSONObject location = geom.getJSONObject("location");
//                    latMKC[i] = location.getDouble("lat");
//                    longMKC[i] = location.getDouble("lng");
//
//                    JSONObject openHrs = resultMKC.getJSONObject("opening_hours");
//                    isOpenMKC[i] = openHrs.getBoolean("open_now");
//
//                } catch (JSONException e) {
//                    Log.d(TAG, "CatchException" + e);
//                    e.printStackTrace();
//                }
//            }
//
//            ArrayList<CardObjectTypeSelected> finalPlaceDetails = new ArrayList<CardObjectTypeSelected>(100);
//            for (int i = 0; i < 10; i++) {
//                Log.d(TAG, "eachPlaceDetails:: " + nameMKC[i] + "\n" + urlMKC[i] + "\n" + addressMKC[i] + "\n" + isOpenMKC[i] + "\n" + latMKC[i] + "\n" + longMKC[i] + "\n" + iconMKC[i]);
//                CardObjectTypeSelected obj = new CardObjectTypeSelected(nameMKC[i], addressMKC[i], isOpenMKC[i], iconMKC[i]);
//                finalPlaceDetails.add(i, obj);
//            }
//
//            adapter = new rvAdapterType(finalPlaceDetails);
//            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}