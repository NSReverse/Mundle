package net.nsreverse.mundle.ui.classrooms;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;
import net.nsreverse.mundle.ui.adapters.ClassroomMembersAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import static timber.log.Timber.DebugTree;

/**
 * ClassroomMembersActivity -
 *
 * This Activity simply displays the members of a selected classroom.
 *
 * @author Robert
 * Created on 7/19/2017
 */
public class ClassroomMembersActivity extends AppCompatActivity {

    public static final String KEY_CLASS_ID = "class_id";
    public static final String KEY_CLASS_TITLE = "class_title";
    public static final String KEY_OBJECT_ID = "class_object_id";

    @BindView(R.id.recycler_view_classroom_member_list) RecyclerView membersRecyclerView;

    private Context context;

    /**
     * onCreate(Bundle) -
     *
     * This method is the main entry point for this Activity.
     *
     * @param savedInstanceState A Bundle containing save state information pre-configuration
     *                           state change. (if applicable)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_members);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_CLASS_TITLE));

        context = this;

        ButterKnife.bind(this);
        Timber.plant(new DebugTree());

        setupComponents();
    }

    private void setupComponents() {
        ParseQuery<ParseObject> query = new ParseQuery<>("ClassroomSubscriptions");
        query.whereEqualTo("classroom_id", getIntent().getStringExtra(KEY_OBJECT_ID));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    final List<ParseUser> subscribedUsers = new ArrayList<>();

                    for (ParseObject currentSubscription : objects) {
                        ParseQuery<ParseUser> usersQuery = ParseUser.getQuery();
                        usersQuery.whereEqualTo("objectId",
                                currentSubscription.getString("subscriber_id"));

                        try {
                            List<ParseUser> users = usersQuery.find();

                            if (users.size() > 0) {
                                subscribedUsers.add(users.get(0));
                            }
                        }
                        catch (ParseException ex) {
                            if (MundleApplication.isLoggingEnabled) {
                                Timber.d("Unable to get user: " + ex.getMessage());
                            }
                        }
                    }

                    ClassroomMembersAdapter adapter = new ClassroomMembersAdapter();
                    adapter.setDataSource(context, subscribedUsers);

                    membersRecyclerView.setAdapter(adapter);
                    membersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                }
                else {
                    Toast.makeText(context, "Unable to find subscribers.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
