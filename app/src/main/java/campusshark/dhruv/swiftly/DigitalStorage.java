package campusshark.dhruv.swiftly;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DigitalStorage extends AppCompatActivity {

    EditText et_uname;
    EditText et_password;

    public void runQuery(final CircularProgressButton cb){

        cb.setProgress(50);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DigitalStorage");
        final String uname = et_uname.getText().toString();
        final String pass = et_password.getText().toString();

        query.whereEqualTo("userName",uname);
        query.whereEqualTo("password",pass);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){

                    if(objects.size()>0)
                    {
                        cb.setProgress(100);
                        Intent i = new Intent(DigitalStorage.this,FilesActivity.class);
                        i.putExtra("userName",uname);
                        i.putExtra("password",pass);
                        startActivity(i);
                    }else{
                        cb.setProgress(-1);
                    }

                }else {
                    cb.setProgress(-1);
                }
            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_storage);

        getSupportActionBar().hide();

        et_uname = (EditText)findViewById(R.id.editText3);
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


/*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DigitalStorage");
        query.getInBackground("E0621RFMjN", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                        ParseFile aadhar = (ParseFile) object.get("aadhar");
                        aadhar.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                ImageView img = (ImageView) findViewById(R.id.imageView2);
                                img.setImageBitmap(bitmap);
                            }
                        });
                } else {
                    // something went wrong
                }
            }
        });*/

    }
}
