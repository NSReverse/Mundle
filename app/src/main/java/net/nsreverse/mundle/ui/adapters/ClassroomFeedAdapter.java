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
 * Created by Robert on 7/15/2017.
 */
public class ClassroomFeedAdapter
       extends RecyclerView.Adapter<ClassroomFeedAdapter.ClassroomFeedViewHolder> {

    private Delegate delegate;
    private List<ParseObject> dataSource;

    @Override
    public ClassroomFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_feed_list_item, parent, false);
        return new ClassroomFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassroomFeedViewHolder holder, int position) {
        ParseObject currentObject = dataSource.get(position);

        holder.feedTitleTextView.setText(currentObject.getString("title"));
        holder.feedAuthorTextView.setText(currentObject.getString("author"));
        holder.feedDateTextView.setText(currentObject.getCreatedAt().toString());
        holder.feedContentTextView.setText(currentObject.getString("contents"));
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

    public interface Delegate {
        void adapterItemClicked(int position, ParseObject selectedObject);
    }

    class ClassroomFeedViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView feedTitleTextView;
        TextView feedAuthorTextView;
        TextView feedContentTextView;
        TextView feedDateTextView;

        ClassroomFeedViewHolder(View itemView) {
            super(itemView);

            feedTitleTextView = itemView.findViewById(R.id.text_view_feed_title);
            feedAuthorTextView = itemView.findViewById(R.id.text_view_feed_author);
            feedContentTextView = itemView.findViewById(R.id.text_view_feed_content);
            feedDateTextView = itemView.findViewById(R.id.text_view_feed_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (delegate != null) delegate.adapterItemClicked(getAdapterPosition(),
                    dataSource.get(getAdapterPosition()));
        }
    }
}
