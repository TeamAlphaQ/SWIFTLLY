package campusshark.dhruv.swiftly;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CrisisActivity extends AppCompatActivity {

    String riderOrDriver;
    Context ctx;

    public void userType(String type){

        riderOrDriver=type;

        ParseUser.getCurrentUser().put("riderOrDriver",riderOrDriver);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Log.i("MyApp",riderOrDriver+"signed up");

                    if (riderOrDriver=="rider") {
                        Intent intent = new Intent(ctx, MapsActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ctx, ViewRequests.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crisis);

        getSupportActionBar().hide();

        ctx=this;

        LocationManager locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("GPS IS NOT ENABLED"); // GPS not found
            builder.setMessage("Please click on yes to enable it"); // Want to enable?
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton("NO", null);
            builder.create().show();
            return;
        }

        Button rider = (Button)findViewById(R.id.riderButton);
        Button driver = (Button)findViewById(R.id.driverButton);

        if (ParseUser.getCurrentUser()==null){

            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Log.d("MyApp", "Anonymous login failed.");
                    } else {
                        Log.d("MyApp", "Anonymous user logged in.");
                    }
                }
            });

        }

        rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType("rider");
            }
        });
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType("driver");
            }
        });



    }
}
