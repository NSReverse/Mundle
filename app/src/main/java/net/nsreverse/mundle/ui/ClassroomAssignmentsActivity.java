package net.nsreverse.mundle.ui;

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

public class ClassroomAssignmentsActivity extends AppCompatActivity {

    public static final String KEY_CLASS_ID = "class_id";
    public static final String KEY_CLASS_TITLE = "class_title";
    private static final int ADD_ASSIGNMENT_REQUEST = 101;

    @BindView(R.id.recycler_view_classroom_assign_list) RecyclerView assignmentRecyclerView;
    @BindView(R.id.fab_new_assignment_post) FloatingActionButton fabCreateAssignment;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_assignments);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_CLASS_TITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);

        setupComponents();
    }

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
                startActivityForResult(intent, ADD_ASSIGNMENT_REQUEST);
            }
        });

        reloadDataSource();
    }

    private void reloadDataSource() {
        ParseQuery<ParseObject> query = new ParseQuery<>("Assignments");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ASSIGNMENT_REQUEST && resultCode == RESULT_OK) {
            reloadDataSource();
        }
    }
}
