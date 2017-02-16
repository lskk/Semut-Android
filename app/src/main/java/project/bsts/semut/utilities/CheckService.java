package project.bsts.semut.utilities;


import android.app.ActivityManager;
import android.content.Context;

import static android.content.Context.ACTIVITY_SERVICE;

public class CheckService {


    public static boolean isLocationServiceRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("project.bsts.semut.services.LocationService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
