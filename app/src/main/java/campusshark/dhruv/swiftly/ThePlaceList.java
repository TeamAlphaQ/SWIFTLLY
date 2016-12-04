package campusshark.dhruv.swiftly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class ThePlaceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final String TAG = "ThePlaceList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_place_list);

        Log.d(TAG,"ThePlaceList");
        recyclerView = (RecyclerView) findViewById(R.id.place_list_rv);

        Log.d(TAG,"ThePlaceList1");
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        Log.d(TAG,"ThePlaceList2");
        recyclerView.setLayoutManager(layoutManager);
        adapter = new rvAdapterType(getDataSet());
        Log.d(TAG,"ThePlaceList3");
        recyclerView.setAdapter(adapter);
        Log.d(TAG,"ThePlaceList4");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((rvAdapterType) adapter).setOnItemClickListener(new rvAdapterType.MyClickListener1() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d(TAG, "Clicked on Item " + position);
            }
        });
    }

    private ArrayList<CardObjectTypeSelected> getDataSet() {
        Log.d(TAG,"ThePlaceList5");

        ArrayList result = new ArrayList<CardObjectTypeSelected>();
        for (int i = 0; i < 10; i++) {
            CardObjectTypeSelected obj = new CardObjectTypeSelected("feff","f4f4f4g",true,R.drawable.uber_logotype_black);
            result.add(i, obj);
        }
        Log.d(TAG,"ThePlaceList100001010101010110");
        return result;
    }
}