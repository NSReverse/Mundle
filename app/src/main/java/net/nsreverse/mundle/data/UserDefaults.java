package net.nsreverse.mundle.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * UserDefaults -
 *
 * This class wraps SharedPreferences.
 *
 * @author Robert
 * Created on 7/10/2017.
 */
public class UserDefaults {
    private static final String KEY_DEFAULT_USERNAME = "DEFAULT_USERNAME";
    private static final String KEY_DEFAULT_TEACHER = "DEFAULT_TEACHER";

    public static void setDefaultUsername(Context context, String username) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DEFAULT_USERNAME, username);
        editor.commit();
    }

    public static String getDefaultUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        return preferences.getString(KEY_DEFAULT_USERNAME, "");
    }

    public static void setDefaultTeacher(Context context, boolean isTeacher) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_DEFAULT_TEACHER, isTeacher);
        editor.commit();
    }

    public static boolean getDefaultTeacher(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DEFAULT_TEACHER, false);
    }
}
