package campusshark.dhruv.swiftly;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

public class WhoYouWantToTrack extends AppCompatActivity {

    private static final String TAG = "WhoYouWantToTrack";
    EditText et_Name;
    public static String toBeTrackedID;
    public static ParseGeoPoint bravoLoc;

    public void runQuery(final CircularProgressButton cb) {
        cb.setProgress(50);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Location");
        final String uname = et_Name.getText().toString();

        query.whereEqualTo("userName", uname);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        toBeTrackedID = objects.get(0).getObjectId();
                        cb.setProgress(100);

                        bravoLoc = objects.get(0).getParseGeoPoint("updatedLocation");
                        Log.d(TAG, "bravoLoc: " + bravoLoc);

                        Intent i = new Intent(WhoYouWantToTrack.this, LiveTrackMap.class);
                        startActivity(i);
                    } else {
                        cb.setProgress(-1);
                    }
                } else {
                    cb.setProgress(-1);
                }
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_you_want_to_track);

        getSupportActionBar().hide();

        et_Name = (EditText) findViewById(R.id.editText3);
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
                new CountDownTimer(3000, 300) {
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
