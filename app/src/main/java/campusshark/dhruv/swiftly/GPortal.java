package campusshark.dhruv.swiftly;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

public class GPortal extends AppCompatActivity {

    String firstName="";
    String lastName="";
    String Complaint="";
    String Phone="";

    MaterialEditText fName;
    MaterialEditText lName;
    MaterialEditText complaint;
    MaterialEditText phone;

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gportal);

        getSupportActionBar().setTitle("GRIEVANCE PORTAL");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009688")));

        String title = "GRIEVANCE PORTAL";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        fName = (MaterialEditText)findViewById(R.id.fName);


        lName = (MaterialEditText)findViewById(R.id.lName);


        complaint = (MaterialEditText)findViewById(R.id.complaint);


        phone = (MaterialEditText)findViewById(R.id.phone);


        CheckBox service = (CheckBox)findViewById(R.id.pService);
        CheckBox corruption = (CheckBox)findViewById(R.id.Corruption);
        CheckBox scheme = (CheckBox)findViewById(R.id.Scheme);
        CheckBox harassment = (CheckBox)findViewById(R.id.Harassment);

        checkAllOK();

        ctx = this;

        Button button = (Button)findViewById(R.id.SUBMIT);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = fName.getText().toString();
                lastName = lName.getText().toString();
                Complaint = complaint.getText().toString();
                Phone = phone.getText().toString();


                if (checkAllOK()) {
                    submitForm();
                    Log.i("strings",firstName+" "+lastName+" "+Complaint+" "+Phone);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("COULD NOT SUBMIT"); // GPS not found
                    builder.setMessage("Please enter ALL details correctly"); // Want to enable?
                    builder.setPositiveButton("OK",null);
                    builder.create().show();
                }

            }
        });


    }

    private void submitForm() {

        ParseObject portal = new ParseObject("Portal");
        portal.put("name",firstName+" "+lastName);
        portal.put("phone",Phone);
        portal.put("complaint",Complaint);

        portal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("SUBMITTED GRIEVANCE"); // GPS not found
                    builder.setMessage("Thank You"); // Want to enable?
                    builder.setPositiveButton("OK",null);
                    builder.create().show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("COULD NOT SUBMIT"); // GPS not found
                    builder.setMessage("Please check your internet connection"); // Want to enable?
                    builder.setPositiveButton("OK",null);
                    builder.create().show();
                }
            }
        });
    }

    private boolean checkAllOK() {

        if (!firstName.equals("")&&!lastName.equals("")&&!Complaint.equals("")&&!Phone.equals("")){
            return true;
        }else{
            return false;
        }
    }
}
