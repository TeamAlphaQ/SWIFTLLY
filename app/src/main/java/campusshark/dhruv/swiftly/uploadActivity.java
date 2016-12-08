package campusshark.dhruv.swiftly;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;

public class uploadActivity extends AppCompatActivity {

    private static final String TAG = "uploadAct";
    ParseObject user;
    public Bitmap bitmapBatman;
    ProgressDialog progressDialog;
    public int REQUEST_CODE = 99;
    public int OPEN_CAMERA = ScanConstants.OPEN_CAMERA;
    public int OPEN_MEDIA = ScanConstants.OPEN_MEDIA;
    public String buttonType;
    public static Drawable d;

    public void setUser(ParseObject parseObject) {
        user = parseObject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching Results");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        TextView dialogText = (TextView) dialog.findViewById(android.R.id.message);
        dialogText.setTextColor(Color.BLACK);

        Intent i = getIntent();
        String username = i.getStringExtra("userName");
        String password = i.getStringExtra("password");
        buttonType = i.getStringExtra("button");

        final Context ctx = this;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setCancelable(false);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DigitalStorage");
        query.whereEqualTo("userName", username);
        query.whereEqualTo("password", password);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                dialog.hide();

                if (e == null)
                    setUser(object);
                else {
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
        FancyButton details = (FancyButton) findViewById(R.id.btn_OCR);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFile file;
                file = (ParseFile) user.get(buttonType);
                Log.d(TAG, "file: " + file);
                if (file == null) {
                    Toast.makeText(uploadActivity.this, "Please upload the document first.", Toast.LENGTH_SHORT).show();
                } else {
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  // USER's CURRENT PHOTO
                            Log.d(TAG, "bitmap: " + bitmap);
                            d = new BitmapDrawable(getResources(), bitmap);
                            Intent i = new Intent(uploadActivity.this, ShowDoc.class);
                            startActivity(i);
                        }
                    });
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(uploadActivity.this, "PLEASE USE PROPER LIGHTNING", Toast.LENGTH_LONG).show();
                Intent i = new Intent(uploadActivity.this, ScanActivity.class);
                i.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, OPEN_CAMERA);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(uploadActivity.this, ScanActivity.class);
                i.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, OPEN_MEDIA);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFile file;
                file = (ParseFile) user.get(buttonType);
                if (file == null) {
                    Toast.makeText(uploadActivity.this, "Please upload the document first.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG,"fileURL: "+file.getUrl());
                    Intent i;
                    if(buttonType.equals("aadhar"))
                    {
                        i = new Intent(uploadActivity.this, AadharDetails.class);
                        i.putExtra("url",file.getUrl());
                        startActivity(i);
                    }

                    if(buttonType.equals("licence"))
                    {
                        i = new Intent(uploadActivity.this, LicenceDetails.class);
                        i.putExtra("url",file.getUrl());
                        startActivity(i);
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            Toast.makeText(this, "Image saved in Pictures", Toast.LENGTH_SHORT).show();
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d(TAG, "bitmap: " + bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            bitmapBatman = bitmap;
            Log.d(TAG, "bitmap: " + bitmapBatman);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapBatman.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data1 = stream.toByteArray();
            Log.d(TAG, "bitmapData: " + data1);
            //UPLOADING
            ParseFile file = new ParseFile("docPic.png", data1);

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
                    Log.d("percentDone:", "percentDone: " + percentDone);
                    progressDialog.setProgress(percentDone);
                }
            });

            user.put(buttonType, file);
            user.saveInBackground();

//                getContentResolver().delete(uri, null, null);
//                scannedImageView.setImageBitmap(bitmap);
        }
    }
}
