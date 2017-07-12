package net.nsreverse.mundle.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import static timber.log.Timber.DebugTree;

/**
 * Class MainActivity -> AppCompatActivity -
 *
 * This class is the first screen of this app.
 *
 * @author Robert
 * Created on 7/8/2017
 */
public class MainActivity extends AppCompatActivity {

    private static final String KEY_SESSION_ID = "session";
    private static final int LOGIN_REQUEST_CODE = 101;
    private static final int DELETE_REQUEST_CODE = 102;

    @BindView(R.id.card_view_my_classes) CardView classroomsCardView;
    @BindView(R.id.card_view_my_notes) CardView notesCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Timber.plant(new DebugTree());

        setupComponents();
    }

    private void setupComponents() {
        classroomsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubscribedClassesActivity.class);
                startActivity(intent);
            }
        });

        notesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Show NotesActivity here.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!MundleApplication.startingLogout) {
            ParseUser.logOut();
        }

        if (ParseUser.getCurrentUser() == null &&
            !MundleApplication.isShowingAuthActivity) {

            MundleApplication.isShowingAuthActivity = true;

            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                MundleApplication.startingLogout = true;

                if (MundleApplication.isLoggingEnabled) {
                    String username = ParseUser.getCurrentUser().getUsername();
                    String session = ParseUser.getCurrentUser().getSessionToken();

                    Timber.d("Session (" + username + " - " + session + ") has been started.");
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                MundleApplication.isShowingAuthActivity = false;
                finish();
            }
        }
        else {
            if (requestCode == DELETE_REQUEST_CODE) {
                ParseUser.logOut();
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (ParseUser.getCurrentUser() != null) {
            outState.putString(KEY_SESSION_ID, ParseUser.getCurrentUser().getSessionToken());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_main_logout) {
            if (MundleApplication.isLoggingEnabled) {
                String username = ParseUser.getCurrentUser().getUsername();
                String session = ParseUser.getCurrentUser().getSessionToken();

                Timber.d("Session (" + username + " - " + session + ") has logged out.");
            }

            ParseUser.logOut();
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
        else if (item.getItemId() == R.id.menu_action_main_change_pwd) {
            // TODO: Implement password change activity
        }
        else if (item.getItemId() == R.id.menu_action_main_set_name) {
            // TODO: Implement name set activity
        }
        else if (item.getItemId() == R.id.menu_action_main_close_account) {
            // TODO: Implement account delete activity
        }

        return super.onOptionsItemSelected(item);
    }
}
