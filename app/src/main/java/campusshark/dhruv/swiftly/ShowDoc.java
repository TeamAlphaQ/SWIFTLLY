package campusshark.dhruv.swiftly;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowDoc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doc);

        ImageView imgShowDoc = (ImageView) findViewById(R.id.img_show_doc);
        imgShowDoc.setImageDrawable(uploadActivity.d);
    }
}
