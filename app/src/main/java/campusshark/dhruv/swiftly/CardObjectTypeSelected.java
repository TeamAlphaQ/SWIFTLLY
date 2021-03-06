package campusshark.dhruv.swiftly;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dhruv on 4/12/16.
 */

public class CardObjectTypeSelected {
    private String tvName;
    private String tvAddress;
    private String tvIsOpen;
    private String imgType;

    CardObjectTypeSelected(String name,String address, boolean isOpen, String img)
    {
        tvName = name;
        tvAddress = address;
        if(isOpen==true)
        {
            tvIsOpen = "Open right now";
        }
        else
        {
            tvIsOpen = "";
        }

        imgType = img;
    }



    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getTvAddress() {
        return tvAddress;
    }

    public void setTvAddress(String tvAddress) {
        this.tvAddress = tvAddress;
    }

    public String getTvIsOpen() {
        return tvIsOpen;
    }

    public void setTvIsOpen(String tvIsOpen) {
        this.tvIsOpen = tvIsOpen;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }
}
