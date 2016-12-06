package campusshark.dhruv.swiftly;


import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("yqSbp5dmsGGVFk5w06FISjRMDSLHgavNe7sx9azH")
                .clientKey("IZT03N856jxPW4IZ61n7L5On7llA0AJ9XjUNBH6o")
                .server("https://parseapi.back4app.com/").build()
        );


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
