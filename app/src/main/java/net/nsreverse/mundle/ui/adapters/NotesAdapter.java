package net.nsreverse.mundle.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;
import net.nsreverse.mundle.model.Note;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * NotesAdapter -
 *
 * This class adapts data to a RecyclerView which shows a list of Notes that a user has
 * created.
 *
 * @author Robert
 * Created on 7/21/2017.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private Delegate delegate;
    private List<ParseObject> dataSource;
    private List<Note> loadedNotesDataSource;

    /**
     * onCreateViewHolder(ViewGroup, int) -
     *
     * This method inflates a new NoteViewHolder for displaying data.
     *
     * @param parent The parent container where this ViewHolder will be located.
     * @param viewType Unused.
     * @return A new NoteViewHolder for displaying data.
     */
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_note_list_item, parent, false);
        return new NoteViewHolder(view);
    }

    /**
     * onBindViewHolder(NoteViewHolder, int) -
     *
     * This method binds the data source to the NoteViewHolder at the specified position.
     *
     * @param holder A NoteViewHolder to set data to.
     * @param position An int representing the position of the data source.
     */
    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.titleTextView.setText(loadedNotesDataSource.get(position).getTitle());
        holder.subtitleTextView.setText(loadedNotesDataSource.get(position).getContent());
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
        if (loadedNotesDataSource == null) {
            return 0;
        }

        return loadedNotesDataSource.size();
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
        if (context instanceof NotesAdapter.Delegate) {
            delegate = (NotesAdapter.Delegate)context;
        }

        this.dataSource = dataSource;
    }

    /**
     * setLoadedNotesDataSource(List [-> Notes]) -
     *
     * This method sets the data retrieved from the ContentProvider and a Loader (Udacity's
     * specification).
     *
     * @param notesDataSource A List [-> Note] containing Notes to set as the data source.
     */
    public void setLoadedNotesDataSource(List<Note> notesDataSource) {
        loadedNotesDataSource = notesDataSource;
        notifyDataSetChanged();
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
     * itemClicked(int) -
     *
     * This method uses the Delegate as a callback when an item is selected.
     *
     * @param position An int representing the selected position.
     */
    private void itemClicked(int position) {
        if (delegate != null) delegate.adapterItemClicked(position, dataSource.get(position));
        if (MundleApplication.isLoggingEnabled) Log.d("NotesAdapter", "A Note has been clicked.");
    }

    /**
     * NoteViewHolder -
     *
     * This ViewHolder defines the layout of the View that will display data from the data source.
     */
    class NoteViewHolder extends RecyclerView.ViewHolder
                         implements View.OnClickListener {

        @BindView(R.id.text_view_note_title) TextView titleTextView;
        @BindView(R.id.text_view_note_date) TextView subtitleTextView;

        /**
         * Constructor NoteViewHolder(View) -
         *
         * This is the main constructor for this class to create a new NoteViewHolder.
         *
         * @param itemView A View to display data.
         */
        NoteViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

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
