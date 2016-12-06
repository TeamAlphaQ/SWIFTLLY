package campusshark.dhruv.swiftly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseGeoPoint;

import org.apache.log4j.chainsaw.Main;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity{

    FancyButton btnCabs;
    FancyButton btnNearby;

    public void buttoncode (View view){
        Intent i = new Intent(MainActivity.this,DigitalStorage.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCabs = (FancyButton) findViewById(R.id.btn_cabs);
        btnCabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TravelEntry.class);
                startActivity(i);
            }
        });

        btnNearby = (FancyButton) findViewById(R.id.btn_nearby_places);
        btnNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NearbyPlaces.class);
                startActivity(i);
            }
        });

        Button b = (Button) findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DigitalStorage.class);
                startActivity(i);
            }
        });

    }
}
