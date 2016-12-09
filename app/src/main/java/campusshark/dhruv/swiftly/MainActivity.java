package campusshark.dhruv.swiftly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseGeoPoint;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.apache.log4j.chainsaw.Main;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    CardView btnCabs;
    CardView btnNearby;
    CardView btnLiveTrack;

    public void buttoncode(View view) {
        Intent i = new Intent(MainActivity.this, DigitalStorage.class);
        startActivity(i);
    }
    boolean firstTime;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("campusshark.dhruv.swiflty", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("firstTime",true)) {

            Intent intent = new Intent(MainActivity.this,AppIntro.class);
            startActivity(intent);

            firstTime=false;
            sharedPreferences.edit().putBoolean("firstTime",firstTime).apply();
        }


        btnCabs = (CardView) findViewById(R.id.btn_cabs);
        btnCabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TravelEntry.class);
                startActivity(i);
            }
        });

        btnNearby = (CardView) findViewById(R.id.btn_nearby_places);
        btnNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NearbyPlaces.class);
                startActivity(i);
            }
        });

        btnLiveTrack = (CardView) findViewById(R.id.btn_live_tracking);
        btnLiveTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WhoYouWantToTrack.class);
                startActivity(i);
            }
        });

        CardView b = (CardView) findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DigitalStorage.class);
                startActivity(i);
            }
        });

        CardView crisis = (CardView) findViewById(R.id.crisis);
        crisis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CrisisActivity.class);
                startActivity(i);
            }
        });

        CardView portal = (CardView)findViewById(R.id.portalcard);
        portal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, GPortal.class);
                startActivity(i);
            }
        });

     /*   final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CabActivity.class);
                startActivity(i);
            }
        });

        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NearbyPlaces.class);
                startActivity(i);
            }
        });

        final FloatingActionButton actionC = (FloatingActionButton) findViewById(R.id.action_c);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterForTracking.class);
                startActivity(i);
            }
        });
        */

    }

}
