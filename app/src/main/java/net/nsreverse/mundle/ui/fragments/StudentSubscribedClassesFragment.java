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

import net.nsreverse.mundle.R;
import net.nsreverse.mundle.data.UserDefaults;
import net.nsreverse.mundle.ui.adapters.ClassroomsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSubscribedClassesFragment extends Fragment {

    @BindView(R.id.recycler_view_classroom_list) RecyclerView classroomsRecyclerView;
    public Context context;

    public StudentSubscribedClassesFragment() {
        // Required empty public constructor
    }

    public static StudentSubscribedClassesFragment newInstance(Context context) {
        StudentSubscribedClassesFragment fragment = new StudentSubscribedClassesFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_student_subscribed_classes, container, false);

        ButterKnife.bind(this, root);

        setDataSource();

        return root;
    }

    public void setDataSource() {
        final ClassroomsAdapter adapter = new ClassroomsAdapter();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ClassroomSubscriptions");
        query.whereEqualTo("subscriber_id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<ParseObject> subscribedClasses = new ArrayList<>();

                if (e == null) {
                    for (ParseObject currentSubscription : objects) {
                        ParseQuery<ParseObject> classQuery = new ParseQuery<>("Classroom");
                        classQuery.whereEqualTo("objectId", currentSubscription.getString("classroom_id"));

                        try {
                            subscribedClasses.add(classQuery.find().get(0));
                        }
                        catch (Exception ex) {
                            System.out.println("Unable to add subscribed class for student.");
                        }
                    }

                    UserDefaults.WidgetInfo.setClassSubscriptionCount(context,
                            subscribedClasses.size());

                    adapter.setDataSource(context, subscribedClasses);
                    classroomsRecyclerView.setAdapter(adapter);
                    classroomsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                }
            }
        });
    }
}
