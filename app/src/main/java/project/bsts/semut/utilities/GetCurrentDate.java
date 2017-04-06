package project.bsts.semut.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetCurrentDate {

    public static String now(){
        DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static String tomorrow(String now, int dayToAdd){
        String toMorrow = "";
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(now));
            c.add(Calendar.DATE, dayToAdd);  // number of days to add
            toMorrow = sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return toMorrow;
    }
}
