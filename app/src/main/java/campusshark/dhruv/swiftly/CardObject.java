package campusshark.dhruv.swiftly;

/**
 * Created by dhruv on 4/12/16.
 */

public class CardObject {
    private int mDrawableImage;
    private String mService;

    CardObject(int drawableImage,String service)
    {
        mDrawableImage = drawableImage;
        mService = service;
    }

    public int getmDrawableImage() {
        return mDrawableImage;
    }

    public void setmDrawableImage(int mDrawableImage) {
        this.mDrawableImage = mDrawableImage;
    }

    public String getmService() {
        return mService;
    }

    public void setmService(String mService) {
        this.mService = mService;
    }
}
