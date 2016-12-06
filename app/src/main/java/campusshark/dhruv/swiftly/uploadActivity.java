package campusshark.dhruv.swiftly;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import mehdi.sakout.fancybuttons.FancyButton;

public class uploadActivity extends AppCompatActivity {

    ParseObject user;
    ProgressDialog progressDialog;

    public void setUser(ParseObject parseObject){

        user = parseObject;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching Results");
        dialog.setIndeterminate(true);
        dialog.show();

        TextView dialogText = (TextView) dialog.findViewById(android.R.id.message);
        dialogText.setTextColor(Color.BLACK);

        Intent i = getIntent();
        String username = i.getStringExtra("userName");
        String password = i.getStringExtra("password");
        final String buttonType = i.getStringExtra("button");

        final Context ctx = this;
        progressDialog = new ProgressDialog(ctx);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DigitalStorage");
        query.whereEqualTo("userName",username);
        query.whereEqualTo("password",password);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                dialog.hide();

                if(e==null)
                setUser(object);
                else{
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                    alertDialogBuilder.setTitle("ERROR HAS OCCOURED");
                    alertDialogBuilder.setMessage("Please Check your Internet COnnection");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }
            }
        });

        FancyButton camera = (FancyButton) findViewById(R.id.btn_camera);
        FancyButton gallery = (FancyButton) findViewById(R.id.btn_gallery);
        FancyButton show = (FancyButton) findViewById(R.id.btn_VIEW);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseFile file;
                file = (ParseFile)user.get(buttonType);
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  // USER's CURRENT PHOTO
                    }
                });
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // BITMAP RESOURCE SETTING
                Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(),
                        R.drawable.airport);

                //BITMAP BYTE CONVERSION
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] data = stream.toByteArray();

                //UPLOADING
                ParseFile file = new ParseFile("docPic.png",data);

                progressDialog.setMessage("UPLOADING ...");
                progressDialog.setProgress(0);
                progressDialog.setIndeterminate(false);
                progressDialog.show();


                TextView dialogText2 = (TextView) progressDialog.findViewById(android.R.id.message);
                dialogText2.setTextColor(Color.BLACK);

                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                    }
                }, new ProgressCallback() {
                    @Override
                    public void done(Integer percentDone) {
                        progressDialog.setProgress(percentDone);
                    }
                });

                user.put(buttonType,file);
                user.saveInBackground();
            }
        });

    }
}
