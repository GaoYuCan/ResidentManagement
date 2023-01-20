package top.sinkdev.rm.common.util;


import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class DataValidator {
    private final static Pattern PATTERN_EMAIL = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private final static Pattern PATTERN_PASSWORD = Pattern.compile("^[a-zA-Z0-9@#$%^!_+&*.-]{6,16}$");

    private final static Pattern PATTERN_ID_CN = Pattern.compile("^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    private final static Pattern PATTERN_PHONE = Pattern.compile("^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$");


    public static boolean illegalEmail(@Nullable String email) {
        return email == null || !PATTERN_EMAIL.matcher(email).matches();
    }

    public static boolean illegalEmailPassword(@Nullable String password) {
        return password == null || !PATTERN_PASSWORD.matcher(password).matches();
    }

    public static boolean illegalGender(@Nullable String gender) {
        return gender == null || (!gender.equals("男") && !gender.equals("女"));
    }

    public static boolean illegalName(@Nullable String name) {
        return name == null || name.length() >= 100;
    }

    public static boolean illegalIDCN(@Nullable String idCN) {
        return idCN == null || !PATTERN_ID_CN.matcher(idCN).matches();
    }

    public static boolean illegalPhoneNumber(@Nullable String phone) {
        return phone == null || !PATTERN_PHONE.matcher(phone).matches();
    }

    public static boolean illegalCityName(@Nullable String name) {
        return name == null || name.length() >= 20;
    }

    public static boolean illegalAddress(@Nullable String addr) {
        return addr == null || addr.length() >= 200;
    }
    public static Date convert2Date(@Nullable String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


}
