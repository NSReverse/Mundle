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
    private static final String KEY_DEFAULT_FCM_TOKEN = "DEFAULT_FCM_TOKEN";

    /**
     * setDefaultUsername(Context, String) -
     *
     * This method sets the new login username so that it is available upon next startup.
     *
     * @param context A Context to pass to SharedPreferences.
     * @param username A String that represents the new default String username.
     */
    public static void setDefaultUsername(Context context, String username) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DEFAULT_USERNAME, username);
        editor.commit();
    }

    /**
     * getDefaultUsername(Context) -
     *
     * This method gets the last used username.
     *
     * @param context A Context to pass to SharedPreferences.
     * @return A String representing the last known used username.
     */
    public static String getDefaultUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        return preferences.getString(KEY_DEFAULT_USERNAME, "");
    }

    /**
     * setDefaultTeacher(Context, boolean) -
     *
     * This method sets the new flag if the last logged in user was an instructor.
     *
     * @param context A Context to pass to SharedPreferences.
     * @param isTeacher A boolean representing if the last user was an instructor.
     */
    public static void setDefaultTeacher(Context context, boolean isTeacher) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_DEFAULT_TEACHER, isTeacher);
        editor.commit();
    }

    /**
     * getDefaultTeacher(Context) -
     *
     * This method gets a boolean representing if the last known login was an instructor.
     *
     * @param context A Context to pass to SharedPreferences.
     * @return A boolean representing if the last known user was an instructor.
     */
    public static boolean getDefaultTeacher(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DEFAULT_TEACHER, false);
    }

    /**
     *
     * @param context
     * @param token
     */
    public static void setDefaultFirebaseToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DEFAULT_FCM_TOKEN, token);
        editor.commit();
    }

    public static String getDefaultFirebaseToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
        return preferences.getString(KEY_DEFAULT_FCM_TOKEN, "");
    }

    public static class WidgetInfo {
        private static final String KEY_PREV_CLASS_COUNT = "WIDGET_CLASS_COUNT";
        private static final String KEY_PREV_NOTE_COUNT = "WIDGET_NOTE_COUNT";

        public static void setClassSubscriptionCount(Context context, int count) {
            SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(KEY_PREV_CLASS_COUNT, count);
            editor.commit();
        }

        public static int getClassSubsciptionCount(Context context) {
            SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
            return preferences.getInt(KEY_PREV_CLASS_COUNT, 0);
        }

        public static void setNotesCount(Context context, int count) {
            SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(KEY_PREV_NOTE_COUNT, count);
            editor.commit();
        }

        public static int getNoteCount(Context context) {
            SharedPreferences preferences = context.getSharedPreferences("ParseMundle", Context.MODE_PRIVATE);
            return preferences.getInt(KEY_PREV_NOTE_COUNT, 0);
        }
    }
}
