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
import com.parse.SaveCallback;

import net.nsreverse.mundle.R;
import net.nsreverse.mundle.ui.adapters.ClassroomsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscribeStudentClassActivity extends AppCompatActivity
                                           implements ClassroomsAdapter.Delegate {

    @BindView(R.id.recycler_view_classroom_list) RecyclerView classroomsRecyclerView;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_student_class);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.activity_title_add_class));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);

        setupComponents();
    }

    private void setupComponents() {
        ParseQuery<ParseObject> query = new ParseQuery<>("Classroom");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ClassroomsAdapter adapter = new ClassroomsAdapter();

                    adapter.setDataSource(context, objects);
                    classroomsRecyclerView.setAdapter(adapter);
                    classroomsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void adapterItemClicked(int position, final ParseObject selectedObject) {
        ParseQuery<ParseObject> query = new ParseQuery<>("ClassroomSubscriptions");
        query.whereEqualTo("classroom_id", selectedObject.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        Toast.makeText(context,
                                "Subscribed to classroom!",
                                Toast.LENGTH_SHORT).show();

                        ParseObject newObject = new ParseObject("ClassroomSubscriptions");
                        newObject.put("classroom_id", selectedObject.getObjectId());
                        newObject.put("subscriber_id", ParseUser.getCurrentUser().getObjectId());
                        newObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }
                    else {
                        boolean exists = false;

                        for (ParseObject currentClassroom : objects) {
                            if (currentClassroom.getString("subscriber_id")
                                    .equals(ParseUser.getCurrentUser().getObjectId())) {

                                exists = true;
                            }
                        }

                        if (!exists) {
                            ParseObject newObject = new ParseObject("ClassroomSubscriptions");
                            newObject.put("classroom_id", selectedObject.getObjectId());
                            newObject.put("subscriber_id", ParseUser.getCurrentUser().getObjectId());
                            newObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                        }
                        else {
                            Toast.makeText(context,
                                    context.getString(R.string.content_sub_class_failed_current),
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    }
                }
                else {
                    Toast.makeText(context,
                            context.getString(R.string.content_sub_class_failed) + " "
                                    + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }
}
