package net.nsreverse.mundle.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;
import net.nsreverse.mundle.data.UserDefaults;
import net.nsreverse.mundle.ui.adapters.ClassroomsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherSubscribedClassesFragment extends Fragment {

    @BindView(R.id.recycler_view_classroom_list) RecyclerView classroomsRecyclerView;
    public Context context;

    public TeacherSubscribedClassesFragment() {
        // Required empty public constructor
    }

    public static TeacherSubscribedClassesFragment newInstance(Context context) {
        TeacherSubscribedClassesFragment fragment = new TeacherSubscribedClassesFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_teacher_subscribed_classes,
                container, false);

        ButterKnife.bind(this, root);
        Timber.plant(new Timber.DebugTree());

        setDataSource();

        return root;
    }

    public void setDataSource() {
        final ClassroomsAdapter adapter = new ClassroomsAdapter();

        ParseQuery<ParseObject> query = new ParseQuery<>("Classroom");
        query.whereEqualTo("instructor_id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    UserDefaults.WidgetInfo.setClassSubscriptionCount(context,
                            objects.size());

                    adapter.setDataSource(context, objects);
                    classroomsRecyclerView.setAdapter(adapter);
                    classroomsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                    if (MundleApplication.isLoggingEnabled) {
                        Timber.d("Data source has been set!");
                    }
                }
                else {
                    if (MundleApplication.isLoggingEnabled) {
                        Timber.d("Unable to set data source: " + e.getMessage());
                    }
                }
            }
        });
    }
}
