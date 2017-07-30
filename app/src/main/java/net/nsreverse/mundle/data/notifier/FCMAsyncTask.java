package net.nsreverse.mundle.data.notifier;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.data.ServerConfiguration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * FCMAsyncTask -
 *
 * This class pushes notifications through specific channels in the background.
 *
 * @author Robert
 * Created on 7/21/2017.
 */
public class FCMAsyncTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = FCMAsyncTask.class.getSimpleName();
    private Delegate delegate;

    /**
     * Interface Delegate -
     *
     * This interface is used to give callbacks to implementing objects.
     */
    public interface Delegate {
        void fcmMessagePosted(boolean success);
    }

    /**
     * Constructor FCMAsyncTask(Context) -
     *
     * This is the main constructor for setting up a FCMAsyncTask.
     *
     * @param context The Context of the object implementing the Delegate interface. Can be null.
     */
    public FCMAsyncTask(@Nullable Context context) {
        if (context instanceof Delegate) {
            delegate = (Delegate)context;
        }
    }

    /**
     * doInBackground(String...) -
     *
     * This method handles the background operations to push a notification.
     *
     * @param strings Variadic arguments needed to push a notification.
     * @return A Boolean representing success of pushing a notification.
     */
    @Override
    protected Boolean doInBackground(String... strings) {
        String message = strings[0];
        String id = strings[1];

        boolean result = false;

        HttpURLConnection conn = null;

        try {
            URL url = new URL(ServerConfiguration.X10.getSendMessageAddress(null, null, message, id));
            conn = (HttpURLConnection)url.openConnection();
            conn.getInputStream();

            if (MundleApplication.isLoggingEnabled) {
                Log.d(TAG, "Broadcast: " + url.toString());
            }

            result = true;
        }
        catch (IOException ex) {
            if (MundleApplication.isLoggingEnabled) {
                Log.d(TAG, "Unable to open stream: " + ex.getMessage());
            }
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    /**
     * onPostExecute(Boolean) -
     *
     * This method performs the post-execution callbacks to the Delegate-implementing object.
     *
     * @param success A Boolean representing if the push notification was sent.
     */
    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            if (delegate != null) {
                delegate.fcmMessagePosted(true);
            }
        }
        else {
            if (delegate != null) {
                delegate.fcmMessagePosted(false);
            }
        }
    }
}
