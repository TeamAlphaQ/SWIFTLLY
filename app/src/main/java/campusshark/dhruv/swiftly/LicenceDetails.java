package campusshark.dhruv.swiftly;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class LicenceDetails extends AppCompatActivity {

    public String licenceName = "";
    public String licenceNo = "";
    public String licenceDOB = "";
    public String licenceBloodGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence_details);

        Intent i = getIntent();
        String link = i.getStringExtra("url");

        new OCRAsyncTask(true, "2dd3ab7b7b88957", link, "eng", LicenceDetails.this).execute();
    }


    public class OCRAsyncTask extends AsyncTask {
        public static final String TAG = "OCRAsync";
        private String APIKey;
        String url = "https://api.ocr.space/parse/image";
        private String mImgLink;
        private String mLang;
        private boolean isOverlayRequired = false;
        private Activity mAct;
        private ProgressDialog progressDialog;

        public OCRAsyncTask(boolean isOverlayRequired, String APIKey, String mImgLink, String mLang, Activity mAct) {
            this.isOverlayRequired = isOverlayRequired;
            this.APIKey = APIKey;
            this.mImgLink = mImgLink;
            this.mLang = mLang;
            this.mAct = mAct;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mAct);
            progressDialog.setTitle("Extracting Document Details Using OCR");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                return sendPost(APIKey, isOverlayRequired, mImgLink, mLang);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String sendPost(String APIKey, boolean isOverlayRequired, String mImgLink, String mLang) throws IOException, JSONException {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en,q=0.5");

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("apikey", APIKey);
            postDataParams.put("isOverlayRequired", isOverlayRequired);
            postDataParams.put("url", mImgLink);
            postDataParams.put("language", mLang);

            con.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(getPostDataString(postDataParams));
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return String.valueOf(response);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            String response = (String) result;
            Log.d(TAG, "responseLic: " + response);
            try {
                JSONObject object = new JSONObject(response);
                Log.d(TAG, "parsedObject: " + object);
                JSONArray object1 = object.getJSONArray("ParsedResults");
                Log.d(TAG, "parsedObj1: " + object1);
                JSONObject object2 = object1.getJSONObject(0);
                Log.d(TAG, "parsedObj2: " + object2);
                String saksJ = object2.getString("ParsedText");
                Log.d(TAG, "parsedText: " + saksJ);

                int len = saksJ.length();
                Log.d(TAG, "detailsLen: " + len);

                int countEndLine = 0;
                for (int i = 0; i < len; i++) {
                    if (saksJ.charAt(i) == '\n') {
                        countEndLine++;
                        Log.d(TAG,"countLine: "+countEndLine);
                    }

                    if (countEndLine == 6) {
                        int j;
                        for (j = i; saksJ.charAt(j) != '\n'; j++) {
                            licenceNo += saksJ.charAt(j);
                            Log.d(TAG,"licenceNo: "+licenceNo);
                        }

                        countEndLine++;
                        i = j + 1;
                    }

                    if (countEndLine == 7) {
                        int j;
                        for (j = i; saksJ.charAt(j) != '\n'; j++) {
                            licenceName += saksJ.charAt(j);
                        }
                        countEndLine++;
                        i = j + 1;
                    }

                    if (countEndLine == 9) {
                        int j;
                        for (j = i; saksJ.charAt(j) == '\n'; j++) {
                            if (saksJ.charAt(j) == '1' || saksJ.charAt(j) == '2' || saksJ.charAt(j) == '3' || saksJ.charAt(j) == '4' || saksJ.charAt(j) == '5' || saksJ.charAt(j) == '6' || saksJ.charAt(j) == '7' || saksJ.charAt(j) == '8' || saksJ.charAt(j) == '9' || saksJ.charAt(j) == '0' || saksJ.charAt(j) == '/') {
                                licenceDOB += saksJ.charAt(j);
                            }
                        }

                        i = j + 1;
                    }
                }

                Log.d(TAG, "licenceDetails: " + licenceName + "\n" + licenceDOB + "\n" + licenceNo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getPostDataString(JSONObject params) throws JSONException, UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> itr = params.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                Object value = params.get(key);

                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
    }
}