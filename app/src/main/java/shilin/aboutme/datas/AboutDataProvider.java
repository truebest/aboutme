package shilin.aboutme.datas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

/**
 * Created by beerko on 22.05.16.
 */
public class AboutDataProvider implements LocationListener {
    private String macAddress;
    private String androidId;
    private String imei;
    private String deviceName;
    private double deviceLat;
    private double deviceLon;
    private Context context;
    private LocationManager lm;


    public AboutDataProvider(Context context) {
        this.context = context;
        updateState();

    }

    public boolean updateState() {
        boolean result = true;
        result &= updateAndroidId();
        result &= updateDeviceName();
        result &= updateIMEI();
        result &= updateLocation();
        result &= updateMac();
        return result;
    }

    public String getMacAddress() {

        return macAddress;
    }

    public String getAndroidId() {
        return androidId;
    }

    public String getImei() {
        return imei;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public double getLat() {
        return deviceLat;
    }

    public double getLon() {
        return deviceLon;
    }


    private boolean updateAndroidId() {
        androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return true;
    }


    private boolean updateIMEI() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {

            return false;
        }
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        imei = mngr.getDeviceId();
        return true;
    }

    private boolean updateDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            deviceName = capitalize(model);
        } else {
            deviceName = capitalize(manufacturer) + " " + model;
        }
        return true;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private boolean updateLocation() {

        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        try {
            lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            deviceLon = location.getLongitude();
            deviceLat = location.getLatitude();
        }
        catch (Exception e){
            return false;
        }
        return true;

    }


    private boolean updateMac() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ) {

            return false;
        }
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        macAddress = info.getMacAddress();
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        deviceLat = location.getLatitude();
        deviceLon = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
