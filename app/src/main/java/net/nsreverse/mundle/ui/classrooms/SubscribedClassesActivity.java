package net.nsreverse.mundle.ui.classrooms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;
import net.nsreverse.mundle.ui.adapters.ClassroomsAdapter;
import net.nsreverse.mundle.ui.fragments.StudentSubscribedClassesFragment;
import net.nsreverse.mundle.ui.fragments.TeacherSubscribedClassesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * SubscribedClassesActivity -
 *
 * This class lists the subscribed classes of a student or a teacher.
 *
 * @author Robert
 * Created on 7/11/2017.
 */
public class SubscribedClassesActivity extends AppCompatActivity
                                       implements ClassroomsAdapter.Delegate {

    private static final String FRAGMENT_TAG = "fragment";

    private static final int CREATE_CLASS_REQUEST = 101;
    private static final int ADD_CLASS_REQUEST = 102;

    @Nullable @BindView(R.id.fab_create_class) FloatingActionButton fabCreateClass;
    @Nullable @BindView(R.id.fab_add_class) FloatingActionButton fabAddClass;

    private TeacherSubscribedClassesFragment teacherFragment;
    private StudentSubscribedClassesFragment studentFragment;

    private Context context;

    /**
     * onCreate(Bundle) -
     *
     * This method handles the setting up of this Activity.
     *
     * @param savedInstanceState A Bundle representing previous save state information
     *                           before a configuration change. (if available)
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_classes_teacher);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.activity_title_subscribed_classes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        if (ParseUser.getCurrentUser() == null && MundleApplication.sessionToken != null) {
            try {
                ParseUser.become(MundleApplication.sessionToken);
            }
            catch (ParseException ex) {
                if (MundleApplication.isLoggingEnabled) {
                    Timber.d("Unable to swap sessions: " + ex.getMessage());
                }
            }
        }

        if (ParseUser.getCurrentUser().getBoolean("is_teacher")) {
            if (MundleApplication.isLoggingEnabled) {
                Timber.d("User is a teacher.");
            }

            ButterKnife.bind(this);

            teacherFragment = TeacherSubscribedClassesFragment.newInstance(this);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_fragment, teacherFragment, FRAGMENT_TAG)
                    .commit();

            assert fabCreateClass != null;
            fabCreateClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =
                            new Intent(context, CreateClassActivity.class);
                    startActivityForResult(intent, CREATE_CLASS_REQUEST);
                }
            });
        }
        else {
            if (MundleApplication.isLoggingEnabled) {
                Timber.d("User is a student.");
            }

            setContentView(R.layout.activity_subscribed_classes_student);
            ButterKnife.bind(this);

            studentFragment = StudentSubscribedClassesFragment.newInstance(this);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_fragment, studentFragment, FRAGMENT_TAG)
                    .commit();

            assert fabAddClass != null;
            fabAddClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SubscribeStudentClassActivity.class);
                    startActivityForResult(intent, ADD_CLASS_REQUEST);
                }
            });
        }
    }

    /**
     * onActivityResult(int, int, Intent) -
     *
     * This method handles the result of Activities where a request is made prior to
     * launch.
     *
     * @param requestCode An int constant representing the request being made.
     * @param resultCode An int constant representing the result of the Activity's task.
     * @param data An Intent containing optional data passed from the launched Activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_CLASS_REQUEST) {
            if (resultCode == RESULT_OK) {
                teacherFragment.setDataSource();
            }
        }
        else if (requestCode == ADD_CLASS_REQUEST) {
            if (resultCode == RESULT_OK) {
                studentFragment.setDataSource();
            }
        }
    }

    /**
     * onOptionsItemSelected(MenuItem) -
     *
     * This method handles MenuItem clicks in the ActionBar.
     *
     * @param item A MenuItem representing the clicked item.
     * @return A boolean representing if the action was handled.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * adapterItemClicked(int, ParseObject) -
     *
     * @param position An int representing the position in the RecyclerView that was
     *                 selected.
     * @param selectedObject A ParseObject representing the selected data.
     */
    @Override
    public void adapterItemClicked(int position, ParseObject selectedObject) {
        Intent intent = new Intent(context, SelectedClassroomActivity.class);
        intent.putExtra(SelectedClassroomActivity.KEY_CLASSROOM_ID, selectedObject.getObjectId());
        startActivity(intent);
    }

    /**
     * onResume() -
     *
     * Overridden lifecycle method when activity is brought to foreground.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (currentFragment == null) {
            if (teacherFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_fragment, teacherFragment, FRAGMENT_TAG)
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_fragment, studentFragment, FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    /**
     * onSaveInstanceState(Bundle) -
     *
     * This method handles persisting select data and preparing for a configuration
     * change.
     *
     * @param outState A Bundle to pass to the Activity for restoring a saved
     *                 state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Fragment must be removed before supercall to onSaveInstanceState.
        // Exception thrown otherwise.
        // This is so two lists aren't added at the same time.
        if (teacherFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(teacherFragment)
                    .commit();
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .remove(studentFragment)
                    .commit();
        }

        super.onSaveInstanceState(outState);
    }
}
