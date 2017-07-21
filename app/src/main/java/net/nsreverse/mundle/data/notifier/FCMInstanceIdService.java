package net.nsreverse.mundle.data.notifier;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import net.nsreverse.mundle.data.UserDefaults;

/**
 * Created by Robert on 7/21/2017.
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("New Token: " + refreshedToken);

        UserDefaults.setDefaultFirebaseToken(getApplicationContext(), refreshedToken);
    }
}
