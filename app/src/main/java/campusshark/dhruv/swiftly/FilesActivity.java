package campusshark.dhruv.swiftly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.apache.log4j.chainsaw.Main;

import java.io.File;

import mehdi.sakout.fancybuttons.FancyButton;

public class FilesActivity extends AppCompatActivity {

    FancyButton aadharButton;
    FancyButton panButton;
    FancyButton passportButton;
    FancyButton licenceButton;
    FancyButton otherFileButton;

    Intent i;

    public void tapFunction(FancyButton button) {

        int bId = button.getId();

        Intent intent = new Intent(FilesActivity.this, uploadActivity.class);
        intent.putExtra("userName", i.getStringExtra("userName"));
        intent.putExtra("password", i.getStringExtra("password"));

        if (bId == aadharButton.getId()) {
            intent.putExtra("button", "aadhar");
        } else if (bId == passportButton.getId()) {
            intent.putExtra("button", "passport");
        } else if (bId == panButton.getId()) {
            intent.putExtra("button", "pan");
        } else if (bId == licenceButton.getId()) {
            intent.putExtra("button", "licence");
        } else {
            intent.putExtra("button", "otherfile");
        }

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        aadharButton = (FancyButton) findViewById(R.id.btn_aadhar);
        panButton = (FancyButton) findViewById(R.id.btn_pan);
        passportButton = (FancyButton) findViewById(R.id.btn_passport);
        licenceButton = (FancyButton) findViewById(R.id.btn_licence);
        otherFileButton = (FancyButton) findViewById(R.id.btn_other_file);

        i = getIntent();

        aadharButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapFunction(aadharButton);
            }
        });

        passportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapFunction(passportButton);
            }
        });

        panButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapFunction(panButton);
            }
        });

        licenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapFunction(licenceButton);
            }
        });

        otherFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapFunction(otherFileButton);
            }
        });

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilesActivity.this, TravelEntry.class);
                startActivity(i);
            }
        });

        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilesActivity.this, NearbyPlaces.class);
                startActivity(i);
            }
        });

        final FloatingActionButton actionC = (FloatingActionButton) findViewById(R.id.action_c);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilesActivity.this, RegisterForTracking.class);
                startActivity(i);
            }
        });

        final FloatingActionButton actionD = (FloatingActionButton) findViewById(R.id.action_d);
        actionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilesActivity.this, WhoYouWantToTrack.class);
                startActivity(i);
            }
        });

        final FloatingActionButton actionE = (FloatingActionButton) findViewById(R.id.action_e);
        actionE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilesActivity.this, RegisterForTracking.class);
                startActivity(i);
            }
        });

        final FloatingActionButton actionF = (FloatingActionButton) findViewById(R.id.action_f);
        actionF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilesActivity.this, CrisisActivity.class);
                startActivity(i);
            }
        });

        final FloatingActionButton actionG = (FloatingActionButton) findViewById(R.id.action_g);
        actionG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilesActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}
