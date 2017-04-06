package project.bsts.semut.utilities;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompareDate {

    public static boolean compare(String date1, String now) {
        Log.i("Compare date", "date to compare "+date1);
        Log.i("Compare date", "date now "+now);
        boolean isExpired = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date dateToCompare = sdf.parse(date1);
            Date dateNow = sdf.parse(now);
            if(dateToCompare.before(dateNow)){
                //System.out.println("Date1 is before Date2");
                isExpired = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return isExpired;

    }
}
