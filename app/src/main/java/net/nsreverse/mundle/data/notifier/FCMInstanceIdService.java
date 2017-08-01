package net.nsreverse.mundle.data.notifier;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import net.nsreverse.mundle.data.UserDefaults;

/**
 * FCMInstanceIdService -
 *
 * This class generates and sends a Firebase ID to the Mundle servers in order to enable push
 * notifications.
 *
 * @author Robert
 * Created on 7/21/2017.
 */
public class FCMInstanceIdService extends FirebaseInstanceIdService {

    /**
     * onTokenRefresh() -
     *
     * This method handles when a new Firebase token is assigned to the device.
     */
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("New Token: " + refreshedToken);

        UserDefaults.setDefaultFirebaseToken(getApplicationContext(), refreshedToken);
    }
}
