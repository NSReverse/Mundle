package net.nsreverse.mundle;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

import net.nsreverse.mundle.data.Analytics;
import net.nsreverse.mundle.data.ServerConfiguration;

import timber.log.Timber;

/**
 * MundleApplication -
 *
 * This class provides any preconfiguration required for the app as well as houses
 * app-wide global variables.
 *
 * @author Robert
 * Created 7/8/2017.
 */
public class MundleApplication extends Application {
    public static final boolean isLoggingEnabled = true;

    public static boolean startingLogout = false;
    public static boolean isShowingAuthActivity = false;

    public static String sessionToken;

    /**
     * Overridden Method onCreate -
     *
     * This method handles any post-launch configuration that the app needs in order to run.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.Configuration configuration = new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(ServerConfiguration.Parse.getApplicationId())
                .clientKey(ServerConfiguration.Parse.getMasterKey())
                .server(ServerConfiguration.Parse.getServerAddress())
                .build();

        Parse.initialize(configuration);

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);

        ParseACL.setDefaultACL(acl, true);

        Analytics.firebaseLogAppOpen(getApplicationContext());
    }
}
