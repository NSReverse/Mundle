package net.nsreverse.mundle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
 * Created by Robert on 7/11/2017.
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_classes_teacher);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.activity_title_subscribed_classes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            ButterKnife.bind(this);

            teacherFragment = TeacherSubscribedClassesFragment.newInstance(this);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_fragment, teacherFragment, FRAGMENT_TAG)
                    .commit();

            fabCreateClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: Create a new Classroom.
                    Toast.makeText(SubscribedClassesActivity.this,
                            "This toast is a placeholder for creating a new classroom.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            setContentView(R.layout.activity_subscribed_classes_student);
            ButterKnife.bind(this);

            studentFragment = StudentSubscribedClassesFragment.newInstance(this);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_fragment, studentFragment, FRAGMENT_TAG)
                    .commit();

            fabAddClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SubscribedClassesActivity.this,
                            "This toast is a placeholder for adding a new classroom.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void adapterItemClicked(int position, ParseObject selectedObject) {
        // TODO: Handle selection of a classroom.
    }

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
