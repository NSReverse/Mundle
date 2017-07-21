package net.nsreverse.mundle.ui.classrooms;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;
import net.nsreverse.mundle.data.notifier.FCMAsyncTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * CreateAssignmentActivity -
 *
 * This activity allows a teacher account to create a new assignment.
 *
 * @author Robert
 * Created on 7/16/2017
 */
public class CreateAssignmentActivity extends AppCompatActivity
                                      implements FCMAsyncTask.Delegate {

    public static final String KEY_CLASS_ID = "class_id";
    public static final String KEY_CLASS_NAME = "class_name";
    public static final String KEY_IS_PROJECTS = "is_project";
    private static final String KEY_ASSIGN_TITLE = "assign_title";
    private static final String KEY_ASSIGN_CONTENT = "assign_content";

    @BindView(R.id.edit_text_assign_title) EditText titleEditText;
    @BindView(R.id.edit_text_assign_content) EditText contentEditText;
    @BindView(R.id.date_picker_due) DatePicker dueDatePicker;
    @BindView(R.id.button_save_assign_post) Button saveAssignButton;

    private Context context;

    /**
     * onCreate(Bundle) -
     *
     * This method is the main entry point for setting up this Activity.
     *
     * @param savedInstanceState A Bundle containing save state information before a configuration
     *                           change happens.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_CLASS_NAME));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        if (savedInstanceState != null) {
            titleEditText.setText(savedInstanceState.getString(KEY_ASSIGN_TITLE));
            contentEditText.setText(savedInstanceState.getString(KEY_ASSIGN_CONTENT));
        }

        setupComponents();
    }

    /**
     * setupComponents() -
     *
     * This method provides post-create setup for this activity.
     */
    private void setupComponents() {
        saveAssignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                final String content = contentEditText.getText().toString();

                int day = dueDatePicker.getDayOfMonth();
                int month = dueDatePicker.getMonth();
                int year = dueDatePicker.getYear();

                Calendar calendar = new GregorianCalendar(year, month, day);
                Date dueDate = calendar.getTime();

                String parseClass = "Assignments";

                if (getIntent().hasExtra(KEY_IS_PROJECTS)) {
                    parseClass = "Projects";
                }

                ParseObject newObject = new ParseObject(parseClass);
                newObject.put("classroom_id", getIntent().getStringExtra(KEY_CLASS_ID));
                newObject.put("author", ParseUser.getCurrentUser().getUsername());
                newObject.put("title", title);
                newObject.put("contents", content);
                newObject.put("due_date", dueDate);

                newObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            setResult(RESULT_OK);

                            ParseQuery<ParseObject> query =
                                    new ParseQuery<>("ClassroomSubscriptions");
                            query.whereEqualTo("classroom_id",
                                    getIntent().getStringExtra(KEY_CLASS_ID));
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null && objects.size() > 0) {
                                        for (ParseObject currentSubscription : objects) {
                                            ParseQuery<ParseUser> usersQuery = ParseUser.getQuery();
                                            usersQuery.whereEqualTo("objectId",
                                                    currentSubscription.getString("subscriber_id"));

                                            try {
                                                List<ParseUser> users = usersQuery.find();

                                                String message = context.getString(R.string.notify_new_assign);

                                                if (getIntent().hasExtra(KEY_IS_PROJECTS)) {
                                                    message = context.getString(R.string.notify_new_project);
                                                }

                                                if (users.size() > 0) {
                                                    String fcmToken = users.get(0)
                                                            .getString("fcm_token");
                                                    new FCMAsyncTask(context).execute(message +
                                                        " " +
                                                        getIntent().getStringExtra(KEY_CLASS_NAME),
                                                        fcmToken);
                                                }
                                            }
                                            catch (ParseException ex) {
                                                if (MundleApplication.isLoggingEnabled) {
                                                    Timber.d("Unable to get user: " +
                                                            ex.getMessage());
                                                }

                                                finish();
                                            }
                                        }
                                    }
                                    else {
                                        if (e != null) {
                                            if (MundleApplication.isLoggingEnabled) {
                                                Timber.d("Unable to retrieve subscriptions: " +
                                                    e.getMessage());
                                            }
                                        }
                                        else {
                                            if (MundleApplication.isLoggingEnabled) {
                                                Timber.d("No subscriptions found.");
                                            }
                                        }

                                        finish();
                                    }
                                }
                            });
                        }
                        else {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    }
                });
            }
        });
    }

    /**
     * onOptionsItemSelected(MenuItem) -
     *
     * This method handles a MenuItem click in the ActionBar.
     *
     * @param item A MenuItem representing the selected item.
     * @return A boolean representing if the action was handled.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * onSaveInstanceState(Bundle) -
     *
     * This method writes save state to a Bundle before a configuration change occurs.
     *
     * @param outState A Bundle to write save state information to.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_ASSIGN_TITLE, titleEditText.getText().toString());
        outState.putString(KEY_ASSIGN_CONTENT, contentEditText.getText().toString());
    }

    @Override
    public void fcmMessagePosted(boolean success) {
        if (MundleApplication.isLoggingEnabled) {
            if (success) {
                Timber.d("FCM Message posted.");
            }
            else {
                Timber.d("FCM Message failed.");
            }
        }

        finish();
    }
}
