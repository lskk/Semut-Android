package project.bsts.semut.utilities;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FieldValidator {

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target) || target.length() < 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean userNameValidator(String username){
        String USERNAME_PATTERN = "^[a-z0-9_-]{6,15}$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(username);
        return matcher.matches();

    }


}
