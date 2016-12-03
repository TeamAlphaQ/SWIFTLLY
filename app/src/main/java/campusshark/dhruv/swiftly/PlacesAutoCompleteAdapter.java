package campusshark.dhruv.swiftly;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by dhruv on 3/12/16.
 */
public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable{

    ArrayList<String> resultList;
    Context context;
    int mResource;

    PlaceAPI mPlaceAPI = new PlaceAPI();

    public PlacesAutoCompleteAdapter(Context activity, int p1) {
    }



























}
