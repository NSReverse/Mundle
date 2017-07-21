package net.nsreverse.mundle.data.notifier;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.data.ServerConfiguration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Robert on 7/21/2017.
 */
public class FCMAsyncTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = FCMAsyncTask.class.getSimpleName();
    private Delegate delegate;

    public interface Delegate {
        void fcmMessagePosted(boolean success);
    }

    public FCMAsyncTask(Context context) {
        if (context instanceof Delegate) {
            delegate = (Delegate)context;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

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
