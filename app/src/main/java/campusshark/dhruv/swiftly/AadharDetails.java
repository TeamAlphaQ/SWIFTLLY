package campusshark.dhruv.swiftly;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class AadharDetails extends AppCompatActivity {

    public EditText aadhaarName;
    public EditText aadhaarNumber;
    public EditText aadhaarErollNum;

    public URL imgLink;
    public String aadhar_enroll = "";
    public String aadhar_name = "";
    public String aadhar_num = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_aadhar);

        aadhaarName = (EditText) findViewById(R.id.txt_name_aadh);
        aadhaarErollNum = (EditText) findViewById(R.id.txt_aadhar_enroll_no);
        aadhaarNumber = (EditText) findViewById(R.id.txt_num_aadh);

        Intent i = getIntent();
        String link = i.getStringExtra("url");
        //            imgLink = new URL(link);
        new OCRAsyncTask(true, "2dd3ab7b7b88957", link, "eng", AadharDetails.this).execute();

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
                for (int i = 0; i < len; i++) {
                    if (saksJ.charAt(i) == 'E' && saksJ.charAt(i + 1) == 'n' && saksJ.charAt(i + 2) == 'r') {
                        i += 13;
                        for (int j = i; saksJ.charAt(j) != '\n'; j++) {
                            aadhar_enroll += saksJ.charAt(j);
                        }
                    }

                    if (saksJ.charAt(i) == 'Y' && saksJ.charAt(i + 1) == 'o' && saksJ.charAt(i + 2) == 'u') {
                        int j = 0;
                        Log.d(TAG, "YOUR KE ANDAR");
                        for (j = i; saksJ.charAt(j) != '\n'; j++) {
                            Log.d(TAG, "your: " + saksJ.charAt(j));
                            i++;
                        }
                        for (j = i + 1; saksJ.charAt(j) != '\n'; j++) {
                            aadhar_num += saksJ.charAt(j);
                            Log.d(TAG, "num: " + aadhar_num);
                        }

                        j = j + 1;
                        for (; saksJ.charAt(j) != '\n'; j++) {
                            aadhar_name += saksJ.charAt(j);
                        }
                    }
                }

                Log.d(TAG, "aadhaarDetails: " + aadhar_name + "\n" + aadhar_num + "\n" + aadhar_enroll);

                aadhaarNumber.setText(aadhar_num);
                aadhaarName.setText(aadhar_name);
                aadhaarErollNum.setText(aadhar_enroll);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "OCRresponse: " + response);
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

//{"ParsedResults":[{"TextOverlay":{"Lines":[{"Words":[{"WordText":"Enrollment","Left":148,"Top":7,"Height":14,"Width":82},{"WordText":"No","Left":235,"Top":7,"Height":15,"Width":21},{"WordText":"2019/2207300665","Left":261,"Top":7,"Height":16,"Width":142}],"MaxHeight":16,"MinTop":7},{"Words":[{"WordText":"Dhruv","Left":87,"Top":68,"Height":13,"Width":37},{"WordText":"Rat'S","Left":129,"Top":69,"Height":11,"Width":32}],"MaxHeight":13,"MinTop":68},{"Words":[{"WordText":"SIO","Left":87,"Top":86,"Height":13,"Width":26},{"WordText":"AN","Left":119,"Top":87,"Height":10,"Width":23},{"WordText":"Kumar","Left":146,"Top":87,"Height":11,"Width":42}],"MaxHeight":13,"MinTop":86},{"Words":[{"WordText":"Pitam","Left":90,"Top":120,"Height":12,"Width":36},{"WordText":"pura","Left":131,"Top":123,"Height":11,"Width":27}],"MaxHeight":12,"MinTop":120},{"Words":[{"WordText":"Shahrnar","Left":86,"Top":137,"Height":12,"Width":56},{"WordText":"Bagh","Left":146,"Top":137,"Height":15,"Width":33}],"MaxHeight":15,"MinTop":137},{"Words":[{"WordText":"Shaltma:","Left":86,"Top":154,"Height":12,"Width":55},{"WordText":"Bag'","Left":146,"Top":154,"Height":15,"Width":28},{"WordText":"1","Left":175,"Top":158,"Height":7,"Width":3},{"WordText":"West","Left":324,"Top":155,"Height":12,"Width":32}],"MaxHeight":15,"MinTop":154},{"Words":[{"WordText":"-289","Left":79,"Top":240,"Height":16,"Width":28},{"WordText":"_","Left":325,"Top":252,"Height":3,"Width":7}],"MaxHeight":16,"MinTop":240},{"Words":[{"WordText":"SH257335789DF","Left":189,"Top":307,"Height":14,"Width":124}],"MaxHeight":14,"MinTop":307},{"Words":[{"WordText":"/","Left":229,"Top":461,"Height":22,"Width":17},{"WordText":"Your","Left":254,"Top":461,"Height":19,"Width":50},{"WordText":"Aadhaar","Left":310,"Top":462,"Height":18,"Width":90},{"WordText":"No.","Left":406,"Top":464,"Height":18,"Width":36},{"WordText":":","Left":449,"Top":468,"Height":14,"Width":6}],"MaxHeight":22,"MinTop":461},{"Words":[{"WordText":"5129","Left":89,"Top":502,"Height":33,"Width":88},{"WordText":"3413","Left":191,"Top":503,"Height":32,"Width":87},{"WordText":"9302","Left":292,"Top":503,"Height":32,"Width":87}],"MaxHeight":33,"MinTop":502},{"Words":[{"WordText":"Dhruv","Left":138,"Top":705,"Height":13,"Width":38},{"WordText":"Rathi","Left":180,"Top":705,"Height":13,"Width":34}],"MaxHeight":13,"MinTop":705},{"Words":[{"WordText":"/","Left":188,"Top":733,"Height":11,"Width":5},{"WordText":"Year","Left":197,"Top":733,"Height":12,"Width":30},{"WordText":"of","Left":231,"Top":733,"Height":12,"Width":12},{"WordText":"Birth","Left":247,"Top":733,"Height":12,"Width":29},{"WordText":"1998","Left":289,"Top":733,"Height":13,"Width":31}],"MaxHeight":13,"MinTop":733},{"Words":[{"WordText":"Male","Left":175,"Top":754,"Height":12,"Width":31}],"MaxHeight":12,"MinTop":754},{"Words":[{"WordText":"5129","Left":133,"Top":860,"Height":20,"Width":53},{"WordText":"3413","Left":195,"Top":860,"Height":20,"Width":53},{"WordText":"9302","Left":256,"Top":860,"Height":20,"Width":54}],"MaxHeight":20,"MinTop":860}],"HasOverlay":true,"Message":"Total lines: 14"},"FileParseExitCode":1,"ParsedText":"Enrollment No 2019/2207300665 \r\nDhruv Rat'S \r\nSIO AN Kumar \r\nPitam pura \r\nShahrnar Bagh \r\nShaltma: Bag' 1 West \r\n-289 _ \r\nSH257335789DF \r\n/ Your Aadhaar No. : \r\n5129 3413 9302 \r\nDhruv Rathi \r\n/ Year of Birth 1998 \r\nMale \r\n5129 3413 9302 \r\n","ErrorMessage":"","ErrorDetails":""}],"OCRExitCode":1,"IsErroredOnProcessing":false,"ErrorMessage":null,"ErrorDetails":null,"ProcessingTimeInMilliseconds":"671"}