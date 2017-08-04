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
import java.util.Locale;

/**
 * ClassroomsAdapter -
 *
 * This class adapts data to a RecyclerView which shows a list of Classrooms that a user is
 * currently subscribed to.
 *
 * @author Robert
 * Created on 7/11/2017.
 */
public class ClassroomsAdapter extends RecyclerView.Adapter<ClassroomsAdapter.ClassroomViewHolder> {

    private Context context;
    private Delegate delegate;
    private List<ParseObject> dataSource;

    /**
     * onCreateViewHolder(ViewGroup, int) -
     *
     * This method inflates a new ClassroomViewHolder for displaying data.
     *
     * @param parent The parent container where this ViewHolder will be located.
     * @param viewType Unused.
     * @return A new ClassroomViewHolder for displaying data.
     */
    @Override
    public ClassroomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_classroom_list_item, parent, false);
        return new ClassroomViewHolder(view);
    }

    /**
     * onBindViewHolder(ClassroomViewHolder, int) -
     *
     * This method binds the data source to the ClassroomViewHolder at the specified position.
     *
     * @param holder A ClassroomViewHolder to set data to.
     * @param position An int representing the position of the data source.
     */
    @Override
    public void onBindViewHolder(ClassroomViewHolder holder, int position) {
        ParseObject currentObject = dataSource.get(position);

        String instructorText = String.format(Locale.getDefault(), "%s%s",
                context.getString(R.string.text_view_instructor_label),
                currentObject.getString("instructor_name"));

        holder.classroomIdTextView.setText(currentObject.getString("classroom_name"));
        holder.classroomNameTextView.setText(currentObject.getString("classroom_short_desc"));
        holder.classroomInstructorTextView
                .setText(instructorText);
    }

    /**
     * getItemCount() -
     *
     * This method gets the size of the data source.
     *
     * @return An int representing the size of the data source.
     */
    @Override
    public int getItemCount() {
        if (dataSource == null) {
            return 0;
        }

        return dataSource.size();
    }

    /**
     * setDataSource(Context, List [-> ParseObject]) -
     *
     * This method sets the data source and the delegate of this adapter.
     *
     * @param context The Context (implementing Delegate if needed) to get resources and set
     *                callbacks.
     * @param dataSource A List [-> ParseObject] to use as the data source.
     */
    public void setDataSource(Context context, List<ParseObject> dataSource) {
        if (context instanceof Delegate) {
            delegate = (Delegate)context;
        }

        this.context = context;
        this.dataSource = dataSource;

        notifyDataSetChanged();
    }

    /**
     * itemClicked(int) -
     *
     * This method uses the Delegate as a callback when an item is selected.
     *
     * @param position An int representing the selected position.
     */
    private void itemClicked(int position) {
        if (delegate != null) delegate.adapterItemClicked(position, dataSource.get(position));
    }

    /**
     * Interface Delegate -
     *
     * This Interface allows an Object that implements it to recieve callbacks when registered
     * as this Adapter's delegate.
     */
    public interface Delegate {
        void adapterItemClicked(int position, ParseObject selectedObject);
    }

    /**
     * ClassroomViewHolder -
     *
     * This ViewHolder defines the layout of the View that will display data from the data source.
     */
    class ClassroomViewHolder extends RecyclerView.ViewHolder
                              implements View.OnClickListener {


        TextView classroomIdTextView;
        TextView classroomNameTextView;
        TextView classroomInstructorTextView;

        /**
         * Constructor ClassroomViewHolder(View) -
         *
         * This is the main constructor for this class to create a new ClassroomViewHolder.
         *
         * @param itemView A View to display data.
         */
        ClassroomViewHolder(View itemView) {
            super(itemView);

            classroomIdTextView = itemView.findViewById(R.id.text_view_classroom_id);
            classroomNameTextView = itemView.findViewById(R.id.text_view_classroom_name);
            classroomInstructorTextView = itemView.findViewById(R.id.text_view_instructor);

            itemView.setOnClickListener(this);
        }

        /**
         * onClick(View) -
         *
         * This method handles whenever an item is selected in this View.
         *
         * @param view The View that was selected. Unused.
         */
        @Override
        public void onClick(View view) {
            itemClicked(getAdapterPosition());
        }
    }
}