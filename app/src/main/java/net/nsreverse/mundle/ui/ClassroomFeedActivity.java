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
import net.nsreverse.mundle.ui.adapters.ClassroomFeedAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ClassroomFeedActivity -
 *
 * This class displays the feed contained on the server.
 *
 * @author Robert
 * Created on 7/15/2017
 */
public class ClassroomFeedActivity extends AppCompatActivity {

    public static final String KEY_CLASS_ID = "class_id";
    public static final String KEY_CLASS_TITLE = "class_title";
    private static final int ADD_FEED_REQUEST = 101;

    @BindView(R.id.recycler_view_classroom_feed_list) RecyclerView feedRecyclerView;
    @BindView(R.id.fab_new_feed_post) FloatingActionButton fabCreateFeedPost;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_feed);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_CLASS_TITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);

        setupComponents();
    }

    private void setupComponents() {
        if (!ParseUser.getCurrentUser().getBoolean("is_teacher")) {
            fabCreateFeedPost.setVisibility(View.GONE);
        }

        fabCreateFeedPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateFeedPostActivity.class);
                intent.putExtra(CreateFeedPostActivity.KEY_CLASS_ID,
                        getIntent().getStringExtra(KEY_CLASS_ID));
                startActivityForResult(intent, ADD_FEED_REQUEST);
            }
        });

        reloadDataSource();
    }

    private void reloadDataSource() {
        ParseQuery<ParseObject> query = new ParseQuery<>("ClassroomFeed");
        query.whereEqualTo("classroom_id", getIntent().getStringExtra(KEY_CLASS_ID));
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ClassroomFeedAdapter adapter = new ClassroomFeedAdapter();
                adapter.setDataSource(context, objects);

                feedRecyclerView.setAdapter(adapter);
                feedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
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
        if (requestCode == ADD_FEED_REQUEST && resultCode == RESULT_OK) {
            reloadDataSource();
        }
    }
}
