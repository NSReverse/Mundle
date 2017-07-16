package net.nsreverse.mundle.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import net.nsreverse.mundle.R;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Robert on 7/16/2017.
 */
public class ClassroomAssignmentsAdapter
       extends RecyclerView.Adapter<ClassroomAssignmentsAdapter.AssignmentsViewHolder> {

    @SuppressWarnings("FieldCanBeLocal") private final String colorStatusClosed = "#8c0101";
    @SuppressWarnings("FieldCanBeLocal") private final String colorStatusOpened = "#00830d";

    private Delegate delegate;
    private Context context;
    private List<ParseObject> dataSource;

    public interface Delegate {
        void adapterItemClicked(int position, ParseObject selectedObject);
    }

    @Override
    public AssignmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_assignment_list_item, parent, false);
        return new AssignmentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssignmentsViewHolder holder, int position) {
        ParseObject currentObject = dataSource.get(position);

        Date dueDate = currentObject.getDate("due_date");
        Date currentDate = new Date();

        holder.titleTextView.setText(currentObject.getString("title"));
        holder.authorTextView.setText(currentObject.getString("author"));
        holder.contentTextView.setText(currentObject.getString("contents"));
        holder.dateTextView.setText(currentObject.getDate("due_date").toString());

        if (dueDate.after(currentDate)) {
            holder.statusTextView.setText(context.getString(R.string.text_view_assignment_open));
            holder.statusTextView.setTextColor(Color.parseColor(colorStatusOpened));
        }
        else if (dueDate.before(currentDate)) {
            holder.statusTextView.setText(context.getString(R.string.text_view_assignment_closed));
            holder.statusTextView.setTextColor(Color.parseColor(colorStatusClosed));
        }
    }

    @Override
    public int getItemCount() {
        if (dataSource == null) {
            return 0;
        }

        return dataSource.size();
    }

    public void setDataSource(Context context, List<ParseObject> dataSource) {
        if (context instanceof Delegate) {
            delegate = (Delegate)context;
        }

        this.dataSource = dataSource;
        this.context = context;
    }

    class AssignmentsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.text_view_assignment_title) TextView titleTextView;
        @BindView(R.id.text_view_assignment_author) TextView authorTextView;
        @BindView(R.id.text_view_assignment_content) TextView contentTextView;
        @BindView(R.id.text_view_assignment_date) TextView dateTextView;
        @BindView(R.id.text_view_assignment_status) TextView statusTextView;

        AssignmentsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (delegate != null) delegate.adapterItemClicked(getAdapterPosition(),
                    dataSource.get(getAdapterPosition()));
        }
    }
}
