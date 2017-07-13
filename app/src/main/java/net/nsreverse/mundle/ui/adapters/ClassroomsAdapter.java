package net.nsreverse.mundle.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import net.nsreverse.mundle.R;

import java.util.List;

/**
 * Created by Robert on 7/11/2017.
 */

public class ClassroomsAdapter extends RecyclerView.Adapter<ClassroomsAdapter.ClassroomViewHolder> {

    private Delegate delegate;
    private List<ParseObject> dataSource;

    @Override
    public ClassroomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_classroom_list_item, parent, false);
        return new ClassroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassroomViewHolder holder, int position) {
        ParseObject currentObject = dataSource.get(position);

        holder.classroomIdTextView.setText(currentObject.getString("classroom_name"));
        holder.classroomNameTextView.setText(currentObject.getString("classroom_short_desc"));
        holder.classroomInstructorTextView.setText("Instructor: " + currentObject.getString("instructor_name"));
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

        notifyDataSetChanged();
    }

    private void itemClicked(int position) {
        if (delegate != null) delegate.adapterItemClicked(position, dataSource.get(position));
    }

    public interface Delegate {
        void adapterItemClicked(int position, ParseObject selectedObject);
    }

    class ClassroomViewHolder extends RecyclerView.ViewHolder
                              implements View.OnClickListener {


        TextView classroomIdTextView;
        TextView classroomNameTextView;
        TextView classroomInstructorTextView;

        ClassroomViewHolder(View itemView) {
            super(itemView);

            classroomIdTextView = itemView.findViewById(R.id.text_view_classroom_id);
            classroomNameTextView = itemView.findViewById(R.id.text_view_classroom_name);
            classroomInstructorTextView = itemView.findViewById(R.id.text_view_instructor);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClicked(getAdapterPosition());
        }
    }
}