package campusshark.dhruv.swiftly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    }
}
