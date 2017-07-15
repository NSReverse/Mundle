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
 * ClassroomFeedAdapter -
 *
 * This adapter binds data to ViewHolders needed by the RecyclerView in ClassroomFeedActivity.
 *
 * @author Robert
 * Created on 7/15/2017.
 */
public class ClassroomFeedAdapter
       extends RecyclerView.Adapter<ClassroomFeedAdapter.ClassroomFeedViewHolder> {

    private Delegate delegate;
    private List<ParseObject> dataSource;

    /**
     * onCreateViewHolder(ViewGroup, int) -
     *
     * This method inflates a view for usage with this adapter to list an item.
     *
     * @param parent A ViewGroup where the view is a child.
     * @param viewType An int representing the view type.
     * @return A ClassroomFeedViewHolder for containing list item data.
     */
    @Override
    public ClassroomFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_feed_list_item, parent, false);
        return new ClassroomFeedViewHolder(view);
    }

    /**
     * onBindViewHolder(ClassroomFeedViewHolder, int) -
     *
     * This method binds data to the ViewHolder.
     *
     * @param holder The ViewHolder for containing data.
     * @param position An int representing the position in the List where data should be binded
     *                 to the current ViewHolder.
     */
    @Override
    public void onBindViewHolder(ClassroomFeedViewHolder holder, int position) {
        ParseObject currentObject = dataSource.get(position);

        holder.feedTitleTextView.setText(currentObject.getString("title"));
        holder.feedAuthorTextView.setText(currentObject.getString("author"));
        holder.feedDateTextView.setText(currentObject.getCreatedAt().toString());
        holder.feedContentTextView.setText(currentObject.getString("contents"));
    }

    /**
     * getItemCount() -
     *
     * This method gets the count of the current data source.
     *
     * @return An int representing the size of the current data source.
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
     * This method sets the data source and the delegate of this adapter. The adapter is notified
     * of the change.
     *
     * @param context The Context [-> Delegate] for handling callbacks.
     * @param dataSource A List [-> ParseObject] representing the new data source.
     */
    public void setDataSource(Context context, List<ParseObject> dataSource) {
        if (context instanceof Delegate) {
            delegate = (Delegate)context;
        }

        this.dataSource = dataSource;

        notifyDataSetChanged();
    }

    /**
     * Interface Delegate -
     *
     * This Interface defines how callbacks should be handled.
     */
    public interface Delegate {

        /**
         * adapterItemClicked(int, ParseObject) -
         *
         * This method handles the click event on the RecyclerView.
         *
         * @param position The position of the selected item in the RecyclerView
         * @param selectedObject A ParseObject representing the selected item in the RecyclerView.
         */
        void adapterItemClicked(int position, ParseObject selectedObject);
    }

    /**
     * Class ClassroomFeedViewHolder -
     *
     * This class represents the layout of a list item in this adapter's corresponding
     * RecyclerView.
     */
    class ClassroomFeedViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView feedTitleTextView;
        TextView feedAuthorTextView;
        TextView feedContentTextView;
        TextView feedDateTextView;

        /**
         * Constructor ClassroomFeedViewHolder(View) -
         *
         * This constructor instantiates a new ClassroomFeedViewHolder.
         *
         * @param itemView An inflated View for getting the TextViews required for data binding.
         */
        ClassroomFeedViewHolder(View itemView) {
            super(itemView);

            feedTitleTextView = itemView.findViewById(R.id.text_view_feed_title);
            feedAuthorTextView = itemView.findViewById(R.id.text_view_feed_author);
            feedContentTextView = itemView.findViewById(R.id.text_view_feed_content);
            feedDateTextView = itemView.findViewById(R.id.text_view_feed_date);

            itemView.setOnClickListener(this);
        }

        /**
         * onClick(View) -
         *
         * This method handles the click event for this view.
         *
         * @param view A View representing the clicked view.
         */
        @Override
        public void onClick(View view) {
            if (delegate != null) delegate.adapterItemClicked(getAdapterPosition(),
                    dataSource.get(getAdapterPosition()));
        }
    }
}
