package vandy.mooc.jsonweather;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data related
 * to wind downloaded in Json from the Weather Service.
 */
public class Wind {

    public final static String deg_JSON = "deg";
    public final static String speed_JSON = "speed";

    private double mSpeed;
    private double mDeg;

    public double getSpeed() {
        return mSpeed;
    }
    public void setSpeed(double speed) {
        mSpeed = speed;
    }

    public double getDeg() {
        return mDeg;
    }
    public void setDeg(double deg) {
        mDeg = deg;
    }
}
