package campusshark.dhruv.swiftly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

public class RegisterForTracking extends AppCompatActivity {

    EditText et_uName;
    EditText et_password;
    public static String trackingID;

    public void runQuery(final CircularProgressButton cb)
    {
        cb.setProgress(50);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Location");
        final String uName  = et_uName.getText().toString();
        final String pass = et_password.getText().toString();

        query.whereEqualTo("userName",uName);
        query.whereEqualTo("password",pass);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        trackingID=objects.get(0).getObjectId();
                        SharedPreferences trackID  =RegisterForTracking.this.getSharedPreferences("trackingID", Context.MODE_PRIVATE);
                        trackID.edit().putString("trackingID",trackingID).apply();

                        cb.setProgress(100);

//                        Intent i =new Intent();
//                        i.setClassName("campushshark.dhruv","campusshark.dhruv.swiftly.LocationService");
//                        startActivity(i);
                        Intent i = new Intent(RegisterForTracking.this,LocationService.class);
                        RegisterForTracking.this.startService(i);
                    }
                    else
                    {
                        cb.setProgress(-1);
                    }
                }
                else
                {
                    cb.setProgress(-1);
                }
            }
        });

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_tracking);

        getSupportActionBar().hide();

        et_uName = (EditText)findViewById(R.id.editText3);
        et_password = (EditText)findViewById(R.id.editText4);

        final CircularProgressButton circularProgressButton = (CircularProgressButton) findViewById(R.id.cbutton);
        ShimmerTextView shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_tv);

        final Shimmer shimmer = new Shimmer();
        shimmer.start(shimmerTextView);

        circularProgressButton.setProgress(0);
        circularProgressButton.setIndeterminateProgressMode(true);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PARSE QUERY

                circularProgressButton.setProgress(50);
                new CountDownTimer(3000,300) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        shimmer.cancel();
                        runQuery(circularProgressButton);
                    }
                }.start();
            }
        });


    }
}
