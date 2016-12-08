package campusshark.dhruv.swiftly;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class AppIntro extends IntroActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){

        setFullscreen(true);
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title("Emergency")
                .description("Don't be a bystander in the time of crisis. Emergency services are now just a Tap away !")
                .image(R.drawable.maps)
                .background(R.color.teal1)
                .backgroundDark(R.color.slide1ColorDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Digital Storage")
                .description("Store all your important documents like pan card, passport, and many more securely and access them at any time")
                .image(R.drawable.ds)
                .background(R.color.cpb_green_dark)
                .backgroundDark(R.color.cpb_green)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Taxi")
                .description("Get fare estimates and book a cab without actually downloading the specific cab applications")
                .image(R.drawable.taxis)
                .background(R.color.cpb_red)
                .backgroundDark(R.color.cpb_red_dark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Tracking & Nearbys")
                .description("Track your friends and family in real time anywhere. Also, get information about places around you.")
                .image(R.drawable.nearbys)
                .background(R.color.cpb_blue)
                .backgroundDark(R.color.slide1ColorDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Grievance Portal")
                .description("Register your grievances anonymously and get them sorted out much faster and efficiently")
                .image(R.drawable.portals)
                .background(R.color.orange)
                .backgroundDark(R.color.red_real)
                .build());

        String[] perm={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET};

        addSlide(new SimpleSlide.Builder()
                .title("Just One Last Thing")
                .description("We will need a few permissions from you. Please grant them if you haven't already. Thank you and enjoy.")
                .image(R.drawable.camera)
                .background(R.color.slide1Color)
                .backgroundDark(R.color.slide1ColorDark)
                .permissions(perm)
                .build());
    }
}
