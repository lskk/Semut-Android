package project.bsts.semut.utilities;


import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

public class CheckService {


    public static boolean isLocationServiceRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("project.bsts.semut.services.GetLocation".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static boolean isGpsEnabled(Context context){
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            isActive = true;
        }else{
            isActive = false;
        }

        return isActive;
    }
}
