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
 * This class
 *
 * @author Robert
 * Created on 7/19/2017.
 */
public class ClassroomMembersAdapter
       extends RecyclerView.Adapter<ClassroomMembersAdapter.MemberViewHolder> {

    private List<ParseUser> dataSource;
    private Context context;

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_classroom_member_list_item, parent, false);
        return new MemberViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        if (dataSource == null) {
            return 0;
        }

        return dataSource.size();
    }

    public void setDataSource(Context context, List<ParseUser> dataSource) {
        this.context = context;
        this.dataSource = dataSource;

        notifyDataSetChanged();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_classroom_member_name) TextView nameTextView;
        @BindView(R.id.text_view_classroom_member_username) TextView usernameTextView;

        MemberViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
