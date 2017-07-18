package net.nsreverse.mundle.data;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Analytics -
 *
 * This class provides static convenience methods for logging with Firebase Analytics.
 *
 * @author Robert
 * Created on 7/9/2017.
 */
public class Analytics {

    /**
     * firebaseLogAppOpen(Context) -
     *
     * This method logs to Firebase Analytics when the app opens.
     *
     * @param context The Context required to get the FirebaseAnalytics instance.
     */
    public static void firebaseLogAppOpen(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }

    /**
     * firebaseLogUserLogin(Context) -
     *
     * This method logs to Firebase Analytics when the app logs a user in.
     *
     * @param context The Context required to get the FirebaseAnalytics instance.
     */
    public static void firebaseLogUserLogin(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.LOGIN, null);
    }

    /**
     * firebaseLogNewUserType(Context) -
     *
     * This method logs to Firebase Analytics when the app registers and logs a user in under a
     * new group type (teacher or student).
     *
     * @param context The Context required to get the FirebaseAnalytics instance.
     */
    public static void firebaseLogNewUserType(Context context, boolean isTeacher) {
        String group = "student";

        if (isTeacher) group = "teacher";

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.GROUP_ID, group);

        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.JOIN_GROUP,
                bundle);
    }
}
