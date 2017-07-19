package net.nsreverse.mundle.ui.classrooms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.nsreverse.mundle.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedClassroomActivity extends AppCompatActivity {

    public static final String KEY_CLASSROOM_ID = "classroom";

    @BindView(R.id.card_view_class_feed) CardView feedCardView;
    @BindView(R.id.card_view_class_assignments) CardView assignmentsCardView;
    @BindView(R.id.card_view_class_projects) CardView projectsCardView;
    @BindView(R.id.card_view_class_members) CardView membersCardView;

    private ParseObject currentClassroom;
    private String currentClassTitle;
    private Context context;

    private enum SelectedCard {
        FEED,
        ASSIGNMENTS,
        PROJECTS,
        MEMBERS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_classroom);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);

        ParseQuery<ParseObject> query = new ParseQuery<>("Classroom");
        query.whereEqualTo("objectId", getIntent().getStringExtra(KEY_CLASSROOM_ID));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    currentClassroom = objects.get(0);

                    assert getSupportActionBar() != null;

                    currentClassTitle = currentClassroom.getString("classroom_name");
                    getSupportActionBar().setTitle(currentClassTitle);
                }
                else {
                    Toast.makeText(context,
                            "Unable to open classroom: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        setupComponents();
    }

    private void setupComponents() {
        feedCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSelectedCard(SelectedCard.FEED);
            }
        });

        assignmentsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSelectedCard(SelectedCard.ASSIGNMENTS);
            }
        });

        projectsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSelectedCard(SelectedCard.PROJECTS);
            }
        });

        membersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSelectedCard(SelectedCard.MEMBERS);
            }
        });
    }

    private void handleSelectedCard(SelectedCard selectedCard) {
        String classId = getIntent().getStringExtra(KEY_CLASSROOM_ID);

        /* intent.putString("KEY_CLASS_ID", classId);
         * intent.putString("KEY_CLASS_TITLE", currentClassTitle);
         */

        switch (selectedCard) {
            case FEED: {
                showToast("Showing Feed");
                Intent intent = new Intent(context, ClassroomFeedActivity.class);
                intent.putExtra(ClassroomFeedActivity.KEY_CLASS_ID, classId);
                intent.putExtra(ClassroomFeedActivity.KEY_CLASS_TITLE, currentClassTitle);
                startActivity(intent);
            } break;
            case ASSIGNMENTS: {
                showToast("Showing Assignments");
                Intent intent = new Intent(context, ClassroomAssignmentsActivity.class);
                intent.putExtra(ClassroomAssignmentsActivity.KEY_CLASS_ID, classId);
                intent.putExtra(ClassroomAssignmentsActivity.KEY_CLASS_TITLE, currentClassTitle);
                startActivity(intent);
            } break;
            case PROJECTS: {
                showToast("Showing Projects");
                Intent intent = new Intent(context, ClassroomAssignmentsActivity.class);
                intent.putExtra(ClassroomAssignmentsActivity.KEY_CLASS_ID, classId);
                intent.putExtra(ClassroomAssignmentsActivity.KEY_CLASS_TITLE, currentClassTitle);
                intent.putExtra(ClassroomAssignmentsActivity.KEY_IS_PROJECTS, true);
                startActivity(intent);
            } break;
            case MEMBERS: {
                showToast("Showing Members");
            } break;
            default: break;
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

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
