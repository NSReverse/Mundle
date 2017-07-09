package net.nsreverse.mundle.data;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Robert on 7/9/2017.
 */

public class Analytics {
    public static void firebaseLogAppOpen(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }

    public static void firebaseLogUserLogin(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.LOGIN, null);
    }

    public static void firebaseLogNewUserType(Context context, boolean isTeacher) {
        String group = "student";

        if (isTeacher) group = "teacher";

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.GROUP_ID, group);

        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.JOIN_GROUP,
                bundle);
    }
}
