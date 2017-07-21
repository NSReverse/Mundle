package net.nsreverse.mundle.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import net.nsreverse.mundle.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ClassroomMembersAdapter -
 *
 * This adapter binds data for list items in ClassroomMembersActivity for listing members of a
 * currently subscribed classroom.
 *
 * @author Robert
 * Created on 7/19/2017.
 */
public class ClassroomMembersAdapter
       extends RecyclerView.Adapter<ClassroomMembersAdapter.MemberViewHolder> {

    private List<ParseUser> dataSource;
    private Context context;

    /**
     * onCreateViewHolder(ViewGroup, int) -
     *
     * This method inflates the View to be contained within an AssignmentsViewHolder.
     *
     * @param parent The ViewGroup from which the Context should inflate from.
     * @param viewType An int representing the view type. Unused.
     * @return An MemberViewHolder to contain and adapt data to displaying in a RecyclerView.
     */
    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_classroom_member_list_item, parent, false);
        return new MemberViewHolder(view);
    }

    /**
     * onBindViewHolder(AssignmentsViewHolder, int) -
     *
     * This method binds data to a given MemberViewHolder at a given position.
     *
     * @param holder The AssignmentsViewHolder to contain data for displaying.
     * @param position An int representing the position of this ViewHolder.
     */
    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        ParseUser currentUser = dataSource.get(position);

        String firstName = currentUser.getString("first_name");
        String lastName = currentUser.getString("last_name");

        String name;
        String username;

        if (firstName != null && !firstName.isEmpty()) {
            name = firstName + " ";
        }
        else {
            name = "";
        }

        if (lastName != null && !lastName.isEmpty()) {
            name += lastName;
        }
        else {
            name += "";

            if (name.isEmpty()) {
                name = context.getString(R.string.text_view_name_unset);
            }
        }

        if (currentUser.getBoolean("is_teacher")) {
            username = currentUser.getUsername() + " (Instructor)";
        }
        else {
            username = currentUser.getUsername();
        }

        holder.nameTextView.setText(name);
        holder.usernameTextView.setText(username);
    }

    /**
     * getItemCount -
     *
     * This method gets the count of the current data source.
     *
     * @return An int representing the current size of this data source.
     */
    @Override
    public int getItemCount() {
        if (dataSource == null) {
            return 0;
        }

        return dataSource.size();
    }

    /**
     * setDataSource(Context, List [->ParseObject]) -
     *
     * This method sets the Context and the new data source of this adapter.
     *
     * @param context The Context is responsible for retrieving String resources.
     * @param dataSource A List [->ParseUser] representing the new data source.
     */
    public void setDataSource(Context context, List<ParseUser> dataSource) {
        this.context = context;
        this.dataSource = dataSource;

        notifyDataSetChanged();
    }

    /**
     * MemberViewHolder -
     *
     * This class allows data to be contained for displaying on a RecyclerView.
     */
    class MemberViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_classroom_member_name) TextView nameTextView;
        @BindView(R.id.text_view_classroom_member_username) TextView usernameTextView;

        MemberViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
