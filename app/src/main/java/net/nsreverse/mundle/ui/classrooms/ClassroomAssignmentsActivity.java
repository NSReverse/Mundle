package net.nsreverse.mundle.ui.classrooms;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import net.nsreverse.mundle.R;
import net.nsreverse.mundle.ui.adapters.ClassroomAssignmentsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ClassroomAssignmentsActivity -
 *
 * This class allows a teacher or a student to view assignments or projects assigned to a
 * selected classroom.
 *
 * @author Robert
 * Created on 7/16/2017
 */
public class ClassroomAssignmentsActivity extends AppCompatActivity {

    public static final String KEY_CLASS_ID = "class_id";
    public static final String KEY_CLASS_TITLE = "class_title";
    public static final String KEY_IS_PROJECTS = "is_projects";
    private static final int ADD_ASSIGNMENT_REQUEST = 101;

    @BindView(R.id.recycler_view_classroom_assign_list) RecyclerView assignmentRecyclerView;
    @BindView(R.id.fab_new_assignment_post) FloatingActionButton fabCreateAssignment;

    private Context context;
    private boolean isProjects = false;

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
        setContentView(R.layout.activity_classroom_assignments);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_CLASS_TITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);

        if (getIntent().hasExtra(KEY_IS_PROJECTS)) {
            isProjects = true;
        }

        setupComponents();
    }

    /**
     * setupComponents() -
     *
     * This method performs post-onCreate setup.
     */
    private void setupComponents() {
        if (!ParseUser.getCurrentUser().getBoolean("is_teacher")) {
            fabCreateAssignment.setVisibility(View.GONE);
        }

        fabCreateAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classId = getIntent().getStringExtra(KEY_CLASS_ID);

                Intent intent = new Intent(context, CreateAssignmentActivity.class);
                intent.putExtra(CreateAssignmentActivity.KEY_CLASS_ID, classId);

                if (isProjects) {
                    intent.putExtra(CreateAssignmentActivity.KEY_IS_PROJECTS, true);
                }

                startActivityForResult(intent, ADD_ASSIGNMENT_REQUEST);
            }
        });

        reloadDataSource();
    }

    /**
     * reloadDataSource() -
     *
     * This method handles reloading of the data in this Activity's RecyclerView.
     */
    private void reloadDataSource() {
        String parseClass = "Assignments";

        if (isProjects) {
            parseClass = "Projects";
        }

        ParseQuery<ParseObject> query = new ParseQuery<>(parseClass);
        query.whereEqualTo("classroom_id", getIntent().getStringExtra(KEY_CLASS_ID));
        query.addDescendingOrder("due_date");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ClassroomAssignmentsAdapter adapter = new ClassroomAssignmentsAdapter();
                adapter.setDataSource(context, objects);

                assignmentRecyclerView.setAdapter(adapter);
                assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        });
    }

    /**
     * onOptionsItemSelected(MenuItem) -
     *
     * This method handles clicking of items in this Activity's ActionBar.
     *
     * @param item A MenuItem representing the selected item.
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
     * onActivityResult(int, int, Intent) -
     *
     * This method handles when this Activity makes a request to another Activity to perform a
     * task and handles that result.
     *
     * @param requestCode An int representing the nature of the request to the called Activity.
     * @param resultCode An int representing the result of the task performed in the called
     *                   Activity.
     * @param data An optional Intent containing post-task data. Unused here.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ASSIGNMENT_REQUEST && resultCode == RESULT_OK) {
            reloadDataSource();
        }
    }
}
