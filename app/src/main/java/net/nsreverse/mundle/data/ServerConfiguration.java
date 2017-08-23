package net.nsreverse.mundle.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.nsreverse.mundle.MundleApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import timber.log.Timber;

/**
 * Class ServerConfiguration -
 *
 * This class is meant to provide the necessary configuration to connect to the servers it needs
 * in order to operate.
 *
 * @author Robert
 * Created on 7/8/2017.
 */
public class ServerConfiguration {
    /**
     * testConnectivity(Context) -
     *
     * This method tests Internet connectivity.
     *
     * @param context A Context to get the Connectivity Service.
     * @return A boolean representing if the test was successful.
     */
    @SuppressWarnings("unused")
    public static boolean testConnectivity(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting();
    }

    /**
     * Class ServerConfiguration.Parse -
     *
     * This class gets a server address of the Parse Server that this app is dependent on
     * for handling of data and accounts.
     */
    public static class Parse {
        private static final boolean useLocalServer = false;
        private static final boolean useHerokuServer = true; // Overrides other options

        private static final String publicHerokuAddress = "https://nsserver2.herokuapp.com/parse/";
        private static final String publicServerAddress = "http://75.65.191.106:1337/parse/";
        private static final String localServerAddress = "http://10.0.0.48:1337/parse/";

        public static String getServerAddress() {
            String currentAddress = publicServerAddress;

            if (useLocalServer) currentAddress = localServerAddress;

            if (useHerokuServer) currentAddress = publicHerokuAddress;

            return currentAddress;
        }

        /**
         * Method getApplicationId -
         *
         * This method has the application id as required by the Parse server to identify the
         * database that it should work with.
         *
         * @return A String representing the application id for Parse server to work with.
         */
        public static String getApplicationId() {
            String appId = "capstone";

            if (useHerokuServer) {
                appId = "myAppId";
            }

            return appId;
        }

        /**
         * Method getMasterKey -
         *
         * This method has the key to the test Parse server. Having it here for learning purposes
         * is unimportant. Having this exposed in a production app is very important, however.
         *
         * @return A String representing the client key to access the server.
         */
        public static String getMasterKey() {
            return "myMasterKey";
        }
    }

    /**
     * Class X10 -
     *
     * This class gets a server address of the custom API server for creating and writing notes.
     */
    public static class X10 {

        /**
         * Method getServerAddress() -
         *
         * This method gets the base server address for holding and viewing notes.
         *
         * @return A String representing the base API URL for holding notes data.
         */
        private static String getServerAddress() {
            return "http://reverseeffectapps.x10.mx/";
        }

        /**
         * This method creates and returns an address based on the username of the currently logged
         * in user, their message, and the message id.
         *
         * @param username A String representing the username of the authenticated user.
         * @param message A String representing the message content.
         * @param id A String representing the message id stored on the server.
         * @return A String representing the constructed address.
         */
        public static String getSendMessageAddress(String username, String title, String message,
                                                   String id) {
            String encoding = "utf-8";

            String encodedTitle = title;
            String encodedMessage = message;

            if (username == null) {
                username = "null";
            }

            if (title == null) {
                title = "null";
            }

            if (message == null) {
                message = "null";
            }

            try {
                encodedTitle = URLEncoder.encode(title, encoding);
                encodedMessage = URLEncoder.encode(message, encoding);
            }
            catch (UnsupportedEncodingException ex) {
                if (MundleApplication.isLoggingEnabled) Timber.e("Unable to URL encode message: " +
                                                                ex.getMessage());
            }

            return String.format("%ssend.php?u=%s&t=%s&m=%s&id=%s", getServerAddress(),
                    username, encodedTitle, encodedMessage, id);
        }

        /**
         * This method creates and returns an address based on the username of the currently
         * authenticated user and the id of the message requested.
         *
         * @param username A String representing the username of the authenticated user.
         * @param id A String representing the id of the note requested.
         * @return A String representing the constructed address.
         */
        public static String getViewMessageAddress(String username, String id) {
            return String.format("%sview.php?u=%s&id=%s", getServerAddress(), username, id);
        }
    }
}
